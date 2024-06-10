package com.tabnote.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alibaba.fastjson2.JSONObject
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.tabnote.item.NoteItems
import com.tabnote.operation.NoteOperation
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.ContactEvent
import com.tabnote.room.ContactState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedTransitionTargetStateParameter")
@Composable
fun HomeScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    onEdit: (Boolean) -> Unit,
    launchApp: (String) -> Unit,
    toastShow: (String) -> Unit
) {
    var notesList by remember {
        mutableStateOf("今日计划")
    }
    var refreshing by rememberSaveable {
        mutableStateOf(false)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    val token = dataStore.getToken.collectAsState(initial = "").value
    val id = dataStore.getID.collectAsState(initial = "").value

    LaunchedEffect(refreshing) {
        if (refreshing) {
            withContext(Dispatchers.IO) {
                try {
                    val list = NoteOperation.notesRequest(state.allContacts,id ,token)
                    if (list == null) {
                        toastShow("")
                        toastShow("网络错误")
                    } else {
                        toastShow("")
                        toastShow("同步成功")
                        for (i in 0 until list.size) {
                            val json = JSONObject.parseObject(list.get(i).toString())
                            onEvent(ContactEvent.SetId(json.getString("plan_id")))
                            onEvent(ContactEvent.SetContext(json.getString("content")))
                            onEvent(ContactEvent.SetLink(json.getString("link")))
                            onEvent(ContactEvent.SetDate(json.getString("date")))
                            onEvent(ContactEvent.SetDone(json.getBoolean("done")))
                            println(json.getString("plan_id")+":"+json.getBoolean("done"))
                            Thread.sleep(100)
                            onEvent(ContactEvent.SaveContact)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            delay(100)
            refreshing = false
        }
    }
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Box {
            SwipeRefresh(
                state = rememberSwipeRefreshState(refreshing),
                onRefresh = { refreshing = true },
            ) {
                Scaffold(
                    Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.background,
                    topBar = {
                        MediumTopAppBar(
                            title = { Text(text = notesList, modifier = Modifier.padding(10.dp)) },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                scrolledContainerColor = MaterialTheme.colorScheme.background
                            ),
                            actions = {
                                IconButton(onClick = {
                                    if (notesList.equals("今日计划")) notesList = "所有计划"
                                    else if (notesList.equals("所有计划")) notesList = "今日计划"
                                }) {
                                    if (notesList.equals("今日计划")) {
                                        Icon(
                                            Icons.Default.MoreVert,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.FormatListBulleted,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.width(15.dp))
                            }
                        )
                    }) { values ->
                    when (notesList) {
                        "今日计划" -> NoteItems(state, onEvent, values, onEdit, launchApp, true)
                        "所有计划" -> NoteItems(state, onEvent, values, onEdit,  launchApp, false)
                    }
                }
            }
        }
    }
}

