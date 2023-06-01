package com.example.spendmaster.components

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.RequiresApi

class miSQLiteHelper(context: Context) : SQLiteOpenHelper(
    context, "spendmaster.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        try {
            val creacionOperacion = "CREATE TABLE operation " +
                    "(operation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT, " +
                    "isIncome INTEGER, " +
                    "category TEXT, " +
                    "description TEXT, " +
                    "value REAL)"

            db!!.execSQL(creacionOperacion)
        } catch (e: Exception) {
            Log.e("Error onCreate", e.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val BorradoOperacion = "DROP TABLE IF EXISTS operation"
        db!!.execSQL(BorradoOperacion)
        onCreate(db)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun addDatos(title: String, isIncome: Int, category: String, description: String, value: Double) {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("title", title)
            contentValues.put("isIncome", isIncome)
            contentValues.put("category", category)
            contentValues.put("description", description)
            contentValues.put("value", value)
            db.insert("operation", null, contentValues)
            db.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error al insertar los datos: ${e.message}")
        }
    }
}