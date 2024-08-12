package com.cogniheroid.framework.shared.core.fatherai.datasource.fatherai

import app.cash.paging.PagingSource
import com.cogniheroid.framework.shared.core.fatherai.FatherAIDomainCore
import kotlinx.coroutines.flow.Flow
import migrations.FatherAIEntity

interface FatherAIDataSource {

    companion object{
        fun getInstance(): FatherAIDataSource {
            return FatherAIDomainCore.instance.fatherAIDataSource
        }
    }

    suspend fun getFatherAIInfoById(id:Long):FatherAIEntity

    suspend fun getLastLocalFatherAIId(): Long

    fun getFatherAIList(): PagingSource<Int, FatherAIEntity>

    suspend fun insertFatherAI(fatherAIEntity: FatherAIEntity)

    suspend fun updateFatherAI(fatherAIEntity: FatherAIEntity)

    suspend fun deleteFatherAI(id: Long)
}
