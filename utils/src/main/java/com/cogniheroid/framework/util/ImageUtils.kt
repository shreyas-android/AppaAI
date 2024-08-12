package com.cogniheroid.framework.util


import android.text.TextUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import java.util.regex.Pattern

object ImageUtils {

    private fun getInitialStrings(nameString: String?): String {
        var initialNameString = nameString
        if (initialNameString == null) {
            return ""
        } else if (TextUtils.isEmpty(initialNameString)) {
            return ""
        }

        val pattern = Pattern.compile("\\b[a-zA-Z]")
        val matcher = pattern.matcher(initialNameString)
        val initialString = StringBuffer()
        while (matcher.find()) {
            initialString.append(matcher.group())
        }

        initialNameString = if (initialString.toString().length > 2) initialString.toString()
            .substring(0, 1) else initialString.toString()
        return initialNameString.uppercase()
    }
}