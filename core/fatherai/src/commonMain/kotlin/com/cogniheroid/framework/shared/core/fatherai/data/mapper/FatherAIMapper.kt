package com.cogniheroid.framework.shared.core.fatherai.data.mapper

import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel
import com.cogniheroid.framework.shared.core.fatherai.data.enum.ModelReturnType
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import migrations.FatherAIEntity

fun FatherAIInfo.toFatherAIEntity() : FatherAIEntity {
    return FatherAIEntity(id, model.modelName, title, generateButtonText, modelReturnType.value,
        isInputNeeded = isInputNeeded,
        mediaType.value, prompt)
}

fun FatherAIInfo.toNewFatherAIEntity(id:Long) : FatherAIEntity {
    return FatherAIEntity(id, model.modelName, title, generateButtonText, modelReturnType.value,
        isInputNeeded = isInputNeeded,
        mediaType.value, prompt)
}

fun FatherAIEntity.toFatherAIInfo() : FatherAIInfo {
   return FatherAIInfo(id, title,  modelName.toGeminiAIModel(),
        fileType.toFileMimeType(),
        buttonText,
        isInputNeeded = isInputNeeded,
        returnType.toModelReturnType(), prompt)
}

fun String.toGeminiAIModel() : GeminiAIModel {
   return when(this){
        GeminiAIModel.GEMINI_FLASH.modelName ->  GeminiAIModel.GEMINI_FLASH
        else -> {
            GeminiAIModel.GEMINI_PRO
        }
    }
}

fun Int.toFileMimeType() : MediaType {
   return when(this){
       MediaType.ALL.value -> MediaType.ALL
        MediaType.IMAGE.value -> MediaType.IMAGE
        MediaType.VIDEO.value -> MediaType.VIDEO
        MediaType.AUDIO.value -> MediaType.AUDIO
        MediaType.DOCUMENT.value -> MediaType.DOCUMENT
       else -> MediaType.NONE

    }
}

fun Int.toModelReturnType() : ModelReturnType {
    return when(this){
        ModelReturnType.TEXT.value -> ModelReturnType.TEXT
        ModelReturnType.CODE_EXECUTION.value -> ModelReturnType.CODE_EXECUTION
        ModelReturnType.JSON.value -> ModelReturnType.JSON
        ModelReturnType.TABLE.value -> ModelReturnType.TABLE
       else -> {
           ModelReturnType.HTML
       }
    }
}