package org.wit.juggle.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.CalendarScopes
import org.wit.juggle.MainActivity
import org.wit.juggle.R
import timber.log.Timber


class JuggleSignIn : AppCompatActivity(), View.OnClickListener {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.activity_google_sign_in)

        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
    }

    public override fun onStart() {
        super.onStart()
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        signInViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser ->
            if (firebaseUser != null)
                startActivity(Intent(this, MainActivity::class.java))
        })
        signInViewModel.firebaseAuthorization.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })
        setupGoogleSignInCallback()

    }

    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(
                this,
                "fail",
                Toast.LENGTH_LONG
            ).show()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> googleSignIn()
        }
    }

    private fun googleSignIn() {
        val signInIntent = signInViewModel.firebaseAuthorization
            .googleSignInClient.value!!.signInIntent
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(signInIntent)
        Timber.i("line 110" + result.toString())
        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        Timber.i("in setupGoogleSIgnInCallback()")
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        Timber.i("task.result.idToken: " + task.result.idToken)
                        Timber.i("task.result.serverAuthCode: " + task.result.serverAuthCode)
                        //trying to find the access token
                        var mCredential: GoogleAccountCredential? = null
                        mCredential =
                            GoogleAccountCredential.usingOAuth2(
                                applicationContext,
                                arrayListOf(CalendarScopes.CALENDAR)
                            )
                                .setBackOff(ExponentialBackOff())
                        Timber.i("credential line 187: " + mCredential.toString())
                        Timber.i(mCredential.selectedAccountName)
//                        val transport = AndroidHttp.newCompatibleTransport()
//                        val jsonFactory = JacksonFactory.getDefaultInstance()
//                        val service = com.google.api.services.calendar.Calendar.Builder(
//                            transport, jsonFactory, mCredential)
//                            .setApplicationName("Juggle")
//                            .build()
//
//                        val calendar = service.calendars().get("primary").execute()
//
//                        println("println"+calendar.summary)
                        // end of trying to find the access token


                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            Timber.i("account.grantedScopes.toString(): " + account.grantedScopes.toString())
                            signInViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            // Timber.i( "Google sign in failed $e")
                        }
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {}
                }
            }
    }
}


