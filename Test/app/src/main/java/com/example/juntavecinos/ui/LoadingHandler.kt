package com.example.juntavecinos.ui

import androidx.compose.runtime.Composable

@Composable
fun LoadingHandler(onLoggedIn : () -> Unit, onNotLoggedIn : () -> Unit){
    //This function will simply check if we're logged in.
    // If we're not, go to login homer
    // If we're logged in, go to the proper account...

    // FOR NOW WE ONLY MOVE THE USER TO NOT LOGGED IN
    // cuz... there's no logged in logic yet, yay!

    onNotLoggedIn()
}