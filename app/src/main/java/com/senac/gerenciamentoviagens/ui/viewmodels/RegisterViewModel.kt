package com.senac.gerenciamentoviagens.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senac.gerenciamentoviagens.data.dao.UserDao
import com.senac.gerenciamentoviagens.data.model.User
import kotlinx.coroutines.launch

/**
 * ViewModel responsável pela lógica de cadastro de novos usuários.
 * Gerencia o estado do formulário e a persistência inicial no banco de dados.
 */
class RegisterViewModel(private val userDao: UserDao) : ViewModel() {
    // Estados reativos para cada campo do formulário de registro
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    
    // Controle de exibição de erros e mensagens de feedback
    var showErrors by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // Funções de atualização de estado chamadas pela UI
    fun onNameChange(value: String) { name = value }
    fun onEmailChange(value: String) { email = value }
    fun onPhoneChange(value: String) { phone = value }
    fun onPasswordChange(value: String) { password = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }

    /**
     * Realiza a validação dos campos e tenta inserir o novo usuário no Room.
     * Verifica se o e-mail já existe antes de prosseguir.
     * @param onSuccess Callback executado após o cadastro bem-sucedido.
     */
    fun validateAndRegister(onSuccess: () -> Unit) {
        showErrors = true
        // Validação básica de campos vazios
        if (name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Todos os campos são obrigatórios"
        } 
        // Validação de confirmação de senha
        else if (password != confirmPassword) {
            errorMessage = "As senhas não coincidem"
        } else {
            errorMessage = null
            viewModelScope.launch {
                try {
                    // Regra de negócio: E-mail deve ser único
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
