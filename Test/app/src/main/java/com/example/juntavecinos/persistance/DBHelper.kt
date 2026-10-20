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

        /* val id: Int = 0,
        val title: String,
        val description: String,
        val scheduledByUserEmail: String,
        val scheduledStartDate: LocalDateTime,
        val scheduledEndDate: LocalDateTime,
        val eventType: EventType,
        val location: String,
        val price: Double
         */

        val createTableQuery2 =
            """
                CREATE TABLE $TABLE_NAME2
                $ID_COL2 INTEGER PRIMARY KEY AUTOINCREMENT,
                $TITLE_COL TEXT,
                $DESCRIPTION_COL TEXT,
                $SCHEDULEDBYUSEREMAIL_COL TEXT,
                $SCHEDULEDSTARTDATE_COL TEXT,
                $SCHEDULEDENDDATE_COL TEXT,
                $EVENTTYPE_COL TEXT,
                $LOCATION_COL TEXT,
                $PRICE_COL TEXT
            """.trimIndent()

        db.execSQL(createTableQuery)
        db.execSQL(createTableQuery2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME2")
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

    fun addEvent(
        title: String,
        description: String,
        scheduledByUserEmail: String,
        scheduledStartDate: String,
        scheduledEndDate: String,
        eventType: String,
        location: String,
        price: String) {

        val values = ContentValues().apply {
            put(TITLE_COL, title)
            put(DESCRIPTION_COL, description)
            put(SCHEDULEDBYUSEREMAIL_COL, scheduledByUserEmail)
            put(SCHEDULEDSTARTDATE_COL, scheduledStartDate)
            put(SCHEDULEDENDDATE_COL, scheduledEndDate)
            put(EVENTTYPE_COL, eventType)
            put(LOCATION_COL, location)
            put(PRICE_COL, price)
        }

        writableDatabase.use {
            db -> db.insert(TABLE_NAME2, null, values)
        }
    }

    fun getUser(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getEvent(): Cursor {
        return readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME2", null)
    }

    companion object {
        private const val DATABASE_NAME = "USERS"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "user_table"
        const val ID_COL = "id"
        const val USERRUT_COL = "rut"

        const val TABLE_NAME2 = "event_table"
        const val ID_COL2 = "id"
        const val TITLE_COL = "title"
        const val DESCRIPTION_COL = "description"
        const val SCHEDULEDBYUSEREMAIL_COL = "scheduledByUserEmail"

        const val SCHEDULEDSTARTDATE_COL = "scheduledStartDate"
        const val SCHEDULEDENDDATE_COL = "scheduledEndDate"
        const val EVENTTYPE_COL = "eventType"
        const val LOCATION_COL = "location"
        const val PRICE_COL = "price"
    }
}