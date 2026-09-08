package com.example.app_grupo9.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_grupo9.ui.screens.*
import com.example.app_grupo9.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object StudentDashboard : Screen("student_dashboard")
    object StudentGrades : Screen("student_grades")
    object StudentTasks : Screen("student_tasks")
    object StudentAttendance : Screen("student_attendance")

    object ParentDashboard : Screen("parent_dashboard")
    object ParentJustifications : Screen("parent_justifications")

    object TeacherDashboard : Screen("teacher_dashboard")
    object TeacherGrades : Screen("teacher_grades")

    object AdminDashboard : Screen("admin_dashboard")
    object AdminUsers : Screen("admin_users")
    object AdminJustifications : Screen("admin_justifications")

    object ChangePassword : Screen("change_password")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = when (currentUser?.rol) {
        "ALUMNO" -> Screen.StudentDashboard.route
        "REPRESENTANTE" -> Screen.ParentDashboard.route
        "PROFESOR" -> Screen.TeacherDashboard.route
        "ADMINISTRADOR" -> Screen.AdminDashboard.route
        else -> Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val user = authViewModel.currentUser.value
                    val dest = when (user?.rol) {
                        "ALUMNO" -> Screen.StudentDashboard.route
                        "REPRESENTANTE" -> Screen.ParentDashboard.route
                        "PROFESOR" -> Screen.TeacherDashboard.route
                        "ADMINISTRADOR" -> Screen.AdminDashboard.route
                        else -> Screen.Login.route
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ALUMNO
        composable(Screen.StudentDashboard.route) {
            val studentVm: StudentViewModel = viewModel()
            StudentDashboardScreen(
                viewModel = studentVm,
                userName = "${currentUser?.nombres} ${currentUser?.apellidos}",
                onNavigateToGrades = { navController.navigate(Screen.StudentGrades.route) },
                onNavigateToTasks = { navController.navigate(Screen.StudentTasks.route) },
                onNavigateToAttendance = { navController.navigate(Screen.StudentAttendance.route) },
                onNavigateToAnnouncements = {},
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.StudentGrades.route) {
            val studentVm: StudentViewModel = viewModel()
            StudentGradesScreen(viewModel = studentVm, onBack = { navController.popBackStack() })
        }

        composable(Screen.StudentTasks.route) {
            val studentVm: StudentViewModel = viewModel()
            StudentTasksScreen(viewModel = studentVm, onBack = { navController.popBackStack() })
        }

        composable(Screen.StudentAttendance.route) {
            val studentVm: StudentViewModel = viewModel()
            StudentAttendanceScreen(viewModel = studentVm, onBack = { navController.popBackStack() })
        }

        // REPRESENTANTE
        composable(Screen.ParentDashboard.route) {
            val parentVm: ParentViewModel = viewModel()
            ParentDashboardScreen(
                viewModel = parentVm,
                userName = "${currentUser?.nombres} ${currentUser?.apellidos}",
                onNavigateToGrades = { navController.navigate(Screen.StudentGrades.route) },
                onNavigateToTasks = { navController.navigate(Screen.StudentTasks.route) },
                onNavigateToAttendance = { navController.navigate(Screen.StudentAttendance.route) },
                onNavigateToJustifications = { navController.navigate(Screen.ParentJustifications.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.ParentJustifications.route) {
            val parentVm: ParentViewModel = viewModel()
            ParentJustificationsScreen(viewModel = parentVm, onBack = { navController.popBackStack() })
        }

        // PROFESOR
        composable(Screen.TeacherDashboard.route) {
            TeacherDashboardScreen(
                userName = "${currentUser?.nombres} ${currentUser?.apellidos}",
                onNavigateToGradeMatrix = { navController.navigate(Screen.TeacherGrades.route) },
                onNavigateToCreateTask = {},
                onNavigateToTakeAttendance = {},
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.TeacherGrades.route) {
            TeacherGradesScreen(onBack = { navController.popBackStack() })
        }

        // ADMINISTRADOR
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                userName = "${currentUser?.nombres} ${currentUser?.apellidos}",
                onNavigateToUsers = { navController.navigate(Screen.AdminUsers.route) },
                onNavigateToJustifications = { navController.navigate(Screen.AdminJustifications.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) }
                }
            )
        }

        composable(Screen.AdminUsers.route) {
            val adminVm: AdminViewModel = viewModel()
            AdminUsersScreen(viewModel = adminVm, onBack = { navController.popBackStack() })
        }

        composable(Screen.AdminJustifications.route) {
            val adminVm: AdminViewModel = viewModel()
            AdminJustificationsScreen(viewModel = adminVm, onBack = { navController.popBackStack() })
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(onBack = { navController.popBackStack() })
        }
    }
}
