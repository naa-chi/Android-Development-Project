package com.example.juntavecinos.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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

// THIS HANDLES ONLY THE WELCOMING SCREEN!!
// We shouldn't check any logic here, only
// move the user to whatever they click...
@Composable
fun LoginHoming(
    onNavigateToNeighbor: () -> Unit,
    onNavigateToDirective: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        BasicText(
            text = stringResource(R.string.selectRole),
            style = TextStyle(
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )

        Spacer(modifier = Modifier.weight(0.15f))

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
                BasicText(
                    text = stringResource(R.string.roleDirective),
                    style = TextStyle(
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    autoSize = TextAutoSize.StepBased(),
                )
            }

            Button(
                onClick = onNavigateToNeighbor,
                shape = RectangleShape,
                modifier = Modifier
                    .height(60.dp)
                    .weight(1f)
            ) {
                BasicText(
                    text = stringResource(R.string.roleNeighbor),
                    style = TextStyle(
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    autoSize = TextAutoSize.StepBased(),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1.1f))
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    JuntaVecinosTheme() {
        LoginHoming({}, {})
    }
}
