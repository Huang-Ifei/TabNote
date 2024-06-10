package com.tabnote.module

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabnote.room.ContactEvent

@Composable
fun LinkSet(startValue: String, onEvent: (ContactEvent) -> Unit) {
    var link by remember {
        mutableStateOf(startValue)
    }
    var addLink by remember {
        mutableStateOf(false)
    }
    if (!link.equals("")) addLink = true
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            Spacer(modifier = Modifier.width(15.dp))
            if (addLink) {
                Text(text = "链接 ", fontSize = 18.sp)
                onEvent(ContactEvent.SetLink(link))
                BasicTextField(
                    value = link,
                    onValueChange = { link = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 15.dp),
                    textStyle = TextStyle(fontSize = 18.sp,color = MaterialTheme.colorScheme.primary,)
                )
            } else {
                Text(text = "添加链接", fontSize = 18.sp, modifier = Modifier.clickable { addLink = true })
            }
            Spacer(modifier = Modifier.width(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}