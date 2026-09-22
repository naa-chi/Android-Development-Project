package com.example.juntavecinos.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.Blue4
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

@Composable
fun NeighborLogin(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.loggingAsNeighbor),
            color = Blue4,
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.neighborCode))
            }
        )
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun NeighborLoginPreview(){
    JuntaVecinosTheme() {
        Scaffold() { innerPadding ->
            JuntaTopbar(Navigation.NeighborLogin, false, false, { })
            NeighborLogin()

        }
    }
}