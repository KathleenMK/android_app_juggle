package org.wit.juggle.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.firebaseintegration.FirebaseAuthorization
import org.wit.juggle.models.Calendar

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
        MutableLiveData<List<Calendar>>()

    val observableCalendarsList: LiveData<List<Calendar>>
        get() = calendarsList

    val text: LiveData<String> = _text
}