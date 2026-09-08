package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.ParentViewModel

@Composable
fun ParentDashboardScreen(
    viewModel: ParentViewModel,
    userName: String,
    onNavigateToGrades: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToJustifications: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            InstitutionalTopBar(
                userRole = "REPRESENTANTE",
                userName = userName,
                onLogoutClick = onLogout
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Estudiante Representado",
                                color = SecondaryTurquoise,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mateo Sebastian Mendoza Perez",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Curso: 8vo EGB 'A' | AMIE: 18H00123",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                item {
                    Text("Acciones de Representante", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Calificaciones", "Ver notas y promedios", Icons.Default.Grade, PrimaryBlue, Modifier.weight(1f), onNavigateToGrades)
                            ModuleCard("Tareas", "Supervisar deberes", Icons.AutoMirrored.Filled.Assignment, SecondaryTurquoise, Modifier.weight(1f), onNavigateToTasks)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Asistencia", "Faltas y atrasos", Icons.Default.CalendarMonth, AccentOrange, Modifier.weight(1f), onNavigateToAttendance)
                            ModuleCard("Justificativos", "Enviar y ver estado", Icons.Default.VerifiedUser, StatusGreen, Modifier.weight(1f), onNavigateToJustifications)
                        }
                    }
                }
            }
        }
    }
}
