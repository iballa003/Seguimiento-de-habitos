//package org.iesharia.seguimientodehabitos.ui.screen
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun PerfilScreen(
//    usuario: Usuario,
//    onCerrarSesion: () -> Unit,
//    onEditarPerfil: () -> Unit = {}
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Perfil", fontSize = 28.sp, fontWeight = FontWeight.Bold)
//
//        Text("Nombre: ${usuario.nombre}")
//        Text("Email: ${usuario.email}")
//
//        Button(onClick = onEditarPerfil) {
//            Text("Editar perfil")
//        }
//
//        Button(
//            onClick = onCerrarSesion,
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
//        ) {
//            Text("Cerrar sesión", color = Color.White)
//        }
//    }
//}
