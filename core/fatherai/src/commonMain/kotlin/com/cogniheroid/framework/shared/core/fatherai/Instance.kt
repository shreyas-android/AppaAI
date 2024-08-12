package com.cogniheroid.framework.shared.core.fatherai

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI.FatherAIManagerImpl
import com.cogniheroid.framework.shared.core.fatherai.database.FatherAIDatabase
import com.cogniheroid.framework.shared.core.fatherai.datasource.fatherai.FatherAIDataSourceImpl
import com.cogniheroid.framework.shared.core.fatherai.repository.fatherai.FatherAIRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import migrations.FatherAIEntity

class Instance(private val driver: SqlDriver) {

    private fun getDatabase(): FatherAIDatabase {

        val intAdapter = object : ColumnAdapter<Int, Long> {
            override fun decode(databaseValue: Long): Int {
                return databaseValue.toInt()
            }

            override fun encode(value: Int): Long {
                return value.toLong()
            }
        }

        val booleanAdapter = object : ColumnAdapter<Boolean, Long> {
            override fun decode(databaseValue: Long): Boolean {
                return if (databaseValue == 1L) {
                    true
                } else {
                    false
                }
            }

            override fun encode(value: Boolean): Long {
                return if (value) {
                    1
                } else {
                    0
                }
            }
        }

       return FatherAIDatabase(
           driver, FatherAIEntityAdapter = FatherAIEntity.Adapter(intAdapter, intAdapter))
    }

    private val fatherAIDatabase by lazy {
        getDatabase()
    }

   private val dispatcher by lazy {
        Dispatchers.IO
    }

    val fatherAIDataSource by lazy {
        FatherAIDataSourceImpl(dispatcher, fatherAIDatabase.fatheraiQueries)
    }

     val fatherAIRepository by lazy {
        FatherAIRepositoryImpl(fatherAIDataSource)
    }



    val fatherAIManager by lazy {
        FatherAIManagerImpl(fatherAIRepository)
    }



}