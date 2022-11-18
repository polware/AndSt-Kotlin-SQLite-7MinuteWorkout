package com.polware.a7minuteworkout

import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.polware.a7minuteworkout.databinding.ActivityFinishBinding
import com.polware.a7minuteworkout.model.HistoryModel
import com.polware.a7minuteworkout.sqlite.OpenHelper
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
    lateinit var bindingFinish: ActivityFinishBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingFinish = ActivityFinishBinding.inflate(layoutInflater)
        val view = bindingFinish.root
        setContentView(view)

        setSupportActionBar(bindingFinish.toolbarFinish)

        addDateToDatabase()

        bindingFinish.buttonFinish.setOnClickListener {
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addDateToDatabase() {
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("Date : ", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime) // dateTime is formatted in the given format.
        Log.i("Formatted Date : ", "" + date)

        val dbHandler = OpenHelper(this, null)
        dbHandler.addDate(HistoryModel(0, date))
        Log.i("Date : ", "Added to DB")
    }

}