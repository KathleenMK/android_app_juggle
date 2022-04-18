package org.wit.juggle.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.R
import org.wit.juggle.firebaseintegration.FirebaseAuthorization
import org.wit.juggle.firebaseintegration.FirebaseDB
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.CalendarModel
import org.wit.juggle.models.UserModel
import timber.log.Timber

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "Your Calendars..."
    }

    var firebaseAuthorization: FirebaseAuthorization = FirebaseAuthorization(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthorization.liveFirebaseUser
    var loggedOut: MutableLiveData<Boolean> = firebaseAuthorization.loggedOut
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()


    private val calendars =
        MutableLiveData<List<CalendarModel>>()

    val observableCalendars: LiveData<List<CalendarModel>>
        get() = calendars

    val user =
        MutableLiveData<UserModel>()

    val observableUser: LiveData<UserModel>
        get() = user

    val text: LiveData<String> = _text

    val token = app.getString(R.string.temp_bearer_access_token)

    init {
        load()
    }

    fun load() {
        try {
            CalendarManager.findCalendars(token, calendars)
            Timber.i("Retrofit Success : $calendars.value")
        } catch (e: Exception) {
            Timber.i("Retrofit Error : $e.message")
        }
    }

    fun saveUser(
        firebaseUser: MutableLiveData<FirebaseUser>,
        jugglers: HashMap<String, String>,
        juggled: HashMap<String, String>
    ) {
        try {
            FirebaseDB.saveUser(firebaseUser, jugglers, juggled)
        } catch (e: IllegalArgumentException) {
            Timber.i(e.toString())
        }
    }

    fun getUser(
        firebaseUser: MutableLiveData<FirebaseUser>
    ) {
        try {
            FirebaseDB.getUser(firebaseUser, user)
            Timber.i("Firebase DB User Success : " + user.value?.userUid)
        } catch (e: IllegalArgumentException) {
            Timber.i(e.toString())
        }
    }
}