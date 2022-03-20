package org.wit.juggle

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import org.wit.juggle.databinding.ActivityMainBinding
import org.wit.juggle.ui.signin.SignedInViewModel
import org.wit.juggle.ui.signin.JuggleSignIn


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var signedInViewModel: SignedInViewModel
    private val mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_eventslist, R.id.navigation_addevent
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    public override fun onStart() {
        super.onStart()
        signedInViewModel = ViewModelProvider(this).get(SignedInViewModel::class.java)
        signedInViewModel.liveFirebaseUser.observe(this, { firebaseUser ->
            if (firebaseUser != null) {
                //updateNavHeader(firebaseUser)
                Toast.makeText(
                    this,
                    //getString(R.string.auth_failed),
                    "Welcome " + signedInViewModel.liveFirebaseUser.value?.displayName,
                    Toast.LENGTH_LONG
                ).show()


            }
        })

        signedInViewModel.loggedOut.observe(this, { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, JuggleSignIn::class.java))
            }
        })
    }


    // following menu methods adapted from https://developer.android.com/guide/topics/ui/menus 05Mar22
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_juggle, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_signout -> {
                signedInViewModel.logOut()
                true
            }
            R.id.menu_revoke -> {
                signedInViewModel.revoke()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}