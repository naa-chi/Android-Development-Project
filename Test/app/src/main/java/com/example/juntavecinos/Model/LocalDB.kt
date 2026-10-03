package com.example.juntavecinos.Model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "neighborhoods.db"
private const val DATABASE_VERSION = 1

class DBHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) { // I assume null would stand in for a password

    companion object {
        const val TABLE_NEIGHBORHOODS = "neighborhoods"
        const val TABLE_USERS = "users"
        const val TABLE_EVENTS = "events"

        const val COL_HOOD_ID = "id"
        const val COL_HOOD_NAME = "name"
        const val COL_HOOD_POPULATION = "population"

        const val COL_USER_ID = "id"
        const val COL_USER_RUT = "rut"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PHONE = "phone"
        const val COL_USER_HOOD_ID = "neighborhood_id"
        const val COL_USER_FIRST_NAME = "first_name"
        const val COL_USER_SECOND_NAME = "second_name"
        const val COL_USER_LAST_NAME = "last_name"
        const val COL_USER_ACCOUNT_TYPE = "account_type"

        const val COL_EVENT_ID = "id"
        const val COL_EVENT_TITLE = "title"
        const val COL_EVENT_DESCRIPTION = "description"
        const val COL_EVENT_SCHEDULED_BY_USER_NAME = "scheduled_by_user_name"
        const val COL_EVENT_SCHEDULED_DATE = "scheduled_date"
        const val COL_EVENT_TYPE = "type"
        const val COL_EVENT_LOCATION = "location"

        @Volatile // Not sure what this does, document later
        private var INSTANCE: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DBHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NEIGHBORHOODS (
                $COL_HOOD_ID TEXT PRIMARY KEY,
                $COL_HOOD_NAME TEXT NOT NULL,
                $COL_HOOD_POPULATION INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_RUT TEXT NOT NULL UNIQUE,
                $COL_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USER_PHONE TEXT NOT NULL,
                $COL_USER_HOOD_ID TEXT NOT NULL,
                $COL_USER_FIRST_NAME TEXT NOT NULL,
                $COL_USER_SECOND_NAME TEXT,
                $COL_USER_LAST_NAME TEXT NOT NULL,
                $COL_USER_ACCOUNT_TYPE TEXT NOT NULL,
                FOREIGN KEY($COL_USER_HOOD_ID) REFERENCES $TABLE_NEIGHBORHOODS($COL_HOOD_ID)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_EVENTS (
                $COL_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EVENT_TITLE TEXT NOT NULL,
                $COL_EVENT_DESCRIPTION TEXT NOT NULL,
                $COL_EVENT_SCHEDULED_BY_USER_NAME TEXT NOT NULL,
                $COL_EVENT_SCHEDULED_DATE TEXT NOT NULL,
                $COL_EVENT_TYPE TEXT NOT NULL,
                $COL_EVENT_LOCATION TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NEIGHBORHOODS")
        onCreate(db)
    }

    fun register(user: User): Result<Unit> {
        val db = writableDatabase
        // Should prevent duplicates? I'm not sure, DeepSeek made this check, I have no way of running it.
        db.query(
            TABLE_USERS,
            arrayOf(COL_USER_ID),
            "$COL_USER_EMAIL = ? OR $COL_USER_RUT = ?",
            arrayOf(user.email, user.rut),
            null,
            null,
            null
        ).use { cursor ->
            if (cursor.count > 0) {
                return Result.failure(Exception("User with this email or RUT already exists"))
            }
        }

        val values = ContentValues().apply {
            put(COL_USER_RUT, user.rut)
            put(COL_USER_EMAIL, user.email)
            put(COL_USER_PHONE, user.phoneNumber)
            put(COL_USER_HOOD_ID, user.neighborhoodId)
            put(COL_USER_FIRST_NAME, user.firstName)
            put(COL_USER_SECOND_NAME, user.secondName)
            put(COL_USER_LAST_NAME, user.lastName)
            put(COL_USER_ACCOUNT_TYPE, user.accountType.name)
        }

        val output = db.insert(TABLE_USERS, null, values)
        return if (output != -1L) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("DB insert error"))
        }
    }

    fun addHardcodedValues() {
        val db = writableDatabase

        db.beginTransaction()
        try {
            val hoodVals = ContentValues().apply {
                put(COL_HOOD_ID, "H1")
                put(COL_HOOD_NAME, "Burgstowne")
                put(COL_HOOD_POPULATION, 10000)
            }

            db.insertWithOnConflict(
                TABLE_NEIGHBORHOODS,
                null,
                hoodVals,
                SQLiteDatabase.CONFLICT_REPLACE
            )

            val hardcodedUsers = listOf(
                User("9352233-K", "admin1@hood1admin.adm.in", "+10000000001", "Aaron", "Aardvark", "Johnson", AccountType.ADMIN, "H1"),
                User("8999999-9", "admin2@hood1admin.adm.in", "+10000000002", "Abaddon", "Abbas", "Greg", AccountType.ADMIN, "H1"),
                User("11352233-K", "treasurer1@hood1trea.su.re", "+10000000003", "Trea", "Su", "Ren", AccountType.TREASURER, "H1"),
                User("11244322-2", "treasurer2@hood1.trea.su.re", "+10000000004", "Trent", "Shu", "Ren", AccountType.TREASURER, "H1"),
                User("9352233-K", "neigh@hood1neighbo.ur", "+10000000005", "Ron", "Dverik", "Keigh", AccountType.NEIGHBOR, "H1"),
                User("3453433-2", "neigh2@hood1neighbo.ur", "+10000000006", "Rond", "Verikk", "Eigh", AccountType.NEIGHBOR, "H1"),
                User("3333333-2", "neigh3@hood1neighbo.ur", "+10000000007", "Rondv", "Erikke", "Igh", AccountType.NEIGHBOR, "H1"),
                User("4444444-4", "secretary1@hood1.secreta.ry", "+10000000008", "Sec", "Ret", "Aria", AccountType.SECRETARY, "H1")
            ) // wyd if your name was aaron aardvark johnson?

            for (user in hardcodedUsers) {
                val values = ContentValues().apply {
                    put(COL_USER_RUT, user.rut)
                    put(COL_USER_EMAIL, user.email)
                    put(COL_USER_PHONE, user.phoneNumber)
                    put(COL_USER_HOOD_ID, user.neighborhoodId)
                    put(COL_USER_FIRST_NAME, user.firstName)
                    put(COL_USER_SECOND_NAME, user.secondName)
                    put(COL_USER_LAST_NAME, user.lastName)
                    put(COL_USER_ACCOUNT_TYPE, user.accountType.name)
                } // kinda dumb that you can't do it all at once but i digress

                db.insertWithOnConflict(
                    TABLE_USERS,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE
                )
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}