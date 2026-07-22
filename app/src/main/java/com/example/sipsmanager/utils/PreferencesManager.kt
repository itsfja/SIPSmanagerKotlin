package com.example.sipsmanager.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sipsmanager.types.PlanterBox
import com.example.sipsmanager.types.UserProfile
import com.example.sipsmanager.types.NotificationAlert
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("SipsPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveActiveProfile(profile: UserProfile?) {
        if (profile == null) {
            prefs.edit().remove("active_profile").apply()
        } else {
            prefs.edit().putString("active_profile", gson.toJson(profile)).apply()
        }
    }

    fun getActiveProfile(): UserProfile? {
        val json = prefs.getString("active_profile", null) ?: return null
        return gson.fromJson(json, UserProfile::class.java)
    }

    fun saveProfiles(profiles: List<UserProfile>) {
        prefs.edit().putString("all_profiles", gson.toJson(profiles)).apply()
    }

    fun getProfiles(): List<UserProfile> {
        val json = prefs.getString("all_profiles", null) ?: return emptyList()
        val type = object : TypeToken<List<UserProfile>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun savePlanters(username: String, planters: List<PlanterBox>) {
        prefs.edit().putString("planters_$username", gson.toJson(planters)).apply()
    }

    fun getPlanters(username: String): List<PlanterBox> {
        val json = prefs.getString("planters_$username", null)
        if (json == null) {
            // Seed 2 default planters for new users
            val defaultPlanters = listOf(
                PlanterBox(
                    id = "tomato_box_1",
                    name = "Patio Earthbox 1",
                    createdAt = System.currentTimeMillis() - 25 * 24 * 60 * 60 * 1000L, // 25 days ago
                    plantId = "cherry_tomato",
                    plantCount = 2,
                    plantingDate = System.currentTimeMillis() - 25 * 24 * 60 * 60 * 1000L,
                    waterReservoirLevel = 45,
                    soilMoisture = 65,
                    notes = mutableListOf("Planted cherry tomato seedlings with standard cage support.", "Added 2 cups of dolomite.", "Sprouted nicely on day 7!"),
                    planterType = "earthbox"
                ),
                PlanterBox(
                    id = "basil_box_1",
                    name = "Windowsill Box",
                    createdAt = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L, // 5 days ago
                    plantId = "sweet_basil",
                    plantCount = 6,
                    plantingDate = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000L,
                    waterReservoirLevel = 80,
                    soilMoisture = 75,
                    notes = mutableListOf("Seated 6 sweet basil starts.", "Placed central fertilizer band."),
                    planterType = "ourwarm_windowsill"
                )
            )
            savePlanters(username, defaultPlanters)
            return defaultPlanters
        }
        val type = object : TypeToken<List<PlanterBox>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun deleteUserData(username: String) {
        prefs.edit()
            .remove("planters_$username")
            .remove("alerts_$username")
            .apply()
    }

    fun saveAlerts(username: String, alerts: List<NotificationAlert>) {
        prefs.edit().putString("alerts_$username", gson.toJson(alerts)).apply()
    }

    fun getAlerts(username: String): List<NotificationAlert> {
        val json = prefs.getString("alerts_$username", null) ?: return emptyList()
        val type = object : TypeToken<List<NotificationAlert>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
