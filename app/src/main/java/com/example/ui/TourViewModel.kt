package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.BookingEntity
import com.example.data.TourItemEntity
import com.example.data.TourRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TourViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TourRepository

    init {
        val tourDao = AppDatabase.getDatabase(application).tourDao()
        repository = TourRepository(tourDao)
    }

    val allItems: StateFlow<List<TourItemEntity>> = repository.allItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val featuredItems: StateFlow<List<TourItemEntity>> = repository.featuredItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createBooking(
        itemId: Long,
        itemTitle: String,
        category: String,
        date: String,
        guests: Int,
        customerName: String,
        customerPhone: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val booking = BookingEntity(
                itemId = itemId,
                itemTitle = itemTitle,
                category = category,
                date = date,
                guests = guests,
                customerName = customerName,
                customerPhone = customerPhone,
                status = "Confirmée"
            )
            repository.insertBooking(booking)
            onSuccess()
        }
    }

    fun deleteBooking(id: Long) {
        viewModelScope.launch {
            repository.deleteBooking(id)
        }
    }

    suspend fun askAiAssistant(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("iranja") || lower.contains("tortue") ->
                "Nosy Iranja est l'excursion incontournable de Nosy Vanona Tours ! C'est l'île aux tortues reliée par un somptueux banc de sable blanc de 1,2 km (traversée en bateau rapide et déjeuner aux fruits de mer inclus)."
            lower.contains("lokobe") || lower.contains("forêt") || lower.contains("lémurien") ->
                "La Réserve Naturelle de Lokobe vous invite à une immersion au cœur de la forêt primaire de Nosy Be. En pirogue traditionnelle, découvrez les lémuriens makis, caméléons et plantes médicinales."
            lower.contains("tanikely") || lower.contains("snorkeling") || lower.contains("marin") ->
                "Nosy Tanikely est un sanctuaire marin protégé exceptionnel. Nagez au milieu des coraux et tortues marines."
            lower.contains("varona") || lower.contains("île privée") || lower.contains("ecolodge") ->
                "L'île privée 'Nosy Varona' est notre joyau d'hébergement. Vivez l'expérience Robinson Crusoé de luxe dans un écolodge pieds dans l'eau avec chef privé."
            lower.contains("belamandy") || lower.contains("bungalow") || lower.contains("ankify") ->
                "Le Belamandy Lodge à Ankify propose des bungalows de charme au bord de l'eau avec piscine à débordement."
            lower.contains("transfert") || lower.contains("aéroport") ->
                "Nous assurons les transferts VIP depuis l'aéroport de Nosy Be Fascene jusqu'à votre hôtel ou île privée avec chauffeur privé et speed-boat."
            lower.contains("bateau") || lower.contains("charter") ->
                "Vous pouvez privatiser un bateau rapide avec skipper professionnel afin de naviguer à votre rythme dans l'archipel."
            else ->
                "Bonjour ! En tant qu'expert officiel de Nosy Vanona Tours à Madagascar (Nosy Be), je suis à votre entière disposition pour vous renseigner sur nos excursions (Nosy Iranja, Lokobe, Tanikely), nos hébergements (Nosy Varona, Belamandy) et nos services de transport. Que souhaitez-vous savoir ?"
        }
    }
}
