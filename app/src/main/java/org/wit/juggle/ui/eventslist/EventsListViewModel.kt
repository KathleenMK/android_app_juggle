package org.wit.juggle.ui.eventslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.wit.juggle.R
import org.wit.juggle.firebaseintegration.FirebaseAuthorization
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.EventModel
import timber.log.Timber

//class EventsListViewModel : ViewModel() {
class EventsListViewModel (app: Application) : AndroidViewModel(app) {
    private val _text = MutableLiveData<String>().apply {
        value = "This is eventslist Fragment"
    }

    var firebaseAuthorization : FirebaseAuthorization = FirebaseAuthorization(app)
//    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthorization.liveFirebaseUser
//    var loggedOut : MutableLiveData<Boolean> = firebaseAuthorization.loggedOut
//    var googleSignInClient = MutableLiveData<GoogleSignInClient>()


    private val events =
        MutableLiveData<List<EventModel>>()

    val observableEvents: LiveData<List<EventModel>>
        get() = events

    val text: LiveData<String> = _text

    val token = app.getString(R.string.temp_bearer_access_token)

    fun findCalendarEvents(calendar:CalendarModel) {
        try {
            CalendarManager.findCalendarEvents(token, calendar, events)
            Timber.i("Retrofit Success : $events.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }
}