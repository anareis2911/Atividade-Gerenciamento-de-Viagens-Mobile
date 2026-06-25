package com.senac.gerenciamentoviagens.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Componente customizado de campo de texto para senhas.
 * Encapsula a lógica de alternância de visibilidade (mostrar/ocultar senha).
 */
@Composable
fun PasswordTextField(
    value: String,                          // Valor atual do campo
    onValueChange: (String) -> Unit,        // Callback disparado ao digitar
    label: String,                          // Rótulo exibido no campo
    modifier: Modifier = Modifier,
    isError: Boolean = false                // Indica se o campo deve exibir estado de erro
) {
    // Estado local para controlar se o texto está visível ou mascarado
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        // Aplica a transformação visual baseada no estado de visibilidade
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            // Ícone de olho que alterna o estado
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = image, 
                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha"
                )
            }
        }
    )
}
