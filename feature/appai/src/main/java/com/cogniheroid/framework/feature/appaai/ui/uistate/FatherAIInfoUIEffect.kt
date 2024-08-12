package com.cogniheroid.framework.feature.appaai.ui.uistate

import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo

sealed class FatherAIInfoUIEffect {

    data class NavigateCustomFatherAIInfo(val fatherAIInfo : FatherAIInfo) : FatherAIInfoUIEffect()
    data class NavigateCreateFatherAIInfo(val fatherAIInfo : FatherAIInfo) : FatherAIInfoUIEffect()
    data class LaunchPicker(val mediaType : MediaType) : FatherAIInfoUIEffect()

    data class AddHomeScreen(val fatherAIInfo : FatherAIInfo):FatherAIInfoUIEffect()
}