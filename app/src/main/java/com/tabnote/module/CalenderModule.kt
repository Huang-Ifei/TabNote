package com.tabnote.module

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alibaba.fastjson2.JSONObject
import com.tabnote.operation.NoteOperation
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.room.*
import com.tabnote.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek.*
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalenderModule(state: ContactState, onEvent: (ContactEvent) -> Unit,toastShow: (String) -> Unit) {
    var year by remember {
        mutableStateOf(LocalDate.now().year)
    }
    var month by remember {
        mutableStateOf(LocalDate.now().monthValue)
    }
    var showCalender by remember {
        mutableStateOf(true)
    }
    var monthValue by remember {
        mutableStateOf(0)
    }
    var dateValue by remember {
        mutableStateOf(0)
    }
    var maxValue by remember {
        mutableStateOf(0)
    }
    var launchOnline by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    val id = dataStore.getID.collectAsState(initial = "").value
    val token = dataStore.getToken.collectAsState(initial = "").value

//    LaunchedEffect(launchOnline) {
//        if (launchOnline){
//            try{
//                withContext(Dispatchers.IO){
//                    val list = NoteOperation.historyNotesRequest(state.contacts,id,token)
//                    for(i in 0 until  list.size){
//                        val json = JSONObject.parseObject(list.get(i).toString())
//                        onEvent(ContactEvent.SetId(json.getString("plan_id")))
//                        onEvent(ContactEvent.SetContext(json.getString("content")))
//                        onEvent(ContactEvent.SetLink(json.getString("link")))
//                        onEvent(ContactEvent.SetDate(json.getString("date")))
//                        Thread.sleep(100)
//                        onEvent(ContactEvent.SaveContact)
//                    }
//                    launchOnline = false
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//                toastShow("网络错误")
//            }
//        }
//    }
    Card(
        Modifier.width(360.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(22.dp))
            Row() {
                Text(text = "${year}-${month}", fontSize = 23.sp)
            }

            Spacer(modifier = Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                Text(
                    text = "日",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "一",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "二",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "三",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "四",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "五",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "六",
                    fontSize = 12.sp,
                    modifier = Modifier.width(45.dp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (showCalender) {
                writeToShowArr(
                    state,
                    year,
                    month,
                    monthValues = { monthValue = it },
                    dateValues = { dateValue = it })
                writeToArr(
                    firstDateOfMonth(year, "%02d".format(month)),
                    year,
                    month,
                    maxValues = { maxValue = it })
                for (i in 0..4) {
                    Column(Modifier.height(50.dp)) {
                        Row() {
                            for (j in 0..6) {
                                Column(Modifier.width(45.dp), Arrangement.Top, CenterHorizontally) {
                                    Text(text = arrDate[i][j], fontSize = 18.sp)
                                    if (arrDate[i][j] != "") {
                                        ColorModule(arrShowColor[arrDate[i][j].toInt()])
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box {
                if (showCalender) {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        for (j in 0..6) {
                            println(arrDate[5][j])
                            Column(Modifier.width(45.dp), Arrangement.Top, CenterHorizontally) {
                                Text(text = arrDate[5][j], fontSize = 18.sp)
                                if (arrDate[5][j] != "") {
                                    ColorModule(arrShowColor[arrDate[5][j].toInt()])
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        border = BorderStroke(width = 1.dp, color = Border)
                    ) {
                        Icon(
                            Icons.Default.ArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .clickable {
                                    if (month == 1) {
                                        year--
                                        month = 12
                                    } else month--
                                    showCalender = false
                                    showCalender = true
                                },
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Card(
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                        border = BorderStroke(width = 1.dp, color = Border),
                    ) {
                        Icon(
                            Icons.Default.ArrowRight,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .clickable {
                                    if (month == 12) {
                                        year++
                                        month = 1
                                    } else month++
                                    showCalender = false
                                    showCalender = true
                                },
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.width(35.dp))
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
    }

}

fun firstDateOfMonth(year: Int, month: String): Int {
    return when (LocalDate.parse("$year-$month-01").dayOfWeek) {
        MONDAY -> 1
        TUESDAY -> 2
        WEDNESDAY -> 3
        THURSDAY -> 4
        FRIDAY -> 5
        SATURDAY -> 6
        SUNDAY -> 0
    }
}

fun writeToArr(startWeek: Int, year: Int, month: Int, maxValues: (Int) -> Unit) {
    var max = 0
    if (year % 4 == 0 && year % 100 != 0) {
        when (month) {
            1 -> max = 31
            2 -> max = 29
            3 -> max = 31
            4 -> max = 30
            5 -> max = 31
            6 -> max = 30
            7 -> max = 31
            8 -> max = 31
            9 -> max = 30
            10 -> max = 31
            11 -> max = 30
            12 -> max = 31
        }
    } else {
        when (month) {
            1 -> max = 31
            2 -> max = 28
            3 -> max = 31
            4 -> max = 30
            5 -> max = 31
            6 -> max = 30
            7 -> max = 31
            8 -> max = 31
            9 -> max = 30
            10 -> max = 31
            11 -> max = 30
            12 -> max = 31
        }
    }
    var n = 1
    for (i in 0..5) {
        for (j in 0..6) {
            arrDate[i][j] = ""
        }
    }
    for (j in 0..6) {
        if (j >= startWeek) {
            arrDate[0][j] = n.toString()
            n++
        }
    }
    for (i in 1..5) {
        for (j in 0..6) {
            if (n <= max) {
                arrDate[i][j] = n.toString()
                n++
            }
        }
    }
    maxValues(max)
}


fun writeToShowArr(
    state: ContactState,
    year: Int,
    month: Int,
    monthValues: (Int) -> Unit,
    dateValues: (Int) -> Unit,
) {
    var value = 0
    var dateValue = 0
    for (i in 1..31) {
        arrShowColor[i] = 0
    }
    for (contacts in state.hisContacts) {
        value++
        for (i in 1..31) {
            if (contacts.date == "${year}-${"%02d".format(month)}-${"%02d".format(i)}") {
                arrShowColor[i]++
            }
        }
    }
    for (i in 1..31) {
        if (arrShowColor[i] > 0) {
            dateValue++
        }
    }
    monthValues(value)
    dateValues(dateValue)
}



