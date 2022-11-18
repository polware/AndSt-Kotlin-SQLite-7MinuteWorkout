package com.polware.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.polware.a7minuteworkout.adapter.ExercisesAdapter
import com.polware.a7minuteworkout.databinding.ActivityExercisesBinding
import com.polware.a7minuteworkout.model.ExercisesModel
import java.util.*

class ExercisesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var exerciseAdapter: ExercisesAdapter? = null
    private var exerciseList: ArrayList<ExercisesModel>? = null
    private var currentExerciseNumber = -1
    lateinit var bindingExercises: ActivityExercisesBinding
    private var restTimer: CountDownTimer? = null
    private var timerDuration: Long = 11_000
    private var restProgress = 0
    private var timerExercise: CountDownTimer? = null
    private var timerDurationExercise: Long = 30_000
    private var progressExercise = 0
    private var textToSpeech: TextToSpeech? = null
    private var soundPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingExercises = ActivityExercisesBinding.inflate(layoutInflater)
        val view = bindingExercises.root
        setContentView(view)

        textToSpeech = TextToSpeech(this, this)
        exerciseList = Constants.defaultExerciseList()
        setupMainView()
        setupExercisesAdapter()

        bindingExercises.textViewTimer.text = (timerDuration / 1000).toString()

        setSupportActionBar(bindingExercises.toolbarExercises)
        val actionBar = supportActionBar
        when {
            actionBar != null -> {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }

        // Listener de flecha atrás en la actividad Exercise
        bindingExercises.toolbarExercises.setNavigationOnClickListener {
            customConfirmationDialog()
        }

    }

    override fun onDestroy() {
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        if (timerExercise != null){
            timerExercise!!.cancel()
            progressExercise = 0
        }
        if (textToSpeech != null){
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        if (soundPlayer != null){
            soundPlayer!!.stop()
        }
        super.onDestroy()
    }

    // Función que controla la vista Main
    private fun setupMainView(){

        try {
            soundPlayer = MediaPlayer.create(this, R.raw.start_notification)
            soundPlayer!!.isLooping = false
            soundPlayer!!.start()
        } catch (e: Exception){
            e.printStackTrace()
        }

        bindingExercises.linearLayoutMainView.visibility = View.VISIBLE
        bindingExercises.linearLayoutExerciseView.visibility = View.GONE
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        bindingExercises.textViewUpcomingExercise.text = exerciseList!![currentExerciseNumber + 1]
            .getName()
        setMainProgressBar()
    }

    private fun setMainProgressBar(){
        bindingExercises.progressBar.progress = restProgress
        restTimer = object: CountDownTimer(timerDuration, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress = (millisUntilFinished/1000).toInt()
                bindingExercises.progressBar.progress = restProgress
                bindingExercises.textViewTimer.text = restProgress.toString()
            }

            override fun onFinish() {
                currentExerciseNumber++
                exerciseList!![currentExerciseNumber].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    // Función que controla la vista de los ejercicios
    private fun setupExerciseView(){
        bindingExercises.linearLayoutMainView.visibility = View.GONE
        bindingExercises.linearLayoutExerciseView.visibility = View.VISIBLE

        if (timerExercise != null){
            timerExercise!!.cancel()
            progressExercise = 0
        }
        speakOut(exerciseList!![currentExerciseNumber].getName())
        setExerciseProgressBar()
        bindingExercises.imageViewExercise.setImageResource(exerciseList!![currentExerciseNumber]
            .getImage())
        bindingExercises.textViewExerciseName.text = exerciseList!![currentExerciseNumber]
            .getName()
    }

    private fun setExerciseProgressBar(){
        bindingExercises.progressBarExercise.progress = progressExercise
        timerExercise = object: CountDownTimer(timerDurationExercise, 1000){
            override fun onTick(millisUntilFinished: Long) {
                progressExercise = (millisUntilFinished/1000).toInt()
                bindingExercises.progressBarExercise.progress = progressExercise
                bindingExercises.textViewTimerExercise.text = progressExercise.toString()
            }

            override fun onFinish() {
                // if (currentExerciseNumber < 2){ // Only for testing
                if (currentExerciseNumber < exerciseList?.size!! - 1){
                    exerciseList!![currentExerciseNumber].setIsSelected(false)
                    exerciseList!![currentExerciseNumber].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupMainView()
                }
                else {
                    //Toast.makeText(this@ExercisesActivity, "You have completed the 7 minutes workout", Toast.LENGTH_SHORT).show()
                    finish()
                    val intent = Intent(this@ExercisesActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    // Método asociado al listener de TextToSpeech
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = textToSpeech!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported")
            }
        }
        else {
            Log.e("TTS", "Initialization Failed")
        }
    }

    // Función para reproducir nombre del ejercicio
    private fun speakOut(text: String){
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExercisesAdapter() {
        bindingExercises.recyclerViewListExercises.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExercisesAdapter(exerciseList!!, this)
        bindingExercises.recyclerViewListExercises.adapter = exerciseAdapter
    }

    private fun customConfirmationDialog(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custom_confirmation_dialog)
        customDialog.setCancelable(false)
        val buttonYes = customDialog.findViewById(R.id.buttonDialogYes) as Button
        val buttonNo = customDialog.findViewById(R.id.buttonDialogNo) as Button
        buttonYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        buttonNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.window?.decorView?.setBackgroundResource(R.drawable.round_corner_dialog)
        customDialog.show()
    }

}