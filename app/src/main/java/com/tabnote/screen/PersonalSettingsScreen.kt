package com.tabnote.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.tabnote.module.CheckInButton
import com.tabnote.module.ColumnCard
import com.tabnote.module.TextInput
import com.tabnote.operation.AccountOperation
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.service.MesType
import kotlinx.coroutines.launch

@Composable
fun PersonalSettingsScreen(personalSettings: (Boolean) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    val name = dataStore.getName.collectAsState(initial = "").value.toString()
    val id = dataStore.getID.collectAsState(initial = "").value.toString()
    val token = dataStore.getToken.collectAsState(initial = "").value

    var tokens by remember { mutableStateOf(JSONArray()) }

    var response by remember {
        mutableStateOf("")
    }
    var resJson by remember {
        mutableStateOf(JSONObject())
    }

    val coroutineScope = rememberCoroutineScope()

    var password by remember {
        mutableStateOf("")
    }
    var change by remember {
        mutableStateOf("")
    }

    BackHandler(enabled = true, onBack = { personalSettings(false) })
    var edit by remember {
        mutableStateOf(0)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        if (edit == 0) {
            Row(
                Modifier
                    .statusBarsPadding()
                    .padding(start = 15.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { personalSettings(false) }) {
                    Icon(Icons.Default.ChevronLeft, null, modifier = Modifier.size(30.dp))
                }
                Text(text = "个人信息", fontSize = 18.sp)
            }
        }
        Box {
            LazyColumn {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        ColumnCard(text = "名字： ${name}") {
                            response = ""
                            edit = MesType.resetName
                        }
                        ColumnCard(text = "账号： ${id}") {
                            response = ""
                            edit = MesType.resetID
                        }
                        ColumnCard(text = "密码更改") {
                            response = ""
                            edit = MesType.resetPassword
                        }
                        ColumnCard(text = "登录管理") {
                            response = ""
                            edit = MesType.tokenSettings
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                item {

                    CheckInButton(color = Color.Black, text = "注销登录") {
                        val t = Thread {
                            AccountOperation.cancelLogin(token)
                            scope.launch {
                                dataStore.saveToken("")
                                dataStore.savePassWord("")
                                dataStore.saveID("")
                                dataStore.saveName("")
                                dataStore.saveWS(true)
                                personalSettings(false)
                            }
                        }
                        t.start()
                    }
                }
            }
            if (edit == MesType.resetName) {
                change = name
                Card {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.Center
                    ) {
                        BackHandler {
                            edit = 0
                        }
                        Text(
                            text = "输入新名字",
                            modifier = Modifier.padding(start = 25.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextInput(value = change, onValueChange = { change = it })
                        Text(
                            text = response,
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        CheckInButton(color = MaterialTheme.colorScheme.primary, text = "重置") {
                            val t = Thread {
                                resJson = AccountOperation.resetName(token, change)
                                response = resJson.getString("response")
                                scope.launch {
                                    if (response.equals("success")) {
                                        dataStore.saveName(resJson.getString("name"))
                                        edit = 0
                                    }
                                }
                            }
                            t.start()
                        }
                    }
                }
                Row(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 15.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { edit = 0 }) {
                        Icon(Icons.Default.ChevronLeft, null, modifier = Modifier.size(30.dp))
                    }
                    Text(text = "编辑名字", fontSize = 18.sp)
                }
            } else if (edit == MesType.resetID) {
                change = id
                Card {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.Center
                    ) {
                        BackHandler {
                            edit = 0
                        }
                        Text(
                            text = "输入新账号",
                            modifier = Modifier.padding(start = 25.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextInput(value = change, onValueChange = { change = it })
                        Text(
                            text = response,
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        CheckInButton(color = MaterialTheme.colorScheme.primary, text = "重置") {
                            val t = Thread {
                                resJson = AccountOperation.resetID(token, change)
                                response = resJson.getString("response")
                                scope.launch {
                                    if (response.equals("success")) {
                                        dataStore.saveID(resJson.getString("id"))
                                        edit = 0
                                    }
                                }
                            }
                            t.start()
                        }
                    }
                }
                Row(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 15.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { edit = 0 }) {
                        Icon(Icons.Default.ChevronLeft, null, modifier = Modifier.size(30.dp))
                    }
                    Text(text = "编辑账号", fontSize = 18.sp)
                }
            } else if (edit == MesType.resetPassword) {
                password = ""
                change = ""
                Card {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.Center
                    ) {
                        BackHandler {
                            edit = 0
                        }
                        Text(
                            text = "输入原密码",
                            modifier = Modifier.padding(start = 25.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextInput(value = password, onValueChange = { password = it })
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "输入新密码",
                            modifier = Modifier.padding(start = 25.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextInput(value = change, onValueChange = { change = it })
                        Text(
                            text = response,
                            Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        CheckInButton(color = MaterialTheme.colorScheme.primary, text = "重置") {
                            val t = Thread {
                                resJson = AccountOperation.resetPassword(id, password, change)
                                response = resJson.getString("response")
                                scope.launch {
                                    if (response.equals("success")) {
                                        dataStore.savePassWord(resJson.getString("password"))
                                        edit = 0
                                    }
                                }
                            }
                            t.start()
                        }
                    }
                }
                Row(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 15.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { edit = 0 }) {
                        Icon(Icons.Default.ChevronLeft, null, modifier = Modifier.size(30.dp))
                    }
                    Text(text = "编辑密码", fontSize = 18.sp)
                }
            } else if (edit == MesType.tokenSettings) {
                val t = Thread {
                    try {
                        val jsonObject = AccountOperation.getTokensById(id, token)
                        val array = jsonObject.getJSONArray("tokens")
                        if (array != null) {
                            tokens = array
                        } else {
                            tokens.add("{\"date\":\"\",\"token\":\"您的Token可能已经失效\"}")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        tokens.add("{\"date\":\"\",\"token\":\"获取失败\"}")
                    }
                }
                t.start()
                Card {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BackHandler {
                            edit = 0
                        }
                        LazyColumn {
                            item {
                                Text(
                                    "本机token:$token", color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text("服务器Tokens：", color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            items(tokens.size) {
                                Text(
                                    "登录token:" + tokens.getJSONObject(it).getString("token") + "\n" +
                                            "首次登录时间：" + tokens.getJSONObject(it).getString("date"),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                if (!tokens.getJSONObject(it).getString("token").equals(token)) {
                                    Text("强制下线", color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable{
                                        val tt = Thread {
                                            try {
                                                AccountOperation.cancelLogin(tokens.getJSONObject(it).getString("token"))
                                                val jsonObject = AccountOperation.getTokensById(id, token)
                                                val array = jsonObject.getJSONArray("tokens")
                                                if (array != null) {
                                                    tokens = array
                                                } else {
                                                    tokens.add("{\"date\":\"\",\"token\":\"您的Token可能已经失效\"}")
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                tokens.add("{\"date\":\"\",\"token\":\"获取失败\"}")
                                            }
                                        }
                                        tt.start()
                                    })

                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
                Row(
                    Modifier
                        .statusBarsPadding()
                        .padding(start = 15.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { edit = 0 }) {
                        Icon(Icons.Default.ChevronLeft, null, modifier = Modifier.size(30.dp))
                    }
                    Text(text = "登录管理", fontSize = 18.sp)
                }
            }
        }
    }
}