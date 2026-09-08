package com.example.app_grupo9.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_grupo9.data.model.UserDto
import com.example.app_grupo9.data.repository.ResultState
import com.example.app_grupo9.ui.components.*
import com.example.app_grupo9.ui.theme.*
import com.example.app_grupo9.ui.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    val usersState by viewModel.usersState.collectAsState()
    var showCreateUserDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Usuarios", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateUserDialog = true }, containerColor = PrimaryBlue, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Usuario")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = usersState) {
                is ResultState.Loading -> LoadingView("Cargando usuarios...")
                is ResultState.Error -> ErrorView(state.message) { viewModel.loadData() }
                is ResultState.Success -> {
                    val users = state.data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(users) { user ->
                            UserItemCard(user = user)
                        }
                    }
                }
            }
        }
    }

    if (showCreateUserDialog) {
        CreateUserDialog(onDismiss = { showCreateUserDialog = false }, onCreate = { showCreateUserDialog = false; viewModel.loadData() })
    }
}

@Composable
fun UserItemCard(user: UserDto) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${user.apellidos} ${user.nombres}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Text("Usuario: ${user.username} | Cédula: ${user.cedula}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                StatusChip(status = user.rol)
            }
            Row {
                IconButton(onClick = {}) { Icon(Icons.Default.LockReset, contentDescription = "Reset Clave", tint = AccentOrange) }
                IconButton(onClick = {}) { Icon(Icons.Default.Delete, contentDescription = "Desactivar", tint = StatusRed) }
            }
        }
    }
}

@Composable
fun CreateUserDialog(onDismiss: () -> Unit, onCreate: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Nuevo Usuario", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = cedula, onValueChange = { cedula = it }, label = { Text("Cédula") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = onCreate, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
