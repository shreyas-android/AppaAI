package com.cogniheroid.framework.shared.core.fatherai.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.cogniheroid.framework.shared.core.fatherai.database.fatherai.newInstance
import com.cogniheroid.framework.shared.core.fatherai.database.fatherai.schema
import database.FatheraiQueries
import kotlin.Unit
import migrations.FatherAIEntity

public interface FatherAIDatabase : Transacter {
  public val fatheraiQueries: FatheraiQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = FatherAIDatabase::class.schema

    public operator fun invoke(driver: SqlDriver, FatherAIEntityAdapter: FatherAIEntity.Adapter):
        FatherAIDatabase = FatherAIDatabase::class.newInstance(driver, FatherAIEntityAdapter)
  }
}
