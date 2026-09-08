package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.example.app_grupo9.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminJustificationsScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val justificationsState by viewModel.justificationsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aprobación de Justificativos", color = Color.White) },
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
            when (val state = justificationsState) {
                is ResultState.Loading -> LoadingView("Cargando justificativos pendientes...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val list = state.data
                    if (list.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay solicitudes pendientes.", color = TextSecondary)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(list) { item ->
                                AdminJustificationItemCard(
                                    item = item,
                                    onApprove = { viewModel.processJustification(item.justificativoId, "APROBADO", "Aprobado formalmente por Administración") },
                                    onReject = { viewModel.processJustification(item.justificativoId, "RECHAZADO", "Rechazado por inconsistencia") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminJustificationItemCard(
    item: JustificationDto,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${item.alumnoApellidos} ${item.alumnoNombres}", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                StatusChip(status = item.estado)
            }
            Text("Representante: ${item.repNombres} ${item.repApellidos}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Fecha Inasistencia: ${item.fechaAsistencia}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text("Motivo: ${item.motivo}", style = MaterialTheme.typography.bodyMedium)

            if (item.estado == "PENDIENTE") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = onReject, colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRed)) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rechazar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onApprove, colors = ButtonDefaults.buttonColors(containerColor = StatusGreen)) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aprobar")
                    }
                }
            }
        }
    }
}
