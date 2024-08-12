package com.cogniheroid.framework.feature.appaai.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.compose.LazyPagingItems
import com.cogniheroid.framework.feature.appaai.utils.items
import com.cogniheroid.framework.feature.appai.R
import com.cogniheroid.framework.shared.core.fatherai.data.enum.GeminiAIModel
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo
import com.google.android.material.elevation.SurfaceColors

@Composable
fun FatherAIListScreen(
        modifier : Modifier, fatherAIInfoItems : LazyPagingItems<FatherAIInfo>,
        onAddToHomeScreen : (FatherAIInfo) -> Unit, onEditFatherAIInfo : (FatherAIInfo) -> Unit,
        onDeleteFatherAIInfo : (FatherAIInfo) -> Unit, onItemClick : (FatherAIInfo) -> Unit) {
    PagingFatherAIInfoContainer(
        modifier
            .fillMaxSize()
            .padding(top = 16.dp), fatherAIInfoItems, onAddToHomeScreen,
        onEditFatherAIInfo, onDeleteFatherAIInfo, onItemClick)
}

@Composable
fun PagingFatherAIInfoContainer(
        modifier : Modifier, fatherAIInfoItems : LazyPagingItems<FatherAIInfo>,
        onAddToHomeScreen : (FatherAIInfo) -> Unit, onEditFatherAIInfo : (FatherAIInfo) -> Unit,
        onDeleteFatherAIInfo : (FatherAIInfo) -> Unit, onItemClick : (FatherAIInfo) -> Unit) {

    LazyColumn(modifier = modifier) {
        items(fatherAIInfoItems) { fatherAIInfo ->
            if(fatherAIInfo != null) {
                FatherAIInfoCard(fatherAIInfo = fatherAIInfo, onDeleteFatherAIInfo = {
                    onDeleteFatherAIInfo(fatherAIInfo)
                }, onEditFatherAIInfo = {
                    onEditFatherAIInfo(fatherAIInfo)
                }, onAddToHomeScreen = {
                    onAddToHomeScreen(fatherAIInfo)
                }, onItemClick = {
                    onItemClick(it)
                })
            }
        }
    }
}

@Composable
fun FatherAIInfoCard(
        fatherAIInfo : FatherAIInfo, onAddToHomeScreen : (FatherAIInfo) -> Unit,
        onEditFatherAIInfo : (FatherAIInfo) -> Unit, onDeleteFatherAIInfo : (FatherAIInfo) -> Unit,
        onItemClick : (FatherAIInfo) -> Unit) {

    val context = LocalContext.current
    androidx.compose.material3.Card(
        colors = CardDefaults.cardColors(containerColor = Color(SurfaceColors.SURFACE_5.getColor(context))),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick(fatherAIInfo)
            }) {
        ConstraintLayout(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {
            val (title, model, menu) = createRefs()
            Text(text = fatherAIInfo.title, fontSize = 16.sp, fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(6.dp))

            val modelName = when(fatherAIInfo.model){
                GeminiAIModel.GEMINI_PRO -> stringResource(id = R.string.label_gemini_pro)
                GeminiAIModel.GEMINI_FLASH -> stringResource(id = R.string.label_gemini_flash)
            }
            Text(text = modelName, fontSize = 14.sp,
                modifier = Modifier
                    .constrainAs(model) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(6.dp))

            DropDownMenu(Modifier.constrainAs(menu) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }, onAddToHomeScreen = {
                onAddToHomeScreen(fatherAIInfo)
            }, onDeleteFatherAIInfo = {
                onDeleteFatherAIInfo(fatherAIInfo)
            }, onEditFatherAIInfo = {
                onEditFatherAIInfo(fatherAIInfo)
            })
        }
    }
}

@Composable
private fun DropDownMenu(
        modifier : Modifier, onAddToHomeScreen : () -> Unit, onEditFatherAIInfo : () -> Unit,
        onDeleteFatherAIInfo : () -> Unit) {

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        MenuIconButton {
            isDropDownExpanded.value = !isDropDownExpanded.value
        }
        androidx.compose.material.MaterialTheme(
            shapes = androidx.compose.material.MaterialTheme.shapes.copy(
                small = RoundedCornerShape(10.dp),
                medium = RoundedCornerShape(10.dp),
                large = RoundedCornerShape(10.dp))) {
            DropdownMenu(modifier = Modifier, expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                CustomDropDownMenuItem(R.string.label_add_to_home_screen) {
                    isDropDownExpanded.value = false
                    onAddToHomeScreen()
                }
                CustomDropDownMenuItem(R.string.label_edit) {
                    isDropDownExpanded.value = false
                    onEditFatherAIInfo()
                }
                CustomDropDownMenuItem(R.string.label_delete) {
                    isDropDownExpanded.value = false
                    onDeleteFatherAIInfo()
                }
            }
        }
    }
}

@Composable
fun CustomDropDownMenuItem(stringRes : Int, onClick : () -> Unit) {
    DropdownMenuItem(text = {
        Text(stringResource(stringRes), )
    }, onClick = onClick)
}

@Composable
fun MenuIconButton(onClick : () -> Unit) {

    Icon(
        painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_menu_overflow_material),
        contentDescription = stringResource(R.string.desc_menu), modifier = Modifier.clickable {
                onClick()
            })
}
