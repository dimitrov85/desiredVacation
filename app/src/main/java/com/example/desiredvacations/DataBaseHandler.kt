package com.example.desiredvacations

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        private const val DATABASE_NAME = "MyDB"

        private const val TABLE_NAME = "Vacations"
        private const val COL_ID = "_id"
        private const val COL_NAME = "Name"
        private const val COL_LOCATION = "Location"
        private const val COL_HOTEL = "Hotel"
        private const val COL_MONEY = "Money"
        private const val COL_DESCRIPTION = "Description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create table Vacations
        val createTable = ("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY, "
                + COL_NAME + " TEXT, "
                + COL_LOCATION + " TEXT, "
                + COL_HOTEL + " TEXT, "
                + COL_MONEY + " TEXT, "
                + COL_DESCRIPTION +" TEXT" + ");")

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(vacation: Vacation) : Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, vacation.name)
        cv.put(COL_LOCATION, vacation.location)
        cv.put(COL_HOTEL, vacation.hotel)
        cv.put(COL_MONEY, vacation.neededMoney)
        cv.put(COL_DESCRIPTION, vacation.description)
        Log.e("tag", "cv $cv")
        val result = db.insert(TABLE_NAME, null, cv)

        if (result == (-1).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }

        db.close()
        return result
    }

    fun viewData(): ArrayList<Vacation> {

        val dataList: ArrayList<Vacation> = ArrayList<Vacation>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var vacationId: Int
        var name: String
        var location: String
        var hotel: String

        if (cursor.moveToFirst()) {
            do {
                vacationId = cursor.getInt(cursor.getColumnIndex(COL_ID))
                name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                location = cursor.getString(cursor.getColumnIndex(COL_LOCATION))
                hotel = cursor.getString(cursor.getColumnIndex(COL_HOTEL))

                val data = Vacation(vacationId = vacationId, name = name, location = location, hotel = hotel)
                dataList.add(data)
            } while (cursor.moveToNext())
        }

        return dataList
    }

    fun updateData(vacation: Vacation): Int {
        val db = this.writableDatabase
//        val query = "SELECT * FROM $TABLE_NAME"
//        val result = db.rawQuery(query, null)
        var cv = ContentValues()
        cv.put(COL_NAME, vacation.name)
        cv.put(COL_LOCATION, vacation.location)
        cv.put(COL_HOTEL, vacation.hotel)

        val result = db.update(TABLE_NAME, cv, "id=" + vacation.vacationId, null)

        db.close()

        return result
    }

    fun deleteData(vacation: Vacation) : Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_ID, vacation.vacationId)

        val result = db.delete(TABLE_NAME, COL_ID + "=" + vacation.vacationId, null)
        db.close()

        return result
    }
}