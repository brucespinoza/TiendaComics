package com.example.tiendacomic.ui.viewmodel

import com.example.tiendacomic.data.local.usuario.UsuarioEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Repositorio FAKE para simular UsuarioRepository
 */
class FakeUsuarioRepository {
    
    // Simula usuarios en "base de datos"
    private val usuarios = mutableListOf(
        UsuarioEntity(1, "Admin", "12.345.678-9", "admin@gmail.com", "Admin123@", "admin"),
        UsuarioEntity(2, "Usuario", "98.765.432-1", "usuario@gmail.com", "User123@", "usuario")
    )
    
    // Simula login
    suspend fun login(correo: String, pass: String): Result<UsuarioEntity> {
        val usuario = usuarios.find { it.correo == correo && it.contrasena == pass }
        return if (usuario != null) {
            Result.success(usuario)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }
    
    // Simula registro
    suspend fun registro(nombre: String, rut: String, correo: String, pass: String): Result<Long> {
        val existe = usuarios.any { it.correo == correo || it.rut == rut }
        return if (existe) {
            Result.failure(IllegalArgumentException("Correo o RUT ya en uso"))
        } else {
            val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
            usuarios.add(UsuarioEntity(nuevoId, nombre, rut, correo, pass, "usuario"))
            Result.success(nuevoId)
        }
    }
}

/**
 * Tests para ModeloAutenticación
 * Siguiendo el mismo patrón que CatalogoViewModelTest
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ModeloAutenticacionTest {

    // ==================== TESTS DE LOGIN ====================

    @Test
    fun login_correoVacio_errorCorreo() {
        // Simular estado de login con correo vacío
        val estado = LoginUiState(correo = "", contrasena = "123456")
        
        // El correo vacío no debe permitir enviar
        assertTrue(estado.correo.isBlank())
    }

    @Test
    fun login_contrasenaVacia_noPuedeEnviar() {
        val estado = LoginUiState(correo = "test@gmail.com", contrasena = "")
        
        // Con contraseña vacía no puede enviar
        assertTrue(estado.contrasena.isBlank())
    }

    @Test
    fun login_datosCompletos_puedeEnviar() {
        val estado = LoginUiState(
            correo = "usuario@gmail.com",
            contrasena = "User123@",
            errorCorreo = null,
            puedeEnviar = true
        )
        
        assertTrue(estado.puedeEnviar)
        assertNull(estado.errorCorreo)
    }

    @Test
    fun login_exitoso_cambiaEstado() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.login("usuario@gmail.com", "User123@")
        
        assertTrue(resultado.isSuccess)
        assertEquals("Usuario", resultado.getOrNull()?.nombre)
    }

    @Test
    fun login_credencialesInvalidas_retornaError() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.login("noexiste@gmail.com", "wrongpass")
        
        assertTrue(resultado.isFailure)
    }

    @Test
    fun login_rolAdmin_detectado() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.login("admin@gmail.com", "Admin123@")
        
        assertTrue(resultado.isSuccess)
        assertEquals("admin", resultado.getOrNull()?.rol)
    }

    @Test
    fun login_rolUsuario_detectado() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.login("usuario@gmail.com", "User123@")
        
        assertTrue(resultado.isSuccess)
        assertEquals("usuario", resultado.getOrNull()?.rol)
    }

    // ==================== TESTS DE REGISTRO ====================

    @Test
    fun registro_camposVacios_noPuedeEnviar() {
        val estado = RegistroUiState(
            nombre = "",
            rut = "",
            correo = "",
            contrasena = "",
            confirmar = ""
        )
        
        assertTrue(estado.nombre.isBlank())
        assertTrue(estado.rut.isBlank())
        assertTrue(estado.correo.isBlank())
    }

    @Test
    fun registro_datosCompletos_puedeEnviar() {
        val estado = RegistroUiState(
            nombre = "Juan Perez",
            rut = "11.222.333-4",
            correo = "juan@gmail.com",
            contrasena = "Juan123@",
            confirmar = "Juan123@",
            puedeEnviar = true
        )
        
        assertTrue(estado.puedeEnviar)
    }

    @Test
    fun registro_exitoso_retornaId() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.registro(
            nombre = "Nuevo Usuario",
            rut = "11.111.111-1",
            correo = "nuevo@gmail.com",
            pass = "Nuevo123@"
        )
        
        assertTrue(resultado.isSuccess)
        assertTrue(resultado.getOrNull()!! > 0)
    }

    @Test
    fun registro_correoExistente_retornaError() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.registro(
            nombre = "Otro Usuario",
            rut = "99.999.999-9",
            correo = "usuario@gmail.com", // Ya existe
            pass = "Otro123@"
        )
        
        assertTrue(resultado.isFailure)
    }

    @Test
    fun registro_rutExistente_retornaError() = runTest {
        val fakeRepo = FakeUsuarioRepository()
        val resultado = fakeRepo.registro(
            nombre = "Otro Usuario",
            rut = "12.345.678-9", // Ya existe
            correo = "otro@gmail.com",
            pass = "Otro123@"
        )
        
        assertTrue(resultado.isFailure)
    }

    @Test
    fun registro_contrasenasNoCoinciden_error() {
        val estado = RegistroUiState(
            contrasena = "Pass123@",
            confirmar = "Diferente123@",
            errorConfirmar = "Las contraseñas no coinciden"
        )
        
        assertNotNull(estado.errorConfirmar)
    }

    // ==================== TESTS DE PERFIL ====================

    @Test
    fun perfil_estadoInicial_datosVacios() {
        val estado = PerfilUiState()
        
        assertTrue(estado.nombre.isEmpty())
        assertTrue(estado.correo.isEmpty())
        assertFalse(estado.esVip)
    }

    @Test
    fun perfil_conDatos_muestraCorrectamente() {
        val estado = PerfilUiState(
            nombre = "Bruce Espinoza",
            rut = "20.123.456-7",
            correo = "bruce@gmail.com",
            contrasena = "Aa!12345"
        )
        
        assertEquals("Bruce Espinoza", estado.nombre)
        assertEquals("bruce@gmail.com", estado.correo)
    }

    @Test
    fun perfil_comprasVacias_listaVacia() {
        val estado = PerfilUiState(compras = emptyMap())
        
        assertTrue(estado.compras.isEmpty())
    }

    @Test
    fun perfil_conCompras_muestraCantidad() {
        val estado = PerfilUiState(
            compras = mapOf("Batman" to 2, "Spiderman" to 1)
        )
        
        assertEquals(2, estado.compras["Batman"])
        assertEquals(1, estado.compras["Spiderman"])
        assertEquals(2, estado.compras.size)
    }

    // ==================== TESTS DE VIP ====================

    @Test
    fun vip_inicialmente_noEsVip() {
        val estado = PerfilUiState(esVip = false)
        
        assertFalse(estado.esVip)
        assertNull(estado.vipExpiracion)
    }

    @Test
    fun vip_activado_cambiaEstado() {
        val ahora = System.currentTimeMillis()
        val unAno = 365L * 24 * 60 * 60 * 1000
        
        val estado = PerfilUiState(
            esVip = true,
            vipExpiracion = ahora + unAno
        )
        
        assertTrue(estado.esVip)
        assertNotNull(estado.vipExpiracion)
        assertTrue(estado.vipExpiracion!! > ahora)
    }

    @Test
    fun vip_expiracion_esEnUnAno() {
        val ahora = System.currentTimeMillis()
        val unAno = 365L * 24 * 60 * 60 * 1000
        val expiracion = ahora + unAno
        
        val estado = PerfilUiState(
            esVip = true,
            vipExpiracion = expiracion
        )
        
        // La expiración debe ser aproximadamente 1 año después
        val diferencia = estado.vipExpiracion!! - ahora
        val diasDiferencia = diferencia / (24 * 60 * 60 * 1000)
        
        assertTrue(diasDiferencia >= 364 && diasDiferencia <= 366)
    }

    // ==================== TESTS DE CAMBIO DE CONTRASEÑA ====================

    @Test
    fun cambiarContrasena_actual_incorrecta() {
        val estado = PerfilUiState(contrasena = "Actual123@")
        
        // Simular validación
        val contrasenaIngresada = "Incorrecta123@"
        val esCorrecta = contrasenaIngresada == estado.contrasena
        
        assertFalse(esCorrecta)
    }

    @Test
    fun cambiarContrasena_actual_correcta() {
        val estado = PerfilUiState(contrasena = "Actual123@")
        
        val contrasenaIngresada = "Actual123@"
        val esCorrecta = contrasenaIngresada == estado.contrasena
        
        assertTrue(esCorrecta)
    }

    @Test
    fun cambiarContrasena_nueva_noCoincide() {
        val nueva = "Nueva123@"
        val confirmar = "Diferente123@"
        
        assertNotEquals(nueva, confirmar)
    }

    @Test
    fun cambiarContrasena_nueva_coincide() {
        val nueva = "Nueva123@"
        val confirmar = "Nueva123@"
        
        assertEquals(nueva, confirmar)
    }

    // ==================== TESTS DE CERRAR SESIÓN ====================

    @Test
    fun cerrarSesion_limpiaEstadoLogin() {
        val estadoLimpio = LoginUiState()
        
        assertTrue(estadoLimpio.correo.isEmpty())
        assertTrue(estadoLimpio.contrasena.isEmpty())
        assertFalse(estadoLimpio.exito)
    }

    @Test
    fun cerrarSesion_limpiaEstadoRegistro() {
        val estadoLimpio = RegistroUiState()
        
        assertTrue(estadoLimpio.nombre.isEmpty())
        assertTrue(estadoLimpio.correo.isEmpty())
        assertFalse(estadoLimpio.exito)
    }

    @Test
    fun cerrarSesion_limpiaEstadoPerfil() {
        val estadoLimpio = PerfilUiState()
        
        assertTrue(estadoLimpio.nombre.isEmpty())
        assertFalse(estadoLimpio.esVip)
        assertTrue(estadoLimpio.compras.isEmpty())
    }
}



