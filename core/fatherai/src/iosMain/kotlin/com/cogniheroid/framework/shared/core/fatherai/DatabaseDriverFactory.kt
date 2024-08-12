package com.cogniheroid.framework.shared.core.fatherai

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.cogniheroid.framework.shared.core.fatherai.database.FatherAIDatabase

actual class DatabaseDriverFactory {
  actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(FatherAIDatabase.Schema, "ios_father_ai.db")
  }
}