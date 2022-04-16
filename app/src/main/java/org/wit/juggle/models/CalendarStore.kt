package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData

interface CalendarStore {

    fun findCalendars(token:String, calendars: MutableLiveData<List<CalendarModel>>)
    fun findCalendarEvents(token:String, calendarId:String, events: MutableLiveData<List<EventModel>>)
    //fun findEvent(calendar:CalendarModel, event:EventModel, eventReturned: MutableLiveData<EventModel>)
    fun addRelatedEvent(token:String, eventId: String, calendarId:String, ownerAlias:String, event: AddEventModel)

}