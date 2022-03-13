package org.wit.juggle.utils

import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import org.wit.juggle.R


fun createTickTock(activity: FragmentActivity) : AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true) // 'false' if you want user to wait
        .setView(R.layout.ticktock)
    var loader = loaderBuilder.create()
    loader.setTitle(R.string.app_name)
    loader.setIcon(R.mipmap.ic_launcher_round)

    return loader
}

fun showTickTock(ticktock: AlertDialog, message: String) {
    if (!ticktock.isShowing) {
        ticktock.setTitle(message)
        ticktock.show()
    }
}

fun hideTickTock(ticktock: AlertDialog) {
    if (ticktock.isShowing)
        ticktock.dismiss()
}

fun calendarUnavailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Calendar API is not responding...",
        Toast.LENGTH_LONG
    ).show()
}

fun calendarAvailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "Calendar API is looking good...",
        Toast.LENGTH_LONG
    ).show()
}