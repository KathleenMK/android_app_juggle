package org.wit.juggle.utils

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import org.wit.juggle.R


fun createTickTock(activity: FragmentActivity): AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true) // 'false' if you want user to wait
        .setView(R.layout.ticktock)
    var loader = loaderBuilder.create()
    loader.setTitle(R.string.app_name)
    loader.setIcon(R.mipmap.juggle_text_launcher_round)
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