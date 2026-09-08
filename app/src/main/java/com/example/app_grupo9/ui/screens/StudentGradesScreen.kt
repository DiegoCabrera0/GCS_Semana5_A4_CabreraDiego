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
import com.example.app_grupo9.data.model.SubjectGradesDto
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    viewModel: StudentViewModel,
    onBack: () -> Unit
) {
    val gradesState by viewModel.gradesState.collectAsState()
    var selectedTrimester by remember { mutableStateOf(2) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calificaciones por Materia", color = Color.White) },
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
            TabRow(
                selectedTabIndex = selectedTrimester - 1,
                containerColor = SurfaceWhite,
                contentColor = PrimaryBlue
            ) {
                Tab(selected = selectedTrimester == 1, onClick = { selectedTrimester = 1 }) {
                    Text("1º Trimestre", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold)
                }
                Tab(selected = selectedTrimester == 2, onClick = { selectedTrimester = 2 }) {
                    Text("2º Trimestre", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold)
                }
                Tab(selected = selectedTrimester == 3, onClick = { selectedTrimester = 3 }) {
                    Text("3º Trimestre", modifier = Modifier.padding(14.dp), fontWeight = FontWeight.Bold)
                }
            }

            when (val state = gradesState) {
                is ResultState.Loading -> LoadingView("Cargando libreta de calificaciones...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val subjects = state.data.materias
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(subjects) { subject ->
                            SubjectGradeCard(subject = subject, trimesterNum = selectedTrimester)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectGradeCard(subject: SubjectGradesDto, trimesterNum: Int) {
    val tData = subject.trimestres[trimesterNum.toString()]

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = subject.materia, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Text(text = "Prof: ${subject.profesor}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                val promText = tData?.promedio?.let { String.format("%.2f", it) } ?: "—"
                Surface(
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Prom: $promText",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BackgroundGray)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GradeComponentBox("Aportes (40%)", tData?.aportes)
                GradeComponentBox("Proyecto (30%)", tData?.proyecto)
                GradeComponentBox("Evaluación (30%)", tData?.evaluacion)
            }
        }
    }
}

@Composable
fun GradeComponentBox(title: String, score: Double?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = score?.let { String.format("%.2f", it) } ?: "—",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (score == null) TextSecondary else if (score >= 7.0) StatusGreen else StatusRed
        )
    }
}
