package org.wit.juggle.ui.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.firebaseintegration.FirebaseAuthorization

class SignedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthorization: FirebaseAuthorization = FirebaseAuthorization(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthorization.liveFirebaseUser
    var loggedOut: MutableLiveData<Boolean> = firebaseAuthorization.loggedOut
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    fun logOut() {
        firebaseAuthorization.logOut()
    }

    fun revoke() {
        firebaseAuthorization.revoke()
    }
}