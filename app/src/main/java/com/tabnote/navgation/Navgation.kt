package com.tabnote.navgation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tabnote.module.EditNoteScreen
import com.tabnote.module.InputNote
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.*
import com.tabnote.screen.*
import kotlinx.coroutines.launch


@Composable
fun Navigation(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    launchApp: (String) -> Unit,
    toastShow: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)

    var navgation by remember {
        mutableStateOf("HomeScreen")
    }
    var editOpen by remember {
        mutableStateOf(false)
    }
    var personalSettings by remember {
        mutableStateOf(false)
    }
    var class_id by remember {
        mutableStateOf("")
    }
    val welcome_screen = dataStore.getWelcomeScreen.collectAsState(initial = false).value

    Box {
        Column(Modifier.padding(bottom = 60.dp)) {
            when (navgation) {
                "HomeScreen" -> HomeScreen(state, onEvent, { editOpen = it }, launchApp, toastShow)
                "PersonScreen" -> PersonScreen(state, onEvent,{ personalSettings = it },toastShow)
                "StudyScreen" -> StudyScreen()
            }
        }
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            BottomNavgation(navgation) { navgation = it }
        }
        if (navgation.equals("HomeScreen")) {
            InputNote(state, onEvent,toastShow)
        }
        if (personalSettings) {
            PersonalSettingsScreen({ personalSettings = it })
        }
        EditNoteScreen(state, onEvent, { editOpen = it }, editOpen,toastShow)
        if (welcome_screen) {
            AccountScreen()
        }
    }
}