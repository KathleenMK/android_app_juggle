package org.wit.juggle.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    var userUid: String = "",
    var googleId: String = "",
    var jugglers: HashMap<String,String> = hashMapOf(),
    var juggled: HashMap<String,String> = hashMapOf()
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {    // required to handle JSON FB DB
        return mapOf(
             "userUid" to userUid,
            "googleId" to googleId,
            "jugglers" to jugglers,
            "juggled" to juggled
        )
    }
}
