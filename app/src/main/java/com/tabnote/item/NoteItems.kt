package com.tabnote.item

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tabnote.room.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItems(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    paddingValues: PaddingValues,
    onEdit: (Boolean) -> Unit,
    launchApp: (String) -> Unit,
    daily: Boolean
) {
    val date by remember {
        mutableStateOf(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()))
    }
    ContactEvent.SortContact(sortType = SortType.DATE)
        LazyColumn(Modifier.padding(paddingValues)) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(state.contacts) { contact ->
                if (daily && date == contact.date) {
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .padding(start = 22.dp, end = 22.dp, bottom = 12.dp),
                        onClick = {
                            editNum = contact.id
                            onEdit(true)
                        },
                    ) {
                        NoteItem(true, onEvent, contact, launchApp)
                    }
                } else if (!daily) {
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .padding(start = 22.dp, end = 22.dp, bottom = 12.dp),
                        onClick = {
                            editNum = contact.id
                            onEdit(true)
                        },
                    ) {
                        NoteItem(false, onEvent, contact, launchApp)
                    }
                }
            }
            item {
                if((daily && noDailyItem(state))||state.contacts.isEmpty()){
                    Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background), modifier = Modifier.fillMaxWidth().height(300.dp)) {

                    }
                }
            }
            item { Spacer(modifier = Modifier.height(90.dp)) }
        }

        //暂无计划
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
                .padding(paddingValues)
        ) {
            if (daily && noDailyItem(state)) {
                Text(text = "今日暂无计划", color = Color.DarkGray)
            } else if (state.contacts.isEmpty()) {
                Text(text = "请添加一个计划", color = Color.DarkGray)
            }
        }
}

//当日是否无计划
fun noDailyItem(state: ContactState): Boolean {
    val date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
    for (contact in state.contacts) {
        if (contact.date == date) return false
    }
    return true
}