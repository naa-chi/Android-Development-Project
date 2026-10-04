package com.example.juntavecinos.ui.Login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.juntavecinos.ui.Navigation
import com.example.juntavecinos.ui.System.JuntaTopbar
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@Composable
fun NeighborLogin(
    onLogin : (ssn : String) -> Unit
) {
    var neighborSSN by rememberSaveable { mutableStateOf("") }
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
        OutlinedTextField(
            value = neighborSSN,
            onValueChange = { neighborSSN = it },
            label = { Text(stringResource(R.string.neighborCode)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.weight(1.8f))
        Button(
            onClick = {
                onLogin(neighborSSN)
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
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun NeighborLoginPreview(){
    JuntaVecinosTheme() {
        NeighborLogin({_ -> })
    }
}