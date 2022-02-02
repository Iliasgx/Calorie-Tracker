package com.iliasg.tracker_domain.use_case

import com.iliasg.tracker_domain.model.TrackedFood
import com.iliasg.tracker_domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val repository: TrackerRepository
) {

    suspend operator fun invoke(trackedFood: TrackedFood) {
        repository.deleteTrackedFood(trackedFood)
    }
}