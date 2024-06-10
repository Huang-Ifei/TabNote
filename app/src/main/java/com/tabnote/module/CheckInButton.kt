package com.tabnote.module

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabnote.room.ContactEvent

@Composable
fun CheckInButton(color: Color,text:String,onClick :()->Unit){
    Button(
        onClick = { onClick() },
        contentPadding = PaddingValues(),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = AbsoluteRoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .fillMaxSize(),
            Alignment.Center
        ) {
            Text(text = text, color = Color.White, fontSize = 17.sp)

        }
    }
}

@Composable
fun BrushCheckInButton(color: Brush,text:String,onClick :()->Unit){
    Button(
        onClick = { onClick() },
        contentPadding = PaddingValues(),
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = AbsoluteRoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .fillMaxSize(),
            Alignment.Center
        ) {
            Text(text = text, color = Color.White, fontSize = 17.sp)

        }
    }
}