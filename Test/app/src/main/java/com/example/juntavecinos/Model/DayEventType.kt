package com.example.juntavecinos.Model

enum class DayEventType {
    Free,
    ReservedAvailable,
    ReservedFull;

    companion object {
        fun random(): DayEventType = entries.random()
    }
}