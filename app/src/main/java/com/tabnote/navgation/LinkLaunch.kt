package com.tabnote.navgation

import androidx.compose.runtime.Composable
import copyToClipboard

@Composable
fun LinkLaunch(link: String, launchApp:(String)->Unit) {
    if (link.contains("抖音") || link.contains("douyin.com")) {
        copyToClipboard(link)
        launchApp("com.ss.android.ugc.aweme}{com.ss.android.ugc.aweme.main.MainActivity}{${link}")

    } else if (link.contains("http://")||link.contains("https://")) {
        val stringSplit = link.split("http")
        copyToClipboard(link)
        launchApp("web}{http${stringSplit[stringSplit.size-1]}")
    } else {
        copyToClipboard(link)
        launchApp("web}{https://www.baidu.com/s?word=$link")
    }
}


