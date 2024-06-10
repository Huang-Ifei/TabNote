package com.tabnote.module

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColumnCard(text:String,onClick:()-> Unit){
    Column(modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onClick() }, verticalArrangement = Arrangement.Center) {
        Text(text = text, modifier = Modifier.padding(horizontal = 20.dp))
    }
}