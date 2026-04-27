package com.example.a000111522_parcial1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            val orden = remember { mutableStateMapOf<Int, Int>() }

            NavHost(navController, startDestination = "menu") {

                //menu
                composable("menu") {

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Menú") },
                                actions = {
                                    TextButton(onClick = {
                                        navController.navigate("orden")
                                    }) {
                                        Text("Orden (${orden.values.sum()})")
                                    }
                                }
                            )
                        }
                    ) { padding ->

                        LazyColumn(contentPadding = padding) {

                            items(menu) { producto ->

                                val cantidad = orden[producto.id] ?: 0

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            orden[producto.id] = cantidad + 1
                                        }
                                ) {
                                    Row(modifier = Modifier.padding(8.dp)) {

                                        AsyncImage(
                                            model = producto.imagenUrl,
                                            contentDescription = null,
                                            modifier = Modifier.size(80.dp)
                                        )

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column {
                                            Text(producto.nombre)
                                            Text("$${producto.precio}")

                                            if (cantidad > 0) {
                                                Text("Cantidad: $cantidad")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //orden
                composable("orden") {

                    val context = LocalContext.current

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Mi Orden") }
                            )
                        }
                    ) { padding ->
                        Column(modifier = Modifier.padding(padding)) {

                            LazyColumn(modifier = Modifier.weight(1f)) {

                                items(menu.filter { (orden[it.id] ?: 0) > 0 }) { producto ->

                                    val cantidad = orden[producto.id] ?: 0
                                    val subtotal = cantidad * producto.precio

                                    Row(modifier = Modifier.padding(8.dp)) {

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(producto.nombre)
                                            Text("Cantidad: $cantidad")
                                            Text("Subtotal: $${subtotal}")
                                        }

                                        Button(onClick = {
                                            orden.remove(producto.id)
                                        }) {
                                            Text("Eliminar")
                                        }
                                    }
                                }
                            }

                            val total = orden.entries.sumOf { (id, cant) ->
                                val p = menu.find { it.id == id }
                                (p?.precio ?: 0.0) * cant
                            }

                            Text("Total: $${total}", modifier = Modifier.padding(8.dp))

                            Button(
                                onClick = {
                                    orden.clear()
                                    Toast.makeText(context, "Orden confirmada", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirmar")
                            }

                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Volver")
                            }
                        }
                    }
                }
            }
        }
    }
}

//modelo

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String
)

//datos

val pupusaImg = "https://comedera.com/wp-content/uploads/sites/9/2023/05/Pupusas-de-queso-shutterstock_1803502444.jpg"
val cafeImg   = "https://i.blogs.es/139e0f/cafe-americano2/840_560.jpeg"
val chocoImg  = "https://cocinaconcoqui.com/wp-content/uploads/2025/12/chocolate-caliente-casero-500x500.jpg"
val cocaImg   = "https://d23esi1h40dfmi.cloudfront.net/wp-content/uploads/2025/08/01124509/00732.jpg"

val menu = listOf(
    Producto(1, "Pupusa de queso", 0.75, pupusaImg),
    Producto(2, "Pupusa de frijol con queso", 0.75, pupusaImg),
    Producto(3, "Pupusa revuelta", 1.00, pupusaImg),
    Producto(4, "Pupusa de chicharrón", 1.00, pupusaImg),
    Producto(5, "Pupusa de loroco con queso", 1.00, pupusaImg),
    Producto(6, "Pupusa de ayote", 0.75, pupusaImg),
    Producto(7, "Pupusa de espinaca", 0.85, pupusaImg),
    Producto(8, "Pupusa de jalapeño con queso", 1.00, pupusaImg),
    Producto(9, "Café", 1.00, cafeImg),
    Producto(10, "Chocolate", 1.50, chocoImg),
    Producto(11, "Coca-Cola", 1.25, cocaImg)
)