package com.cogniheroid.framework.shared.core.fatherai.repository.fatherai


import app.cash.paging.PagingSource
import com.cogniheroid.framework.shared.core.fatherai.datasource.fatherai.FatherAIDataSource
import kotlinx.coroutines.flow.Flow
import migrations.FatherAIEntity

class FatherAIRepositoryImpl(private val fatherAIDataSource : FatherAIDataSource): FatherAIRepository {

    override suspend fun getLastLocalFatherAIId() : Long {
        return fatherAIDataSource.getLastLocalFatherAIId()
    }

    override suspend fun getFatherAIInfoById(id : Long) : FatherAIEntity {
        return fatherAIDataSource.getFatherAIInfoById(id)
    }

    override fun getFatherAIList() : PagingSource<Int, FatherAIEntity> {
       return fatherAIDataSource.getFatherAIList()
    }

    override suspend fun insertFatherAI(fatherAIEntity : FatherAIEntity) {
        fatherAIDataSource.insertFatherAI(fatherAIEntity)
    }

    override suspend fun updateFatherAI(fatherAIEntity : FatherAIEntity) {
        fatherAIDataSource.updateFatherAI(fatherAIEntity)
    }

    override suspend fun deleteFatherAI(id : Long) {
        fatherAIDataSource.deleteFatherAI(id)
    }

}