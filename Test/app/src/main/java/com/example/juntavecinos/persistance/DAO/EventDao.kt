package com.example.juntavecinos.persistance.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.juntavecinos.Model.DayTimelineEvent
import com.example.juntavecinos.persistance.dataModel.DayEventData

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event : DayEventData)

    @Query("SELECT * FROM DayEventData WHERE startTime >= :start AND endTime <= :end")
    suspend fun getEventsInDay(start : Long, end : Long) : List<DayEventData>

    @Query("DELETE FROM DayEventData WHERE id = :id")
    suspend fun deleteEventById(id : Int)

    @Query("DELETE FROM DayEventData WHERE startTime >= :start AND endTime <= :end")
    suspend fun deleteEventsInDay(start: Long, end: Long)

    @Query("SELECT * FROM DayEventData WHERE title = :title AND description = :description")
    suspend fun getEventByTitleAndDescription(title : String, description : String) : DayEventData

    @Delete
    suspend fun deleteEvent(event : DayEventData)

    @Query("SELECT COUNT(*) FROM DayEventData WHERE startTime >= :start AND endTime <= :end")
    suspend fun countEventsByDay(start: Long, end: Long): Int
}