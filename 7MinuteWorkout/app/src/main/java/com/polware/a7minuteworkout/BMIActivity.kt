package com.polware.a7minuteworkout

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.polware.a7minuteworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    lateinit var bindingBMI: ActivityBmiactivityBinding
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"
    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingBMI = ActivityBmiactivityBinding.inflate(layoutInflater)
        val view = bindingBMI.root
        setContentView(view)

        setSupportActionBar(bindingBMI.toolbarBMI)
        val actionBar = supportActionBar
        when {
            actionBar != null -> {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.title = "Calculate BMI"
            }
        }

        bindingBMI.toolbarBMI.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricView()
        bindingBMI.radioGroupUnits.setOnCheckedChangeListener { group, id ->
            if (id == R.id.rbMetricUnits){
                makeVisibleMetricView()
            }
            else {
                makeVisibleUSUnitsView()
            }
        }

        bindingBMI.buttonCalculateUnits.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW){
                if (validateMetricUnits()){
                    val heightValue: Float = bindingBMI.etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = bindingBMI.etMetricUnitWeight.text.toString().toFloat()
                    val bmi = weightValue / (heightValue * heightValue)
                    BMIResult(bmi)
                }
                else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.",
                        Toast.LENGTH_SHORT).show()
                }
            }
            else {
                if (validateUSUnits()){
                    val usUnitHeightValueFeet: String = bindingBMI.etUsUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch: String = bindingBMI.etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue: Float = bindingBMI.etUsUnitWeight.text.toString().toFloat()
                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    BMIResult(bmi)
                }
                else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.",
                        Toast.LENGTH_SHORT).show()
                }
            }
            // Autohide keyboard of screen
            val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if(bindingBMI.etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if (bindingBMI.etMetricUnitHeight.text.toString().isEmpty())
            isValid = false
        else if (bindingBMI.etMetricUnitHeight.text.toString().toFloat() <= 60 &&
            bindingBMI.etMetricUnitWeight.text.toString().toFloat() <= 30)
            isValid = false

        return isValid
    }

    private fun validateUSUnits(): Boolean {
        var isValid = true
        if(bindingBMI.etUsUnitHeightFeet.text.toString().isEmpty())
            isValid = false
        else if (bindingBMI.etUsUnitHeightInch.text.toString().isEmpty())
            isValid = false
        else if (bindingBMI.etUsUnitWeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }

    private fun BMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String
        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(bmi, 16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(bmi, 18.5f ) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(bmi, 25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(bmi, 35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(bmi, 40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        bindingBMI.linearLayoutBMIResult.visibility = View.VISIBLE
        // Round the result value to 2 decimal after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        bindingBMI.tvBMIValue.text = bmiValue
        bindingBMI.tvBMIType.text = bmiLabel
        bindingBMI.tvBMIDescription.text = bmiDescription
    }

    private fun makeVisibleMetricView(){
        currentVisibleView = METRIC_UNITS_VIEW
        bindingBMI.llMetricUnitsView.visibility = View.VISIBLE

        bindingBMI.etMetricUnitHeight.text!!.clear()
        bindingBMI.etMetricUnitWeight.text!!.clear()

        bindingBMI.llUsUnitsView.visibility = View.GONE
        bindingBMI.linearLayoutBMIResult.visibility = View.GONE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        bindingBMI.llMetricUnitsView.visibility = View.GONE

        bindingBMI.etUsUnitWeight.text!!.clear()
        bindingBMI.etUsUnitHeightFeet.text!!.clear()
        bindingBMI.etUsUnitHeightInch.text!!.clear()

        bindingBMI.llUsUnitsView.visibility = View.VISIBLE
        bindingBMI.linearLayoutBMIResult.visibility = View.GONE
    }

}