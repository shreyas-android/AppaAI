package com.cogniheroid.framework.feature.appaai.ui.uistate

import android.graphics.Bitmap
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate
import com.cogniheroid.framework.feature.appaai.utils.FileUriInfo
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import java.net.URI

data class FatherAIInfoUIState(
        val homeUIState : HomeUIState, val createFatherAIInfoUIState : CreateFatherAIInfoUIState,
        val customFatherAIInfoUIState : CustomFatherAIInfoUIState)

data class HomeUIState(val fatherAIInfoItems : LazyPagingItems<FatherAIInfo>){

}

data class CreateFatherAIInfoUIState( val fatherAIInfo : FatherAIInfo, val isNew:Boolean){

    companion object{
        fun getDefault() = CreateFatherAIInfoUIState(FatherAIInfo.getDefault(), true)
    }
}

data class CustomFatherAIInfoUIState(
        val fatherAIInfo : FatherAIInfo,
        val inputText : String,
        val outputText : String,
        val geminiAIGenerate : GeminiAIGenerate?,
        val fileUriInfoItems : List<FileUriInfo>,
){
    companion object{

        fun getDefault() = CustomFatherAIInfoUIState(FatherAIInfo.getDefault(), "", "",
            null, listOf())
    }
}