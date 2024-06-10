package com.tabnote.navgation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavgationIcon(
    name: String,
    width: Float,
    imageVector: ImageVector,
    navgation: String,
    navgationChange: (String) -> Unit
) {
    if (navgation == name) {
        Card(
            modifier = Modifier
                .fillMaxWidth(width)
                .height(50.dp)
                .padding(10.dp), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector,
                    contentDescription = null,
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    } else {
        Card(modifier = Modifier
            .fillMaxWidth(width)
            .height(50.dp)
            .padding(10.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            onClick = {navgationChange(name)}
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black,
                )
            }
        }
    }
}