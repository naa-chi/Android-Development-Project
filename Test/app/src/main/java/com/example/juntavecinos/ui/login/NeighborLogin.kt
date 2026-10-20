package com.example.juntavecinos.ui.login

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.Model.EventType
import com.example.juntavecinos.Model.SSNLastDigits
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme
import com.example.juntavecinos.validation.LoginValidator.validateSsn
import com.example.juntavecinos.validation.SsnError
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.example.juntavecinos.validation.EventTitleError
import com.example.juntavecinos.validation.EventValidator

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NeighborLogin(
    onLogin : (ssn : String) -> Unit
) {
    var neighborSSN by rememberSaveable { mutableStateOf("") }
    var currentSsnError by rememberSaveable { mutableStateOf(SsnError.NONE) }
    var showDialog by remember { mutableStateOf(false) }
    var showSSNLastDigitDropdown by remember { mutableStateOf(false) }
    var ssnLastDigit by rememberSaveable { mutableStateOf(SSNLastDigits.ZERO) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1.8f))
        BasicText(
            text = stringResource(R.string.loggingAsNeighbor),
            style = TextStyle(
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            OutlinedTextField(
                value = neighborSSN,
                onValueChange = { txt ->
                    neighborSSN = txt.filter {
                        it.isDigit()
                    }
                    if (neighborSSN.length > 9) {
                        neighborSSN = neighborSSN.substring(0, 9)
                    }
                    //hide error after typing
                    if (currentSsnError != SsnError.NONE) {
                        currentSsnError = SsnError.NONE
                        showDialog = false
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.neighborCode)) },
                modifier = Modifier.weight(1f).height(100.dp),
                singleLine = true,
                isError = currentSsnError != SsnError.NONE,
                supportingText = {
                    when (currentSsnError) {
                        SsnError.TOO_LONG -> Text(
                            stringResource(R.string.ssnErrorTooLong)
                        )
                        SsnError.TOO_SHORT -> Text(
                            stringResource(R.string.ssnErrorTooShort)
                        )
                        SsnError.INVALID_FORMAT -> Text(
                            stringResource(R.string.ssnErrorInvalidFormat)
                        )
                        SsnError.INVALID_SSN -> Text(
                            stringResource(R.string.invalidSSN)
                        )
                        SsnError.NONE -> {}
                    }
                }
            )
            Spacer(modifier = Modifier.width(10.dp))

            ExposedDropdownMenuBox(
                expanded = showSSNLastDigitDropdown,
                onExpandedChange = { showSSNLastDigitDropdown = !showSSNLastDigitDropdown }
            )
            {
                OutlinedTextField(
                    value =
                        when (ssnLastDigit) {
                            SSNLastDigits.ZERO -> "0"
                            SSNLastDigits.ONE -> "1"
                            SSNLastDigits.TWO -> "2"
                            SSNLastDigits.THREE -> "3"
                            SSNLastDigits.FOUR -> "4"
                            SSNLastDigits.FIVE -> "5"
                            SSNLastDigits.SIX -> "6"
                            SSNLastDigits.SEVEN -> "7"
                            SSNLastDigits.EIGHT -> "8"
                            SSNLastDigits.NINE -> "9"
                            SSNLastDigits.K -> "K"
                        },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.ssnLastDigit)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showSSNLastDigitDropdown) },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    shape = RectangleShape,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    ),
                    supportingText = {}
                )
                ExposedDropdownMenu(
                    expanded = showSSNLastDigitDropdown,
                    onDismissRequest = { showSSNLastDigitDropdown = false }
                ) {
                    SSNLastDigits.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(
                                when (type) {
                                    SSNLastDigits.ZERO -> "0"
                                    SSNLastDigits.ONE -> "1"
                                    SSNLastDigits.TWO -> "2"
                                    SSNLastDigits.THREE -> "3"
                                    SSNLastDigits.FOUR -> "4"
                                    SSNLastDigits.FIVE -> "5"
                                    SSNLastDigits.SIX -> "6"
                                    SSNLastDigits.SEVEN -> "7"
                                    SSNLastDigits.EIGHT -> "8"
                                    SSNLastDigits.NINE -> "9"
                                    SSNLastDigits.K -> "K"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                           },
                            onClick = {
                                ssnLastDigit = type
                                showSSNLastDigitDropdown = false
                            }
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.weight(1.8f))
        Button(
            onClick = {
                val result = validateSsn(neighborSSN, ssnLastDigit)
                if (result == SsnError.NONE) {
                    onLogin(neighborSSN)
                } else {
                    currentSsnError = result
                    showDialog = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RectangleShape
        ) {
            BasicText(
                text = stringResource(R.string.login),
                style = TextStyle(
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.headerMessageWarning)) },
            text = { Text(stringResource(
                when (currentSsnError) {
                    SsnError.INVALID_FORMAT -> R.string.ssnErrorInvalidFormat
                    SsnError.TOO_SHORT -> R.string.ssnErrorTooShort
                    SsnError.TOO_LONG -> R.string.ssnErrorTooLong
                    SsnError.NONE -> R.string.ssnErrorUnkown
                    SsnError.INVALID_SSN -> R.string.invalidSSN
                }
            )) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun NeighborLoginPreview(){
    JuntaVecinosTheme() {
        NeighborLogin({_ -> })
    }
}