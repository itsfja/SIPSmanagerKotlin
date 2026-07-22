package com.example.sipsmanager.types

data class UserProfile(
    val username: String,
    val postcode: String,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val lastFrostDate: String? = null,
    val hasBiometrics: Boolean = false,
    val avatarSeed: String
)

data class GeocodeResult(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val admin1: String? = null,
    val country: String,
    val postcode: String? = null
)

data class GrowthStage(
    val dayRangeMin: Int,
    val dayRangeMax: Int,
    val name: String,
    val description: String,
    val careTips: String,
    val illustrationSeed: String,
    val heightInches: Int
)

data class DiagramPoint(val x: Float, val y: Float)

data class PlantLayout(
    val rows: Int,
    val plantsPerRow: Int,
    val totalPlants: Int,
    val fertilizerBand: String, // "center" | "side" | "double-side"
    val diagramPoints: List<DiagramPoint>,
    val instructions: String
)

data class PlantType(
    val id: String,
    val name: String,
    val scientificName: String,
    val category: String, // "Vegetable" | "Herb" | "Fruit"
    val description: String,
    val climbingFrameRequired: Boolean,
    val climbingFrameType: String? = null,
    val shadeRequirement: String, // "Full Sun" | "Partial Shade" ...
    val waterConsumption: String, // "Low" | "Moderate" ...
    val waterGallonsPerWeekLocal: Double,
    val daysToHarvest: Int,
    val daysToSprout: Int,
    val recommendedPlantingSeason: String,
    val earthboxLayout: PlantLayout,
    val stages: List<GrowthStage>
)

data class PlanterBox(
    val id: String,
    val name: String,
    val createdAt: Long,
    val plantId: String,
    val plantCount: Int,
    val plantingDate: Long,
    var waterReservoirLevel: Int, // 0 to 100
    var soilMoisture: Int, // 0 to 100
    val notes: MutableList<String> = mutableListOf(),
    var refilledAt: Long? = null,
    val planterType: String = "earthbox",
    val growBagType: String? = null
)

data class ForecastDay(
    val day: String,
    val temp: Int,
    val state: String
)

data class NotificationAlert(
    val id: String,
    val type: String, // "freeze" | "storm" | "water" | "heat" | "sys"
    val title: String,
    val message: String,
    val timestamp: Long,
    var read: Boolean = false
)

data class WeatherData(
    val temp: Int,
    val state: String, // "Sunny" | "Rainy" | "Cloudy" | "Freezing" | "Windy" | "Stormy" | "Calm"
    val humidity: Int,
    val windSpeed: Int,
    val forecast: List<ForecastDay>,
    val alerts: List<NotificationAlert>
)
