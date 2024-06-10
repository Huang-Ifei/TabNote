package com.tabnote.module

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(value: String, onValueChange: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            Spacer(modifier = Modifier.width(15.dp))
            BasicTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun PasswordInput(value: String, onValueChange: (String) -> Unit) {
    var s by remember {
        mutableStateOf(value)
    }
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier.padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            Spacer(modifier = Modifier.width(15.dp))
            BasicTextField(
                value = s,
                onValueChange = { s = it;onValueChange(s) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSecondaryContainer),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun AiTextInput(value: String, onValueChange: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp).fillMaxWidth(0.75f),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row() {
            Spacer(modifier = Modifier.width(15.dp))
            BasicTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                maxLines = 10,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 17.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}