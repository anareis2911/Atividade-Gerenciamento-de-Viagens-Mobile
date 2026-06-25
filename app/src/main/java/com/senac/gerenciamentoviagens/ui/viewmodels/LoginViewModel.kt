package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.UserDao
import kotlinx.coroutines.launch

/**
 * ViewModel responsável pela lógica da tela de Login.
 * Gerencia o estado dos campos de entrada e a validação de credenciais.
 */
class LoginViewModel(private val userDao: UserDao) : ViewModel() {
    // Estados observáveis para os campos de e-mail e senha
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    // Controla se a senha deve ser exibida em texto plano ou oculta
    var passwordVisible by mutableStateOf(false)
    
    // Armazena mensagens de erro para feedback ao usuário
    var errorMessage by mutableStateOf<String?>(null)

    fun onEmailChange(newValue: String) {
        email = newValue
        errorMessage = null // Limpa erro ao digitar
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        errorMessage = null
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    /**
     * Valida as credenciais no banco de dados local (Room).
     * Se bem-sucedido, chama o callback de sucesso com o e-mail logado.
     */
    fun validateLogin(onSuccess: (String) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha todos os campos"
            return
        }

        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            // Verifica se o usuário existe e se a senha coincide
            if (user != null && user.password == password) {
                errorMessage = null
                onSuccess(email)
            } else {
                errorMessage = "E-mail ou senha inválidos"
            }
        }
    }
}
