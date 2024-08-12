package com.cogniheroid.android.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.cogniheroid.android.FatherAIActivity

object ShortcutUtils {

    const val EXTRAS_SHORTCUT_ID = "shortcut_id"

    private fun getShortcut(context : Context, id:Long, title:String): ShortcutInfo {
        val shortcutIntent = Intent(context, FatherAIActivity::class.java)
        shortcutIntent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
        )
        shortcutIntent.action = Intent.ACTION_VIEW
        shortcutIntent.putExtra(EXTRAS_SHORTCUT_ID, id)
        return ShortcutInfo.Builder(context, title)
            .setShortLabel(title)
            .setLongLabel(title)
            .setIntent(shortcutIntent)
            .build()
    }

    fun pinShortcut(context : Context, id:Long, title:String){
        Log.d("CHECKSHORTCUT","CHECKING TYHE SHORT CUTS ")
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        if (shortcutManager!!.isRequestPinShortcutSupported) {
            val pinShortcutInfo = getShortcut(context, id, title)

            val pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo)


            val successCallback = PendingIntent.getBroadcast(context,0,
                pinnedShortcutCallbackIntent, PendingIntent.FLAG_IMMUTABLE)

            shortcutManager.requestPinShortcut(pinShortcutInfo,
                successCallback.intentSender)
        }
    }

    fun getShortcutsExtra(intent: Intent): Long {
        val shortcutIntentValue = intent.getLongExtra(EXTRAS_SHORTCUT_ID, -1L)
        return run {
            intent.removeExtra(EXTRAS_SHORTCUT_ID)
            shortcutIntentValue
        }
    }
}