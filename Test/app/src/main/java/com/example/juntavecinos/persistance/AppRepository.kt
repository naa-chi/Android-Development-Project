package com.example.juntavecinos.persistance

import com.example.juntavecinos.Model.DayEventType
import com.example.juntavecinos.persistance.DAO.EventDao
import com.example.juntavecinos.persistance.DAO.SessionDao
import com.example.juntavecinos.persistance.dataModel.DayEventData
import com.example.juntavecinos.persistance.dataModel.SessionData

class AppRepository(
    private val sessionDao: SessionDao,
    private val eventDao: EventDao

) {
    suspend fun saveSession(session: SessionData) {
        sessionDao.saveSessionData(session)
    }

    suspend fun getSession(): SessionData? {
        return sessionDao.getSessionData()
    }

    suspend fun deleteSession() {
        sessionDao.deleteSessionData()
    }

    suspend fun insertEvent(event : DayEventData){
        eventDao.insertEvent(event)
    }

    suspend fun getEventsInDay(start: Long, end: Long): List<DayEventData> {
        return eventDao.getEventsInDay(start, end)
    }

    suspend fun deleteEventById(id : Int) {
        eventDao.deleteEventById(id)
    }

    suspend fun deleteEventsInDay(start: Long, end: Long){
        eventDao.deleteEventsInDay(start, end)
    }

    suspend fun getEventByTitleAndDescription(title : String, description : String) : DayEventData?{
        return eventDao.getEventByTitleAndDescription(title, description)
    }

    suspend fun deleteEvent(event : DayEventData){
        eventDao.deleteEvent(event)
    }

    suspend fun countEventsByDay(start : Long, end : Long) : Int{
        return eventDao.countEventsByDay(start, end)
    }


    fun getDayType(events: List<DayEventData>): DayEventType {
        if (events.isEmpty()) {
            return DayEventType.Free
        }

        var occupiedMinutes = 0
        events.forEach { event ->
            // Calculate duration of each event in minutes
            val durationMillis = event.endTime - event.startTime
            occupiedMinutes += (durationMillis / (1000 * 60)).toInt()
        }

        // Total available time: 08:00 to 22:00 = 14 hours = 840 minutes
        val totalAvailableMinutes = 14 * 60
        val remainingMinutes = totalAvailableMinutes - occupiedMinutes

        return when {
            //if at least 1 hour is free, it's available
            remainingMinutes >= 60 -> DayEventType.ReservedAvailable
            else -> DayEventType.ReservedFull
        }
    }
}