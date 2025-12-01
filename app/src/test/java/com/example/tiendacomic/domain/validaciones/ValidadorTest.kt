
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
    //fin de validarEmail

    //inicio de ValidarNombreSoloLetras

    //Nombre Validado
    @Test
    fun ValidarNombre_RetornarNull() {
        val error = ValidarNombreSoloLetras("Don Perez")
        assertNull(error)
    }

    //Nombrer Vacio
    @Test
    fun Validarnombre_vacio_muestraError() {
        val error = ValidarNombreSoloLetras("")
        assertTrue(error!!.contains("El nombre es obligatorio"))
    }

    @Test
    fun ValidarNombre_con_numeros() {
        val error = ValidarNombreSoloLetras("Juan123")
        assertTrue(error!!.contains("Solo se permiten letras y espacios"))
    }

    @Test
    fun ValidarNombre_con_simbolos() {
        val error = ValidarNombreSoloLetras("Hola@gmail.com")
        assertTrue(error!!.contains("Solo se permiten letras y espacios"))
    }

    //Nombre sin espacio
    @Test
    fun ValidarNombre_con_espacios_validos() {
        val error = ValidarNombreSoloLetras("Joel Perez")
        assertNull(error)
    }

    //validarRut
    //rut obligatorio vacio
    @Test
    fun validarRut_vacio_muestraError() {
        val error = validarRut("")
        assertTrue(error!!.contains("El RUT es obligatorio"))
    }
    //rut sin gion
    @Test
    fun validarRut_sin_guion_muestraError() {
        val error = validarRut("12345678K")
        assertTrue(error!!.contains("Formato de RUT incorrecto"))
    }
    //rut con mas de 2 giones
    @Test
    fun validarRut_con_dos_guiones_muestraError() {
        val error = validarRut("12-3456-7")
        assertTrue(error!!.contains("Formato de RUT incorrecto"))
    }
    //rut solo con numero no letras
    @Test
    fun validarRut_cuerpo_con_letras_muestraError() {
        val error = validarRut("ABCDEF12-K")
        assertTrue(error!!.contains("El RUT solo debe contener números"))
    }
    //rut con mas de un dijito
    @Test
    fun validarRut_dv_largo_muestraError() {
        val error = validarRut("12345678-AB")
        assertTrue(error!!.contains("Formato de RUT incorrecto"))
    }
    //rut correcto retorno
    @Test
    fun validarRut_correcto_retornaNull() {
        val error = validarRut("12.345.678-K")
        assertNull(error)
    }

    //Test para validar contraseña

    //validar contraseña si esta vacio muestra error
    @Test
    fun validarContraseña_vacia_muestraError() {
        val error = validarContraseña("")
        assertTrue(error!!.contains("La contraseña es obligatoria"))
    }
    //validar contraseña si la contraseña tiene menos de 8 cacacteres muestra error
    @Test
    fun validarContraseña_corta_muestraError() {
        val error = validarContraseña("Ab1@")
        assertTrue(error!!.contains("Debe tener al menos 8 caracteres"))
    }
    //validar contraseña debe contenher una letra minusucula
    @Test
    fun validarContraseña_sin_minuscula_muestraError() {
        val error = validarContraseña("ABCD123@")
        assertTrue(error!!.contains("Debe incluir una letra minúscula"))
    }
    //validar contraseña debe contener una letra mayuscula
    @Test
    fun validarContraseña_sin_mayuscula_muestraError() {
        val error = validarContraseña("abcd123@")
        assertTrue(error!!.contains("Debe incluir una letra mayúscula"))
    }
    //validar contraseña debe ibcluir un numero
    @Test
    fun validarContraseña_sin_numero_muestraError() {
        val error = validarContraseña("Abcdefg@")
        assertTrue(error!!.contains("Debe incluir un número"))
    }
    //validar contraseña debe inculir un simbolo
    @Test
    fun validarContraseña_sin_simbolo_muestraError() {
        val error = validarContraseña("Abcdefg1")
        assertTrue(error!!.contains("Debe incluir un símbolo"))
    }
    //validar contraseña si tiene un espacio muestras error
    @Test
    fun validarContraseña_con_espacios_muestraError() {
        val error = validarContraseña("Abcdef1 @")
        assertTrue(error!!.contains("No debe contener espacios"))
    }

    @Test
    fun validarContraseña_valida_retornaNull() {
        val error = validarContraseña("Abcdef1@")
        assertNull(error)
    }

    //test de validarConfirmarContraseña
    //validarconfirmarcontraseña un espacio vacio muestra el error
    @Test
    fun validarConfirmarContraseña_vacio_muestraError() {
        val error = validarConfirmarContraseña(
            pass = "Abcdef1@",
            confirm = ""
        )
        assertTrue(error!!.contains("Confirma tu contraseña"))
    }
    //validarconfirmarcontraseña si es diferente muestra error
    @Test
    fun validarConfirmarContraseña_diferente_muestraError() {
        val error = validarConfirmarContraseña(
            pass = "Abcdef1@",
            confirm = "Otro123@"
        )
        assertTrue(error!!.contains("Las contraseñas no coinciden"))
    }
    //validarconfirmarcontraseña si las contraseña coiciden muestra correcto
    @Test
    fun validarConfirmarContraseña_correcto_retornaNull() {
        val error = validarConfirmarContraseña(
            pass = "Abcdef1@",
            confirm = "Abcdef1@"
        )
        assertNull(error)
    }







}