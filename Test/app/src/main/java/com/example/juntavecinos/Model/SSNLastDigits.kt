package com.example.juntavecinos.Model

enum class SSNLastDigits {
    ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, K;
    fun getValueOfDV() : Int{
        return when (this) {
            ZERO -> 0
            ONE -> 1
            TWO -> 2
            THREE -> 3
            FOUR -> 4
            FIVE -> 5
            SIX -> 6
            SEVEN -> 7
            EIGHT -> 8
            NINE -> 9
            K -> 10
        }
    }
}

