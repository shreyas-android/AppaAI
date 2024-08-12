package com.cogniheroid.framework.feature.appaai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidai.framework.shared.core.model.manager.GeminiAIManager
import com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI.FatherAIManager

class FatherAIViewModelFactory(private val geminiAIManager : GeminiAIManager, private val fatherAIManager : FatherAIManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FatherAIViewModel(geminiAIManager, fatherAIManager) as T
    }
}