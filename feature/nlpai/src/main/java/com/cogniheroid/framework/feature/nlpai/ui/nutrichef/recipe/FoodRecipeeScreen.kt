package com.cogniheroid.framework.feature.nlpai.ui.nutrichef.recipe

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.cogniheroid.framework.feature.nlpai.NLPAICore
import com.cogniheroid.framework.feature.nlpai.R
import com.cogniheroid.framework.feature.nlpai.ui.nutrichef.recipe.uistate.FoodRecipeUIEffect
import com.cogniheroid.framework.feature.nlpai.ui.nutrichef.recipe.uistate.FoodRecipeUIEvent
import com.cogniheroid.framework.feature.nlpai.ui.nutrichef.recipe.uistate.FoodRecipeUIState
import com.cogniheroid.framework.feature.nlpai.utils.NLPAIUtils
import com.cogniheroid.framework.feature.nlpai.utils.getAnnotatedString
import com.cogniheroid.framework.ui.component.AdUIContainer
import com.cogniheroid.framework.ui.component.CustomButton
import com.cogniheroid.framework.ui.theme.Dimensions
import com.cogniheroid.framework.util.ContentUtils
import com.configheroid.framework.core.avengerad.AvengerAd
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodRecipeScreen(
        avengerAd : AvengerAd,
    onAddImage: () -> Unit, navigateBack: () -> Unit) {

    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    val coroutineScope = rememberCoroutineScope()
    val advanceTextGenerationViewModel = viewModel<FoodRecipeViewModel>(
        factory = NLPAICore.foodRecipeeViewModelFactory
    )

    LaunchedEffect(key1 = NLPAICore.imageIntentFlow) {
        NLPAICore.imageIntentFlow.collectLatest { intent ->
            if (intent != null) {
                val data = intent.extras?.get("data")
                if (data is Bitmap) {
                    val imageRequest = imageRequestBuilder
                        .data(data)
                        .size(size = 480)
                        .precision(Precision.EXACT)
                        .build()

                    coroutineScope.launch {
                        val bitmap = try {
                            val result = imageLoader.execute(imageRequest)
                            if (result is SuccessResult) {
                                (result.drawable as BitmapDrawable).bitmap
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            null
                        }

                        advanceTextGenerationViewModel.performIntent(
                            FoodRecipeUIEvent.AddImage(bitmap)
                        )
                    }
                } else {
                    val singleData = intent.data
                    val clipData = intent.clipData
                    val tempList: List<Uri> = if (singleData != null) {
                        listOf(singleData)
                    } else if (clipData != null) {
                        val list = mutableListOf<Uri>()
                        for (i in 0 until clipData.itemCount) {
                            list.add(clipData.getItemAt(i).uri)
                        }
                        list
                    } else {
                        listOf()
                    }

                    tempList.forEach { uri ->
                        coroutineScope.launch {
                            val imageRequest = imageRequestBuilder
                                .data(uri)
                                .size(size = 480)
                                .precision(Precision.EXACT)
                                .build()

                            val bitmap = try {
                                val result = imageLoader.execute(imageRequest)
                                if (result is SuccessResult) {
                                    (result.drawable as BitmapDrawable).bitmap
                                } else {
                                    null
                                }
                            } catch (e: Exception) {
                                null
                            }
                            advanceTextGenerationViewModel.performIntent(
                                FoodRecipeUIEvent
                                    .AddImage(bitmap)
                            )
                        }
                    }
                }
            }

        }

    }

    LaunchedEffect(key1 = Unit) {
        advanceTextGenerationViewModel.foodRecipeUIEffectFlow.collectLatest {
            when (it) {
                FoodRecipeUIEffect.ShowImagePicker -> {
                    onAddImage()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        val colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
        Surface(tonalElevation = 3.dp) {
            TopAppBar(modifier = Modifier, colors = colors, title = {
                Text(
                    text = stringResource(id = R.string.title_food_recipe),
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "", tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            })
        }
        FoodRecipeView(modifier = Modifier.navigationBarsPadding().fillMaxSize(),
            avengerAd = avengerAd,
            foodRecipeUIState = advanceTextGenerationViewModel.foodRecipeUIState.collectAsState().value,
            performIntent = { textGenerationUIEvent ->
                advanceTextGenerationViewModel.performIntent(textGenerationUIEvent)
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FoodRecipeView(
        modifier: Modifier,
        avengerAd : AvengerAd,
        foodRecipeUIState: FoodRecipeUIState,
        performIntent: (FoodRecipeUIEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    AdUIContainer(modifier = modifier,
        content = { childModifier ->
            Column(
                modifier = childModifier
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(top = Dimensions.padding24,
                        start = Dimensions.defaultPadding, end = Dimensions.defaultPadding),
                    text = stringResource(id = R.string.placeholder_choose_image_text),
                    fontSize = Dimensions.primaryFontSize,
                    color = MaterialTheme.colorScheme.onSurface)


                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {

                    val (image, textInput) = createRefs()
                    Box(modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(textInput.top)
                            bottom.linkTo(textInput.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(textInput.start)
                        }
                        .padding(start = 16.dp, end = 8.dp)
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(8.dp)) {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    focusManager.clearFocus()
                                    performIntent(FoodRecipeUIEvent.OnOpenImagePicker)
                                },
                            painter = painterResource(id = R.drawable.ic_add_photo),
                            contentDescription = "", tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextInputField(modifier = Modifier.constrainAs(textInput) {
                        top.linkTo(parent.top)
                        start.linkTo(image.end)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
                        foodRecipeUIState.inputText,
                        onInputTextChange = { inputData ->
                            performIntent(FoodRecipeUIEvent.InputText(inputData))
                        },
                        onClear = {
                            performIntent(FoodRecipeUIEvent.ClearText)
                        })

                }

                if (foodRecipeUIState.bitmaps.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .wrapContentSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(foodRecipeUIState.bitmaps) { image ->
                            ConstraintLayout(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(end = 12.dp)
                            ) {
                                val (bitmap, close) = createRefs()
                                Image(
                                    modifier = Modifier
                                        .padding(top = 8.dp, end = 8.dp)
                                        .constrainAs(bitmap) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                        }, bitmap = image.asImageBitmap(), contentDescription = ""
                                )

                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .constrainAs(close) {
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                        }
                                        .background(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        )
                                        .padding(2.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .clickable {
                                                performIntent(
                                                    FoodRecipeUIEvent.RemoveImage(
                                                        image
                                                    )
                                                )
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

                CustomButton(
                    modifier = Modifier.padding(top = 16.dp),
                    label = stringResource(R.string.placeholder_button_recipe),
                    onClick = {
                        focusManager.clearFocus()
                        performIntent(
                            FoodRecipeUIEvent.GenerateText(
                                foodRecipeUIState.inputText, defaultMessage))
                    })


                if (foodRecipeUIState.outputText.isNotEmpty() || foodRecipeUIState.isGenerating) {

                    if (!foodRecipeUIState.isGenerating) {
                        Text(
                            text = stringResource(id = R.string.label_generated_by_gemini),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(top = 32.dp, bottom = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    val cardColors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )

                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = cardColors,
                        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 3.dp)
                    ) {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val (text, share, copy) = createRefs()

                            if (!foodRecipeUIState.isGenerating) {
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
                                            data = foodRecipeUIState.outputText
                                        )
                                    },
                                    painter = painterResource(id = R.drawable.ic_share),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.primary
                                )

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
                                            result = foodRecipeUIState.outputText
                                        )
                                    },
                                    painter = painterResource(id = R.drawable.ic_copy),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            val result = if (foodRecipeUIState.isGenerating) {
                                stringResource(id = R.string.placeholder_generating_food_recipe)
                            } else {
                                getAnnotatedString(foodRecipeUIState.outputText).toString()
                            }

                            Log.d("CHECKGENERATE","CHEKCING THE OUTPUT = $result")

                            Text(text = result, fontSize = 16.sp,
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
                                            result = foodRecipeUIState.outputText
                                        )
                                    }, onLongClick = {
                                        ContentUtils.copyAndShowToast(
                                            context = context,
                                            result = foodRecipeUIState.outputText
                                        )
                                    })
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }, bannerAd2 = {
            avengerAd.getAdMobBannerView(context = context, NLPAIUtils.getNutriChefBannerAd3())
        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputField(
    modifier: Modifier = Modifier,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onClear: () -> Unit
) {

    OutlinedTextField(
        value = inputText,
        onValueChange = {
            onInputTextChange(it)
        },
        label = {
            Text(text = stringResource(R.string.hint_food_recipe))
        },
        modifier = modifier
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(), trailingIcon = {
            if (inputText.isNotEmpty()) {
                IconButton(onClick = {
                    onClear()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "", tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

