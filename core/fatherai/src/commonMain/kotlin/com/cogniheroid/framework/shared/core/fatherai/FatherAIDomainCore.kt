package com.cogniheroid.framework.shared.core.fatherai

internal object FatherAIDomainCore {

    lateinit var instance: Instance
    fun init(databaseDriverFactory: DatabaseDriverFactory){
        instance = Instance(databaseDriverFactory.createDriver())
    }
}