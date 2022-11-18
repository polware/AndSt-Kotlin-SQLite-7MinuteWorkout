package com.polware.a7minuteworkout.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.polware.a7minuteworkout.model.HistoryModel

class OpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "SevenMinutesWorkout.db"
        private const val TABLE_HISTORY = "history"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_COMPLETED_DATE = "completed_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HISTORY_TABLE = ("CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_COMPLETED_DATE
                + " TEXT" + ")")
        db?.execSQL(CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllDatesList(): ArrayList<HistoryModel> {
        val list: ArrayList<HistoryModel> = ArrayList<HistoryModel>()
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM $TABLE_HISTORY", null)
        var id: Int
        var date: String
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            date = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_DATE))
            val record = HistoryModel(id = id, date = date)
            list.add(record)
        }
        cursor.close()
        return list
    }

    fun addDate(dateItem: HistoryModel) {
        val values = ContentValues() // Creates an empty set of values using the default initial size
        values.put(COLUMN_COMPLETED_DATE, dateItem.date)
        val db = this.writableDatabase
        db.insert(TABLE_HISTORY, null, values)
        db.close()
    }

    fun deleteDate(dateItem: HistoryModel): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, dateItem.id)
        val success = db.delete(TABLE_HISTORY, "$COLUMN_ID=${dateItem.id}", null)
        db.close()
        return success
    }

}