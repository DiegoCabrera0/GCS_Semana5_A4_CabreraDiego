package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.StudentViewModel

@Composable
fun StudentDashboardScreen(
    viewModel: StudentViewModel,
    userName: String,
    onNavigateToGrades: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToAnnouncements: () -> Unit,
    onLogout: () -> Unit
) {
    val gradesState by viewModel.gradesState.collectAsState()

    Scaffold(
        topBar = {
            InstitutionalTopBar(
                userRole = "ALUMNO",
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
            val isOffline = (gradesState as? ResultState.Success)?.isOffline == true
            if (isOffline) {
                OfflineBanner()
            }

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
                                text = "Panel Académico del Estudiante",
                                color = SecondaryTurquoise,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userName,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Curso: 8vo EGB 'A'",
                                    color = Color.White.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Período: 2025-2026",
                                    color = Color.White.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Rendimiento General",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricCard(title = "T1 Promedio", value = "9.10/10", color = StatusGreen, modifier = Modifier.weight(1f))
                        MetricCard(title = "T2 En Curso", value = "8.90/10", color = AccentOrange, modifier = Modifier.weight(1f))
                        MetricCard(title = "Prom. Final", value = "9.00/10", color = PrimaryBlue, modifier = Modifier.weight(1f))
                    }
                }

                item {
                    Text(
                        text = "Módulos de Gestión",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Calificaciones", "Notas por trimestre", Icons.Default.Grade, PrimaryBlue, Modifier.weight(1f), onNavigateToGrades)
                            ModuleCard("Tareas", "Deberes y entregas", Icons.Default.Assignment, SecondaryTurquoise, Modifier.weight(1f), onNavigateToTasks)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Asistencia", "Atrasos y faltas", Icons.Default.CalendarMonth, AccentOrange, Modifier.weight(1f), onNavigateToAttendance)
                            ModuleCard("Comunicados", "Notificaciones UEAPD", Icons.Default.Campaign, StatusGreen, Modifier.weight(1f), onNavigateToAnnouncements)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun ModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}
