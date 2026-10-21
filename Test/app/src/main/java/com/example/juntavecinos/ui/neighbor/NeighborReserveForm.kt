package com.example.juntavecinos.ui.neighbor

import androidx.compose.material3.AlertDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.KeyboardType
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Slider
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.style.TextDirection
import com.example.juntavecinos.persistance.AppRepository
import com.example.juntavecinos.persistance.dataModel.DayEventData
import com.example.juntavecinos.validation.EventDescriptionError
import com.example.juntavecinos.validation.EventScheduleError
import com.example.juntavecinos.validation.EventTitleError
import com.example.juntavecinos.validation.AttendeesError
import com.example.juntavecinos.validation.EventValidator
import com.example.juntavecinos.validation.HourError
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeighborReserveForm(
    dateSelected: LocalDate,
    repository: AppRepository,
    onNavigateBack: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf(EventTitleError.NONE) }
    var descriptionError by remember {mutableStateOf(EventDescriptionError.NONE)}
    var scheduleError by remember { mutableStateOf(EventScheduleError.NONE) }

    var description by remember { mutableStateOf("") }
    var selectedEventType by remember { mutableStateOf(EventType.OTHER) }
    var expandedDropdown by remember { mutableStateOf(false) }

    var attendeesStr by remember { mutableStateOf("0") }
    var attendeeCountState by remember { mutableStateOf(AttendeesError.NONE)}
    var eventHourState by remember { mutableStateOf(HourError.NONE) }
    var currentDate by remember { mutableStateOf(dateSelected) }
    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(11, 0)) }
    val minHour = 8
    val maxHour = 22

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<Int?>(null) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateSelected.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    val pickerTextFieldColors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val scope = rememberCoroutineScope()

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
                    shape = RectangleShape,
                    colors = pickerTextFieldColors
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
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            shape = RectangleShape,
                            colors = pickerTextFieldColors,
                        )
                    }
                    // Red hint for Start Time
                    if (startTime.hour !in minHour..<maxHour) {
                        Text(
                            text = "Debe ser entre ${minHour}:00 y ${maxHour}:00",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            shape = RectangleShape,
                            colors = pickerTextFieldColors,
                        )
                    }
                    // Red hint for End Time
                    if (endTime.hour !in minHour..maxHour || (endTime.hour == maxHour && endTime.minute > 0)) {
                        Text(
                            text = "Debe ser entre ${minHour}:00 y ${maxHour}:00",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
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
                    if (newTitle.length <= EventValidator.TITLE_MAX_LENGTH) title = newTitle
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                singleLine = true,
                isError = titleError != EventTitleError.NONE,
                supportingText = {
                    when (titleError) {
                        EventTitleError.TOO_SHORT -> Text("Mínimo ${EventValidator.TITLE_MIN_LENGTH} caracteres")
                        EventTitleError.TOO_LONG  -> Text("Máximo ${EventValidator.TITLE_MAX_LENGTH} caracteres")
                        EventTitleError.INVALID_CHARACTERS -> Text(stringResource(R.string.invalidCharactersTitle))
                        else -> {}
                    }
                }
            )

            BasicText(
                text = stringResource(R.string.expectedAttendees),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            OutlinedTextField(
                value = attendeesStr,
                onValueChange = { newValue ->
                    val num = newValue.toIntOrNull()
                    if (num != null) {
                        attendeesStr = num.toString()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = attendeeCountState != AttendeesError.NONE,
                supportingText = {
                    when (attendeeCountState) {
                        AttendeesError.SMALLER_THAN_ZERO -> Text(stringResource(R.string.attendeesMoreThanZeroWarning))
                        AttendeesError.INVALID_NUMBER -> Text(stringResource(R.string.invalidAttendeeCount))
                        AttendeesError.MORE_THAN_MAXIMUM_POSSIBLE -> Text(stringResource(R.string.moreThanMaximumAttendee) + " (100)")
                        else -> {}
                    }
                }
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
                onValueChange = { newDescription ->
                    if (newDescription.length <= EventValidator.DESCRIPTION_MAX_LENGTH) {
                        description = newDescription
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                minLines = 3,
                isError = descriptionError != EventDescriptionError.NONE,
                supportingText = {
                    when (descriptionError) {
                        EventDescriptionError.NONE_AT_ALL -> Text("Escribe una descripción")
                        EventDescriptionError.TOO_LONG    -> Text("Máximo ${EventValidator.DESCRIPTION_MAX_LENGTH} caracteres")
                        EventDescriptionError.INVALID_CHARACTERS -> Text(stringResource(R.string.invalidCharactersDescription))
                        else -> {}
                    }
                }
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
                    value = stringResource(
                        when (selectedEventType) {
                            EventType.PARTY -> R.string.PARTY
                            EventType.MEETING -> R.string.MEETING
                            EventType.BIRTHDAY -> R.string.BIRTHDAY
                            EventType.WEDDING -> R.string.WEDDING
                            EventType.ANNIVERSARY -> R.string.ANNIVERSARY
                            EventType.BABY_SHOWER -> R.string.BABY_SHOWER
                            EventType.FAIRE -> R.string.FAIRE
                            EventType.RELIGIOUS_EVENT -> R.string.RELIGIOUS_EVENT
                            EventType.OTHER -> R.string.OTHER
                        }
                    )

                    ,
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
                            text = { Text(
                                stringResource(
                                    when (type) {
                                        EventType.PARTY -> R.string.PARTY
                                        EventType.MEETING -> R.string.MEETING
                                        EventType.BIRTHDAY -> R.string.BIRTHDAY
                                        EventType.WEDDING -> R.string.WEDDING
                                        EventType.ANNIVERSARY -> R.string.ANNIVERSARY
                                        EventType.BABY_SHOWER -> R.string.BABY_SHOWER
                                        EventType.FAIRE -> R.string.FAIRE
                                        EventType.RELIGIOUS_EVENT -> R.string.RELIGIOUS_EVENT
                                        EventType.OTHER -> R.string.OTHER
                                    }
                                )
                            ) },
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
                titleError = EventValidator.validateTitle(title)
                descriptionError = EventValidator.validateDescription(description)
                scheduleError = EventValidator.validateSchedule(startTime, endTime)
                attendeeCountState = EventValidator.validateAttendeeCount(attendeesStr)
                eventHourState = EventValidator.validateEventHour(startTime, endTime)


                when {
                    titleError != EventTitleError.NONE -> errorMessage = R.string.checkTitle
                    descriptionError != EventDescriptionError.NONE -> errorMessage = R.string.checkDescription
                    scheduleError != EventScheduleError.NONE -> errorMessage = R.string.invalidDate
                    currentDate < LocalDate.now() -> errorMessage = R.string.dateOutOfBounds
                    attendeeCountState != AttendeesError.NONE -> errorMessage = R.string.checkAttendeeCount
                    eventHourState != HourError.NONE -> errorMessage = R.string.timeSlotOutOfBounds

                    else -> {
                        scope.launch {
                            val startMillis = currentDate.atTime(startTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val endMillis = currentDate.atTime(endTime).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                            // check for conflicts
                            // We fetch all events in the day to check for overlaps manually
                            val dayStart = currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val dayEnd = currentDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

                            val existingEvents = repository.getEventsInDay(dayStart, dayEnd)

                            // Overlap condition: ExistingStart < NewEnd AND ExistingEnd > NewStart
                            val hasConflict = existingEvents.any { event ->
                                event.startTime < endMillis && event.endTime > startMillis
                            }

                            if (hasConflict) {
                                errorMessage = R.string.timeSlotOccupied
                            } else {
                                val event = DayEventData(
                                    title = title,
                                    hoster = "USUARIO LOCAL",
                                    description = description,
                                    startTime = startMillis,
                                    endTime = endMillis,
                                )

                                repository.insertEvent(event)
                                onNavigateBack()
                            }
                        }
                    }
                }
            },
            shape = RectangleShape
        ) {
            BasicText(
                text = stringResource(R.string.calendarReserveHour),
                style = TextStyle(
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            )
        }

        errorMessage?.let { value ->
            AlertDialog(
                onDismissRequest = { errorMessage = null },
                title = { Text(stringResource(R.string.headerMessageWarning)) },
                text = { Text(stringResource(value)) },
                confirmButton = {
                    TextButton(onClick = { errorMessage = null }) { Text(stringResource(R.string.ok)) }
                }
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
        key("StartTimePicker") {
            val timePickerState = rememberTimePickerState(
                initialHour = startTime.hour,
                initialMinute = startTime.minute,
                is24Hour = true
            )
            var pendingHour by remember { mutableIntStateOf(timePickerState.hour) }
            var pendingMinute by remember { mutableIntStateOf(timePickerState.minute) }

            LaunchedEffect(timePickerState.hour) { pendingHour = timePickerState.hour }
            LaunchedEffect(timePickerState.minute) { pendingMinute = timePickerState.minute }

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
                        TextButton(onClick = { showStartTimePicker = false }) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        TextButton(onClick = {
                            startTime = LocalTime.of(pendingHour, pendingMinute)
                            showStartTimePicker = false
                        }) {
                            Text(
                                text = stringResource(R.string.ok),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEndTimePicker) {
        key("endTimePicker") {
            val timePickerState = rememberTimePickerState(
                initialHour = endTime.hour,
                initialMinute = endTime.minute,
                is24Hour = true
            )

            var pendingHour by remember { mutableIntStateOf(timePickerState.hour) }
            var pendingMinute by remember { mutableIntStateOf(timePickerState.minute) }

            LaunchedEffect(timePickerState.hour) { pendingHour = timePickerState.hour }
            LaunchedEffect(timePickerState.minute) { pendingMinute = timePickerState.minute }

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
                        TextButton(onClick = { showEndTimePicker = false }) {
                            Text(
                                stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.onPrimary)

                        }
                        TextButton(onClick = {
                            endTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                            showEndTimePicker = false
                        }) {
                            Text(
                                stringResource(R.string.ok),
                                color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }


}