package org.wit.juggle.firebaseintegration

import android.R.attr
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.wit.juggle.R
import timber.log.Timber
import android.R.attr.data
import com.google.android.gms.auth.GoogleAuthUtil

import com.google.android.gms.auth.api.Auth

import com.google.android.gms.auth.api.signin.GoogleSignInResult





class FirebaseAuthorization (application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        var serverClientId =
            application!!.getString(R.string.default_web_client_id)    // oauth_client client_id
        Timber.i("configureGoogleGI serverClientId " + serverClientId)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/calendar"))
            //.requestScopes(Scope("https://www.googleapis.com/auth/calendar.readonly"))
            .requestIdToken(serverClientId)
            .requestServerAuthCode(serverClientId)
            .requestEmail()
            .build()

        googleSignInClient.value = GoogleSignIn.getClient(application!!.applicationContext, gso)
        Timber.i("line 61 FirebaseA: "+googleSignInClient.value.toString())
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update with the signed-in user's information
                    Timber.i( "signInWithCredential:success")
                    Timber.i( "line 58: "+acct.idToken)
                    Timber.i("line 59: " + acct.serverAuthCode)
                    Timber.i(acct.grantedScopes.toString())
                    Timber.i(acct.requestedScopes.toString())
                    Timber.i("line 64"+task.result.toString())
                    Timber.i("line 64"+acct.idToken)
                    Timber.i(acct.account.toString())
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)

                } else {
                    // If sign in fails, display a message to the user.
                   Timber.i( "signInWithCredential:failure $task.exception")
                    errorStatus.postValue(true)

                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        googleSignInClient.value!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }

    fun revoke() {
        firebaseAuth!!.signOut()
        googleSignInClient.value!!.revokeAccess()
        googleSignInClient.value!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }

}