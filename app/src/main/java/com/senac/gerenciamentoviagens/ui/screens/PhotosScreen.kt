package com.senac.gerenciamentoviagens.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.senac.gerenciamentoviagens.ui.viewmodels.PhotosViewModel
import java.io.File

/**
 * Tela de Galeria de Fotos vinculada a uma viagem específica.
 * Permite visualizar, capturar (Câmera) e importar (Galeria) imagens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    tripId: Int,
    viewModel: PhotosViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    // Coleta as fotos da viagem como um estado do Compose
    val photos by viewModel.getPhotos(tripId).collectAsState(initial = emptyList())
    
    // URI temporária para armazenar a foto capturada pela câmera
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para capturar imagem da câmera (Intent)
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            viewModel.addPhoto(context, tripId, tempUri!!)
        }
    }

    // Launcher para selecionar imagem da galeria do sistema (Intent)
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.addPhoto(context, tripId, it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galeria da Viagem") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            // Botões flutuantes para adição de fotos
            Column {
                // Botão Câmera
                FloatingActionButton(
                    onClick = {
                        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                        tempUri = uri
                        cameraLauncher.launch(uri)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Tirar Foto")
                }
                // Botão Galeria
                FloatingActionButton(
                    onClick = { galleryLauncher.launch("image/*") }
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Abrir Galeria")
                }
            }
        }
    ) { padding ->
        if (photos.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Nenhuma foto capturada ainda.")
            }
        } else {
            // Grade de fotos em 3 colunas
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(photos) { photo ->
                    AsyncImage(
                        model = photo.uri,
                        contentDescription = null,
                        modifier = Modifier.aspectRatio(1f).fillMaxWidth(),
                        contentScale = ContentScale.Crop // Garante que as fotos preencham o quadrado
                    )
                }
            }
        }
    }
}
