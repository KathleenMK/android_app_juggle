package org.wit.juggle.ui.addevent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.R
import org.wit.juggle.firebaseintegration.FirebaseDB
import org.wit.juggle.models.AddEventModel
import org.wit.juggle.models.CalendarManager
import org.wit.juggle.models.UserModel
import timber.log.Timber

class AddEventViewModel (app: Application) : AndroidViewModel(app) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val user =
        MutableLiveData<UserModel>()

    val observableUser: LiveData<UserModel>
        get() = user

    val token = app.getString(R.string.temp_bearer_access_token)

    fun addNewEvent(calendarId: String, event: AddEventModel){
        Timber.i("In my new add function"+event.toString())
        CalendarManager.addNewEvent(token, calendarId, event)
    }

    fun getUser(
        firebaseUser: MutableLiveData<FirebaseUser>
    ) {
        try {
            FirebaseDB.getUser(firebaseUser, user)
            Timber.i("Firebase DB User Success : "+user.value?.userUid)
        } catch (e: IllegalArgumentException) {
            Timber.i(e.toString())
        }

    }
}