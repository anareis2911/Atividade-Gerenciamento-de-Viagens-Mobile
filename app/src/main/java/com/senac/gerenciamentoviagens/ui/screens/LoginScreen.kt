package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagens.ui.components.PasswordTextField
import com.senac.gerenciamentoviagens.ui.viewmodels.LoginViewModel

/**
 * Tela de entrada do aplicativo.
 * Permite que usuários cadastrados acessem suas informações de viagem.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,        // Callback para navegação após login válido
    onNavigateToRegister: () -> Unit,       // Callback para abrir tela de cadastro
    onNavigateToForgotPassword: () -> Unit, // Callback para abrir tela de recuperação
    viewModel: LoginViewModel = viewModel() // Gerenciador de estado da tela
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logotipo ou Identidade Visual do App
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("LOGO", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campo para entrada do E-mail
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Componente customizado para entrada de senha com controle de visibilidade
            PasswordTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Senha"
            )

            // Exibe mensagem de erro caso as credenciais sejam inválidas
            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de acesso que dispara a validação na ViewModel
            Button(
                onClick = { 
                    viewModel.validateLogin(onLoginSuccess)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Login")
            }

            // Opções adicionais de navegação para novos usuários ou recuperação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = onNavigateToRegister) {
                    Text("Novo Usuário")
                }

                TextButton(onClick = onNavigateToForgotPassword) {
                    Text("Esqueci a Senha")
                }
            }
        }
    }
}
