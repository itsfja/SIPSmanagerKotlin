package com.example.sipsmanager.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sipsmanager.data.PlantCatalog
import com.example.sipsmanager.types.*
import com.example.sipsmanager.utils.PreferencesManager
import com.example.sipsmanager.utils.WeatherUtils
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    user: UserProfile,
    prefsManager: PreferencesManager,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // State Variables
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    var isWeatherLoading by remember { mutableStateOf(true) }
    var plantersList by remember { mutableStateOf(prefsManager.getPlanters(user.username)) }
    var alertsList by remember { mutableStateOf(prefsManager.getAlerts(user.username)) }
    
    // UI control states
    var isAddPlanterOpen by remember { mutableStateOf(false) }
    var isAlertsOpen by remember { mutableStateOf(false) }
    var expandedPlanterId by remember { mutableStateOf<String?>(null) }

    // New Planter Form State
    var newPlanterName by remember { mutableStateOf("") }
    var selectedPlantId by remember { mutableStateOf(PlantCatalog.EXHAUSTIVE_PLANT_CATALOG[0].id) }
    var selectedPlanterType by remember { mutableStateOf("earthbox") }

    // Fetch Weather on launch
    LaunchedEffect(user.postcode) {
        isWeatherLoading = true
        coroutineScope.launch {
            try {
                val geocode = WeatherUtils.geocodePostcode(user.postcode)
                val weather = WeatherUtils.fetchLocalWeather(geocode.latitude, geocode.longitude)
                weatherData = weather

                // Populate user-specific alerts
                val currentAlerts = alertsList.toMutableList()
                weather.alerts.forEach { alert ->
                    if (currentAlerts.none { it.id == alert.id }) {
                        currentAlerts.add(alert)
                    }
                }
                alertsList = currentAlerts
                prefsManager.saveAlerts(user.username, currentAlerts)
            } catch (e: Exception) {
                weatherData = WeatherUtils.getFallbackWeather()
            } finally {
                isWeatherLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(user.avatarSeed, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "${user.username}'s Garden",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F),
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "${user.city ?: "Saint Helens"}, ${user.country ?: "UK"}",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                },
                actions = {
                    // Alert Icon with count badge
                    val unreadAlertsCount = alertsList.count { !it.read }
                    IconButton(onClick = { isAlertsOpen = !isAlertsOpen }) {
                        BadgedBox(
                            badge = {
                                if (unreadAlertsCount > 0) {
                                    Badge(containerColor = Color(0xFFEF4444)) {
                                        Text(unreadAlertsCount.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = Color(0xFF78350F))
                        }
                    }

                    // Logout Icon
                    IconButton(onClick = {
                        prefsManager.saveActiveProfile(null)
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color(0xFF78350F))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFCF9F2))
            )
        },
        containerColor = Color(0xFFF9F6EE) // Vintage garden parchment cream
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notifications Area drawer-like dropdown
            AnimatedVisibility(visible = isAlertsOpen) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Active SIPS System Alerts", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF991B1B))
                            TextButton(onClick = {
                                val updated = alertsList.map { it.copy(read = true) }
                                alertsList = updated
                                prefsManager.saveAlerts(user.username, updated)
                            }) {
                                Text("Mark all read", fontSize = 11.sp, color = Color(0xFFDC2626))
                            }
                        }

                        if (alertsList.isEmpty()) {
                            Text("No alerts found.", fontSize = 12.sp, color = Color(0xFF71717A))
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                alertsList.forEach { alert ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (alert.read) Color(0x33FCA5A5) else Color(0x66FCA5A5),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = when (alert.type) {
                                                "freeze" -> Icons.Default.AcUnit
                                                "storm" -> Icons.Default.Storm
                                                "water" -> Icons.Default.WaterDrop
                                                else -> Icons.Default.Info
                                            },
                                            contentDescription = "Alert type",
                                            tint = Color(0xFF991B1B),
                                            modifier = Modifier.size(18.dp).padding(top = 2.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(alert.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7F1D1D))
                                            Text(alert.message, fontSize = 11.sp, color = Color(0xFF991B1B), lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Weather Widget section
            if (isWeatherLoading) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFB45309))
                    }
                }
            } else {
                weatherData?.let { weather ->
                    WeatherCard(weather = weather, lastFrostDate = user.lastFrostDate)
                }
            }

            // Quick stats & header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Tended Planters (${plantersList.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF18181B),
                    fontFamily = FontFamily.Serif
                )

                Button(
                    onClick = { isAddPlanterOpen = !isAddPlanterOpen },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Box", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Planter", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }

            // Add Planter Form
            AnimatedVisibility(visible = isAddPlanterOpen) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E4E7))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Add New Planter Box / Grow Bag", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF18181B))

                        OutlinedTextField(
                            value = newPlanterName,
                            onValueChange = { newPlanterName = it },
                            label = { Text("Planter Label", fontSize = 13.sp) },
                            placeholder = { Text("e.g. Tomato Box A, Patio Herb Tub", fontSize = 13.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF16A34A),
                                unfocusedBorderColor = Color(0xFFE4E4E7)
                            )
                        )

                        // Select Plant variety dropdown simulation using simple row of chip options
                        Text("Choose Your Crop Variety:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF52525B))
                        ScrollableChipRow(
                            chips = PlantCatalog.EXHAUSTIVE_PLANT_CATALOG.map { it.name },
                            selectedChip = PlantCatalog.getPlantById(selectedPlantId)?.name ?: "",
                            onChipSelected = { name ->
                                selectedPlantId = PlantCatalog.EXHAUSTIVE_PLANT_CATALOG.find { it.name == name }?.id ?: selectedPlantId
                            }
                        )

                        // Select Planter Type
                        Text("Choose Box / Containment Vessel:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF52525B))
                        val vesselTypes = listOf("earthbox" to "Earthbox Standard", "hozelock_growbag" to "Hozelock Growbag", "emsco_citypicker" to "Emsco Citypicker", "ourwarm_windowsill" to "OurWarm Windowsill", "earthbox_junior" to "Earthbox Junior")
                        ScrollableChipRow(
                            chips = vesselTypes.map { it.second },
                            selectedChip = vesselTypes.find { it.first == selectedPlanterType }?.second ?: "",
                            onChipSelected = { name ->
                                selectedPlanterType = vesselTypes.find { it.second == name }?.first ?: selectedPlanterType
                            }
                        )

                        Button(
                            onClick = {
                                if (newPlanterName.isBlank()) return@Button
                                val selectedPlant = PlantCatalog.getPlantById(selectedPlantId) ?: return@Button
                                val newBox = PlanterBox(
                                    id = UUID.randomUUID().toString(),
                                    name = newPlanterName.trim(),
                                    createdAt = System.currentTimeMillis(),
                                    plantId = selectedPlantId,
                                    plantCount = selectedPlant.earthboxLayout.totalPlants,
                                    plantingDate = System.currentTimeMillis(),
                                    waterReservoirLevel = 100,
                                    soilMoisture = 85,
                                    notes = mutableListOf("Successfully planted ${selectedPlant.name} seedlings in ${newPlanterName.trim()}!"),
                                    planterType = selectedPlanterType
                                )

                                val updatedList = plantersList + newBox
                                plantersList = updatedList
                                prefsManager.savePlanters(user.username, updatedList)

                                // Reset form
                                newPlanterName = ""
                                isAddPlanterOpen = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                        ) {
                            Text("Plant & Register SIPS Box", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Planters List
            if (plantersList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You don't have any planters registered yet. Tap 'Add Planter' above to start your first SIPS Box!",
                        fontSize = 13.sp,
                        color = Color(0xFF71717A),
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                plantersList.forEach { planter ->
                    val plant = PlantCatalog.getPlantById(planter.plantId)
                    if (plant != null) {
                        PlanterBoxCard(
                            planter = planter,
                            plant = plant,
                            isExpanded = expandedPlanterId == planter.id,
                            onToggleExpand = {
                                expandedPlanterId = if (expandedPlanterId == planter.id) null else planter.id
                            },
                            onWaterRefilled = {
                                planter.waterReservoirLevel = 100
                                planter.soilMoisture = 90
                                planter.refilledAt = System.currentTimeMillis()
                                planter.notes.add(0, "Reservoir topped off fully.")
                                prefsManager.savePlanters(user.username, plantersList)
                                plantersList = plantersList.toList() // Trigger state recompose
                            },
                            onAddNote = { text ->
                                planter.notes.add(0, text)
                                prefsManager.savePlanters(user.username, plantersList)
                                plantersList = plantersList.toList()
                            },
                            onSimulateTime = {
                                // Shift planting date 10 days back to simulate progression
                                planter.waterReservoirLevel = Math.max(0, planter.waterReservoirLevel - 20)
                                planter.soilMoisture = Math.max(20, planter.soilMoisture - 15)
                                val updatedDate = planter.plantingDate - 10 * 24 * 60 * 60 * 1000L
                                val copiedBox = planter.copy(
                                    plantingDate = updatedDate,
                                    waterReservoirLevel = planter.waterReservoirLevel,
                                    soilMoisture = planter.soilMoisture
                                )
                                copiedBox.notes.addAll(planter.notes)
                                copiedBox.notes.add(0, "Simulated 10 days of fast growing weather.")
                                val updatedList = plantersList.map { if (it.id == planter.id) copiedBox else it }
                                plantersList = updatedList
                                prefsManager.savePlanters(user.username, updatedList)
                            },
                            onDelete = {
                                val updatedList = plantersList.filter { it.id != planter.id }
                                plantersList = updatedList
                                prefsManager.savePlanters(user.username, updatedList)
                            }
                        )
                    }
                }
            }

            // Diagnostics module attached cleanly at bottom
            Spacer(modifier = Modifier.height(12.dp))
            PestDiagnosticsModule()
        }
    }
}

@Composable
fun WeatherCard(
    weather: WeatherData,
    lastFrostDate: String?,
    modifier: Modifier = Modifier
) {
    val isFrostRisk = lastFrostDate?.contains("Frosts predicted") == true

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Main Weather Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Today's Weather", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF71717A))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${weather.temp}°F",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF18181B)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = when (weather.state) {
                                "Sunny" -> Icons.Default.WbSunny
                                "Cloudy" -> Icons.Default.Cloud
                                "Rainy" -> Icons.Default.WaterDrop
                                "Freezing" -> Icons.Default.AcUnit
                                else -> Icons.Default.WbSunny
                            },
                            contentDescription = weather.state,
                            tint = when (weather.state) {
                                "Sunny" -> Color(0xFFF59E0B)
                                "Cloudy" -> Color(0xFF94A3B8)
                                "Rainy" -> Color(0xFF3B82F6)
                                "Freezing" -> Color(0xFF38BDF8)
                                else -> Color(0xFFF59E0B)
                            },
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(weather.state, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF27272A))
                    Text("Humidity: ${weather.humidity}%", fontSize = 11.sp, color = Color(0xFF71717A))
                    Text("Wind: ${weather.windSpeed} km/h", fontSize = 11.sp, color = Color(0xFF71717A))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Frost Risk Banner with Ladybird aesthetic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isFrostRisk) Color(0xFFFEF2F2) else Color(0xFFF0FDF4),
                        RoundedCornerShape(8.dp)
                    )
                    .border(
                        1.dp,
                        if (isFrostRisk) Color(0xFFFCA5A5) else Color(0xFFBBF7D0),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = if (isFrostRisk) Icons.Default.AcUnit else Icons.Default.CheckCircle,
                        contentDescription = "Frost status",
                        tint = if (isFrostRisk) Color(0xFFDC2626) else Color(0xFF16A34A),
                        modifier = Modifier.size(16.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = lastFrostDate ?: "Last frost risk passed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFrostRisk) Color(0xFF991B1B) else Color(0xFF166534)
                        )
                        if (isFrostRisk) {
                            Text(
                                text = "Soil sub-surface models verify that seedlings shouldn't be planted out of cold frames until soil matches 60°F consistently.",
                                fontSize = 11.sp,
                                color = Color(0xFF991B1B),
                                lineHeight = 15.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4-day forecast cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weather.forecast.forEach { day ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(day.day, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Icon(
                            imageVector = when (day.state) {
                                "Sunny" -> Icons.Default.WbSunny
                                "Cloudy" -> Icons.Default.Cloud
                                "Rainy" -> Icons.Default.WaterDrop
                                "Freezing" -> Icons.Default.AcUnit
                                else -> Icons.Default.WbSunny
                            },
                            contentDescription = day.state,
                            tint = when (day.state) {
                                "Sunny" -> Color(0xFFF59E0B)
                                "Cloudy" -> Color(0xFF94A3B8)
                                "Rainy" -> Color(0xFF3B82F6)
                                "Freezing" -> Color(0xFF38BDF8)
                                else -> Color(0xFFF59E0B)
                            },
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${day.temp}°F", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                    }
                }
            }
        }
    }
}

@Composable
fun PlanterBoxCard(
    planter: PlanterBox,
    plant: PlantType,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onWaterRefilled: () -> Unit,
    onAddNote: (String) -> Unit,
    onSimulateTime: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate current age
    val daysPlanted = Math.max(0, ((System.currentTimeMillis() - planter.plantingDate) / (24 * 60 * 60 * 1000L)).toInt())

    // Determine current growth stage based on age
    val currentStage = plant.stages.find { stage ->
        daysPlanted >= stage.dayRangeMin && daysPlanted <= stage.dayRangeMax
    } ?: plant.stages.last()

    var noteText by remember { mutableStateOf("") }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E4E7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Summary Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = planter.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B)
                    )
                    Text(
                        text = "${plant.name} (${plant.scientificName})",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF71717A)
                    )
                    Text(
                        text = "Vessel: ${planter.planterType.replace("_", " ").uppercase()} • Days Planted: $daysPlanted",
                        fontSize = 11.sp,
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand details",
                    tint = Color(0xFF71717A),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Reservoir Water Level Progress
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WaterDrop, contentDescription = "Water Reservoir", tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sub-Irrigated Water Level", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF52525B))
                    }
                    Text("${planter.waterReservoirLevel}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = planter.waterReservoirLevel / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF0284C7),
                    trackColor = Color(0xFFE0F2FE)
                )

                if (planter.waterReservoirLevel < 30) {
                    Text(
                        text = "⚠️ Low Water! Refill the SIPS tube immediately.",
                        color = Color(0xFFEF4444),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Soil Moisture Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Capillary Soil Moisture (Around Roots):", fontSize = 11.sp, color = Color(0xFF71717A))
                Text(
                    text = "${planter.soilMoisture}% (${if (planter.soilMoisture > 70) "Optimal Wet" else if (planter.soilMoisture > 40) "Damp" else "Dry"})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (planter.soilMoisture > 50) Color(0xFF15803D) else Color(0xFFD97706)
                )
            }

            // Expanded detail section
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Divider(color = Color(0xFFE4E4E7))

                    // Layout Diagram
                    SipsDiagramVisualizer(plant = plant)

                    // Growth Stage Timeline
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Current Growth Phase: ${currentStage.name}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "Predicted Height: ~${currentStage.heightInches} inches",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                text = currentStage.description,
                                fontSize = 12.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Care instructions: ${currentStage.careTips}",
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF475569),
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Simulated Controls Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onWaterRefilled() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                        ) {
                            Icon(Icons.Default.WaterDrop, contentDescription = "Water", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Water Reservoir", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onSimulateTime() },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309))
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = "Simulate", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simulate +10 Days", fontSize = 11.sp)
                        }
                    }

                    // Diary Logs section
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Gardening Diary Logs", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27272A))
                        
                        // Add diary form
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = noteText,
                                onValueChange = { noteText = it },
                                placeholder = { Text("Log fertilizer, blooming, pruning notes...", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF16A34A),
                                    unfocusedBorderColor = Color(0xFFE4E4E7)
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    if (noteText.isNotBlank()) {
                                        onAddNote(noteText.trim())
                                        noteText = ""
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Text("Log", fontSize = 12.sp)
                            }
                        }

                        // Diary items list
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            planter.notes.forEach { note ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF4F4F5), RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                ) {
                                    Text("• ", fontSize = 11.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                                    Text(note, fontSize = 11.sp, color = Color(0xFF27272A), lineHeight = 15.sp)
                                }
                            }
                        }
                    }

                    // Delete planter box
                    OutlinedButton(
                        onClick = { onDelete() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete planter", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove Planter Box", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
