package org.wit.juggle.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CalendarModel(
    val summary: String,
    val id: String,
    val accessRole: String
)  : Parcelable
