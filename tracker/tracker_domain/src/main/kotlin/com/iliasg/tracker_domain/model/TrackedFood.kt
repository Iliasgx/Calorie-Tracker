package com.iliasg.tracker_domain.model

import java.time.LocalDate

data class TrackedFood(
    val id: Int? = null,
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int
)