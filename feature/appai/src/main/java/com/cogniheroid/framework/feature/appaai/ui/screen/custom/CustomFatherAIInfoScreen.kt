package com.cogniheroid.framework.feature.appaai.ui.screen.custom

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androidai.framework.shared.core.model.data.enums.GeminiAIGenerate
import com.cogniheroid.framework.feature.appaai.FatherAICore
import com.cogniheroid.framework.feature.appaai.ui.uistate.CustomFatherAIInfoUIState
import com.cogniheroid.framework.feature.appaai.ui.viewmodel.FatherAIViewModel
import com.cogniheroid.framework.feature.appaai.ui.uistate.FatherUIEvent
import com.cogniheroid.framework.feature.appaai.utils.AttachmentFileUtils
import com.cogniheroid.framework.feature.appaai.utils.FileUriInfo
import com.cogniheroid.framework.feature.appai.R
import com.cogniheroid.framework.shared.core.fatherai.data.enum.MediaType
import com.cogniheroid.framework.ui.component.AdUIContainer
import com.cogniheroid.framework.ui.component.CustomButton
import com.cogniheroid.framework.util.ContentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFatherAIInfoScreen(
        customFatherAIInfoUIState : CustomFatherAIInfoUIState,
        onOpenFilePicker : (MediaType) -> Unit, onInputTextChanged : (String) -> Unit,
        onRemoveFile:(FileUriInfo) -> Unit,
        onClear : () -> Unit, onGenerateContent : (String, String) -> Unit,
        navigateBack : () -> Unit) {

    val fatherAIInfo = customFatherAIInfoUIState.fatherAIInfo

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
                    text = fatherAIInfo.title, fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            }, navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "", tint = MaterialTheme.colorScheme.onSurface)
                }
            })
        }
        FatherAIInfoView(
            modifier = Modifier.fillMaxSize(),
            customFatherAIInfoUIState = customFatherAIInfoUIState, onOpenFilePicker,
            onInputTextChanged, onRemoveFile, onClear, onGenerateContent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FatherAIInfoView(
        modifier : Modifier, customFatherAIInfoUIState : CustomFatherAIInfoUIState,
        onOpenFilePicker : (MediaType) -> Unit, onInputTextChanged : (String) -> Unit,
        onRemoveFile:(FileUriInfo) -> Unit,
        onClear : () -> Unit, onGenerateContent : (String, String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    AdUIContainer(modifier = modifier.navigationBarsPadding(), content = { childModifier ->
        Column(
            modifier = childModifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)) {

                val (image, textInput) = createRefs()
                val canShowIcon = customFatherAIInfoUIState.fatherAIInfo.mediaType != MediaType.NONE
                if(canShowIcon) {
                    Box(modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(textInput.top)
                            bottom.linkTo(textInput.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(textInput.start)
                        }
                        .padding(start = 16.dp, end = 8.dp)
                        .background(
                            shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp)) {
                        val resId = when(customFatherAIInfoUIState.fatherAIInfo.mediaType) {
                            MediaType.ALL -> {
                                R.drawable.ic_attach_file
                            }

                            MediaType.DOCUMENT -> {
                                R.drawable.ic_attach_file
                            }

                            MediaType.IMAGE -> {
                                R.drawable.ic_add_photo
                            }

                            MediaType.VIDEO -> {
                                R.drawable.ic_video_file
                            }

                            MediaType.AUDIO -> {
                                R.drawable.ic_audio_file
                            }

                            MediaType.NONE -> {
                                R.drawable.ic_attach_file
                            }
                        }
                        Icon(modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            onOpenFilePicker(customFatherAIInfoUIState.fatherAIInfo.mediaType)
                        }, painter = painterResource(id = resId), contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }

                TextInputField(modifier = Modifier.constrainAs(textInput) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }, customFatherAIInfoUIState.inputText, onInputTextChange = { inputData ->
                    onInputTextChanged(inputData)
                }, onClear = {
                    onClear()
                })

            }

            if(customFatherAIInfoUIState.fileUriInfoItems.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .wrapContentSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center) {
                    items(customFatherAIInfoUIState.fileUriInfoItems) { uriInfo ->
                        ConstraintLayout(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 12.dp)
                        ) {
                            val (bitmap, close) = createRefs()
                            val imageResId = AttachmentFileUtils.getAttachmentIconResId(attachmentContentType = AttachmentFileUtils.getAttachmentContentTypeByMime(uriInfo.mimeType))
                            Box(
                                modifier = Modifier
                                    .height(100.dp).width(60.dp).background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp))
                                    .constrainAs(bitmap) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                                    , contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                                    painter = painterResource(id = imageResId),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary)
                            }

                            Box(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(20.dp)
                                    .constrainAs(close) {
                                        top.linkTo(parent.top)
                                        end.linkTo(parent.end)
                                    }
                                    .background(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer)
                                    .padding(2.dp)
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .clickable {
                                            onRemoveFile(uriInfo)
                                        },
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }


                        }

                    }
                }
            }

            val defaultMessage = stringResource(id = R.string.message_default_error_ai)

            CustomButton(modifier = Modifier.padding(top = 16.dp),
                label = customFatherAIInfoUIState.fatherAIInfo.generateButtonText, onClick = {
                    focusManager.clearFocus()
                    onGenerateContent(customFatherAIInfoUIState.inputText, defaultMessage)
                })

            val isGenerating =
                customFatherAIInfoUIState.geminiAIGenerate is GeminiAIGenerate.Generating

            val isSuccess = customFatherAIInfoUIState.geminiAIGenerate is GeminiAIGenerate.StreamContentSuccess

            if(customFatherAIInfoUIState.outputText.isNotEmpty() || isGenerating) {

                Log.d("CHECKOUTPUTTEXT","CHEKCIG THE OUTPUT TEXT = ${customFatherAIInfoUIState.outputText} and $isGenerating")
                if(isSuccess) {
                    Text(
                        text = stringResource(id = R.string.label_generated_by_gemini),
                        fontSize = 16.sp, modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface)
                }

                val cardColors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface)

                Card(
                    modifier = Modifier.padding(16.dp), colors = cardColors,
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 3.dp)) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth()) {
                        val (text, share, copy) = createRefs()

                        if(!isSuccess) {
                            Icon(modifier = Modifier
                                .size(28.dp)
                                .padding(top = 8.dp, end = 8.dp)
                                .constrainAs(share) {
                                    top.linkTo(parent.top)
                                    end.linkTo(copy.start)

                                }
                                .clickable {
                                    focusManager.clearFocus()
                                    ContentUtils.shareContent(
                                        context = context,
                                        data = customFatherAIInfoUIState.outputText)
                                }, painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "", tint = MaterialTheme.colorScheme.primary)

                            Icon(modifier = Modifier
                                .size(28.dp)
                                .padding(top = 8.dp, end = 8.dp)
                                .constrainAs(copy) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                }
                                .clickable {
                                    focusManager.clearFocus()
                                    ContentUtils.copyAndShowToast(
                                        context = context,
                                        result = customFatherAIInfoUIState.outputText)
                                }, painter = painterResource(id = R.drawable.ic_copy),
                                contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                        }

                        val result = if(isGenerating) {
                            stringResource(id = R.string.placeholder_advance_generate_text)
                        } else {
                            customFatherAIInfoUIState.outputText
                        }

                        Text(text = HtmlCompat.fromHtml(result, 0).toString(), fontSize = 16.sp,
                            modifier = Modifier
                                .constrainAs(text) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(share.start)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                }
                                .combinedClickable(onClick = {
                                    ContentUtils.copyAndShowToast(
                                        context = context,
                                        result = customFatherAIInfoUIState.outputText)
                                }, onLongClick = {
                                    ContentUtils.copyAndShowToast(
                                        context = context,
                                        result = customFatherAIInfoUIState.outputText)
                                })
                                .padding(16.dp))
                    }
                }
            }
        }
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputField(
        modifier : Modifier = Modifier, inputText : String, onInputTextChange : (String) -> Unit,
        onClear : () -> Unit) {

    OutlinedTextField(value = inputText, onValueChange = {
        onInputTextChange(it)
    }, label = {
        Text(text = stringResource(R.string.hint_generate_text))
    }, modifier = modifier
        .padding(start = 8.dp, end = 16.dp)
        .fillMaxWidth(), trailingIcon = {
        if(inputText.isNotEmpty()) {
            IconButton(onClick = {
                onClear()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close), contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary)
            }
        }
    })
}

