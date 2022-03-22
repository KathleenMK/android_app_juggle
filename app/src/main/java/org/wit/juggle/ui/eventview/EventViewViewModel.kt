package org.wit.juggle.ui.eventview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.juggle.models.AddEventModel
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventModel
import timber.log.Timber

class EventViewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is eventslist Fragment"


    }

//    private val event =
//        MutableLiveData<EventModel>()
//
//    val observableEvent: LiveData<EventModel>
//        get() = event

    val text: LiveData<String> = _text

    fun addRelatedEvent(event: AddEventModel){
        Timber.i("In my new add function"+event.toString())
        CalendarManager.addRelatedEvent(event)
    }



}