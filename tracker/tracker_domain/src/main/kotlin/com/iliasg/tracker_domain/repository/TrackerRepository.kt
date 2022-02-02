package com.iliasg.tracker_domain.repository

import com.iliasg.tracker_domain.model.TrackableFood
import com.iliasg.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrackerRepository {

    suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>>

    fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>>

    suspend fun insertTrackedFood(food: TrackedFood)

    suspend fun deleteTrackedFood(food: TrackedFood)
}