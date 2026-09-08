package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.data.model.JustificationDto
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.ParentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentJustificationsScreen(
    viewModel: ParentViewModel,
    onBack: () -> Unit
) {
    val justificationsState by viewModel.justificationsState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Justificativos de Inasistencia", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Enviar Justificativo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = justificationsState) {
                is ResultState.Loading -> LoadingView("Cargando justificativos...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val list = state.data
                    if (list.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No registra justificativos enviados.", color = TextSecondary)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(list) { item ->
                                JustificationCard(item = item)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateJustificationDialog(
            onDismiss = { showCreateDialog = false },
            onSubmit = {
                showCreateDialog = false
                viewModel.loadData()
            }
        )
    }
}

@Composable
fun JustificationCard(item: JustificationDto) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Fecha: ${item.fechaEnvio}", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                StatusChip(status = item.estado)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Motivo:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
            Text(text = item.motivo, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)

            item.observacionAdmin?.let {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Respuesta Administrador: $it", style = MaterialTheme.typography.bodySmall, color = PrimaryBlue, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun CreateJustificationDialog(onDismiss: () -> Unit, onSubmit: () -> Unit) {
    var motivo by remember { mutableStateOf("") }
    var fileAttached by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enviar Justificativo de Inasistencia", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Describa el motivo de la inasistencia o atraso del estudiante:", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo / Explicación") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { fileAttached = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (fileAttached) "Evidencia Adjunta: certificado.pdf" else "Adjuntar Evidencia Medica/Legal")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                enabled = motivo.isNotBlank()
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
