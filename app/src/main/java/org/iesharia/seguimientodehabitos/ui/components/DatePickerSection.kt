package org.iesharia.seguimientodehabitos.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DatePickerSection(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val year = selectedDate.year
    val month = selectedDate.monthValue - 1 // DatePicker usa 0-based months
    val day = selectedDate.dayOfMonth

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, y: Int, m: Int, d: Int ->
                onDateChange(LocalDate.of(y, m + 1, d))
            },
            year, month, day
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Row(
        modifier = Modifier
            .clickable { datePickerDialog.show() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = selectedDate.toString())
    }
}

