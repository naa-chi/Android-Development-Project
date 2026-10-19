package com.example.juntavecinos.persistance // Wire this to LoginScreen "somehow"

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            """
                CREATE TABLE $TABLE_NAME 
                (
                $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, 
                $USERRUT_COL TEXT
                )
            """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(userRut: String) {
        val values = ContentValues().apply {
            put(USERRUT_COL, userRut)
        }

        writableDatabase.use {
            db -> db.insert(TABLE_NAME, null, values)
        }
    }

    fun getUser(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    companion object {
        private const val DATABASE_NAME = "USERS"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "user_table"
        const val ID_COL = "id"
        const val USERRUT_COL = "rut"
    }
}