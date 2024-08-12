package com.cogniheroid.framework.feature.appaai.ui.screen.create

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cogniheroid.framework.feature.appaai.ui.uistate.CreateFatherAIInfoUIState
import com.cogniheroid.framework.feature.appai.R
import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel
import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.shared.core.fatherai.data.enum.ModelReturnType
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFatherAIInfoScreen(
        createFatherAIInfoUIState : CreateFatherAIInfoUIState,
        onSaveClick : (FatherAIInfo, Boolean) -> Unit, navigateBack : () -> Unit) {

    val fatherAIInfo = remember(createFatherAIInfoUIState.fatherAIInfo) {
        mutableStateOf(createFatherAIInfoUIState.fatherAIInfo)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)) {

        val colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
        Surface(tonalElevation = 3.dp) {
            TopAppBar(modifier = Modifier, colors = colors, title = {
                Text(
                    text = stringResource(id = R.string.title_create_ai), fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            }, navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "", tint = MaterialTheme.colorScheme.onSurface)
                }
            }, actions = {
                Text(modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        onSaveClick(fatherAIInfo.value, createFatherAIInfoUIState.isNew)
                    }, text = stringResource(id = R.string.label_save), fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary)
            })
        }

        CreateFatherAIInfo(fatherAIInfo)
    }
}

@Composable
fun CreateFatherAIInfo(fatherAIInfoState : MutableState<FatherAIInfo>) {
    val fatherAIInfo = fatherAIInfoState.value
    val modelList = listOf(GeminiAIModel.GEMINI_PRO.name, GeminiAIModel.GEMINI_FLASH.name)
    val mediaTypeList = listOf(
        MediaType.NONE.name, MediaType.ALL.name, MediaType.IMAGE.name, MediaType.VIDEO.name,
        MediaType.AUDIO.name, MediaType.DOCUMENT.name)
    val modelReturnTypeList = listOf(
        ModelReturnType.TEXT.name, ModelReturnType.CODE_EXECUTION.name, ModelReturnType.JSON.name)

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .imePadding()) {
        EditContainers(
            Modifier.padding(16.dp), input = fatherAIInfo.title, label = stringResource(
                id = R.string.label_title), stringResource(id = R.string.desc_title)) {
            fatherAIInfoState.value = fatherAIInfoState.value.copy(title = it)
        }

        DropDownEditContainer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            stringResource(id = R.string.label_gemini_model),
            stringResource(id = R.string.desc_gemini_model), fatherAIInfo.model.name, modelList) {
            val type = when(it) {
                GeminiAIModel.GEMINI_PRO.name -> GeminiAIModel.GEMINI_PRO
                GeminiAIModel.GEMINI_FLASH.name -> GeminiAIModel.GEMINI_FLASH
                else -> GeminiAIModel.GEMINI_PRO
            }
            fatherAIInfoState.value = fatherAIInfoState.value.copy(model = type)
        }


        EditContainers(
            Modifier.padding(16.dp), input = fatherAIInfo.prompt, label = stringResource(
                id = R.string.label_prompt), stringResource(id = R.string.desc_prompt)) {
            fatherAIInfoState.value = fatherAIInfoState.value.copy(prompt = it)
        }

        DropDownEditContainer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), stringResource(
                id = R.string.label_media_type), stringResource(id = R.string.desc_media_type),
            fatherAIInfo.mediaType.name,
            mediaTypeList) {
            val type = when(it) {
                MediaType.NONE.name -> MediaType.NONE
                MediaType.ALL.name -> MediaType.ALL
                MediaType.IMAGE.name -> MediaType.IMAGE
                MediaType.AUDIO.name -> MediaType.AUDIO
                MediaType.VIDEO.name -> MediaType.VIDEO
                MediaType.DOCUMENT.name -> MediaType.DOCUMENT
                else -> MediaType.NONE
            }
            fatherAIInfoState.value = fatherAIInfoState.value.copy(mediaType = type)
        }

        EditContainers(
            Modifier.padding(16.dp), input = fatherAIInfo.generateButtonText, stringResource(
                id = R.string.label_generate_button),
            stringResource(id = R.string.desc_generate_button)) {
            fatherAIInfoState.value = fatherAIInfoState.value.copy(generateButtonText = it)
        }

        DropDownEditContainer(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), stringResource(
                id = R.string.label_model_return), stringResource(id = R.string.desc_model_return),
            fatherAIInfo.modelReturnType.name,
            modelReturnTypeList) {
            val type = when(it) {
                ModelReturnType.JSON.name -> ModelReturnType.JSON
                ModelReturnType.CODE_EXECUTION.name -> ModelReturnType.CODE_EXECUTION
                else -> ModelReturnType.TEXT
            }
            fatherAIInfoState.value = fatherAIInfoState.value.copy(modelReturnType = type)
        }

    }

}

@Composable
fun DropDownEditContainer(
        modifier : Modifier, label : String, placeholder : String, selectedOption:String, options : List<String>,
        onSelectedOption : (String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val selectedOptionText = remember(selectedOption) { mutableStateOf(selectedOption) }
    Log.d("CHECKSELECTEDOPTION","CHEKCIT RHE SELECTED OPTION = ${selectedOptionText.value}")

    val icon = Icons.Filled.ArrowDropDown
    val rotate = if(expanded.value) {
        0f
    } else {
        180f
    }

    Box(modifier = modifier.clickable {
        expanded.value = !expanded.value
    }) {

        EditContainers(input = selectedOptionText.value, label = label, placeholder = placeholder,
            trailingIcon = {
                Icon(icon, "contentDescription", Modifier
                    .rotate(rotate)
                    .clickable {
                        expanded.value = !expanded.value
                    }, tint = MaterialTheme.colorScheme.primary)
            }) {}

        DropdownMenu(modifier = Modifier.padding(16.dp), expanded = expanded.value,
            onDismissRequest = {
                expanded.value = !expanded.value
            }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(onClick = {
                    selectedOptionText.value = selectionOption
                    onSelectedOption(selectionOption)
                    expanded.value = false
                }, text = {
                    Text(text = selectionOption)
                })
            }
        }

        Box(modifier = Modifier
            .background(Color.Red)
            .clickable {

            }
            .fillMaxSize())
    }
}

@Composable
fun EditContainers(
        modifier : Modifier = Modifier,
        input : String,
        label : String,
        placeholder : String,
        trailingIcon : @Composable (() -> Unit)? = null,
        onInputContent : (String) -> Unit,
) {
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
    )

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = input,
        onValueChange = {
            onInputContent(it)
        },
        label = {
            LabelText(label)
        },
        placeholder = {
            HintText(placeholder)
        },
        trailingIcon = trailingIcon,
        colors = textFieldColors,
        maxLines = 5,

        )
}

@Composable
private fun LabelText(label : String) {
    androidx.compose.material.Text(
        text = label, fontSize = 14.sp, textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary)
}

@Composable
private fun HintText(hint : String) {
    androidx.compose.material.Text(
        text = hint, fontSize = 14.sp, textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant)
}
