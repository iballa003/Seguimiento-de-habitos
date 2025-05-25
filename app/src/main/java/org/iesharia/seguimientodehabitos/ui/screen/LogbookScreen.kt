package org.iesharia.seguimientodehabitos.ui.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.seguimientodehabitos.ui.components.DatePickerSection
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogbookScreen(
    selectedDate: LocalDate = LocalDate.now(),
    onDateChange: (LocalDate) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val habitsThatDay = remember(selectedDate) {
        listOf(
        "Leer 30 min",
        "Hacer ejercicio",
        "Meditar",
        "Beber 2L de agua")
    } // Puedes conectar esto después a la base de datos

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
                    titleContentColor = androidx.compose.ui.graphics.Color.White,
                    navigationIconContentColor = androidx.compose.ui.graphics.Color.White,
                    actionIconContentColor = androidx.compose.ui.graphics.Color.White
                ),
                title = {
                    Text("Registro", style = MaterialTheme.typography.headlineSmall, fontSize = 24.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            DatePickerSection(
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Hábitos completados el ${selectedDate}",
                style = MaterialTheme.typography.titleMedium, fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(habitsThatDay) { habit ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = habit,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
