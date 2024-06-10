package com.tabnote.module

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.tabnote.navgation.LinkLaunch

@Composable
fun LinkButton(link:String,launchApp:(String)->Unit) {
    //处理链接打开点击
    var click by remember {
        mutableStateOf(false)
    }
    if (click) {
        LinkLaunch(link,launchApp)
        click=false
    }

    if(!link.equals("")){
        Text(
            text = "打开链接",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                click=true
            }
        )
    }
}