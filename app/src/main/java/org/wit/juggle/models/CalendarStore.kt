package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData

interface CalendarStore {

    fun findCalendars(calendars: MutableLiveData<List<CalendarModel>>)
    fun findCalendarEvents(calendar:CalendarModel, events: MutableLiveData<List<EventModel>>)
    //fun findEvent(calendar:CalendarModel, event:EventModel, eventReturned: MutableLiveData<EventModel>)
    fun addRelatedEvent(event: AddEventModel)

}