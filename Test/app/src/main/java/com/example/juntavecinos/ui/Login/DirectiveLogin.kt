package com.example.juntavecinos.ui.Login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juntavecinos.R
import com.example.juntavecinos.ui.System.JuntaTopbar
import com.example.juntavecinos.ui.Navigation
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme


@Composable
fun DirectiveLogin(
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        BasicText(
            text = stringResource(R.string.directiveMasterCode),
            style = TextStyle(
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.directiveActivationCode))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(.1f))

        BasicText(
            text = stringResource(R.string.directiveOtherwise),
            style = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )


        Spacer(Modifier.weight(.1f))
        BasicText(
            text = stringResource(R.string.directiveLoginAsMemberOfDirective),
            style = TextStyle(
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        OutlinedTextField(
            state = rememberTextFieldState(),
            label = {
                Text(stringResource(R.string.directiveDirectiveCode))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.weight(1f))
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun DirPreview(){
    JuntaVecinosTheme() {
        Scaffold() { innerPadding ->
            JuntaTopbar(Navigation.DirectiveLogin, false, false, { })
            DirectiveLogin()

        }
    }
}