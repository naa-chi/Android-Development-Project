package com.example.juntavecinos.persistance.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

//hola
@Entity
data class DayEventData(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val title : String,
    val description : String,
    val hoster : String,
    val startTime : Long,
    val endTime : Long,
)