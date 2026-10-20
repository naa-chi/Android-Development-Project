package com.example.juntavecinos.persistance.dataModel


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionData(
    @PrimaryKey val id : Int = 1,
    val accountType: String,
    val cookie: String,
    val fullSSN: String,
    val ssnDV: String
)