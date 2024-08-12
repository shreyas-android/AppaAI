package com.cogniheroid.framework.feature.appaai.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.androidai.framework.shared.core.model.GeminiAIAndroidCore
import com.cogniheroid.android.ad.ui.theme.ComposeUITheme
import com.cogniheroid.framework.feature.appaai.FatherAICore
import com.cogniheroid.framework.feature.appaai.ui.screen.create.CreateFatherAIInfoScreen
import com.cogniheroid.framework.feature.appaai.ui.screen.custom.CustomFatherAIInfoScreen
import com.cogniheroid.framework.feature.appaai.ui.screen.home.HomeScreen
import com.cogniheroid.framework.feature.appaai.ui.uistate.CreateUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.CustomFatherAIInfoUIState
import com.cogniheroid.framework.feature.appaai.ui.uistate.CustomUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.FatherAIInfoUIEffect
import com.cogniheroid.framework.feature.appaai.ui.uistate.FatherUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.HomeUIEvent
import com.cogniheroid.framework.feature.appaai.ui.uistate.HomeUIState
import com.cogniheroid.framework.feature.appaai.ui.viewmodel.FatherAIViewModel
import com.cogniheroid.framework.feature.appaai.utils.AttachmentFileUtils
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import kotlinx.coroutines.flow.collectLatest

enum class AvengerAIRoute(val route : String) { CUSTOM("custom"),
    CREATE("create"),
    HOME("home")
}

@Composable
fun FatherAINavigation(onAddToHomeScreen:(FatherAIInfo) -> Unit) {

    val navController = rememberNavController()

    val viewModel = viewModel<FatherAIViewModel>(factory = FatherAICore.fatherAIViewModelFactory)

    val context = LocalContext.current
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) {
        viewModel.performIntent(
            FatherUIEvent.UpdateFileUriItems(
                AttachmentFileUtils.getFileUriInfoListFromActivityResult(context = context, it)))
    }

    LaunchedEffect(key1 = Unit) {
        FatherAICore.fatherAIInfoIdFlow.collectLatest {
            viewModel.onPinnedShortcutSelected(it)
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.fatherAIInfoUIEffectFlow.collectLatest {
            when(it) {
               is FatherAIInfoUIEffect.LaunchPicker -> {
                    pickerLauncher.launch(AttachmentFileUtils.getAttachmentIntent(it.mediaType))
                }



                is FatherAIInfoUIEffect.NavigateCreateFatherAIInfo -> {
                    navController.navigate(AvengerAIRoute.CREATE.route)
                }

                is FatherAIInfoUIEffect.NavigateCustomFatherAIInfo -> {
                    navController.navigate(AvengerAIRoute.CUSTOM.route)
                }

                is FatherAIInfoUIEffect.AddHomeScreen -> {
                    onAddToHomeScreen(it.fatherAIInfo)
                }
            }
        }
    }


    ComposeUITheme {

        NavHost(navController = navController, startDestination = AvengerAIRoute.HOME.route) {

            composable(AvengerAIRoute.HOME.route) {
                val pagingData = viewModel.fatherAIInfoPagingFlow.collectAsLazyPagingItems()
                val homeUIState = HomeUIState(pagingData)
                HomeScreen(homeUIState, onCreateClick = {
                    viewModel.performIntent(
                        HomeUIEvent.OnCreateFatherInfo(FatherAIInfo.getDefault()))
                }, onItemClick = {
                    viewModel.performIntent(HomeUIEvent.OnFatherInfoItemClick(it))
                }, onDeleteFatherAIInfo = {
                    viewModel.performIntent(HomeUIEvent.OnDeleteFatherAIInfo(it))
                }, onEditFatherAIInfo = {
                    viewModel.performIntent(HomeUIEvent.OnEditFatherAIInfo(it))
                }, onAddToHomeScreen = {
                    viewModel.performIntent(HomeUIEvent.OnAddToHomeScreenFatherAIInfo(it))
                })
            }

            composable(AvengerAIRoute.CREATE.route) {
                val createFatherAIInfoUIState =
                    viewModel.createFatherAIInfoUIState.collectAsState().value
                CreateFatherAIInfoScreen(createFatherAIInfoUIState, onSaveClick = { fatherAIInfo, isNew ->
                    viewModel.performIntent(CreateUIEvent.OnSaveClicked(fatherAIInfo, isNew))
                    navController.navigateUp()
                }, navigateBack = {
                    navController.navigateUp()
                })
            }

            composable(AvengerAIRoute.CUSTOM.route) {
                val customFatherAIInfoUIState =
                    viewModel.customFatherAIInfoUIState.collectAsState().value
                CustomFatherAIInfoScreen(customFatherAIInfoUIState, onOpenFilePicker = {
                    viewModel.performIntent(CustomUIEvent.OnAddMediaClick(it))
                }, onGenerateContent = { input, errorMessage ->
                    viewModel.performIntent(CustomUIEvent.GenerateText(input, errorMessage))
                }, onInputTextChanged = {
                    viewModel.performIntent(CustomUIEvent.InputText(it))
                }, onClear = {
                    viewModel.performIntent(CustomUIEvent.ClearText)
                }, navigateBack = {
                    navController.navigateUp()
                }, onRemoveFile = {
                    viewModel.performIntent(CustomUIEvent.RemoveFileItem(it))
                })
            }

        }
    }
}

