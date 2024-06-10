package com.tabnote.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadRowItems(classes:List<String>) {

    LazyRow() {
        item {
            Spacer(modifier = Modifier.width(20.dp))
        }
        for (i in 0 until classes.size) {
            item {
                Card ( onClick = {},colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(text = classes[i],color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}