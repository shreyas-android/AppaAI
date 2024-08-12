package com.cogniheroid.framework.shared.core.fatherai.database.fatherai

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.cogniheroid.framework.shared.core.fatherai.database.FatherAIDatabase
import database.FatheraiQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass
import migrations.FatherAIEntity

internal val KClass<FatherAIDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = FatherAIDatabaseImpl.Schema

internal fun KClass<FatherAIDatabase>.newInstance(driver: SqlDriver,
    FatherAIEntityAdapter: FatherAIEntity.Adapter): FatherAIDatabase = FatherAIDatabaseImpl(driver,
    FatherAIEntityAdapter)

private class FatherAIDatabaseImpl(
  driver: SqlDriver,
  FatherAIEntityAdapter: FatherAIEntity.Adapter,
) : TransacterImpl(driver), FatherAIDatabase {
  override val fatheraiQueries: FatheraiQueries = FatheraiQueries(driver, FatherAIEntityAdapter)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE FatherAIEntity (
          |    id INTEGER PRIMARY KEY,
          |    modelName TEXT NOT NULL,
          |    title TEXT NOT NULL,
          |    buttonText TEXT NOT NULL,
          |    returnType INTEGER DEFAULT 0 NOT NULL,
          |    isInputNeeded INTEGER DEFAULT 0 CHECK (isInputNeeded IN (0, 1)) NOT NULL,
          |    fileType INTEGER DEFAULT 0 NOT NULL,
          |    prompt TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    private fun migrateInternal(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
    ): QueryResult.Value<Unit> {
      if (oldVersion <= 0 && newVersion > 0) {
        driver.execute(null, """
            |CREATE TABLE FatherAIEntity (
            |    id INTEGER PRIMARY KEY,
            |    modelName TEXT NOT NULL,
            |    title TEXT NOT NULL,
            |    buttonText TEXT NOT NULL,
            |    returnType INTEGER DEFAULT 0 NOT NULL,
            |    isInputNeeded INTEGER DEFAULT 0 CHECK (isInputNeeded IN (0, 1)) NOT NULL,
            |    fileType INTEGER DEFAULT 0 NOT NULL,
            |    prompt TEXT NOT NULL
            |)
            """.trimMargin(), 0)
        driver.execute(null, """
            |INSERT INTO FatherAIEntity (id, modelName, title, buttonText, returnType, isInputNeeded, fileType, prompt)
            |VALUES (1,"gemini-1.5-pro", "Video transcript","Generate transcript", 4,1,4,"Analyze the video and give a transcript for it with duration")
            """.trimMargin(), 0)
      }
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> {
      var lastVersion = oldVersion

      callbacks.filter { it.afterVersion in oldVersion until newVersion }
      .sortedBy { it.afterVersion }
      .forEach { callback ->
        migrateInternal(driver, oldVersion = lastVersion, newVersion = callback.afterVersion + 1)
        callback.block(driver)
        lastVersion = callback.afterVersion + 1
      }

      if (lastVersion < newVersion) {
        migrateInternal(driver, lastVersion, newVersion)
      }
      return QueryResult.Unit
    }
  }
}
