package com.cogniheroid.framework.shared.core.fatherai

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.cogniheroid.framework.shared.core.fatherai.database.FatherAIDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(schema = FatherAIDatabase.Schema, context =  context, name = "android_chat.db")
    }
}