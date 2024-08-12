package com.cogniheroid.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.androidai.galaxy.ad.ui.theme.FatherAITheme
import com.cogniheroid.android.utils.SharedPrefUtils
import com.cogniheroid.android.utils.ShortcutUtils
import com.cogniheroid.framework.feature.appaai.FatherAICore
import com.cogniheroid.framework.feature.appaai.ui.FatherAINavigation
import kotlinx.coroutines.launch

class FatherAIActivity : BaseActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FatherAITheme {
                FatherAINavigation {
                    ShortcutUtils.pinShortcut(this, it.id, it.title)
                }
            }

        }

        val shortcutValue = ShortcutUtils.getShortcutsExtra(this.intent)
        if(shortcutValue != -1L) {
            FatherAICore.onPinnedShortcutClicked(shortcutValue)
        }

        val isPredefinedAdded = FatherAIApplication.INSTANCE.pref.getBoolean(
            SharedPrefUtils.predefinedKey, false)
        if(!isPredefinedAdded) {
            lifecycleScope.launch {
                FatherAICore.addPredefinedFatherAIInfo()
                FatherAIApplication.INSTANCE.prefEditor.putBoolean(
                    SharedPrefUtils.predefinedKey, true).commit()
            }
        }
    }
}
