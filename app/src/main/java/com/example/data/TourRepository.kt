package com.example.data

import kotlinx.coroutines.flow.Flow

class TourRepository(private val tourDao: TourDao) {
    val allItems: Flow<List<TourItemEntity>> = tourDao.getAllItems()
    val featuredItems: Flow<List<TourItemEntity>> = tourDao.getFeaturedItems()
    val allBookings: Flow<List<BookingEntity>> = tourDao.getAllBookings()

    fun getItemsByCategory(category: String): Flow<List<TourItemEntity>> {
        return tourDao.getItemsByCategory(category)
    }

    suspend fun getItemById(id: Long): TourItemEntity? {
        return tourDao.getItemById(id)
    }

    suspend fun insertBooking(booking: BookingEntity) {
        tourDao.insertBooking(booking)
    }

    suspend fun deleteBooking(id: Long) {
        tourDao.deleteBooking(id)
    }
}
