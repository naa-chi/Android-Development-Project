package com.example.juntavecinos.validation

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

enum class EventTitleError { TOO_SHORT, TOO_LONG, INVALID_CHARACTERS, NONE }
enum class EventScheduleError { ENDS_BEFORE_STARTS, NONE}
enum class EventDescriptionError { NONE_AT_ALL, TOO_LONG, INVALID_CHARACTERS, NONE}
enum class AttendeesError { SMALLER_THAN_ZERO, MORE_THAN_MAXIMUM_POSSIBLE, INVALID_NUMBER, NONE }

@RequiresApi(Build.VERSION_CODES.O)
object EventValidator {
    const val TITLE_MIN_LENGTH = 5
    const val TITLE_MAX_LENGTH = 40
    const val DESCRIPTION_MAX_LENGTH = 150

    // Limits strings to a-Z chars, 0-9 digits, hyphen and hashtag
    private val validRegex = Regex("""^[a-zA-Z0-9#\s-]+$""")
    fun validateTitle(title: String): EventTitleError = when {
        title.length < TITLE_MIN_LENGTH -> EventTitleError.TOO_SHORT
        title.length > TITLE_MAX_LENGTH -> EventTitleError.TOO_LONG
        !validRegex.matches(title) -> EventTitleError.INVALID_CHARACTERS
        else -> EventTitleError.NONE
    }

    fun validateDescription(description: String): EventDescriptionError = when {
        description.isBlank() -> EventDescriptionError.NONE_AT_ALL
        description.length > 150 -> EventDescriptionError.TOO_LONG
        !validRegex.matches(description) -> EventDescriptionError.INVALID_CHARACTERS
        else -> EventDescriptionError.NONE
    }

    fun validateSchedule(start: LocalTime, end: LocalTime): EventScheduleError = when {
        !start.isBefore(end) -> EventScheduleError.ENDS_BEFORE_STARTS
        else -> EventScheduleError.NONE
    }

    fun effectiveStart(start: LocalTime, now: LocalTime = LocalTime.now()): LocalTime =
        if (start.isBefore(now)) now else start



    fun validateAttendeeCount(attendees : String): AttendeesError {
        return try {
            val num : Int? = attendees.toIntOrNull()

            if (num == null) {
                AttendeesError.INVALID_NUMBER
            } else {
                if (num > 100) {
                    AttendeesError.MORE_THAN_MAXIMUM_POSSIBLE
                } else if (num < 0) {
                    AttendeesError.SMALLER_THAN_ZERO
                } else {
                    AttendeesError.NONE
                }
            }
        } catch (e : Exception) {
            AttendeesError.INVALID_NUMBER
        }
    }


}