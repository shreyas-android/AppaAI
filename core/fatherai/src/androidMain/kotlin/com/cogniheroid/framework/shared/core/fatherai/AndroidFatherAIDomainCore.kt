package com.cogniheroid.framework.shared.core.fatherai

import android.content.Context

object AndroidFatherAIDomainCore {

    fun init(context: Context){
        val databaseDriverFactory =
            DatabaseDriverFactory(context)
        FatherAIDomainCore.init(databaseDriverFactory)
    }

}