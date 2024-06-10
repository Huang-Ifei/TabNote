package com.tabnote.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tabnote.module.CalenderModule
import com.tabnote.operation.Set
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.ContactEvent
import com.tabnote.room.ContactState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    personalSettings: (Boolean) -> Unit,
    toastShow: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    val name = dataStore.getName.collectAsState(initial = " ").value
    val id = dataStore.getID.collectAsState(initial = "").value
    LazyColumn(
        Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.statusBarsPadding())
            Spacer(Modifier.height(55.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.size(90.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    onClick = {
                        if (name.toString().equals("")) {
                            scope.launch {
                                dataStore.saveWS(true)
                            }
                        }
                    }) {
                    AsyncImage(
                        model = Set.ip+"accountImg?id="+id,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(90.dp)
                            .height(90.dp)
                            .background(MaterialTheme.colorScheme.surface),
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                if (name.toString().equals("")) {
                    Text(text = "点击登录", fontSize = 22.sp, modifier = Modifier.clickable {
                        scope.launch {
                            dataStore.saveWS(true)
                        }
                    })
                } else {
                    Text(text = name.toString(), fontSize = 22.sp, modifier = Modifier.clickable {
                        scope.launch {
                            personalSettings(true)
                        }
                    })
                }

            }
            Spacer(Modifier.height(45.dp))
        }
        item {
            CalenderModule(state, onEvent, toastShow)
        }
    }
}