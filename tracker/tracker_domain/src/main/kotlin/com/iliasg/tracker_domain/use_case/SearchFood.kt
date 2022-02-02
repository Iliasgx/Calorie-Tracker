package com.iliasg.tracker_domain.use_case

import com.iliasg.tracker_domain.model.TrackableFood
import com.iliasg.tracker_domain.repository.TrackerRepository

class SearchFood(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ): Result<List<TrackableFood>> {
        // If no results were found with the query, still return a success with an empty list.
        if(query.isBlank()) return Result.success(emptyList())

        return repository.searchFood(query.trim(), page, pageSize)
    }
}