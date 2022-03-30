package org.wit.juggle.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserModel(
    //var uid: String? = "",
    var userUid: String? = "",
    var googleId: String = "",
    var juggled: ArrayList<String> = arrayListOf(),
    var jugglers: ArrayList<String> = arrayListOf()
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {    // required to handle JSON FB DB
        return mapOf(
            //"uid" to uid,
            "userUid" to userUid,
            "googleId" to googleId,
            "juggled" to juggled,
            "jugglers" to jugglers
        )
    }
}
