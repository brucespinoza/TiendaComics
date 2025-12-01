package com.example.tiendacomic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendacomic.data.repositorio.UsuarioRepository
import com.example.tiendacomic.domain.validaciones.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ------------------- LOGIN UI -------------------
data class LoginUiState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val enviando: Boolean = false,
    val puedeEnviar: Boolean = false,
    val exito: Boolean = false,
    val mensajeError: String? = null
)

// ------------------- REGISTRO UI -------------------
data class RegistroUiState(
    val nombre: String = "",
    val rut: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmar: String = "",
    val errorNombre: String? = null,
    val errorRut: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmar: String? = null,
    val enviando: Boolean = false,
    val puedeEnviar: Boolean = false,
    val exito: Boolean = false,
    val mensajeError: String? = null
)

// ------------------- PERFIL UI -------------------
data class PerfilUiState(
    val nombre: String = "",
    val rut: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val compras: Map<String, Int> = emptyMap(), // 🔹 Map para contar repeticiones
    val esVip: Boolean = false,
    val vipExpiracion: Long? = null
)

// ------------------- VIEWMODEL -------------------
class ModeloAutenticacion(private val repository: UsuarioRepository) : ViewModel() {

    // ------------------- LOGIN -------------------
    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login

    private val _registro = MutableStateFlow(RegistroUiState())
    val registro: StateFlow<RegistroUiState> = _registro

    private var _ultimoRol: String? = null
    val ultimoRol: String? get() = _ultimoRol

    // ------------------- PERFIL -------------------
    private val _perfilUiState = MutableStateFlow(PerfilUiState())
    val perfilUiState: StateFlow<PerfilUiState> = _perfilUiState

    init {
        _perfilUiState.value = PerfilUiState(
            nombre = "Bruce Espinoza",
            rut = "20.123.456-7",
            correo = "bruce@gmail.com",
            contrasena = "Aa!12345",
            compras = emptyMap(),
            esVip = false,
            vipExpiracion = null
        )
    }

    // ------------------- LOGIN LÓGICA -------------------
    fun alCambiarCorreoLogin(valor: String) {
        _login.update { it.copy(correo = valor, errorCorreo = validarEmail(valor)) }
        recalcularPuedeEnviarLogin()
    }

    fun alCambiarContrasenaLogin(valor: String) {
        _login.update { it.copy(contrasena = valor) }
        recalcularPuedeEnviarLogin()
    }

    private fun recalcularPuedeEnviarLogin() {
        val s = _login.value
        val puede = s.errorCorreo == null && s.correo.isNotBlank() && s.contrasena.isNotBlank()
        _login.update { it.copy(puedeEnviar = puede) }
    }

    fun enviarLogin() {
        val s = _login.value
        if (!s.puedeEnviar || s.enviando) return

        viewModelScope.launch {
            _login.update { it.copy(enviando = true, mensajeError = null, exito = false) }
            delay(400)
            val result = repository.login(s.correo, s.contrasena)

            _login.update { estado ->
                if (result.isSuccess) {
                    val usuario = result.getOrNull()!!
                    _perfilUiState.update {
                        it.copy(
                            nombre = usuario.nombre,
                            rut = usuario.rut,
                            correo = usuario.correo,
                            contrasena = usuario.contrasena
                        )
                    }
                    _ultimoRol = usuario.rol
                    estado.copy(enviando = false, exito = true, mensajeError = null)
                } else {
                    estado.copy(
                        enviando = false,
                        exito = false,
                        mensajeError = result.exceptionOrNull()?.message ?: "Credenciales inválidas"
                    )
                }
            }
        }
    }

    fun limpiarResultadoLogin() {
        _login.update { it.copy(exito = false, mensajeError = null) }
    }

    // ------------------- REGISTRO LÓGICA -------------------
    fun alCambiarNombre(valor: String) {
        val filtrado = valor.filter { it.isLetter() || it.isWhitespace() }
        _registro.update {
            it.copy(nombre = filtrado, errorNombre = ValidarNombreSoloLetras(filtrado))
        }
        recalcularPuedeEnviarRegistro()
    }

    fun alCambiarRut(valor: String) {
        val normalizado = valor.trim()
        _registro.update { it.copy(rut = normalizado, errorRut = validarRut(normalizado)) }
        recalcularPuedeEnviarRegistro()
    }

    fun alCambiarCorreoRegistro(valor: String) {
        _registro.update { it.copy(correo = valor, errorCorreo = validarEmail(valor)) }
        recalcularPuedeEnviarRegistro()
    }

