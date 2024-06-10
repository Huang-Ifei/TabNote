
package com.tabnote.module

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabnote.operation.NoteOperation
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.Contact
import com.tabnote.room.ContactEvent
import com.tabnote.room.ContactState
import com.tabnote.room.editNum
import com.tabnote.ui.theme.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//通过id来寻找contact
fun whichContact(state: ContactState): Contact {
    for (contact in state.contacts) {
        if (contact.id == editNum) {
            return contact
        }
    }
    return TODO("提供返回值")
}

@Composable
fun EditNoteScreen(state: ContactState, onEvent: (ContactEvent) -> Unit, onEdit: (Boolean) -> Unit, editOpen: Boolean,toastShow: (String) -> Unit){
    val animatedAlpha by animateFloatAsState(
        targetValue = if (editOpen) 1.0f else 0f,
        label = "alpha",
        animationSpec = tween(500)
    )
    Box {
        if (animatedAlpha!=0.0f){
            BackHandler(enabled = true, onBack = { onEdit( false) })
            Card(colors = CardDefaults.cardColors(HalfTransparent), modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { onEdit(false) },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
                .graphicsLayer { alpha = animatedAlpha }) {
            }
            EditNote(state, onEvent, onEdit ,editOpen,toastShow)
        }
    }
}

@Composable
fun EditNote(state: ContactState, onEvent: (ContactEvent) -> Unit, onEdit: (Boolean) -> Unit, editOpen: Boolean,toastShow: (String) -> Unit) {
    val contactSelect by remember {
        mutableStateOf(whichContact(state))
    }
    val contactId by remember {
        mutableStateOf(contactSelect.id)
    }
    var context by remember {
        mutableStateOf(contactSelect.context)
    }
    var link by remember {
        mutableStateOf(contactSelect.link)
    }
    var dateSelect by remember {
        mutableStateOf(LocalDate.parse(contactSelect.date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    }
    var formatDate by remember {
        mutableStateOf(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateSelect))
    }
    val contextt = LocalContext.current
    val dataStore = PreferencesDataStore(contextt)
    val id = dataStore.getID.collectAsState(initial = "").value
    val token = dataStore.getToken.collectAsState(initial = "").value

    var isError by remember {
        mutableStateOf(false)
    }
    var isDelete by remember {
        mutableStateOf(false)
    }
    var isSave by remember {
        mutableStateOf(false)
    }
    var isClose by remember {
        mutableStateOf(false)
    }
    var isOpen by remember {
        mutableStateOf(false)
    }
    var arrangement by remember {
        mutableStateOf(Arrangement.Top)
    }
    val height by animateIntAsState(
        targetValue = if (isDelete || isClose || isSave) 0 else if (isOpen) 460 else 0,
        label = "",
        animationSpec = tween(460)
    )
    val coroutineScope = rememberCoroutineScope()
    onEvent(ContactEvent.SetId(contactId))
    onEvent(ContactEvent.SetDate(formatDate))
    onEvent(ContactEvent.SetContext(context))
    onEvent(ContactEvent.SetLink(link))
    val dateDialogState = rememberMaterialDialogState()
    isOpen = true
    if (editOpen == false) {
        isOpen = false
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .height(460.dp), verticalArrangement = arrangement
        ) {
            Card(
                Modifier
                    .height(height.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.fillMaxSize()) {
                    Box {
                        Column {
                            Spacer(modifier = Modifier.height(25.dp))
                            Row {
                                Spacer(modifier = Modifier.width(25.dp))
                                Text(
                                    text = "修改计划", fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row {
                                IconButton(onClick = {
                                    arrangement = Arrangement.Top
                                    isClose = true
                                    onEdit(false)
                                }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(7.dp))
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = context,
                            onValueChange = {
                                context = it
                                onEvent(ContactEvent.SetContext(context))
                            },
                            colors = TextFieldDefaults.colors(
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectionColors = TextSelectionColors(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.onSecondary
                                ),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            label = { Text(text = "内容") },
                            minLines = 4,
                            maxLines = 4,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 18.sp),
                            isError = isError,
                            shape = AbsoluteRoundedCornerShape(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    DatePicker(dateDialogState,formatDate)
                    Spacer(modifier = Modifier.height(10.dp))
                    LinkSet(link, onEvent)
                    Spacer(modifier = Modifier.height(10.dp))
                    CheckInButton(color = MaterialTheme.colorScheme.primary, text = "保存计划") {
                        if (context.isBlank()) {
                            isError = true
                        } else {
                            arrangement = Arrangement.Center
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    delay(160)
                                    isSave = true
                                    onEdit(false)
                                    onEvent(ContactEvent.UpsertContact(contactSelect))
                                    try {
                                        NoteOperation.resetNote(id,token,contactId,state.context,state.link,state.date)
                                    }catch (e:Exception){
                                        toastShow("网络错误")
                                    }
                                }
                            }

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CheckInButton(color = Color.Black, text = "删除计划") {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                isDelete = true
                                onEdit(false)
                                arrangement = Arrangement.Bottom
                                onEvent(ContactEvent.DeleteContact(contactSelect))
                                try{
                                    NoteOperation.deleteNote(id,token,contactId,state.context,state.link,state.date)
                                }catch (e:Exception){
                                    e.printStackTrace()
                                    toastShow("网络错误")
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {

                    }
                }
            }
        }
    }
    DatePickerDialog(dateDialogState, dateSelect,{formatDate=it}, onEvent)
}