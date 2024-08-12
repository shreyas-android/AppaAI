package com.cogniheroid.framework.feature.appaai

import android.content.Context
import com.androidai.framework.shared.core.model.GeminiAIAndroidCore
import com.cogniheroid.framework.feature.appaai.ui.viewmodel.FatherAIViewModelFactory
import com.cogniheroid.framework.shared.core.fatherai.AndroidFatherAIDomainCore
import com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI.FatherAIManager
import com.cogniheroid.framework.shared.core.fatherai.utils.PredefinedUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object FatherAICore {

    lateinit var fatherAIViewModelFactory : FatherAIViewModelFactory

    private val fatherAIInfoIdChannel = Channel<Long>()
    val fatherAIInfoIdFlow = fatherAIInfoIdChannel.receiveAsFlow()


    fun init(context: Context, isDebug : Boolean, apiKey : String, modelName : String) {
        GeminiAIAndroidCore.init(isDebug = isDebug, apiKey = apiKey, modelName)
        AndroidFatherAIDomainCore.init(context)
        fatherAIViewModelFactory =
            FatherAIViewModelFactory(GeminiAIAndroidCore.getGeminiAIManagerInstance(),
                FatherAIManager.getInstance())
    }

    fun onPinnedShortcutClicked(id:Long){
        CoroutineScope(Dispatchers.IO).launch {
            fatherAIInfoIdChannel.send(id)
        }
    }

   suspend fun addPredefinedFatherAIInfo(){
       val fatherAIManager = FatherAIManager.getInstance()
        PredefinedUtils.getPredefinedList().forEach {
            fatherAIManager.insertFatherAI(it)
        }
    }
}