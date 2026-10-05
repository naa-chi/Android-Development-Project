package com.example.juntavecinos.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectiveLogin(
    onLoginClick: (isAdmin: Boolean, code: String) -> Unit
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(stringResource(R.string.adminRole), stringResource(R.string.directiveRole))

    var adminCode by rememberSaveable { mutableStateOf("") }
    var directiveCode by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicText(
                text = stringResource(R.string.directiveLoginAs),
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(Modifier.height(16.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = RectangleShape,
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex
                    ) {
                        BasicText(
                            text = label,
                            style = TextStyle(
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            if (selectedIndex == 0) {
                BasicText(
                    text = stringResource(R.string.directiveMasterCode),
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = adminCode,
                    onValueChange = { adminCode = it },
                    label = { Text(stringResource(R.string.directiveActivationCode)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    singleLine = true
                )
            } else {
                BasicText(
                    text = stringResource(R.string.directiveLoginAsMemberOfDirective),
                    style = TextStyle(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = directiveCode,
                    onValueChange = { directiveCode = it },
                    label = { Text(stringResource(R.string.directiveDirectiveCode)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    singleLine = true
                )
            }
        }

        Button(
            onClick = {
                val code = if (selectedIndex == 0) adminCode else directiveCode
                onLoginClick(selectedIndex == 0, code)
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
                )
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun DirPreview() {
    JuntaVecinosTheme {
        DirectiveLogin { _, _ -> }
    }
}