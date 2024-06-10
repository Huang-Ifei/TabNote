package com.tabnote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.tabnote.navgation.Navigation
import com.tabnote.room.ContactDatabase
import com.tabnote.room.ContactViewModel
import com.tabnote.ui.theme.TabNoteTheme


class MainActivity : ComponentActivity() {


    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

    private fun launchLink(link: String) {
        //分割信息
        val stringSplit = link.split("}{")
        //提示
        Toast.makeText(baseContext, "链接已复制，正在跳转", Toast.LENGTH_SHORT).show()
        //确定跳转的方向
        var intent = Intent()
        if (stringSplit[0].equals("web")) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(stringSplit[1]))
        } else {
            intent.setClassName(stringSplit[0], stringSplit[1])
            intent.setData(Uri.parse(stringSplit[2]))
        }
        //跳转
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(baseContext, "出错了！请检查是否安装APP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchToast(s: String) {
        Toast.makeText(baseContext, s, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TabNoteTheme {
                val state by viewModel.state.collectAsState()
                var launchApp by remember {
                    mutableStateOf("")
                }
                if (!launchApp.equals("")) {
                    launchLink(launchApp)
                    launchApp = ""
                }
                var toastShow by remember {
                    mutableStateOf("")
                }
                if (!toastShow.equals("")) {
                    launchToast(toastShow)
                }
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(
                        state,
                        viewModel::onEvent,
                        { launchApp = it },
                        { toastShow = it })
                }
            }
        }
    }
}


