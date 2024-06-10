package com.tabnote.navgation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavgation(navgation: String, navgationChange: (String) -> Unit) {
    Divider(Modifier.fillMaxWidth().height(0.5.dp), color = Color.LightGray)
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            Modifier
                .navigationBarsPadding()
                .height(60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavgationIcon("HomeScreen",1/3f,Icons.Default.Home,navgation,navgationChange)
            NavgationIcon("StudyScreen",1/2f,Icons.Default.AutoAwesome,navgation,navgationChange)
            NavgationIcon("PersonScreen",1f,Icons.Default.Person,navgation,navgationChange)
        }
    }
}