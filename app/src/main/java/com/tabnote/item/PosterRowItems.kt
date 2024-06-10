package com.tabnote.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alibaba.fastjson2.JSONObject

@Composable
fun PosterRowItems(list:ArrayList<JSONObject>,classChange:(String)->Unit) {
    LazyRow() {
        item { Spacer(modifier = Modifier.width(10.dp)) }
        items(list) {
            Card(modifier = Modifier.padding(10.dp)) {
                AsyncImage(
                    model = it.get("link"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable {
                            try {
                                classChange(it.getString("class_id"))
                            }catch (e:Exception){
                             e.printStackTrace()
                            }
                        }
                        .width(300.dp)
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.surface))
            }
        }
        item { Spacer(modifier = Modifier.width(10.dp)) }
    }
}