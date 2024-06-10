package com.tabnote.module

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.tabnote.room.ContactEvent
import com.tabnote.room.ContactState
import com.tabnote.ui.theme.*
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun InputNote(state: ContactState, onEvent: (ContactEvent) -> Unit,toastShow: (String) -> Unit) {
    var isOpen by remember {
        mutableStateOf(false)
    }
    val height by animateIntAsState(targetValue = if (isOpen) 400 else 64, label = "", animationSpec = tween(500))
    val width by animateIntAsState(targetValue = if (isOpen) 410 else 190, label = "", animationSpec = tween(400))
    val pad by animateIntAsState(targetValue = if (isOpen) 20 else 25, label = "", animationSpec = tween(500))
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1.0f else 0f,
        label = "alpha",
        animationSpec = tween(500)
    )
    Box {
        if (animatedAlpha != 0f) {
            Card(colors = CardDefaults.cardColors(HalfTransparent), modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { isOpen = false },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
                .graphicsLayer { alpha = animatedAlpha }) {
            }
        }
        if (isOpen) {
            //返回监听
            BackHandler(enabled = true, onBack = { isOpen = false })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Card(
                    modifier = Modifier
                        .width(width.dp)
                        .height(height.dp)
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                ) {
                    AddNote(state, onEvent, pushback = { isOpen = it },toastShow)
                }
                Spacer(modifier = Modifier.height((pad+60).dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
        ) {
            Row {
                if (!isOpen) {
                    Button(
                        onClick = { isOpen = true }, contentPadding = PaddingValues(),
                        modifier = Modifier
                            .size(width.dp, height.dp)
                            .padding(horizontal = pad.dp),
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp, pressedElevation = 1.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(
                            BrushColor).fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = Color(0xD9FFFFFF)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "添加计划", color = Color(0xD9FFFFFF), fontSize = 15.sp)
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height((pad+60).dp))
        }
    }
}

@Composable
fun AddNote(state: ContactState, onEvent: (ContactEvent) -> Unit, pushback: (Boolean) -> Unit,toastShow: (String) -> Unit) {
    val context = LocalContext.current
    val dataStore = PreferencesDataStore(context)
    val token = dataStore.getToken.collectAsState(initial = "").value

    val dateDialogState = rememberMaterialDialogState()
    var isError by remember {
        mutableStateOf(false)
    }

    val usr_id = dataStore.getID.collectAsState(initial = "${System.currentTimeMillis().hashCode()}").value
    //初始化
    remember {
        onEvent(ContactEvent.SetId(""))
        onEvent(ContactEvent.SetDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())))
        onEvent(ContactEvent.SetContext(""))
        onEvent(ContactEvent.SetLink(""))
    }

    Column(Modifier.fillMaxSize()) {
        Box {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Spacer(modifier = Modifier.width(25.dp))
                    Text(
                        text = "添加一项计划",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                }
            }

            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(9.dp))
                Row {
                    IconButton(onClick = {
                        pushback(false)
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(25.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.context,
                onValueChange = { onEvent(ContactEvent.SetContext(it)) },
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
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 18.sp),
                isError = isError,
                shape = AbsoluteRoundedCornerShape(10.dp),
                minLines = 4,
                maxLines = 4,
            )
        }
        //日期处理
        Spacer(modifier = Modifier.height(10.dp))
        DatePicker(dateDialogState, state.date)
        Spacer(modifier = Modifier.height(10.dp))
        LinkSet("",onEvent)
        Spacer(modifier = Modifier.height(10.dp))
        //确认
        CheckInButton(MaterialTheme.colorScheme.primary, "添加计划") {
            val t = Thread{
                if (state.context.isBlank()) {
                    isError = true
                } else {
                    pushback(false)
                    val planId = ""+usr_id.hashCode()+state.context.hashCode()+state.link.hashCode()+state.date.hashCode()
                    val content = state.context
                    val link = state.link
                    val date = state.date
                    onEvent(ContactEvent.SetId(planId))
                    Thread.sleep(100)
                    onEvent(ContactEvent.SaveContact)
                    try{
                        NoteOperation.addNote(planId,content,link,date,usr_id,token)
                    }catch (e:Exception){
                        toastShow("网络错误")
                    }
                }
            }
            t.start()
        }
        Spacer(modifier = Modifier.width(25.dp))
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
        }
    }
    //选择日期
    DatePickerDialog(dateDialogState, LocalDate.now(), {}, onEvent)
}
