package org.wit.juggle.ui.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import org.wit.juggle.firebaseintegration.FirebaseAuthorization

class SignInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthorization: FirebaseAuthorization = FirebaseAuthorization(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthorization.liveFirebaseUser

    fun authWithGoogle(acct: GoogleSignInAccount) {
        firebaseAuthorization.firebaseAuthWithGoogle(acct)
    }
}