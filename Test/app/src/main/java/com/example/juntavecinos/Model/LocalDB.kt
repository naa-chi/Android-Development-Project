package com.example.juntavecinos.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
private const val DATABASE_NAME = "neighborhoods.db"
private const val DATABASE_VERSION = 3

private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
fun date(s: String): Date = DATE_FORMAT.parse(s)!!

fun randomEventDate(maxDaysAhead: Int = 90): Date {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, Random.nextInt(0, maxDaysAhead))
    cal.set(Calendar.HOUR_OF_DAY, Random.nextInt(0, 24))
    cal.set(Calendar.MINUTE, if (Random.nextBoolean()) 30 else 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}

fun randomEventEnd(start: Date, hoursMin: Long = 1, hoursMax: Long = 6): Date {
    val hours = Random.nextLong(hoursMin, hoursMax)
    return Date(start.time + hours * 60L * 60L * 1000L)
}

private data class EventSeed(
    val title: String,
    val description: String,
    val scheduledByUserEmail: String,
    val eventType: EventType,
    val location: String,
    val price: Double
)

private val EVENT_SEEDS = listOf(
    EventSeed(
        "Block Party",
        "Annual street block party. Bring a dish to share.",
        "admin1@hood1admin.adm.in",
        EventType.PARTY,
        "Calle Los Aromos 1200, front yard",
        0.0
    ),
    EventSeed(
        "Monthly Neighborhood Meeting",
        "Regular monthly meeting to discuss budget and upcoming projects.",
        "secretary1@hood1.secreta.ry",
        EventType.MEETING,
        "Community Hall, Av. Principal 45",
        0.0
    ),
    EventSeed(
        "Sofía's 10th Birthday",
        "Birthday party for Sofía. Cake and games provided.",
        "neigh@hood1neighbo.ur",
        EventType.BIRTHDAY,
        "Pasaje El Roble 88",
        0.0
    ),
    EventSeed(
        "Carlos & Marta's Wedding Reception",
        "Neighborhood celebration for Carlos and Marta. Formal attire.",
        "admin1@hood1admin.adm.in",
        EventType.WEDDING,
        "Club House, Burgstowne",
        15000.0
    ),
    EventSeed(
        "25th Anniversary Dinner",
        "Dinner celebrating Mr. and Mrs. Pérez's 25th wedding anniversary.",
        "treasurer1@hood1trea.su.re",
        EventType.ANNIVERSARY,
        "Restaurant Don Pepe, Av. Central 220",
        20000.0
    ),
    EventSeed(
        "Baby Shower for Valentina",
        "Baby shower for Valentina. Gifts optional, snacks provided.",
        "neigh@hood1neighbo.ur",
        EventType.BABY_SHOWER,
        "Pasaje Los Nogales 12",
        0.0
    ),
    EventSeed(
        "Spring Faire",
        "Craft and food faire. Local vendors welcome. Setup at 09:00.",
        "secretary1@hood1.secreta.ry",
        EventType.FAIRE,
        "Plaza Central",
        500.0
    ),
    EventSeed(
        "Patron Saint's Day Mass",
        "Annual religious celebration. Procession begins at the chapel.",
        "admin1@hood1admin.adm.in",
        EventType.RELIGIOUS_EVENT,
        "Capilla San Miguel",
        0.0
    ),
    EventSeed(
        "Fundraising Bake Sale",
        "Bake sale to raise funds for the new park benches.",
        "treasurer1@hood1trea.su.re",
        EventType.FAIRE,
        "Corner of Av. Principal & Calle 2",
        200.0
    ),
    EventSeed(
        "New Year's Eve Gathering",
        "End-of-year gathering for the whole neighborhood. Fireworks at midnight.",
        "admin1@hood1admin.adm.in",
        EventType.PARTY,
        "Plaza Central",
        0.0
    ),
    EventSeed(
        "Kids' Football Tournament",
        "Friendly football tournament for kids ages 8–14. Registration on site.",
        "neigh@hood1neighbo.ur",
        EventType.OTHER,
        "Cancha Deportiva Burgstowne",
        1000.0
    )
)

class DBHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

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
        const val COL_EVENT_SCHEDULED_BY_USER_EMAIL = "scheduled_by_user_email"
        const val COL_EVENT_SCHEDULED_START_DATE = "scheduled_start_date"
        const val COL_EVENT_SCHEDULED_END_DATE = "scheduled_end_date"
        const val COL_EVENT_TYPE = "type"
        const val COL_EVENT_LOCATION = "location"
        const val COL_EVENT_PRICE = "price"

        @Volatile // Still no idea what @Volatile does
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
                $COL_EVENT_SCHEDULED_BY_USER_EMAIL TEXT NOT NULL,
                $COL_EVENT_SCHEDULED_START_DATE INTEGER NOT NULL,
                $COL_EVENT_SCHEDULED_END_DATE INTEGER NOT NULL,
                $COL_EVENT_TYPE TEXT NOT NULL,
                $COL_EVENT_LOCATION TEXT NOT NULL,
                $COL_EVENT_PRICE REAL NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NEIGHBORHOODS")
        onCreate(db) //As it says
    }
    fun register(user: User): Result<Unit> {
        val db = writableDatabase

        db.query(
            TABLE_USERS,
            arrayOf(COL_USER_ID),
            "$COL_USER_EMAIL = ? OR $COL_USER_RUT = ?",
            arrayOf(user.email, user.rut),
            null, null, null
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
        return if (output != -1L) Result.success(Unit)
        else Result.failure(Exception("DB insert error"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllEvents(): List<Event> {
        val events = mutableListOf<Event>()
        readableDatabase.query(
            TABLE_EVENTS,
            null, null, null, null, null,

            /*
            * Yeah, I don't know, I have no way of testing this code properly
            * since it's a db that it's creating and subsequently populating,
            * but I don't know how to test this yet since we don't really
            * HAVE a way to get to this point on the application yet so in
            * reality this code has no execution tests and is likely broken.
            *
            * By reading this code, you agree to the contract that this cat is
            * not liable or responsible for any damage or loss that may occur
            * to the projects stability or functionality.
            *
            * By reading this comment, you have waived your right to complain
            * or point out bad code structure.
            */

            "$COL_EVENT_SCHEDULED_START_DATE ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                events.add(cursor.toEvent())
            }
        }
        return events
    }

    fun getAllEventPrices(): List<Double> {
        val prices = mutableListOf<Double>()
        readableDatabase.query(
            TABLE_EVENTS,
            arrayOf(COL_EVENT_PRICE),
            null, null, null, null,
            "$COL_EVENT_SCHEDULED_START_DATE ASC"
        ).use { cursor ->
            val priceIndex = cursor.getColumnIndexOrThrow(COL_EVENT_PRICE)
            while (cursor.moveToNext()) {
                prices.add(cursor.getDouble(priceIndex))
            }
        }
        return prices // The return of all the prices in the db, since that was asked of me and i did it like a good girl would right gng
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
                TABLE_NEIGHBORHOODS, null, hoodVals, SQLiteDatabase.CONFLICT_REPLACE
            )
            val hardcodedUsers = listOf(
                User("9352233-K",  "admin1@hood1admin.adm.in",      "+10000000001", "Aaron", "Aardvark", "Johnson", AccountType.ADMIN,     "H1"),
                //User("8999999-9",  "admin2@hood1admin.adm.in",      "+10000000002", "Abaddon","Abbas",    "Greg",    AccountType.ADMIN,     "H1"),
                User("11352233-K", "treasurer1@hood1trea.su.re",    "+10000000003", "Trea",   "Su",       "Ren",     AccountType.TREASURER, "H1"),
                //User("11244322-2", "treasurer2@hood1.trea.su.re",   "+10000000004", "Trent",  "Shu",      "Ren",     AccountType.TREASURER, "H1"),
                User("77443311-5", "neigh@hood1neighbo.ur",         "+10000000005", "Ron",    "Dverik",   "Keigh",   AccountType.NEIGHBOR,  "H1"),
                //User("3453433-2",  "neigh2@hood1neighbo.ur",        "+10000000006", "Rond",   "Verikk",   "Eigh",    AccountType.NEIGHBOR,  "H1"),
                //User("3333333-2",  "neigh3@hood1neighbo.ur",        "+10000000007", "Rondv",  "Erikke",   "Igh",     AccountType.NEIGHBOR,  "H1"),
                User("4444444-4",  "secretary1@hood1.secreta.ry",   "+10000000008", "Sec",    "Ret",      "Aria",    AccountType.SECRETARY, "H1")
            )

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
                }
                db.insertWithOnConflict(
                    TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_REPLACE
                )
            }

            db.delete(TABLE_EVENTS, null, null) // what are these null values for? I don't know. I gave up on human code on this part. Should look into it and document it.

            for (seed in EVENT_SEEDS) {
                val start = randomEventDate()
                val end = randomEventEnd(start)

                val values = ContentValues().apply {
                    put(COL_EVENT_TITLE, seed.title)
                    put(COL_EVENT_DESCRIPTION, seed.description)
                    put(COL_EVENT_SCHEDULED_BY_USER_EMAIL, seed.scheduledByUserEmail)
                    put(COL_EVENT_SCHEDULED_START_DATE, start.time)
                    put(COL_EVENT_SCHEDULED_END_DATE, end.time)
                    put(COL_EVENT_TYPE, seed.eventType.name)
                    put(COL_EVENT_LOCATION, seed.location)
                    put(COL_EVENT_PRICE, seed.price)
                }
                db.insert(TABLE_EVENTS, null, values)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun Cursor.toEvent(): Event {
    val zoneId = ZoneId.systemDefault()

    return Event(
        id = getInt(getColumnIndexOrThrow(DBHelper.COL_EVENT_ID)),
        title = getString(getColumnIndexOrThrow(DBHelper.COL_EVENT_TITLE)),
        description = getString(getColumnIndexOrThrow(DBHelper.COL_EVENT_DESCRIPTION)),
        scheduledByUserEmail = getString(getColumnIndexOrThrow(DBHelper.COL_EVENT_SCHEDULED_BY_USER_EMAIL)),
        scheduledStartDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(getLong(getColumnIndexOrThrow(DBHelper.COL_EVENT_SCHEDULED_START_DATE))),
            zoneId
        ),
        scheduledEndDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(getLong(getColumnIndexOrThrow(DBHelper.COL_EVENT_SCHEDULED_END_DATE))),
            zoneId
        ),
        eventType = EventType.valueOf(getString(getColumnIndexOrThrow(DBHelper.COL_EVENT_TYPE))),
        location = getString(getColumnIndexOrThrow(DBHelper.COL_EVENT_LOCATION)),
        price = getDouble(getColumnIndexOrThrow(DBHelper.COL_EVENT_PRICE))
    )
}