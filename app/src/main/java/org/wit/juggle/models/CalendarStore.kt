package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData

interface CalendarStore {

    fun findCalendars(token: String, calendars: MutableLiveData<List<CalendarModel>>)
    fun findCalendarEvents(
        token: String,
        calendarId: String,
        events: MutableLiveData<List<EventModel>>
    )

    fun addRelatedEvent(
        token: String,
        eventId: String,
        calendarId: String,
        ownerAlias: String,
        event: AddEventModel
    )

    fun addNewEvent(token: String, calendarId: String, event: AddEventModel)

}