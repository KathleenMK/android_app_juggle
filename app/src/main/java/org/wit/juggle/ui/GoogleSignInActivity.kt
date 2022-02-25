package org.wit.juggle.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import org.wit.juggle.R
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import org.wit.juggle.MainActivity


class GoogleSignInActivity : AppCompatActivity() , View.OnClickListener{

    //val TAG = "SignInActivity"
    //val RC_SIGN_IN = 9001

    lateinit var mGoogleSignInClient: GoogleSignInClient
    //var mGoogleSignInClient: GoogleSignInClient? = null
    val mStatusTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/calendar"))
           // .requestScopes(Scope ("https://www.googleapis.com/auth/calendar.readonly"))
           // .requestScopes(Scope ("https://www.googleapis.com/auth/calendar.events"))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //mGoogleSignInClient = GoogleSignIn.getClient(application!!.applicationContext, gso);


        // Button listeners
        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)
        //findViewById<View>(R.id.sign_out_button).setOnClickListener(this)
        //findViewById<View>(R.id.disconnect_button).setOnClickListener(this)

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
        // [END customize_button]

        //findViewById<View>(org.wit.juggle.R.id.sign_in_button).setOnClickListener { view: View? ->
        //    Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
        //    signIn()
        //}

        revokeAccess()

            }

    public override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
       val account = GoogleSignIn.getLastSignedInAccount(this)
       updateUI(account)

    }


   override fun onClick(v: View) {
        Log.w(TAG, "in onclick")
        when (v.id) {
            R.id.sign_in_button -> signIn()
            //R.id.sign_out_button -> signOut()
            //R.id.disconnect_button -> revokeAccess()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        Log.w(TAG, "line 105 : sign out")
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                     updateUI(null)
                           }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                             updateUI(null)
                          }
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
            Log.w(TAG, "line 151 :$account")
            Log.w(TAG, "line 151 :${account.displayName}")
            //mStatusTextView!!.text = getString(org.wit.juggle.R.string.signed_in_fmt, account.displayName)
            //findViewById<View>(org.wit.juggle.R.id.sign_in_button).visibility = View.GONE
            //findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.VISIBLE
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            mStatusTextView?.setText(org.wit.juggle.R.string.signed_out)
            findViewById<View>(R.id.sign_in_button).visibility = View.VISIBLE
            //findViewById<View>(R.id.sign_out_and_disconnect).visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}


