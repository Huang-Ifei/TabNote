package com.tabnote.module

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialogState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(dateDialogState:MaterialDialogState,formatDate:String) {
    Row {
        Card(
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
            modifier = Modifier.padding(horizontal = 20.dp),
            onClick = { dateDialogState.show() }) {
            Spacer(modifier = Modifier.height(10.dp));
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "计划日期:", fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = formatDate,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
            Spacer(modifier = Modifier.height(10.dp));
        }
    }
}
