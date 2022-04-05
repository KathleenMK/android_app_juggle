package org.wit.juggle.ui.eventview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.juggle.R
import org.wit.juggle.models.AddEventModel
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventModel
import timber.log.Timber

class EventViewViewModel (app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is eventslist Fragment"


    }

//    private val event =
//        MutableLiveData<EventModel>()
//
//    val observableEvent: LiveData<EventModel>
//        get() = event

    val text: LiveData<String> = _text

    val token = app.getString(R.string.temp_bearer_access_token)

    fun addRelatedEvent(calendarId: String, event: AddEventModel){
        Timber.i("In my new add function"+event.toString())
        CalendarManager.addRelatedEvent(token, calendarId, event)
    }



}