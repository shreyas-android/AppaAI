package com.cogniheroid.framework.shared.core.fatherai.data.model

import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel
import com.cogniheroid.framework.shared.core.fatherai.data.enum.ModelReturnType

data class FatherAIInfo(
        val id:Long,
        val title : String, val model:GeminiAIModel, val mediaType : MediaType,
        val generateButtonText : String,
        val isInputNeeded: Boolean,
        val modelReturnType : ModelReturnType, val prompt : String) {

    companion object {

        fun getDefault() =
            FatherAIInfo(1L,
                "", GeminiAIModel.GEMINI_PRO,
                MediaType.NONE, "", true,  ModelReturnType.TEXT,
                "")
    }
}



