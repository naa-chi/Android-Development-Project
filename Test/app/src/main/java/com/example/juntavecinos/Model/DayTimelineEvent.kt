package com.example.juntavecinos.Model

import java.time.LocalTime

data class DayTimelineEvent(
    val title: String,
    val hoster: String,
    val startTime: LocalTime,
    val endTime: LocalTime
)
