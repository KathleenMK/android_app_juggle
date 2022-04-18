package org.wit.juggle.api

import org.wit.juggle.models.Time

class EventWrapper {
    var id: String? = null
    var summary: String = ""
    var creator: Creator? = null
    var start: Time? = null
    var end: Time? = null
    var organizer: Organizer? = null
    var status: String = ""
}

data class Organizer(
    val self: String,
    val email: String
)

data class Creator(
    val email: String
)