package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData

interface CalendarStore {

    fun findCalendars(calendars: MutableLiveData<List<CalendarModel>>)
    fun findCalendarEvents(calendar:CalendarModel, events: MutableLiveData<List<EventModel>>)

}