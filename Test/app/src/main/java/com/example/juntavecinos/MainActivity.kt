package com.example.juntavecinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.juntavecinos.ui.Main
import com.example.juntavecinos.ui.theme.JuntaVecinosTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JuntaVecinosTheme(dynamicColor = false) {
                Main()
            }
        }
    }
}