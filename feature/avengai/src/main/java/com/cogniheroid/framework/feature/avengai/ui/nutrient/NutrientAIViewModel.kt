package com.cogniheroid.framework.feature.avengai.ui.nutrient

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cogniheroid.framework.core.ai.AvengerAIManager
import com.cogniheroid.framework.core.ai.data.model.ModelInput
import com.cogniheroid.framework.feature.avengai.ui.nutrient.uistate.NutrientUIEffect
import com.cogniheroid.framework.feature.avengai.ui.nutrient.uistate.NutrientUIEvent
import com.cogniheroid.framework.feature.avengai.ui.nutrient.uistate.NutrientUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NutrientAIViewModel(private val avengerAIManager : AvengerAIManager) : ViewModel() {

    private var imageFlow : MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    private var isModelStartedGeneratingText = MutableStateFlow(false)

    private var result = MutableStateFlow("")

    val nutrientUIStateFlow = combine(imageFlow, result, isModelStartedGeneratingText){ image, result, isGenerating ->
        NutrientUIState(image != null, result, isGenerating, image)
    }

    private val _nutrientUIEffectChannel = Channel<NutrientUIEffect>()
    val nutrientUIEffectFlow = _nutrientUIEffectChannel.receiveAsFlow()
    fun performIntent(nutrientUIEvent : NutrientUIEvent) {
        when(nutrientUIEvent) {
            is NutrientUIEvent.AddImage -> {
                imageFlow.value = nutrientUIEvent.image
            }

            is NutrientUIEvent.GenerateText -> {
                isModelStartedGeneratingText.value = true
                clearResult()

                nutrientUIEvent.image?.let {
                    generateTextAndUpdateResult(
                        it, getDefaultPrompt(), nutrientUIEvent.defaultErrorMessage)
                }

            }

            NutrientUIEvent.OnOpenImagePicker -> {
                setSideEffects(NutrientUIEffect.UploadImagePicker)
            }

            is NutrientUIEvent.RemoveImage -> {
                imageFlow.value = null
            }
        }
    }

    private fun generateTextAndUpdateResult(
            image : Bitmap, text : String, defaultErrorMessage : String) {
        viewModelScope.launch {
            avengerAIManager
                .generateTextStreamContent(getModelInputList(image, text), defaultErrorMessage)
                .collectLatest {
                    result.value += it ?: ""
                    isModelStartedGeneratingText.value = false
                }
        }
    }

    private fun setSideEffects(nutrientUIEffect : NutrientUIEffect) {
        viewModelScope.launch {
            _nutrientUIEffectChannel.send(nutrientUIEffect)
        }
    }

    private fun getModelInputList(images : Bitmap, text : String) : List<ModelInput> {
        val modelInputList = mutableListOf<ModelInput>()
        images.let {
            modelInputList.add(ModelInput.Image(true, it))
        }
        modelInputList.add(ModelInput.Text(true, text))
        return modelInputList
    }

    private fun clearResult() {
        result.value = ""
    }

    private fun getDefaultPrompt() : String {
       return """
          You are an expert in nutritionist where you need to see the food items from the image 
          and calculate the total calories, proteins, vitamins, minerals, carbohydrates, fats etc 
          and what are nutrient data available please provide it. Also provide the details of every 
          food items with calories intake as below format.  Please follow the below format for generated text
          
          List of food Items present in image
          
          Item 1
          Item 2
          Item 3
          
          
          
       
          ITEM 1(Bold) with no of grams
          
          1. no of calories 
          2. no of protein 
          3. no of Carbohydrate
          4. no of fats 
          5. no of vitamins 
          6. no of minerals
          
          ITEM 2 (Bold) with no of grams
          
           1. no of calories 
          2. no of protein 
          3. no of Carbohydrate
          4. no of fats 
          5. no of vitamins 
          6. no of minerals
            
          ---
          ---
        """.trimIndent()

    }
}