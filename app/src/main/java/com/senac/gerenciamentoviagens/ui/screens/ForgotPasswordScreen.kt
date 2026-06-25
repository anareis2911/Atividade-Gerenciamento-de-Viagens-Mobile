package com.senac.gerenciamentoviagens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senac.gerenciamentoviagens.ui.viewmodels.ForgotPasswordViewModel

/**
 * Tela de Recuperação de Senha.
 * Permite ao usuário solicitar o envio de uma nova senha ou link de recuperação por e-mail.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onResetSent: () -> Unit,             // Callback disparado após solicitar a recuperação
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Esqueci a senha") },
                navigationIcon = {
                    IconButton(onClick = onResetSent) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo para informar o e-mail de recuperação
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão para disparar a ação de recuperação
            Button(
                onClick = onResetSent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Senha")
            }
        }
    }
}
