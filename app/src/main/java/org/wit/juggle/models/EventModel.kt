package org.wit.juggle.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    val summary: String,
    val id: String = "",
    val start: Time,
    val end: Time,
    val created: String = ""
) : Parcelable

@Parcelize
data class Time(
    val timeZone: String,
    val dateTime: String    //https://www.json2kt.com/ details how dateTime is a String data type

) : Parcelable

@Parcelize
data class AddEventModel(
    //id and created not needed for inserting event
    val summary: String,
    val start: Time,
    val end: Time,
) : Parcelable

@Parcelize
data class RelatedEventModel(
    val id: String = "",
    val owner: String = "",
    var ownerAlias: String = "",
    val summary: String = "",
    val startTimeZone: String = "",
    val startDateTime: String = "",
    val endTimeZone: String = "",
    val endDateTime: String = "",
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {    // required to handle JSON FB DB
        return mapOf(
            "id" to id,
            "owner" to owner,
            "ownerAlias" to ownerAlias,
            "summary" to summary,
            "startTimeZone" to startTimeZone,
            "startDateTime" to startDateTime,
            "endTimeZone" to endTimeZone,
            "endDateTime" to endDateTime
        )
    }
}
