package org.wit.juggle.firebaseintegration

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.wit.juggle.models.RelatedEventModel
import org.wit.juggle.models.UserModel
import timber.log.Timber

object FirebaseDB {

    private var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://juggle-90c87-default-rtdb.europe-west1.firebasedatabase.app").reference

    fun saveUser(
        firebaseUser: MutableLiveData<FirebaseUser>,
        jugglers: HashMap<String, String>,
        juggled: HashMap<String, String>
    ) {
        Timber.i("in FirebaseDB createUser")
        val userUid = firebaseUser.value!!.uid
        val googleId = firebaseUser.value!!.email.toString()
        val userName = firebaseUser.value!!.displayName.toString()

        val user = UserModel(userUid, googleId, userName, jugglers, juggled)

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
        }.addOnFailureListener {
            Timber.e("firebase Error getting user $it")
        }
    }

    fun saveRelatedEvent(eventId: String, id: String, relatedEvent: RelatedEventModel) {
        Timber.i("in FirebaseDB saveRelatedEvent")

       val relatedEventValues = relatedEvent.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/events/$eventId/$id"] = relatedEventValues


        database.updateChildren(childAdd)
    }

    fun getRelatedEvents(eventId: String, relatedEvents: MutableLiveData<List<RelatedEventModel>>) {
        Timber.i("in FirebaseDB getRelatedEvent")

        database.child("events").child(eventId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.i("Firebase getting related events error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val localList = ArrayList<RelatedEventModel>()
                val children = snapshot.children
                children.forEach {
                    val relatedEvent = it.getValue(RelatedEventModel::class.java)
                    localList.add(relatedEvent!!)
                    Timber.i("relatedEvent = "+relatedEvent.toString())
                }
                database.child("events").child(eventId)
                    .removeEventListener(this)

                relatedEvents.value = localList
            }
        })


    }
}