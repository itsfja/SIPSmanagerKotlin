package com.example.sipsmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.sipsmanager.types.UserProfile
import com.example.sipsmanager.utils.PreferencesManager
import com.example.sipsmanager.utils.WeatherUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    prefsManager: PreferencesManager,
    onLoginSuccess: (UserProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var postcode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val coroutineScope = rememberCoroutineScope()
    var savedProfiles by remember { mutableStateOf(prefsManager.getProfiles()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF9F2)) // Soft Ladybird paper cream background
            .padding(20.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Title Section with Peter and Jane style
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .border(2.dp, Color(0xFF92400E), RoundedCornerShape(16.dp)) // Warm wooden borders
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Little red ladybird icon
                Icon(
                    imageVector = Icons.Default.Nature,
                    contentDescription = "Garden Leaf",
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SIPS Manager",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF78350F), // Rich warm brown
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Peter & Jane's Digital Garden Companion",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFB45309),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Login Form Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Step into the Garden",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27272A),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Your Name (Grower)", fontSize = 13.sp) },
                    placeholder = { Text("e.g. Peter, Jane, Mummy", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB45309),
                        unfocusedBorderColor = Color(0xFFE4E4E7)
                    )
                )

                OutlinedTextField(
                    value = postcode,
                    onValueChange = { postcode = it },
                    label = { Text("Your Postcode / Zip", fontSize = 13.sp) },
                    placeholder = { Text("e.g. WA11 0SD or 90210", fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB45309),
                        unfocusedBorderColor = Color(0xFFE4E4E7)
                    )
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = {
                        if (username.isBlank() || postcode.isBlank()) {
                            errorMessage = "Please enter both your name and postcode."
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            try {
                                val geocode = WeatherUtils.geocodePostcode(postcode)
                                val frost = WeatherUtils.estimateLastFrostDate(geocode.latitude)
                                
                                val newProfile = UserProfile(
                                    username = username.trim(),
                                    postcode = postcode.trim().uppercase(),
                                    city = geocode.name,
                                    state = geocode.admin1,
                                    country = geocode.country,
                                    lastFrostDate = frost,
                                    avatarSeed = username.trim().take(2).uppercase(),
                                    hasBiometrics = false
                                )
                                
                                // Save profile to stored list if not exists
                                val updatedList = savedProfiles.toMutableList()
                                val existingIdx = updatedList.indexOfFirst { it.username.lowercase() == newProfile.username.lowercase() }
                                if (existingIdx != -1) {
                                    updatedList[existingIdx] = newProfile
                                } else {
                                    updatedList.add(newProfile)
                                }
                                prefsManager.saveProfiles(updatedList)
                                prefsManager.saveActiveProfile(newProfile)
                                onLoginSuccess(newProfile)
                            } catch (e: Exception) {
                                errorMessage = "Unable to verify postcode. Please check your connection."
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB45309)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                    } else {
                        Text("Proceed to Garden Dashboard", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Recent Growers Section
        if (savedProfiles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Recent Growers on This Device",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF52525B),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(savedProfiles) { profile ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFAFAFA))
                                    .border(1.dp, Color(0xFFF4F4F5), RoundedCornerShape(8.dp))
                                    .clickable {
                                        prefsManager.saveActiveProfile(profile)
                                        onLoginSuccess(profile)
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Custom avatar circular badge with seed
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFEF3C7)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = profile.avatarSeed,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD97706)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = profile.username,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF18181B)
                                        )
                                        Text(
                                            text = "${profile.postcode} • ${profile.city ?: "United Kingdom"}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF71717A)
                                        )
                                    }
                                }

                                // Delete Button
                                IconButton(
                                    onClick = {
                                        val updatedList = savedProfiles.filter { it.username != profile.username }
                                        prefsManager.saveProfiles(updatedList)
                                        prefsManager.deleteUserData(profile.username)
                                        savedProfiles = updatedList
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete profile",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
