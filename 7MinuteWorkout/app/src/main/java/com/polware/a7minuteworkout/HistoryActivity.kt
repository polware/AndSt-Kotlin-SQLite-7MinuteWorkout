package com.polware.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.polware.a7minuteworkout.adapter.HistoryAdapter
import com.polware.a7minuteworkout.databinding.ActivityHistoryBinding
import com.polware.a7minuteworkout.model.HistoryModel
import com.polware.a7minuteworkout.sqlite.OpenHelper

class HistoryActivity : AppCompatActivity() {
    lateinit var bindingHistory: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingHistory = ActivityHistoryBinding.inflate(layoutInflater)
        val view = bindingHistory.root
        setContentView(view)

        setSupportActionBar(bindingHistory.toolbarHistory)
        val actionBar = supportActionBar
        when {
            actionBar != null -> {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.title = "History"
            }
        }

        getAllDates()

        bindingHistory.toolbarHistory.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun getAllDates() {
        val dbHandler = OpenHelper(this, null)
        val datesList = dbHandler.getAllDatesList()

        if (datesList.size > 0) {
            bindingHistory.textViewHistory.visibility = View.VISIBLE
            bindingHistory.recyclerViewHistory.visibility = View.VISIBLE
            bindingHistory.tvNoDataAvailable.visibility = View.GONE

            bindingHistory.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
            val historyAdapter = HistoryAdapter(this, datesList)
            bindingHistory.recyclerViewHistory.adapter = historyAdapter
        }
        else {
            bindingHistory.textViewHistory.visibility = View.GONE
            bindingHistory.recyclerViewHistory.visibility = View.GONE
            bindingHistory.tvNoDataAvailable.visibility = View.VISIBLE
        }
    }

    fun deleteDateAlertDialog(dateItem: HistoryModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Date")
        builder.setMessage("Are you sure you wants to delete?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val dbHandler = OpenHelper(this, null)
            val status = dbHandler.deleteDate(dateItem)
            if (status > -1) {
                Toast.makeText(this, "Date deleted successfully", Toast.LENGTH_LONG).show()
                getAllDates()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}