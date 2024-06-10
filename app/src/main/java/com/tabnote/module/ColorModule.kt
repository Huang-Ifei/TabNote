package com.tabnote.module

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tabnote.ui.theme.*

@Composable
fun ColorModule(level:Int){
    if(level==0) {

    }
    else if (level==1){
        Card(
            colors = CardDefaults.cardColors(
                YellowLevel1
            ), modifier = Modifier
                .size(10.dp)
                .padding(2.dp)
        ) {}
    }else if (level==2){
        Card(
            colors = CardDefaults.cardColors(
                YellowLevel2
            ), modifier = Modifier
                .size(10.dp)
                .padding(2.dp)
        ) {}
    }else if (level>=3){
        Card(
            colors = CardDefaults.cardColors(
                YellowLevel3
            ), modifier = Modifier
                .size(10.dp)
                .padding(2.dp)
        ) {}
    }
}