package com.example.juntavecinos.ui.neighbor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.juntavecinos.Model.EventType
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeighborReserveForm(
    dateSelected: LocalDate,
    onReserveSubmit: (
        title: String,
        description: String,
        eventType: EventType,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ) -> Unit = { _, _, _, _, _, _ -> }
) {
    var title by remember { mutableStateOf("") } // between 6 and 40
    var description by remember { mutableStateOf("") } // up to 150
    var selectedEventType by remember { mutableStateOf(EventType.OTHER) }
    var expandedDropdown by remember { mutableStateOf(false) }

    var currentDate by remember { mutableStateOf(dateSelected) }
    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(11, 0)) }

    var showDatePicker by remember { mutableStateOf(true) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateSelected.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            BasicText(
                text = stringResource(R.string.date),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = currentDate.format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM yyyy", Locale.getDefault())),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape
                )
            }

            BasicText(
                text = stringResource(R.string.schedule),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showStartTimePicker = true }
                ) {
                    OutlinedTextField(
                        value = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        onValueChange = {},
                        label = { Text(stringResource(R.string.start)) },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showEndTimePicker = true }
                ) {
                    OutlinedTextField(
                        value = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        onValueChange = {},
                        label = { Text(stringResource(R.string.end)) },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape
                    )
                }
            }

            BasicText(
                text = stringResource(R.string.calendarEventTitle),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = title,
                onValueChange = { newTitle ->
                    if (newTitle.length in 5..<40) title = newTitle
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                singleLine = true
            )

            BasicText(
                text = stringResource(R.string.calendarEventMotive),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            OutlinedTextField(
                value = description,
                onValueChange = { newDscription ->
                    if (newDscription.length <= 150) description = newDscription
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                minLines = 3
            )

            BasicText(
                text = stringResource(R.string.calendarEventType),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedEventType.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    shape = RectangleShape
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    EventType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                selectedEventType = type
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                onReserveSubmit(
                    title,
                    description,
                    selectedEventType,
                    currentDate,
                    if (startTime.isBefore(LocalTime.now())) LocalTime.now() else startTime, // Defaults to local time if it attempts to go before this, there's probably a smarter way to do this check but it works well enough
                    if (!startTime.isBefore(endTime))
                        throw IllegalArgumentException("End date cannot be before the start date due to how time works")
                    else endTime
                )
            },
            shape = RectangleShape
        )
        {
            BasicText(
                text = stringResource(R.string.calendarReserveHour),
                style = TextStyle(
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        currentDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                    }
                    showDatePicker = false
                }
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = stringResource(R.string.ok)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        text = stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showStartTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = startTime.hour,
            initialMinute = startTime.minute,
            is24Hour = true
        )
        Dialog(onDismissRequest = { showStartTimePicker = false }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RectangleShape)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timePickerState)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showStartTimePicker = false }) { Text(stringResource(R.string.cancel)) }
                    TextButton(onClick = {
                        startTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        showStartTimePicker = false
                    }) { Text(stringResource(R.string.ok)) }
                }
            }
        }
    }

    if (showEndTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = endTime.hour,
            initialMinute = endTime.minute,
            is24Hour = true
        )
        Dialog(onDismissRequest = { showEndTimePicker = false }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RectangleShape)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = timePickerState)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showEndTimePicker = false }) { Text(stringResource(R.string.cancel)) }
                    TextButton(onClick = {
                        endTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        showEndTimePicker = false
                    }) { Text(stringResource(R.string.ok)) }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewNeighborForm() {
    JuntaVecinosTheme {
        NeighborReserveForm(dateSelected = LocalDate.now())
    }
}