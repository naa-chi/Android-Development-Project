package com.example.juntavecinos.validation

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

enum class EventTitleError { TOO_SHORT, TOO_LONG }
enum class ScheduleError { ENDS_BEFORE_STARTS }
enum class DescriptionError { NONE_AT_ALL, TOO_LONG}

object EventValidator {
    const val TITLE_MIN_LENGTH = 5
    const val TITLE_MAX_LENGTH = 40
    const val DESCRIPTION_MAX_LENGTH = 150

    fun validateTitle(title: String): EventTitleError? = when {
        title.length < TITLE_MIN_LENGTH -> EventTitleError.TOO_SHORT
        title.length > TITLE_MAX_LENGTH -> EventTitleError.TOO_LONG
        else -> null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateSchedule(start: LocalTime, end: LocalTime): ScheduleError? = when {
        !start.isBefore(end) -> ScheduleError.ENDS_BEFORE_STARTS
        else -> null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun effectiveStart(start: LocalTime, now: LocalTime = LocalTime.now()): LocalTime =
        if (start.isBefore(now)) now else start

    fun validateDescription(description: String): DescriptionError? = when {
        description.isBlank() -> DescriptionError.NONE_AT_ALL
        description.length > 150 -> DescriptionError.TOO_LONG
        else -> null
    }
}