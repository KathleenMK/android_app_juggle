package org.wit.juggle.models

import android.os.Parcelable
import com.google.api.client.util.DateTime
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    val summary: String,
    val id: String,
    //@SerializedName("start.dateTime")
    val start: Time,
    val end: Time,
    val created: String
) : Parcelable

@Parcelize
data class Time(
    val timeZone: String,
    val dateTime: String
): Parcelable
