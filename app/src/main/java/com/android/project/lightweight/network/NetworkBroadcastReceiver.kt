package com.android.project.lightweight.network

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.android.project.lightweight.R
import com.android.project.lightweight.util.UIUtils
import com.google.android.material.snackbar.Snackbar


object NetworkBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetworkUtil.hasInternetConnection(context)
        if (!isConnected) {
            UIUtils.createAnchoredSnackbar(context as Activity, context.getString(R.string.snackbar_offline_text), Snackbar.LENGTH_INDEFINITE).apply {
                setAction(context.getString(R.string.snackbar_offline_action_text)) {
                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                }
            }.show()
        }
    }
}