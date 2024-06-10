package com.tabnote.module

import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tabnote.room.ContactEvent
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerDialog(
    dateDialogState:MaterialDialogState,
    dateSelect: LocalDate,
    formatDate : (String) ->Unit,
    onEvent: (ContactEvent) -> Unit){
    var date = dateSelect
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(
                text = "确认",
                onClick = { },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
            )
        },
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = AbsoluteRoundedCornerShape(15.dp)
    ) {
        datepicker(
            initialDate = dateSelect, title = "选择日期", colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = Color.Black,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            date = it
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
            formatDate(format)
            onEvent(ContactEvent.SetDate(format))
        }
    }
}