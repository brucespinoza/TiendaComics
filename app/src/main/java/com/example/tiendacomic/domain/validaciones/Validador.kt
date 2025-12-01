package com.example.tiendacomic.domain.validaciones

import android.util.Patterns


fun validarEmail(email: String): String? {
    val errores = mutableListOf<String>()

    if (email.isBlank()) errores.add("El correo electrónico es obligatorio")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) errores.add("formato de correo inválido")
    if (email.contains(" ")) errores.add("No debe contener espacios")


    return if (errores.isEmpty()) null else errores.joinToString("\n")
}

fun ValidarNombreSoloLetras(name: String): String? {
    val errores = mutableListOf<String>()

    if (name.isBlank()) errores.add("El nombre es obligatorio")
    if (!Regex("^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$").matches(name)) errores.add("Solo se permiten letras y espacios")

    return if (errores.isEmpty()) null else errores.joinToString("\n")
}

fun validarRut(rut: String): String? {
    if (rut.isBlank()) return "El RUT es obligatorio"

    val limpio = rut.trim().uppercase()

    if (!limpio.contains("-")) {
        return "Formato de RUT incorrecto. Ej: 12345678-9 o 12.345.678-K"
    }

    val partes = limpio.split("-")
    if (partes.size != 2) {
        return "Formato de RUT incorrecto. Ej: 12345678-9 o 12.345.678-K"
    }

    val cuerpo = partes[0].replace(".", "")
    val dvInput = partes[1]

    if (!cuerpo.all { it.isDigit() }) return "El RUT solo debe contener números antes del guion"
    if (dvInput.length != 1) return "Formato de RUT incorrecto. Ej: 12345678-9 o 12.345.678-K"

    return null
}







fun validarContraseña(pass: String): String? {
    if (pass.isBlank()) return "La contraseña es obligatoria"

    val requisitos = listOf(
        (pass.length >= 8) to "Debe tener al menos 8 caracteres",
        (pass.any { it.isLowerCase() }) to "Debe incluir una letra minúscula",
        (pass.any { it.isUpperCase() }) to "Debe incluir una letra mayúscula",
        (pass.any { it.isDigit() }) to "Debe incluir un número",
        (pass.any { !it.isLetterOrDigit() }) to "Debe incluir un símbolo (ej: @, #, !)",
        (!pass.contains(' ')) to "No debe contener espacios"
    )


    val errores = requisitos.filter { !it.first }.map { it.second }
    return if (errores.isEmpty()) null else errores.joinToString("\n")
}


fun validarConfirmarContraseña(pass: String, confirm: String): String? {
    val errores = mutableListOf<String>()

    if (confirm.isBlank()) errores.add("Confirma tu contraseña")
    if (pass != confirm) errores.add("Las contraseñas no coinciden")

    return if (errores.isEmpty()) null else errores.joinToString("\n")
}








