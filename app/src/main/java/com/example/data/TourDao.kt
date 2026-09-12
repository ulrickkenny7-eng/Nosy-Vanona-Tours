package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TourDao {
    @Query("SELECT * FROM tour_items")
    fun getAllItems(): Flow<List<TourItemEntity>>

    @Query("SELECT * FROM tour_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<TourItemEntity>>

    @Query("SELECT * FROM tour_items WHERE isFeatured = 1")
    fun getFeaturedItems(): Flow<List<TourItemEntity>>

    @Query("SELECT * FROM tour_items WHERE id = :id")
    suspend fun getItemById(id: Long): TourItemEntity?

    @Query("SELECT COUNT(*) FROM tour_items")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TourItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TourItemEntity>)

    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: Long)
}
