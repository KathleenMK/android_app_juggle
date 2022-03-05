package org.wit.juggle.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.wit.juggle.R
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import org.wit.juggle.MainActivity
import androidx.lifecycle.Observer


class JuggleSignIn : AppCompatActivity() , View.OnClickListener{

    //val TAG = "SignInActivity"
    //val RC_SIGN_IN = 9001

//    lateinit var mGoogleSignInClient: GoogleSignInClient
//    //var mGoogleSignInClient: GoogleSignInClient? = null
//    val mStatusTextView: TextView? = null

    private lateinit var signInViewModel : SignInViewModel
    //private lateinit var loginBinding : G
    private lateinit var startForResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

         // Button listeners
        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
        // [END customize_button]

        //findViewById<View>(org.wit.juggle.R.id.sign_in_button).setOnClickListener { view: View? ->
           Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
        //    signIn()
        //}

        //revokeAccess()

        //googleSignIn()

            }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        signInViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, MainActivity::class.java)) })

        signInViewModel.firebaseAuthorization.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
       //val account = JuggleSignIn.getLastSignedInAccount(this)
       //updateUI(account)

        setupGoogleSignInCallback()

    }

    private fun checkStatus(error:Boolean) {
        if (error)
            Toast.makeText(this,
                //getString(R.string.auth_failed),
                "fail",
                Toast.LENGTH_LONG).show()
    }


   override fun onClick(v: View) {
        Log.w(TAG, "in onclick")
        when (v.id) {
            R.id.sign_in_button -> googleSignIn()
            //R.id.sign_out_button -> signOut()
            //R.id.disconnect_button -> revokeAccess()
        }
    }

    private fun googleSignIn() {
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
        val signInIntent = signInViewModel.firebaseAuthorization
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            startActivity(Intent(this, MainActivity::class.java))
            // Signed in successfully, show authenticated UI.
            updateUI(account)
           } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }


    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            //startActivity(MainActivity)
            Log.w(TAG, "line 151 :${account.account}")
            Log.w(TAG, "line 151 :${account.id}")
            Log.w(TAG, "line 151 :${account.displayName}")
            Log.w(TAG, "line 151 :${account.requestedScopes}")
            //mStatusTextView!!.text = getString(org.wit.juggle.R.string.signed_in_fmt, account.displayName)
            //findViewById<View>(org.wit.juggle.R.id.sign_in_button).visibility = View.GONE
            //findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            //mStatusTextView?.setText(org.wit.juggle.R.string.signed_out)
            findViewById<View>(R.id.sign_in_button).visibility = View.VISIBLE
            //findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            signInViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                           // Timber.i( "Google sign in failed $e")
                           // Snackbar.make(loginBinding.loginLayout, "Authentication Failed.",
                          //      Snackbar.LENGTH_SHORT).show()
                        }
                       // Timber.i("DonationX Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    } else -> { }
                }
            }
    }
}


