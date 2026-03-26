package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagens.ui.components.PasswordTextField
import com.senac.gerenciamentoviagens.ui.viewmodels.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Usuário") },
                navigationIcon = {
                    IconButton(onClick = onRegisterSuccess) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.showErrors && viewModel.name.isBlank(),
                supportingText = {
                    if (viewModel.showErrors && viewModel.name.isBlank()) {
                        Text("Campo obrigatório")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.showErrors && viewModel.email.isBlank(),
                supportingText = {
                    if (viewModel.showErrors && viewModel.email.isBlank()) {
                        Text("Campo obrigatório")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = { Text("Fone") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.showErrors && viewModel.phone.isBlank(),
                supportingText = {
                    if (viewModel.showErrors && viewModel.phone.isBlank()) {
                        Text("Campo obrigatório")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Senha",
                isError = viewModel.showErrors && viewModel.password.isBlank()
            )
            if (viewModel.showErrors && viewModel.password.isBlank()) {
                Text(
                    "Campo obrigatório",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChange(it) },
                label = "Confirmação",
                isError = viewModel.showErrors && viewModel.confirmPassword.isBlank()
            )
            if (viewModel.showErrors && viewModel.confirmPassword.isBlank()) {
                Text(
                    "Campo obrigatório",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                )
            }

            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.validateAndRegister(onRegisterSuccess)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Registrar")
            }
        }
    }
}
