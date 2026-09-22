package com.example.juntavecinos.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme

// THIS HANDLES ONLY THE WELCOMING SCREEN!!
// We shouldn't check any logic here, only
// move the user to whatever they click...
@Composable
fun LoginHoming(
    onNavigateToNeighbor: () -> Unit,
    onNavigateToDirective: () -> Unit,
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
            text = stringResource(R.string.selectRole),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onNavigateToDirective,
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.roleDirective),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = onNavigateToNeighbor,
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.roleNeighbor),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    JuntaVecinosTheme() {
        Scaffold() {innerPadding ->
            JuntaTopbar(Navigation.LoginStarter, false, false, { })
            LoginHoming({}, {})

        }
    }
}
