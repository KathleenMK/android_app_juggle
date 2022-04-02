package org.wit.juggle.firebaseintegration

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.wit.juggle.models.UserModel
import timber.log.Timber

object FirebaseDB {

    private var database: DatabaseReference = FirebaseDatabase.getInstance("").reference

   fun saveUser(firebaseUser: MutableLiveData<FirebaseUser>,  jugglers: HashMap<String,String>, juggled: HashMap<String,String>) {
            Timber.i("in FirebaseDB createUser")
        val userUid = firebaseUser.value!!.uid
       val googleId = firebaseUser.value!!.email.toString()

       val user = UserModel(userUid,googleId,jugglers,juggled)

        val userValues = user.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/users/$userUid"] = userValues


        database.updateChildren(childAdd)
    }

    fun getUser(firebaseUser: MutableLiveData<FirebaseUser>, user: MutableLiveData<UserModel>) {

        val userUid = firebaseUser.value!!.uid
        database.child("users").child(userUid).get().addOnSuccessListener {
            user.value = it.getValue(UserModel::class.java)
            Timber.i("firebase Got User ${it.value}")
        }.addOnFailureListener{
            Timber.e("firebase Error getting user $it")
        }
    }


}