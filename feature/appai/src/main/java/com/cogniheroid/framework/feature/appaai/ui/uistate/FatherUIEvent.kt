package com.cogniheroid.framework.feature.appaai.ui.uistate

import com.cogniheroid.framework.feature.appaai.utils.FileUriInfo
import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo

sealed class FatherUIEvent {

    data class UpdateParam(val fatherAIInfo : FatherAIInfo) : FatherUIEvent()

    data class UpdateFileUriItems(val fileUriInfoItems : List<FileUriInfo>) : FatherUIEvent()
}

sealed class HomeUIEvent : FatherUIEvent() {

    data class OnAddToHomeScreenFatherAIInfo(val fatherAIInfo : FatherAIInfo) : HomeUIEvent()

    data class OnEditFatherAIInfo(val fatherAIInfo : FatherAIInfo) : HomeUIEvent()

    data class OnDeleteFatherAIInfo(val fatherAIInfo : FatherAIInfo) : HomeUIEvent()

    data class OnCreateFatherInfo(val fatherAIInfo : FatherAIInfo) : HomeUIEvent()

    data class OnFatherInfoItemClick(val fatherAIInfo : FatherAIInfo) : HomeUIEvent()
}

sealed class CustomUIEvent:FatherUIEvent(){

    data class OnAddMediaClick(val mediaType : MediaType):CustomUIEvent()

    data class RemoveFileItem(val fileUriInfo : FileUriInfo) : CustomUIEvent()

    data class InputText(val text : String) : CustomUIEvent()
    data class GenerateText(val text : String, val defaultErrorMessage : String) : CustomUIEvent()

    object ClearText : CustomUIEvent()

}

sealed class CreateUIEvent:FatherUIEvent(){

    data class OnSaveClicked(val fatherAIInfo : FatherAIInfo, val isNew:Boolean):CreateUIEvent()
}