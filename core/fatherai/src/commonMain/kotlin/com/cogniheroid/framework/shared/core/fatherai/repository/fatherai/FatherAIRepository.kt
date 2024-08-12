package com.cogniheroid.framework.shared.core.fatherai.repository.fatherai

import app.cash.paging.PagingSource
import com.cogniheroid.framework.shared.core.fatherai.FatherAIDomainCore
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import kotlinx.coroutines.flow.Flow
import migrations.FatherAIEntity

interface FatherAIRepository {

    companion object{

            fun getInstance(): FatherAIRepository {
                return FatherAIDomainCore.instance.fatherAIRepository
            }
    }

    suspend fun getLastLocalFatherAIId(): Long

    suspend fun getFatherAIInfoById(id:Long):FatherAIEntity

    fun getFatherAIList(): PagingSource<Int, FatherAIEntity>

    suspend fun insertFatherAI(fatherAIEntity: FatherAIEntity)

    suspend fun updateFatherAI(fatherAIEntity: FatherAIEntity)

    suspend fun deleteFatherAI(id: Long)
}