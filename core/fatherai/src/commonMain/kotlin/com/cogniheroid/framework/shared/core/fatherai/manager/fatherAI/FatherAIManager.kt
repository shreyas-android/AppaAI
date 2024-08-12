package com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI

import app.cash.paging.PagingSource
import com.cogniheroid.framework.shared.core.fatherai.FatherAIDomainCore
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import migrations.FatherAIEntity

interface FatherAIManager {

    companion object{

        fun getInstance(): FatherAIManager {
            return FatherAIDomainCore.instance.fatherAIManager
        }
    }

    suspend fun getFatherAIInfoById(id:Long):FatherAIInfo

    suspend fun getLastLocalFatherAIId(): Long

    fun getFatherAIPagingSource(): PagingSource<Int, FatherAIEntity>

    suspend fun insertFatherAI(fatherAIInfo: FatherAIInfo)

    suspend fun updateFatherAI(fatherAIInfo: FatherAIInfo)

    suspend fun deleteFatherAI(fatherAIInfo : FatherAIInfo)
}