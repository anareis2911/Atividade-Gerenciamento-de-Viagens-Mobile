package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.UserDao
import com.senac.gerenciamentoviagens.data.model.User
import kotlinx.coroutines.launch

class RegisterViewModel(private val userDao: UserDao) : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    
    var showErrors by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun onNameChange(value: String) { name = value }
    fun onEmailChange(value: String) { email = value }
    fun onPhoneChange(value: String) { phone = value }
    fun onPasswordChange(value: String) { password = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }

    fun validateAndRegister(onSuccess: () -> Unit) {
        showErrors = true
        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Todos os campos são obrigatórios"
        } else if (password != confirmPassword) {
            errorMessage = "As senhas não coincidem"
        } else {
            errorMessage = null
            viewModelScope.launch {
                try {
                    val existingUser = userDao.getUserByEmail(email)
                    if (existingUser != null) {
                        errorMessage = "E-mail já cadastrado"
                    } else {
                        val newUser = User(
                            name = name,
                            email = email,
                            phone = phone,
                            password = password
                        )
                        userDao.insert(newUser)
                        successMessage = "Usuário cadastrado com sucesso!"
                        onSuccess()
                    }
                } catch (e: Exception) {
                    errorMessage = "Erro ao cadastrar usuário: ${e.message}"
                }
            }
        }
    }
}
