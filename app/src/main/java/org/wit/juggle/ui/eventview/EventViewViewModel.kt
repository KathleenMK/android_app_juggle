package org.wit.juggle.ui.eventview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.wit.juggle.R
import org.wit.juggle.firebaseintegration.FirebaseDB
import org.wit.juggle.models.*
import timber.log.Timber

class EventViewViewModel(app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is eventslist Fragment"
    }

    val relatedEvents =
        MutableLiveData<List<RelatedEventModel>>()

    val observableRelatedEvents: LiveData<List<RelatedEventModel>>
        get() = relatedEvents

    val text: LiveData<String> = _text

    val token = app.getString(R.string.temp_bearer_access_token)

    fun addRelatedEvent(
        eventId: String,
        calendarId: String,
        ownerAlias: String,
        event: AddEventModel
    ) {
        Timber.i("In my new add function" + event.toString())
        CalendarManager.addRelatedEvent(token, eventId, calendarId, ownerAlias, event)
    }

    fun getRelatedEvents(
        eventId: String
    ) {
        try {
            FirebaseDB.getRelatedEvents(eventId, relatedEvents)
            Timber.i("Firebase DB Event Related Success : " + relatedEvents.value?.toString())
        } catch (e: IllegalArgumentException) {
            Timber.i(e.toString())
        }
    }

}