/*@Composable
internal fun PhotoReasoningRoute(
    viewModel: PhotoReasoningViewModel = viewModel(factory = GenerativeViewModelFactory)
) {
    val photoReasoningUiState by viewModel.uiState.collectAsState()

    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    PhotoReasoningScreen(
        uiState = photoReasoningUiState,
        onReasonClicked = { inputText, selectedItems ->
            coroutineScope.launch {
                val bitmaps = selectedItems.mapNotNull {
                    val imageRequest = imageRequestBuilder
                        .data(it)
                        .size(size = 768)
                        .precision(Precision.EXACT)
                        .build()
                    try {
                        val result = imageLoader.execute(imageRequest)
                        if (result is SuccessResult) {
                            return@mapNotNull (result.drawable as BitmapDrawable).bitmap
                        } else {
                            return@mapNotNull null
                        }
                    } catch (e: Exception) {
                        return@mapNotNull null
                    }
                }
                viewModel.reason(inputText, bitmaps)
            }
        }
    )
}

@Composable
fun PhotoReasoningScreen(
    uiState: PhotoReasoningUiState = PhotoReasoningUiState.Loading,
    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> }
) {
    var userQuestion by remember Save able { mutableStateOf("") }
    val imageUris = rememberSave able(saver = UriSaver()) { mutableStateListOf() }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }

    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                IconButton(
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .padding(all = Dimensions.padding4)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add_image),
                    )
                }
                OutlinedTextField(
                    value = userQuestion,
                    label = { Text(stringResource(R.string.reason_label)) },
                    placeholder = { Text(stringResource(R.string.reason_hint)) },
                    onValueChange = { userQuestion = it },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
                TextButton(
                    onClick = {
                        if (userQuestion.isNotBlank()) {
                            onReasonClicked(userQuestion, imageUris.toList())
                        }
                    },
                    modifier = Modifier
                        .padding(all = Dimensions.padding4)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(stringResource(R.string.action_go))
                }
            }
            LazyRow(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                items(imageUris) { imageUri ->
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(Dimensions.padding4)
                            .requiredSize(72.dp)
                    )
                }
            }
        }
        when (uiState) {
            PhotoReasoningUiState.Initial -> {
                // Nothing is shown
            }

            PhotoReasoningUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            }

            is PhotoReasoningUiState.Success -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Person Icon",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .requiredSize(36.dp)
                                .drawBehind {
                                    drawCircle(color = Color.White)
                                }
                        )
                        Text(
                            text = uiState.outputText, // TODO(that fire dev): Figure out Markdown support
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            is PhotoReasoningUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }
            }
        }
    }
}*/


