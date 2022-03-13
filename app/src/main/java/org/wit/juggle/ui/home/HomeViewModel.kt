package org.wit.juggle.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.firebaseintegration.FirebaseAuthorization
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel
import timber.log.Timber

//class HomeViewModel : ViewModel() {
class HomeViewModel (app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    //var googleSignInAccount = MutableLiveData<GoogleSignInAccount>()
    //var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    var firebaseAuthorization : FirebaseAuthorization = FirebaseAuthorization(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthorization.liveFirebaseUser
    var loggedOut : MutableLiveData<Boolean> = firebaseAuthorization.loggedOut
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    private val calendarsList =
        MutableLiveData<List<CalendarModel>>()

    val observableCalendarsList: LiveData<List<CalendarModel>>
        get() = calendarsList

    val text: LiveData<String> = _text

    init { load() }

    fun load() {
        try {
            CalendarManager.findCalendars(calendarsList)
            Timber.i("Retrofit Success : $calendarsList.value")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }
}