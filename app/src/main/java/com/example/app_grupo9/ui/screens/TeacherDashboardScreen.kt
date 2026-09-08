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

@Composable
fun TeacherDashboardScreen(
    userName: String,
    onNavigateToGradeMatrix: () -> Unit,
    onNavigateToCreateTask: () -> Unit,
    onNavigateToTakeAttendance: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            InstitutionalTopBar(
                userRole = "PROFESOR",
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
                            Text("Gestión Docente", color = SecondaryTurquoise, style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(userName, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Asignatura: Matemáticas | Cursos: 8vo EGB 'A', 9no EGB 'A'", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    Text("Operaciones de Aula", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Registrar Notas", "Matriz por componente", Icons.Default.Grade, PrimaryBlue, Modifier.weight(1f), onNavigateToGradeMatrix)
                            ModuleCard("Crear Tarea", "Publicar deberes", Icons.AutoMirrored.Filled.Assignment, SecondaryTurquoise, Modifier.weight(1f), onNavigateToCreateTask)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ModuleCard("Tomar Asistencia", "Registro diario escolar", Icons.Default.CalendarMonth, AccentOrange, Modifier.weight(1f), onNavigateToTakeAttendance)
                            ModuleCard("Novedades", "Observaciones de conducta", Icons.Default.Warning, StatusRed, Modifier.weight(1f), {})
                        }
                    }
                }
            }
        }
    }
}
