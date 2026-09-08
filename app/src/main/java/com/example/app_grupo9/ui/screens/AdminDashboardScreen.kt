package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*

@Composable
fun AdminDashboardScreen(
    userName: String,
    onNavigateToUsers: () -> Unit,
    onNavigateToJustifications: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            InstitutionalTopBar(
                userRole = "ADMINISTRADOR",
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
                            Text("Panel del Administrador", color = SecondaryTurquoise, style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(userName, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Administración Central de la Unidad Educativa Dr. Alfredo Pareja Diezcanseco", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MetricCard("Alumnos", "30", PrimaryBlue, Modifier.weight(1f))
                        MetricCard("Profesores", "6", SecondaryTurquoise, Modifier.weight(1f))
                        MetricCard("Representantes", "10", StatusGreen, Modifier.weight(1f))
                    }
                }

                item {
                    Text("Módulos Administrativos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Usuarios", "Gestión CRUD y claves", Icons.Default.People, PrimaryBlue, Modifier.weight(1f), onNavigateToUsers)
                            ModuleCard("Justificativos", "Aprobar/Rechazar solicitudes", Icons.Default.VerifiedUser, StatusGreen, Modifier.weight(1f), onNavigateToJustifications)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Períodos Lectivos", "Apertura y cierre", Icons.Default.CalendarMonth, AccentOrange, Modifier.weight(1f), {})
                            ModuleCard("Comunicados", "Publicación general", Icons.Default.Campaign, SecondaryTurquoise, Modifier.weight(1f), {})
                        }
                    }
                }
            }
        }
    }
}
