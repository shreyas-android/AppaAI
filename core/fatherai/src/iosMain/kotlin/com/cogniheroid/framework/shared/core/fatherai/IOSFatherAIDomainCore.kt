package com.cogniheroid.framework.shared.core.fatherai

object IOSFatherAIDomainCore {

    fun init(){
        val databaseDriverFactory =
            DatabaseDriverFactory()
        FatherAIDomainCore.init(databaseDriverFactory)
    }
}