    fun alCambiarContrasenaRegistro(valor: String) {
        _registro.update { it.copy(contrasena = valor) }
        _registro.update { it.copy(errorConfirmar = validarConfirmarContraseña(it.contrasena, it.confirmar)) }
        recalcularPuedeEnviarRegistro()
    }

    fun alCambiarConfirmar(valor: String) {
        _registro.update { it.copy(confirmar = valor, errorConfirmar = validarConfirmarContraseña(it.contrasena, valor)) }
        recalcularPuedeEnviarRegistro()
    }

    private fun recalcularPuedeEnviarRegistro() {
        val s = _registro.value
        val sinErrores = listOf(s.errorNombre, s.errorRut, s.errorCorreo, s.errorContrasena, s.errorConfirmar).all { it == null }
        val llenos = s.nombre.isNotBlank() && s.rut.isNotBlank() && s.correo.isNotBlank() &&
                s.contrasena.isNotBlank() && s.confirmar.isNotBlank()
        _registro.update { it.copy(puedeEnviar = sinErrores && llenos) }
    }

    fun enviarRegistro() {
        val s = _registro.value
        if (!s.puedeEnviar || s.enviando) return

        viewModelScope.launch {
            _registro.update { it.copy(enviando = true, mensajeError = null, exito = false) }
            delay(700)

            val result = repository.registro(
                nombre = s.nombre.trim(),
                rut = s.rut.trim(),
                correo = s.correo.trim(),
                pass = s.contrasena.trim()
            )

            _registro.update { estado ->
                if (result.isSuccess) {
                    estado.copy(enviando = false, exito = true, mensajeError = null)
                } else {
                    estado.copy(
                        enviando = false,
                        exito = false,
                        mensajeError = result.exceptionOrNull()?.message ?: "No se pudo registrar"
                    )
                }
            }
        }
    }

    fun limpiarResultadoRegistro() {
        _registro.update { it.copy(exito = false, mensajeError = null) }
    }

    // ------------------- COMPRAS -------------------
    fun agregarCompra(tituloComic: String) {
        _perfilUiState.update { estado ->
            val cantidadActual = estado.compras[tituloComic] ?: 0
            estado.copy(compras = estado.compras + (tituloComic to (cantidadActual + 1)))
        }
    }

    // ------------------- PERFIL -------------------
    fun actualizarPerfil(nombreNuevo: String, correoNuevo: String) {
        if (nombreNuevo.isBlank() || correoNuevo.isBlank()) return

        val usuarioActual = _perfilUiState.value
        _perfilUiState.update {
            it.copy(nombre = nombreNuevo.trim(), correo = correoNuevo.trim())
        }

        viewModelScope.launch {
            repository.actualizarPerfil(usuarioActual.correo, nombreNuevo.trim(), correoNuevo.trim())
        }
    }

    fun cambiarContrasena(actual: String, nueva: String, confirmar: String): String {
        val usuario = _perfilUiState.value

        if (actual != usuario.contrasena)
            return "❌ La contraseña actual es incorrecta"

        val errorNueva = validarContraseña(nueva)
        if (errorNueva != null)
            return "❌ $errorNueva"

        if (nueva != confirmar)
            return "❌ Las contraseñas no coinciden"

        viewModelScope.launch {
            val resultado = repository.actualizarContrasena(usuario.correo, nueva)
            if (resultado.isSuccess) {
                _perfilUiState.update { it.copy(contrasena = nueva) }
            }
        }

        return "✅ Contraseña actualizada correctamente"
    }

    // ------------------- VIP -------------------
    fun activarVip() {
        val ahora = System.currentTimeMillis()
        val unAno = 365L * 24 * 60 * 60 * 1000
        _perfilUiState.update {
            it.copy(
                esVip = true,
                vipExpiracion = ahora + unAno
            )
        }
    }

    fun verificarVip(): Boolean {
        val estado = _perfilUiState.value
        return estado.esVip && (estado.vipExpiracion?.let { it > System.currentTimeMillis() } ?: false)
    }

    fun renovarVip() {
        val ahora = System.currentTimeMillis()
        val unAno = 365L * 24 * 60 * 60 * 1000
        _perfilUiState.update {
            it.copy(
                esVip = true,
                vipExpiracion = ahora + unAno
            )
        }
    }

    // ------------------- NUEVO: CERRAR SESIÓN -------------------
    // Esta función limpia el estado interno del ViewModel (login/registro/perfil/rol)
    fun cerrarSesion() {
        _login.update { LoginUiState() }
        _registro.update { RegistroUiState() }
        _perfilUiState.update { PerfilUiState() }
        _ultimoRol = null
    }
}
