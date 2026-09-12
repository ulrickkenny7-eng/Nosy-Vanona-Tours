package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val itemTitle: String,
    val category: String,
    val date: String,
    val guests: Int,
    val customerName: String,
    val customerPhone: String,
    val status: String = "Confirmée",
    val timestamp: Long = System.currentTimeMillis()
)
