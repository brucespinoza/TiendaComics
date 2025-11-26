package com.example.tiendacomic.domain.validaciones


import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [34])
class ValidadorTest {

    // EMAIL OK
    @Test
    fun email_valido_retornaNull() {
        val error = validarEmail("usuario@gmail.com")
        assertNull(error)
    }

    // EMAIL VACÍO
    @Test
    fun email_vacio_muestraError() {
        val error = validarEmail("")
        assertTrue(error!!.contains("El correo electrónico es obligatorio"))
    }

    // EMAIL CON FORMATO INVÁLIDO
    @Test
    fun email_formato_invalido() {
        val error = validarEmail("correo@@gmail")
        assertTrue(error!!.contains("formato de correo inválido"))
    }

    // EMAIL CON ESPACIOS
    @Test
    fun email_con_espacios() {
        val error = validarEmail("correo test@gmail.com")
        assertTrue(error!!.contains("No debe contener espacios"))
    }
}
