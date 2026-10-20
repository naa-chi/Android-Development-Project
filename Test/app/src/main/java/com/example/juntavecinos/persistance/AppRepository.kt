package com.example.juntavecinos.persistance

import com.example.juntavecinos.persistance.DAO.SessionDao
import com.example.juntavecinos.persistance.dataModel.SessionData

class AppRepository(private val sessionDao: SessionDao) {

    suspend fun saveSession(session: SessionData) {
        sessionDao.saveSessionData(session)
    }

    suspend fun getSession(): SessionData? {
        return sessionDao.getSessionData()
    }

    suspend fun deleteSession() {
        sessionDao.deleteSessionData()
    }
}