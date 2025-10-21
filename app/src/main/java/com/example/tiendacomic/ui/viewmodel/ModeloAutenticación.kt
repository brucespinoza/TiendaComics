package com.example.tiendacomic.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendacomic.domain.validaciones.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



//login UI
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
//Registro UI
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

//Perfil UI

data class PerfilUiState(
    val nombre: String = "",
    val rut: String = "",
    val correo: String = "",
    val contrasena: String = "",
)


// Modelo de usuarios

private data class UsuarioDemo(
    val nombre: String,
    val rut: String,
    val correo: String,
    val contrasena: String,
    val rol: String // usuario o admin
)

// Viewmodel

class ModeloAutenticacion : ViewModel() {

    companion object {
        private val USUARIOS = mutableListOf(
            UsuarioDemo("Admin", "11.111.111-1", "Admin@gmail.com", "Admin123!", rol = "admin"),
            UsuarioDemo(nombre = "Demo", rut = "12.345.678-5", correo = "demo@duoc.cl", contrasena = "Demo123!", rol = "usuario")
        )
    }

    // Estados observables
    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login

    private val _registro = MutableStateFlow(RegistroUiState())
    val registro: StateFlow<RegistroUiState> = _registro

    // Pagina de Login

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


    private var _ultimoRol: String? = null
    val ultimoRol: String? get() = _ultimoRol

    fun enviarLogin() {
        val s = _login.value
        if (!s.puedeEnviar || s.enviando) return
        viewModelScope.launch {
            _login.update { it.copy(enviando = true, mensajeError = null, exito = false) }
            delay(500)
            val usuario = USUARIOS.firstOrNull { it.correo.equals(s.correo, ignoreCase = true) }
            val ok = usuario != null && usuario.contrasena == s.contrasena
            _login.update {
                it.copy(
                    enviando = false,
                    exito = ok,
                    mensajeError = if (!ok) "Credenciales inválidas" else null
                )
            }
            //aqui agregamos si el login fue exitoso guardamos el rol en perfil
            if (ok && usuario != null) {
                _perfilUiState.update {
                    it.copy(
                        nombre = usuario.nombre,
                        rut = usuario.rut,
                        correo = usuario.correo,
                        contrasena = usuario.contrasena
                    )
                }
                // Aquí podrías exponer el rol si lo necesitas después
                _ultimoRol = usuario.rol
            }

        }
    }

    fun limpiarResultadoLogin() {
        _login.update { it.copy(exito = false, mensajeError = null) }
    }

    // Pagina de REGISTRO

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
        _registro.update { it.copy(contrasena = valor, errorContrasena = validarContraseña(valor)) }
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
        val llenos = s.nombre.isNotBlank() && s.rut.isNotBlank() && s.correo.isNotBlank() && s.contrasena.isNotBlank() && s.confirmar.isNotBlank()
        _registro.update { it.copy(puedeEnviar = sinErrores && llenos) }
    }



    fun enviarRegistro() {
        val s = _registro.value
        if (!s.puedeEnviar || s.enviando) return
        viewModelScope.launch {
            _registro.update { it.copy(enviando = true, mensajeError = null, exito = false) }
            delay(700)
            val duplicado = USUARIOS.any { it.correo.equals(s.correo, ignoreCase = true) || it.rut.equals(s.rut, ignoreCase = true) }

            if (duplicado) {
                _registro.update { it.copy(enviando = false, exito = false, mensajeError = "El usuario ya existe") }
                return@launch
            }

            USUARIOS.add(
                UsuarioDemo(
                    nombre = s.nombre.trim(),
                    rut = s.rut.trim(),
                    correo = s.correo.trim(),
                    contrasena = s.contrasena,
                    rol = "usuario"
                )
            )

            _registro.update { it.copy(enviando = false, exito = true, mensajeError = null) }
        }
    }

    fun limpiarResultadoRegistro() {
        _registro.update { it.copy(exito = false, mensajeError = null) }
    }

    //Perfil de usuario
    private val _perfilUiState = MutableStateFlow(PerfilUiState())
    val perfilUiState: StateFlow<PerfilUiState> = _perfilUiState

    init {
        // Simulamos cargar los datos del usuario
        _perfilUiState.value = PerfilUiState(
            nombre = "Bruce Espinoza",
            rut = "20.123.456-7",
            correo = "bruce@gmail.com",
            contrasena = "Aa!12345"
        )
    }

}











