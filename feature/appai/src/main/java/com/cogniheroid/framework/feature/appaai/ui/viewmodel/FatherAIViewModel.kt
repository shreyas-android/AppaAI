package com.cogniheroid.framework.feature.appaai.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.map
import com.androidai.framework.shared.core.file.data.response.RemoteResponse
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate
import com.androidai.framework.shared.core.model.data.model.ModelInput
import com.androidai.framework.shared.core.model.manager.GeminiAIManager
import com.cogniheroid.framework.feature.appaai.ui.uistate.CreateFatherAIInfoUIState
import com.cogniheroid.framework.feature.appaai.ui.uistate.CreateUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.CustomFatherAIInfoUIState
import com.cogniheroid.framework.feature.appaai.ui.uistate.CustomUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.FatherAIInfoUIEffect
import com.cogniheroid.framework.feature.appaai.ui.uistate.FatherUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.HomeUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.HomeUIState
import com.cogniheroid.framework.feature.appaai.utils.FileUriInfo
import com.cogniheroid.framework.feature.appai.R
import com.cogniheroid.framework.shared.core.fatherai.data.mapper.toFatherAIInfo
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import com.cogniheroid.framework.shared.core.fatherai.manager.fatherAI.FatherAIManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import migrations.FatherAIEntity

