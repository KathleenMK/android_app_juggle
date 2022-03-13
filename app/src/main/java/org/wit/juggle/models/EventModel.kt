package org.wit.juggle.models

import com.google.api.client.util.DateTime
import com.google.gson.annotations.SerializedName

data class EventModel(
    val summary: String,
    val id: String,
    //@SerializedName("start.dateTime")
    val start: Time,
    val end: Time
   // val end: String

)

data class Time(
    val timeZone: String,
    val dateTime: String
)
