package com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI

import app.cash.paging.PagingSource
import com.cogniheroid.framework.shared.core.fatherai.UniqueIdGenerator
import com.cogniheroid.framework.shared.core.fatherai.data.mapper.toFatherAIEntity
import com.cogniheroid.framework.shared.core.fatherai.data.mapper.toFatherAIInfo
import com.cogniheroid.framework.shared.core.fatherai.data.mapper.toNewFatherAIEntity
import com.cogniheroid.framework.shared.core.fatherai.repository.fatherai.FatherAIRepository
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import migrations.FatherAIEntity

class FatherAIManagerImpl(private val fatherAIRepository: FatherAIRepository): FatherAIManager {

    private val localFatherAIIdGenerator = UniqueIdGenerator {
        fatherAIRepository.getLastLocalFatherAIId()
    }

    override suspend fun getFatherAIInfoById(id : Long) : FatherAIInfo {
        return fatherAIRepository.getFatherAIInfoById(id).toFatherAIInfo()
    }



    override suspend fun getLastLocalFatherAIId() : Long {
        return localFatherAIIdGenerator.get()
    }

    override fun getFatherAIPagingSource() : PagingSource<Int, FatherAIEntity> {
        return fatherAIRepository.getFatherAIList()
    }

    override suspend fun insertFatherAI(fatherAIInfo : FatherAIInfo) {
        val id = getLastLocalFatherAIId()
        fatherAIRepository.insertFatherAI(fatherAIInfo.toNewFatherAIEntity(id))
    }

    override suspend fun updateFatherAI(fatherAIInfo : FatherAIInfo) {
        fatherAIRepository.updateFatherAI(fatherAIInfo.toFatherAIEntity())
    }

    override suspend fun deleteFatherAI(fatherAIInfo : FatherAIInfo) {
      fatherAIRepository.deleteFatherAI(fatherAIInfo.id)
    }

}