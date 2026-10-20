package com.example.juntavecinos.ui

import android.accounts.Account
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.juntavecinos.Model.AccountType
import androidx.navigation.NavHostController
import com.example.juntavecinos.persistance.AppRepository

@Composable
fun LoadingHandler(
    onLoggedIn : (a : AccountType) -> Unit,
    onNotLoggedIn : () -> Unit,
    repository : AppRepository
){
    //This function will simply check if we're logged in.
    // If we're not, go to login homer
    // If we're logged in, go to the proper account...
    LaunchedEffect(Unit) {
        val session = repository.getSession()
        if (session == null) {
            onNotLoggedIn()
        } else {
            val actype = AccountType.fromString(session.accountType)

            if (actype == null) {
                //start fresh from corrupted data
                repository.deleteSession()
                onNotLoggedIn()
            } else {
                onLoggedIn(actype)
            }

        }
    }
}