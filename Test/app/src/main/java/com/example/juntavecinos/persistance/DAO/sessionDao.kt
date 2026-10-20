package com.example.juntavecinos.persistance.DAO

import androidx.room.*
import com.example.juntavecinos.persistance.dataModel.SessionData

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSessionData(session: SessionData)

    @Query("SELECT * FROM SessionData LIMIT 1")
    suspend fun getSessionData(): SessionData?

    @Query("DELETE FROM SessionData")
    suspend fun deleteSessionData()
}