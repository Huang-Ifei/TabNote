package com.tabnote.screen

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.JavascriptInterface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tabnote.operation.Cryptic
import com.tabnote.preferences.PreferencesDataStore
import java.net.HttpCookie

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun StudyScreen() {
    var url by remember {
        mutableStateOf("http://101.42.31.139")
    }

    val context = LocalContext.current
    val dataStore = PreferencesDataStore(context)

    val id = dataStore.getID.collectAsState(initial = "").value
    val token = dataStore.getToken.collectAsState(initial = "").value
    val name = dataStore.getName.collectAsState(initial = "").value

    var webView by remember {
        mutableStateOf<WebView?>(null)
    }
    CookieManager.getInstance().setCookie(url, HttpCookie("id", id).toString())
    CookieManager.getInstance().setCookie(url, HttpCookie("encryptionToken",Cryptic.encrypt(token)).toString())
    CookieManager.getInstance().setCookie(url, HttpCookie("name", name).toString())
    CookieManager.getInstance().setCookie(url, HttpCookie("isApp", "true").toString())

    Card(modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(Color(30,30,30))){
        AndroidView(factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.setSupportMultipleWindows(true)
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {

                }
                loadUrl(url)
                webView?.addJavascriptInterface(
                    CookieManagerWrapper(),
                    "CookieManager"
                )
            }
        }, update = {
            webView?.loadUrl(url)
        }, modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
        )
    }

}

class CookieManagerWrapper {

    @JavascriptInterface
    fun getCookie(url: String): String {
        return CookieManager.getInstance().getCookie(url)
    }

    @JavascriptInterface
    fun setCookie(url: String, value: String) {
        CookieManager.getInstance().setCookie(url, value)
    }

}
