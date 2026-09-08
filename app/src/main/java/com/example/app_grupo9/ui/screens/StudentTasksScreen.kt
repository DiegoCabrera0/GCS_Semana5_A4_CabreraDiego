package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.data.model.TaskDto
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentTasksScreen(
    viewModel: StudentViewModel,
    onBack: () -> Unit
) {
    val tasksState by viewModel.tasksState.collectAsState()
    var selectedTaskForSubmission by remember { mutableStateOf<TaskDto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tareas y Actividades", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = tasksState) {
                is ResultState.Loading -> LoadingView("Cargando lista de tareas...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val tasks = state.data
                    if (tasks.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tiene tareas pendientes registradas.", color = TextSecondary)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(tasks) { task ->
                                TaskCard(task = task, onClick = { selectedTaskForSubmission = task })
                            }
                        }
                    }
                }
            }
        }
    }

    selectedTaskForSubmission?.let { task ->
        SubmitTaskDialog(
            task = task,
            onDismiss = { selectedTaskForSubmission = null },
            onSubmit = {
                selectedTaskForSubmission = null
                viewModel.loadData()
            }
        )
    }
}

@Composable
fun TaskCard(task: TaskDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.materia ?: "Asignatura",
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = task.estado)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = task.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = task.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Límite: ${task.fechaLimite}",
                    style = MaterialTheme.typography.labelSmall,
                    color = StatusRed
                )

                if (task.nota != null) {
                    Text(
                        text = "Nota: ${String.format("%.2f", task.nota)}/10",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = StatusGreen
                    )
                }
            }
        }
    }
}

@Composable
fun SubmitTaskDialog(
    task: TaskDto,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var textDelivery by remember { mutableStateOf("") }
    var fileSelected by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(task.titulo, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(task.descripcion, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Fecha Límite: ${task.fechaLimite}", style = MaterialTheme.typography.bodySmall, color = StatusRed)

                if (task.estado == "CALIFICADA") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(colors = CardDefaults.cardColors(containerColor = StatusGreen.copy(alpha = 0.1f))) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Calificación: ${task.nota}/10", fontWeight = FontWeight.Bold, color = StatusGreen)
                            task.retroalimentacion?.let {
                                Text("Observación: $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = textDelivery,
                        onValueChange = { textDelivery = it },
                        label = { Text("Respuesta o comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { fileSelected = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (fileSelected) "Archivo Adjunto: tarea_completada.pdf" else "Adjuntar Archivo (PDF/DOC)")
                    }
                }
            }
        },
        confirmButton = {
            if (task.estado != "CALIFICADA") {
                Button(
                    onClick = onSubmit,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Enviar Tarea")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
