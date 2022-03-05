package org.wit.juggle.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    var googleSignInAccount = MutableLiveData<GoogleSignInAccount>()
    var googleSignInClient = MutableLiveData<GoogleSignInClient>()

    val text: LiveData<String> = _text
}