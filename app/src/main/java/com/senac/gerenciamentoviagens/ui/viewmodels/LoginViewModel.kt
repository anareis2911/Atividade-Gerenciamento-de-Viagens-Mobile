package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.UserDao
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onEmailChange(newValue: String) {
        email = newValue
        errorMessage = null
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
        errorMessage = null
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun validateLogin(onSuccess: (String) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Preencha todos os campos"
            return
        }

        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                errorMessage = null
                onSuccess(email)
            } else {
                errorMessage = "E-mail ou senha inválidos"
            }
        }
    }
}
