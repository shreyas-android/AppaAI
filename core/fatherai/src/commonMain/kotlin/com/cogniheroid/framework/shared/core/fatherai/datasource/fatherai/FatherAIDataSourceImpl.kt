package com.cogniheroid.framework.shared.core.fatherai.datasource.fatherai

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import com.cogniheroid.framework.shared.core.fatherai.datasource.fatherai.FatherAIDataSource
import database.FatheraiQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import migrations.FatherAIEntity
import kotlin.coroutines.CoroutineContext

class FatherAIDataSourceImpl(private val dispatcher:CoroutineContext,
                             private val fatherAIQueries: FatheraiQueries): FatherAIDataSource {
    override suspend fun getLastLocalFatherAIId(): Long {
        return fatherAIQueries.getLastLocalFatherAIId().executeAsOne().MAX ?: 1L
    }

    override suspend fun getFatherAIInfoById(id : Long) : FatherAIEntity {
        return fatherAIQueries.getFatherAIInfoById(id).executeAsOne()
    }

    override fun getFatherAIList() : PagingSource<Int, FatherAIEntity> {
        return QueryPagingSource(countQuery = fatherAIQueries.countFatherAI(),
            transacter = fatherAIQueries, context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                fatherAIQueries.getFatherAIListByOffset(limit, offset)
            })
    }

    override suspend fun insertFatherAI(fatherAIEntity : FatherAIEntity) {
        fatherAIQueries.insertFatherAI(fatherAIEntity.id, fatherAIEntity.modelName,
            fatherAIEntity.title, fatherAIEntity.buttonText, fatherAIEntity.returnType,
            fatherAIEntity.isInputNeeded, fatherAIEntity.fileType, fatherAIEntity.prompt)
    }

    override suspend fun updateFatherAI(fatherAIEntity : FatherAIEntity) {
        fatherAIQueries.updateFatherAI( fatherAIEntity.modelName,
            fatherAIEntity.title, fatherAIEntity.buttonText, fatherAIEntity.returnType,
            fatherAIEntity.isInputNeeded, fatherAIEntity.fileType, fatherAIEntity.prompt,
            fatherAIEntity.id)
    }

    override suspend fun deleteFatherAI(id : Long){
        fatherAIQueries.deleteFatherAI(id)
    }
}