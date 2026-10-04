package com.example.juntavecinos.Model

enum class AccountType {
    ADMIN,
    TREASURER,
    SECRETARY,
    NEIGHBOR
    // We can scale this btw IF we want a new account or user type
}

data class User(
    val rut: String,
    val email: String,
    val phoneNumber: String,
    var firstName: String,
    var secondName: String? = null,
    val lastName: String,
    val accountType: AccountType,
    val neighborhoodId: String
)

enum class EventType {
    PARTY,
    MEETING,
    BIRTHDAY,
    WEDDING,
    ANNIVERSARY,
    BABY_SHOWER,
    FAIRE,
    RELIGIOUS_EVENT,
    OTHER
    //We can also scale this for new event TYPES if need be
}

data class Event(
    val id: Int = 0,
    val title: String,
    val description: String,
    val scheduledByUserEmail: String,
    val scheduledStartDate: Date,
    val scheduledEndDate: Date,
    val eventType: EventType,
    val location: String,
    val price: Double
)