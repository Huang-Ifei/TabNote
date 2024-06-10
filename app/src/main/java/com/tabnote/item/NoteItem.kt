package com.tabnote.item

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabnote.module.LinkButton
import com.tabnote.operation.NoteOperation
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.Contact
import com.tabnote.room.ContactEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun NoteItem(
    daily: Boolean,
    onEvent: (ContactEvent) -> Unit,
    contact: Contact,
    launchApp:(String)->Unit
) {
    var visible by remember {
        mutableStateOf(true)
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha",
        animationSpec = tween(500)
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    val id = dataStore.getID.collectAsState(initial = "").value
    val token = dataStore.getToken.collectAsState(initial = "").value

    val coroutineScope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .graphicsLayer { alpha = animatedAlpha }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End,
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            visible = false
                            onEvent(ContactEvent.SetId(contact.id))
                            onEvent(ContactEvent.SetDate(contact.date))
                            onEvent(ContactEvent.SetContext(contact.context))
                            onEvent(ContactEvent.SetLink(contact.link))
                            onEvent(ContactEvent.SetDone(true))
                            delay(400)
                            onEvent(ContactEvent.UpsertContact(contact))
                            visible = true
                            NoteOperation.finishNote(contact,id,token)
                        }
                    }
                },
                Modifier
                    .padding(end = 8.dp, bottom = 15.dp)
                    .fillMaxHeight()
                    .clickable() { },
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }

        Column(
            modifier = Modifier.padding(
                top = 27.dp,
                start = 25.dp,
                end = 50.dp,
                bottom = 27.dp
            )
        ) {
            Row() {
                Text(
                    text = contact.context,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Row {
                if (!daily) {
                    Text(
                        text = contact.date+"   ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                LinkButton(contact.link,launchApp)
            }
        }
    }
}

