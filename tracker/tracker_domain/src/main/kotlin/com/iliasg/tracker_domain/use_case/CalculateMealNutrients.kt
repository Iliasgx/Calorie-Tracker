package com.iliasg.tracker_domain.use_case

import com.iliasg.core.domain.models.ActivityLevel
import com.iliasg.core.domain.models.Gender
import com.iliasg.core.domain.models.GoalType
import com.iliasg.core.domain.models.UserInfo
import com.iliasg.core.domain.preferences.Preferences
import com.iliasg.tracker_domain.model.MealType
import com.iliasg.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrients(
    private val preferences: Preferences
) {

    operator fun invoke(trackedFoods: List<TrackedFood>): Result {
        val allNutrients = trackedFoods
            .groupBy { it.mealType }
            .mapValues { (type, foods) ->
                MealNutrients(
                    carbs = foods.sumOf { it.carbs },
                    protein = foods.sumOf { it.protein },
                    fat = foods.sumOf { it.fat },
                    calories = foods.sumOf { it.calories },
                    mealType = type
                )
            }

        val userInfo = preferences.loadUserInfo()
        val calorieGoal = dailyCalorieRequirement(userInfo)

        return Result(
            carbsGoal = (calorieGoal * userInfo.carbRatio / 4f).roundToInt(),
            proteinGoal = (calorieGoal * userInfo.proteinRatio / 4f).roundToInt(),
            fatGoal = (calorieGoal * userInfo.fatRatio / 9f).roundToInt(),
            caloriesGoal = calorieGoal,
            totalCarbs = allNutrients.values.sumOf { it.carbs },
            totalProtein = allNutrients.values.sumOf { it.protein },
            totalFat = allNutrients.values.sumOf { it.fat },
            totalCalories = allNutrients.values.sumOf { it.calories },
            mealNutrients = allNutrients
        )
    }

    /**
     * Calculate the basal metabolic rate (BMR) which represent the daily required energy
     * without movement.
     */
    private fun bmr(userInfo: UserInfo): Int {
        return when(userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }
            is Gender.Female ->  {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }

    /**
     * Calculate the daily calorie requirements based on the BMR and average movement
     * based on the users activity level and includes additional calories for the requested
     * user goal.
     */
    private fun dailyCalorieRequirement(userInfo: UserInfo): Int {
        val activityFactor = when(userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }
        val calorieExtra = when(userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userInfo) * activityFactor + calorieExtra).roundToInt()
    }

    data class MealNutrients(
        val carbs: Int,
        val protein: Int,
        val fat: Int,
        val calories: Int,
        val mealType: MealType
    )

    data class Result(
        val carbsGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val caloriesGoal: Int,
        val totalCarbs: Int,
        val totalProtein: Int,
        val totalFat: Int,
        val totalCalories: Int,
        val mealNutrients: Map<MealType, MealNutrients>
    )
}