package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tour_items")
data class TourItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "excursion", "accommodation", "service"
    val shortDescription: String,
    val fullDescription: String,
    val durationOrType: String,
    val location: String,
    val rating: Float,
    val imageUrl: String,
    val isFeatured: Boolean = false
)
