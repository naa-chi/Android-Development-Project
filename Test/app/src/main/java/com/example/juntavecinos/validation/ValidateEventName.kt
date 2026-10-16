package com.example.juntavecinos.validation

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

enum class EventTitleError { TOO_SHORT, TOO_LONG, NONE }
enum class EventScheduleError { ENDS_BEFORE_STARTS, NONE}
enum class EventDescriptionError { NONE_AT_ALL, TOO_LONG, NONE}
enum class ClientQtyError { SMALLER_THAN_ZERO, MIN_MORE_THAN_MAX, NONE }

@RequiresApi(Build.VERSION_CODES.O)
object EventValidator {
    const val TITLE_MIN_LENGTH = 5
    const val TITLE_MAX_LENGTH = 40
    const val DESCRIPTION_MAX_LENGTH = 150


    fun validateTitle(title: String): EventTitleError = when {
        title.length < TITLE_MIN_LENGTH -> EventTitleError.TOO_SHORT
        title.length > TITLE_MAX_LENGTH -> EventTitleError.TOO_LONG
        else -> EventTitleError.NONE
    }

    fun validateSchedule(start: LocalTime, end: LocalTime): EventScheduleError = when {
        !start.isBefore(end) -> EventScheduleError.ENDS_BEFORE_STARTS
        else -> EventScheduleError.NONE
    }

    fun effectiveStart(start: LocalTime, now: LocalTime = LocalTime.now()): LocalTime =
        if (start.isBefore(now)) now else start

    fun validateDescription(description: String): EventDescriptionError = when {
        description.isBlank() -> EventDescriptionError.NONE_AT_ALL
        description.length > 150 -> EventDescriptionError.TOO_LONG
        else -> EventDescriptionError.NONE
    }

    fun validateMinClient(minClient: Int): ClientQtyError = when {
        minClient < 0 -> ClientQtyError.SMALLER_THAN_ZERO
        else -> ClientQtyError.NONE
    }

    fun validateMaxClient(minClient: Int, maxClient: Int): ClientQtyError = when {
        maxClient < minClient -> ClientQtyError.MIN_MORE_THAN_MAX
        else -> ClientQtyError.NONE
    }
}