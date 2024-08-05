package com.tabnote.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabnote.module.BrushCheckInButton
import com.tabnote.module.CheckInButton
import com.tabnote.module.PasswordInput
import com.tabnote.module.TextInput
import com.tabnote.operation.AccountOperation
import com.tabnote.operation.Cryptic
import com.tabnote.preferences.PreferencesDataStore
import com.tabnote.ui.theme.BrushColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AccountScreen() {

    var doing by remember {
        mutableStateOf("登录")
    }
    var waiting by remember {
        mutableStateOf("注册")
    }
    var name by remember {
        mutableStateOf("")
    }
    var id by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var message by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = PreferencesDataStore(context)
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(Modifier.fillMaxWidth(0.88f)) {
            Text(
                text = doing, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp), fontSize = 28.sp
            )
            Divider(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(78.dp)
                    .height(3.dp)
                    .padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.height(35.dp))
            if (doing.equals("注册")) {
                Text(
                    text = "名字", modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                TextInput(value = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(15.dp))
            }
            Text(
                text = "账号", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            TextInput(value = id, onValueChange = { id = it })
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "密码", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            PasswordInput(value = password, onValueChange = { password = it })
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(3.dp))
            BrushCheckInButton(color = BrushColor, text = doing) {
                loginOrSignup(name,id,password,doing,{message=it},scope, dataStore)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = waiting, modifier = Modifier.clickable { val done = doing;doing = waiting;waiting = done })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "跳过", modifier = Modifier.clickable {
                    scope.launch {
                        dataStore.saveWS(false)
                    }
                })
            }
        }

    }
}

fun loginOrSignup(name:String, id:String,password:String, doing:String,message:(String)->Unit,scope:CoroutineScope,dataStore: PreferencesDataStore){
    if (doing.equals("登录")) {
        val t = Thread {
            message ("发送中...")
            try{
                val mes = AccountOperation.login(id, password)
                if (mes.getString("response").equals("success")) {
                    message ("成功登录")
                    Thread.sleep(200)
                    scope.launch {
                        dataStore.saveName(mes.getString("name"))
                        dataStore.saveID(id)
                        dataStore.savePassWord(password)
                        dataStore.saveWS(false)
                        val tk = mes.getString("token")
                        dataStore.saveToken(Cryptic.decrypt(tk))
                    }
                }else{
                    message(mes.getString("response"))
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        t.start()
    }else if (doing.equals("注册")){
        val t = Thread {
            message ("发送中...")
            val mes = AccountOperation.signUp(name,id,password)
            if (mes.getString("response").equals("success")) {
                message ("成功注册并登录")
                Thread.sleep(200)
                scope.launch {
                    dataStore.saveName(name)
                    dataStore.saveID(id)
                    dataStore.savePassWord(password)
                    dataStore.saveWS(false)
                    val tk = mes.getString("token")
                    dataStore.saveToken(Cryptic.decrypt(tk))
                }
            }else{
                message(mes.getString("response"))
            }
        }
        t.start()
    }
}
