package com.example.juntavecinos.validation

import com.example.juntavecinos.R


enum class SsnError { INVALID_FORMAT, TOO_LONG, TOO_SHORT, NONE}

object LoginValidator {
    private val SSN_REGEX = Regex("""^\d{7,8}-([1-9]|k|K)$""")

    fun validateSsn(ssn: String): SsnError {
        val s = ssn.trim()
        return when {
            s.length < 9 -> SsnError.TOO_SHORT
            s.length > 10 -> SsnError.TOO_LONG
            !SSN_REGEX.matches(s) -> SsnError.INVALID_FORMAT
            else -> SsnError.NONE
        }
    }
}