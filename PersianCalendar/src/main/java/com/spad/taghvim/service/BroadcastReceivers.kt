package com.spad.taghvim.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.spad.taghvim.BROADCAST_ALARM
import com.spad.taghvim.BROADCAST_RESTART_APP
import com.spad.taghvim.BROADCAST_UPDATE_APP
import com.spad.taghvim.KEY_EXTRA_PRAYER_KEY
import com.spad.taghvim.utils.loadApp
import com.spad.taghvim.utils.startAthan
import com.spad.taghvim.utils.startEitherServiceOrWorker
import com.spad.taghvim.utils.update

/**
 * Startup broadcast receiver
 */
class BroadcastReceivers : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED, TelephonyManager.ACTION_PHONE_STATE_CHANGED, BROADCAST_RESTART_APP -> startEitherServiceOrWorker(
                context
            )

            Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIMEZONE_CHANGED -> {
                update(context, true)
                loadApp(context)
            }

            Intent.ACTION_TIME_CHANGED, Intent.ACTION_SCREEN_ON, BROADCAST_UPDATE_APP -> {
                update(context, false)
                loadApp(context)
            }

            BROADCAST_ALARM -> {
                val prayTimeKey = intent.getStringExtra(KEY_EXTRA_PRAYER_KEY) ?: return
                startAthan(context, prayTimeKey)
            }
        }
    }
}
