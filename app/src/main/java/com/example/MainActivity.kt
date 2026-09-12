package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TourItemEntity
import com.example.ui.TourViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Accueil", Icons.Default.Home)
    object Excursions : Screen("excursions", "Excursions", Icons.Default.Explore)
    object Accommodations : Screen("accommodations", "Hébergements", Icons.Default.Hotel)
    object Services : Screen("services", "Services", Icons.Default.DirectionsBoat)
    object Bookings : Screen("bookings", "Réservations", Icons.Default.Bookmark)
    object AiGuide : Screen("ai_guide", "IA Guide", Icons.Default.SmartToy)
}

class MainActivity : ComponentActivity() {
    private val viewModel: TourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: TourViewModel) {
    val items by viewModel.allItems.collectAsStateWithLifecycle()
    val featuredItems by viewModel.featuredItems.collectAsStateWithLifecycle()
    val bookings by viewModel.allBookings.collectAsStateWithLifecycle()

    var currentRoute by remember { mutableStateOf<String>(Screen.Home.route) }
    var selectedItemForDetail by remember { mutableStateOf<TourItemEntity?>(null) }
    var selectedItemForBooking by remember { mutableStateOf<TourItemEntity?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    val screens = listOf(
        Screen.Home,
        Screen.Excursions,
        Screen.Accommodations,
        Screen.Services,
        Screen.Bookings,
        Screen.AiGuide
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (selectedItemForDetail == null) {
                NavigationBar {
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, maxLines = 1) },
                            selected = currentRoute == screen.route,
                            onClick = { currentRoute = screen.route }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (selectedItemForDetail != null) {
                DetailScreen(
                    item = selectedItemForDetail!!,
                    onBack = { selectedItemForDetail = null },
                    onBook = {
                        selectedItemForBooking = selectedItemForDetail
                        selectedItemForDetail = null
                    }
                )
            } else {
                when (currentRoute) {
                    Screen.Home.route -> {
                        HomeScreen(
                            featuredItems = featuredItems.ifEmpty { items },
                            onNavigateToCategory = { cat ->
                                currentRoute = when (cat) {
                                    "excursion" -> Screen.Excursions.route
                                    "accommodation" -> Screen.Accommodations.route
                                    else -> Screen.Services.route
                                }
                            },
                            onItemSelected = { item -> selectedItemForDetail = item }
                        )
                    }
                    Screen.Excursions.route -> {
                        CatalogScreen(
                            items = items,
                            initialCategory = "excursion",
                            onItemSelected = { item -> selectedItemForDetail = item },
                            onBookItem = { item -> selectedItemForBooking = item }
                        )
                    }
                    Screen.Accommodations.route -> {
                        CatalogScreen(
                            items = items,
                            initialCategory = "accommodation",
                            onItemSelected = { item -> selectedItemForDetail = item },
                            onBookItem = { item -> selectedItemForBooking = item }
                        )
                    }
                    Screen.Services.route -> {
                        CatalogScreen(
                            items = items,
                            initialCategory = "service",
                            onItemSelected = { item -> selectedItemForDetail = item },
                            onBookItem = { item -> selectedItemForBooking = item }
                        )
                    }
                    Screen.Bookings.route -> {
                        BookingsScreen(
                            bookings = bookings,
                            onDeleteBooking = { id ->
                                viewModel.deleteBooking(id)
                                snackbarMessage = "Réservation annulée"
                            }
                        )
                    }
                    Screen.AiGuide.route -> {
                        AiGuideScreen(
                            onAskAi = { prompt -> viewModel.askAiAssistant(prompt) }
                        )
                    }
                }
            }

            // Booking Dialog Modal
            if (selectedItemForBooking != null) {
                val itemToBook = selectedItemForBooking!!
                BookingDialog(
                    item = itemToBook,
                    onDismiss = { selectedItemForBooking = null },
                    onConfirm = { date, guests, name, phone ->
                        viewModel.createBooking(
                            itemId = itemToBook.id,
                            itemTitle = itemToBook.title,
                            category = itemToBook.category,
                            date = date,
                            guests = guests,
                            customerName = name,
                            customerPhone = phone,
                            onSuccess = {
                                selectedItemForBooking = null
                                snackbarMessage = "Réservation confirmée avec succès !"
                                currentRoute = Screen.Bookings.route
                            }
                        )
                    }
                )
            }
        }
    }
}
