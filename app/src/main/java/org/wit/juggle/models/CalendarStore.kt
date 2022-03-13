package org.wit.juggle.models

import androidx.lifecycle.MutableLiveData

interface CalendarStore {

    fun findCalendars(calendarList: MutableLiveData<List<CalendarModel>>)
    //fun findCalendarEvents(id: String) : <List<CalendarModel>>?

}