class FatherAIViewModel(private val geminiAIManager : GeminiAIManager,
                        private val fatherAIManager : FatherAIManager) : ViewModel() {

    private val fatherAIPager by lazy {
        Pager(config = PagingConfig(100, prefetchDistance = 20, enablePlaceholders = true),
            pagingSourceFactory = {
                getFatherAIPagingSource()
            })
    }

    val fatherAIInfoPagingFlow = fatherAIPager.flow.map {
        it.map {
            it.toFatherAIInfo()
        }
    }


    private val selectedCreateFatherAIInfo = MutableStateFlow(FatherAIInfo.getDefault())


    private var shouldRefreshFatherAIInfo: (() -> Unit)? = null




    private var isModelStartedGeneratingText = MutableStateFlow(false)


    private val isNewAIInfo = MutableStateFlow(true)
    val createFatherAIInfoUIState = combine(selectedCreateFatherAIInfo, isNewAIInfo){ fatherAIInfo, isNew ->
        CreateFatherAIInfoUIState(fatherAIInfo, isNew)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(),
        CreateFatherAIInfoUIState.getDefault())

    val customFatherAIInfoUIState = MutableStateFlow(CustomFatherAIInfoUIState.getDefault())


    private val _geminiAIDemoUIEffectChannel = Channel<FatherAIInfoUIEffect>()
    val fatherAIInfoUIEffectFlow = _geminiAIDemoUIEffectChannel.receiveAsFlow()

    fun performIntent(fatherUIEvent : FatherUIEvent) {
        when(fatherUIEvent) {

            is FatherUIEvent.UpdateFileUriItems -> {
                customFatherAIInfoUIState.update {
                    it.copy(fileUriInfoItems = fatherUIEvent.fileUriInfoItems)
                }
            }

            is CustomUIEvent.OnAddMediaClick -> {
                setSideEffect(FatherAIInfoUIEffect.LaunchPicker(fatherUIEvent.mediaType))
            }
            is HomeUIEvent.OnAddToHomeScreenFatherAIInfo -> {
                setSideEffect(FatherAIInfoUIEffect.AddHomeScreen(fatherUIEvent.fatherAIInfo))
            }
            is HomeUIEvent.OnCreateFatherInfo -> {
                isNewAIInfo.value = true
                selectedCreateFatherAIInfo.value = fatherUIEvent.fatherAIInfo
                setSideEffect(FatherAIInfoUIEffect.NavigateCreateFatherAIInfo(fatherUIEvent.fatherAIInfo))
            }
            is HomeUIEvent.OnDeleteFatherAIInfo -> {
                viewModelScope.launch {
                    fatherAIManager.deleteFatherAI(fatherUIEvent.fatherAIInfo)
                }
            }
            is HomeUIEvent.OnEditFatherAIInfo -> {
                isNewAIInfo.value = false
                selectedCreateFatherAIInfo.value = fatherUIEvent.fatherAIInfo
                setSideEffect(FatherAIInfoUIEffect.NavigateCreateFatherAIInfo(fatherUIEvent.fatherAIInfo))
            }
            is HomeUIEvent.OnFatherInfoItemClick -> {
                customFatherAIInfoUIState.update {
                  it.copy(fatherAIInfo = fatherUIEvent.fatherAIInfo)
                }
                updateInputText(fatherUIEvent.fatherAIInfo.prompt)
                setSideEffect(FatherAIInfoUIEffect.NavigateCustomFatherAIInfo(fatherUIEvent.fatherAIInfo))
            }

            CustomUIEvent.ClearText -> {
                updateInputText("")
            }
            is CustomUIEvent.InputText -> {
                updateInputText(fatherUIEvent.text)
            }
            is CustomUIEvent.RemoveFileItem -> {
                val mutableList = customFatherAIInfoUIState.value.fileUriInfoItems.toMutableList()
                mutableList.remove(fatherUIEvent.fileUriInfo)
                customFatherAIInfoUIState.update {
                    it.copy(fileUriInfoItems = mutableList)
                }
            }

            is CustomUIEvent.GenerateText -> {
                isModelStartedGeneratingText.value = true
                clearResult()
                generateTextAndUpdateResult(
                    customFatherAIInfoUIState.value.fileUriInfoItems, fatherUIEvent.text,
                    fatherUIEvent.defaultErrorMessage)
            }
            is FatherUIEvent.UpdateParam -> {

            }

            is CreateUIEvent.OnSaveClicked -> {
                viewModelScope.launch {
                    if(fatherUIEvent.isNew) {
                        fatherAIManager.insertFatherAI(fatherUIEvent.fatherAIInfo)
                    }else{
                        fatherAIManager.updateFatherAI(fatherAIInfo = fatherUIEvent.fatherAIInfo)
                    }
                }
            }
        }
    }

    fun updateInputText(text:String){
        customFatherAIInfoUIState.update {
            it.copy(inputText = text)
        }
    }

    private suspend fun getFatherAIInfo(id:Long) : FatherAIInfo {
        return fatherAIManager.getFatherAIInfoById(id)
    }

    fun onPinnedShortcutSelected(id:Long){
        viewModelScope.launch {
            val fatherAIInfo = getFatherAIInfo(id)
            performIntent(HomeUIEvent.OnFatherInfoItemClick(fatherAIInfo))
        }
    }

    private fun getFatherAIPagingSource() : PagingSource<Int, FatherAIEntity> {
        val pagingSource = fatherAIManager.getFatherAIPagingSource()
        shouldRefreshFatherAIInfo = {
            pagingSource.invalidate()
        }
        return pagingSource
    }

    private fun generateTextAndUpdateResult(
            fileUriItems : List<FileUriInfo>, prompt : String, defaultErrorMessage : String) {
        viewModelScope.launch {

            geminiAIManager.generateTextStreamContent(
                getModelInputList(fileUriItems, prompt), null, defaultErrorMessage,
            ).collectLatest {

                Log.d("CHECKCONTENTDATA","CHEKCIG THE CONTENT DATA = $it")
               customFatherAIInfoUIState.update { customFatherAIInfoUIState ->
                   customFatherAIInfoUIState.copy(geminiAIGenerate = it)
               }
                when(it){
                    is GeminiAIGenerate.AllFileUploaded -> {
                        updateResult("All files uploaded")
                    }
                    is GeminiAIGenerate.FileUploaded ->{
                        updateResult("Uploaded the files")
                    }
                    GeminiAIGenerate.FileUploading -> {
                        updateResult("Uploading the file")
                    }

                    GeminiAIGenerate.Generating -> {
                        updateResult("Generating content")
                    }
                    is GeminiAIGenerate.StreamContentError -> {
                        updateResult(it.message)
                    }
                    is GeminiAIGenerate.StreamContentSuccess -> {
                        updateResult(it.text)
                    }

                    GeminiAIGenerate.FileProcessing -> {
                        updateResult("Processing the files. Please wait for few seconds")
                    }
                    GeminiAIGenerate.FileUploadFailed -> {
                        updateResult("Files upload failed. Please try again later")
                    }
                }

                isModelStartedGeneratingText.value = false
            }
        }
    }


    private fun getModelInputList(
            fileUriItems : List<FileUriInfo>, text : String) : List<ModelInput> {
        val modelInputList = mutableListOf<ModelInput>()
        fileUriItems.forEach {
            modelInputList.add(
                ModelInput.File(
                    true, fileName = it.name, mimeType = it.mimeType, data = it.data
                        ?: ByteArray(0), uri = it.path))
        }
        modelInputList.add(ModelInput.Text(true, text))
        return modelInputList
    }

    fun updateResult(result:String){
        customFatherAIInfoUIState.update {
            it.copy(outputText = result)
        }
    }


    private fun refreshFatherAIInfoItems(){
        shouldRefreshFatherAIInfo?.invoke()
    }

    private fun clearResult() {
       updateResult("")
    }

    private fun setSideEffect(fatherAIInfoUIEffect : FatherAIInfoUIEffect) {
        viewModelScope.launch {
            _geminiAIDemoUIEffectChannel.send(fatherAIInfoUIEffect)
        }
    }
}