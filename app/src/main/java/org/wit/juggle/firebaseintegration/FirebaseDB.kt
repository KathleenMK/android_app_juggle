package org.wit.juggle.firebaseintegration

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.wit.juggle.R
import org.wit.juggle.models.UserModel
import timber.log.Timber

object FirebaseDB {

    private var database: DatabaseReference = FirebaseDatabase.getInstance("").reference

   fun saveUser(firebaseUser: MutableLiveData<FirebaseUser>,  jugglers: ArrayList<String>, juggled: ArrayList<String>) {
            Timber.i("in FirebaseDB createUser")
        val userUid = firebaseUser.value!!.uid
       val googleId = firebaseUser.value!!.email.toString()

       val user = UserModel(userUid,googleId,jugglers,juggled)

        val userValues = user.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/users/$userUid"] = userValues


        database.updateChildren(childAdd)
    }

}