package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.data.model.AttendanceRecordDto
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentAttendanceScreen(
    viewModel: StudentViewModel,
    onBack: () -> Unit
) {
    val attendanceState by viewModel.attendanceState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comportamiento y Asistencia", color = Color.White) },
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
            when (val state = attendanceState) {
                is ResultState.Loading -> LoadingView("Cargando registro de asistencia...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val res = state.data
                    val summary = res.resumen

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Resumen del Período", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                        AttendanceMetricBox("Asistencias", "${summary.presentes}", StatusGreen)
                                        AttendanceMetricBox("Atrasos", "${summary.atrasos}", AccentOrange)
                                        AttendanceMetricBox("F. Justif.", "${summary.faltasJustificadas}", SecondaryTurquoise)
                                        AttendanceMetricBox("F. Injustif.", "${summary.faltasInjustificadas}", StatusRed)
                                    }
                                }
                            }
                        }

                        item {
                            Text("Detalle de Registros", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        items(res.registros) { rec ->
                            AttendanceRecordCard(record = rec)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceMetricBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun AttendanceRecordCard(record: AttendanceRecordDto) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Fecha: ${record.fecha}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                record.observacion?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            StatusChip(status = record.estado)
        }
    }
}
