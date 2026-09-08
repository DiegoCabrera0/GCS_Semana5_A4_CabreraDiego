package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.ui.theme.*

data class TeacherStudentGradeItem(
    val alumnoId: Int,
    val nombreCompleto: String,
    var aportes: String = "",
    var proyecto: String = "",
    var evaluacion: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherGradesScreen(onBack: () -> Unit) {
    var savedSuccess by remember { mutableStateOf(false) }

    val studentList = remember {
        mutableStateListOf(
            TeacherStudentGradeItem(1, "Mendoza Perez Mateo Sebastian", "9.50", "9.00", "8.50"),
            TeacherStudentGradeItem(2, "Mendoza Perez Camila Sofia", "8.80", "9.20", "9.00"),
            TeacherStudentGradeItem(3, "Perez Guaman Joaquin Nicolas", "9.00", "8.50", "9.00"),
            TeacherStudentGradeItem(4, "Gomez Chacon Valentina Isabel", "8.50", "9.00", "8.00"),
            TeacherStudentGradeItem(5, "Torres Naranjo Gabriel Alexander", "9.20", "9.50", "9.10")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matriz de Calificaciones - 8vo A", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { savedSuccess = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Save, contentDescription = null) },
                text = { Text("Guardar Notas") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (savedSuccess) {
                Card(colors = CardDefaults.cardColors(containerColor = StatusGreen.copy(alpha = 0.15f))) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Calificaciones guardadas exitosamente en el servidor.", color = StatusGreen, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(studentList) { student ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(student.nombreCompleto, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = student.aportes,
                                    onValueChange = { student.aportes = it },
                                    label = { Text("Aportes") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = student.proyecto,
                                    onValueChange = { student.proyecto = it },
                                    label = { Text("Proyecto") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = student.evaluacion,
                                    onValueChange = { student.evaluacion = it },
                                    label = { Text("Evaluación") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
