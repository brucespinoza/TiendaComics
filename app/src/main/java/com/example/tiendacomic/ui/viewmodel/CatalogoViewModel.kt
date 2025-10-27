package com.example.tiendacomic.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CatalogoViewModel : ViewModel() {

    // inicialmente no es VIP
    var esVip by mutableStateOf(false)
        private set

    fun activarVip() {
        esVip = true
    }
}
