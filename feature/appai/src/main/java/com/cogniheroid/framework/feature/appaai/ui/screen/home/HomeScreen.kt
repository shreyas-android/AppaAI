package com.cogniheroid.framework.feature.appaai.ui.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cogniheroid.framework.feature.appaai.ui.uistate.HomeUIState
import com.cogniheroid.framework.feature.appai.R
import com.cogniheroid.framework.shared.core.fatherai.data.model.FatherAIInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeUIState : HomeUIState,
               onCreateClick:()->Unit,
               onAddToHomeScreen:(FatherAIInfo)->Unit,
               onEditFatherAIInfo:(FatherAIInfo) -> Unit,
               onDeleteFatherAIInfo:(FatherAIInfo) -> Unit,
               onItemClick : (FatherAIInfo) -> Unit){
    Scaffold(topBar = {
        val colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
        Surface(tonalElevation = 3.dp) {
            TopAppBar(modifier = Modifier.statusBarsPadding(), colors = colors, title = {
                Text(
                    text = stringResource(id = R.string.app_name), fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            })
        }
    }, floatingActionButton = {
        FloatingActionButton(onClick = { onCreateClick() }) {
            Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
        }
    }) {

        FatherAIListScreen(Modifier.padding(it), homeUIState.fatherAIInfoItems, onAddToHomeScreen = onAddToHomeScreen,
            onEditFatherAIInfo = onEditFatherAIInfo, onDeleteFatherAIInfo = onDeleteFatherAIInfo){ fatherAIInfo ->
            onItemClick(fatherAIInfo)
        }

    }
}
