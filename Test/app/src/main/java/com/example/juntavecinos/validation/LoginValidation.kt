package com.example.juntavecinos.validation

import androidx.core.text.isDigitsOnly
import com.example.juntavecinos.Model.SSNLastDigits
import com.example.juntavecinos.R


enum class SsnError { INVALID_FORMAT, TOO_LONG, TOO_SHORT, INVALID_SSN, NONE}
val ssnDigits = listOf<Int>(2, 3, 4, 5, 6, 7)
object LoginValidator {
    fun validateSsn(ssn: String, dv : SSNLastDigits): SsnError {
        try {
            val s = ssn.trim()
            return when {
                s.length < 7 -> SsnError.TOO_SHORT
                s.length > 9 -> SsnError.TOO_LONG
                !s.isDigitsOnly() -> SsnError.INVALID_FORMAT
                else -> {
                    var i : Int = 0
                    val results = mutableListOf<Int>()

                    for (char in s.reversed()){
                        val num = char.digitToIntOrNull()
                        if (num == null) {
                            return SsnError.INVALID_FORMAT
                        } else {
                            results.add(
                                num * ssnDigits[i]
                            )
                            i += 1
                            if (i >= ssnDigits.size) {
                                i = 0
                            }
                        }
                    }
                    var res = 11 - (results.sum() % 11)
                    if (res == 11) res = 0

                    if (res == dv.getValueOfDV()) {
                        SsnError.NONE
                    } else {
                        SsnError.INVALID_SSN
                    }
                }
            }
        } catch (e : Error) {
            return SsnError.INVALID_FORMAT
        }

    }
}