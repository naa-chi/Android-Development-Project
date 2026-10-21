package com.example.juntavecinos.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.juntavecinos.persistance.DAO.EventDao
import com.example.juntavecinos.persistance.DAO.SessionDao
import com.example.juntavecinos.persistance.dataModel.DayEventData
import com.example.juntavecinos.persistance.dataModel.SessionData
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(entities = [SessionData::class, DayEventData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun eventDao(): EventDao

    companion object {
        init {
            System.loadLibrary("sqlcipher")
        }

        //ensure only ONE instance exists
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, password: String): AppDatabase {
            //instance already exists? Return it, don't create a new one
            INSTANCE?.let { return it }

            //Instance doesn't exist? Create it
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "session_database"
                )
                    .openHelperFactory(SupportOpenHelperFactory(password.toByteArray()))
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}