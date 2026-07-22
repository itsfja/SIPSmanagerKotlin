package com.example.sipsmanager.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sipsmanager.data.DiagnosticIssue
import com.example.sipsmanager.data.PestCatalog
import com.example.sipsmanager.data.PlantCatalog

@Composable
fun PestDiagnosticsModule(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCropFilter by remember { mutableStateOf("All Crops") }
    var expandedIssueId by remember { mutableStateOf<String?>(null) }

    val cropsList = remember {
        listOf("All Crops") + PlantCatalog.EXHAUSTIVE_PLANT_CATALOG.map { it.name }
    }

    val filteredIssues = remember(searchQuery, selectedCropFilter) {
        PestCatalog.EXHAUSTIVE_PEST_DISEASE_CATALOG.filter { issue ->
            val matchesSearch = issue.name.contains(searchQuery, ignoreCase = true) ||
                    issue.description.contains(searchQuery, ignoreCase = true) ||
                    issue.symptoms.any { it.contains(searchQuery, ignoreCase = true) }

            val cropIdFromFilter = PlantCatalog.EXHAUSTIVE_PLANT_CATALOG
                .find { it.name == selectedCropFilter }?.id

            val matchesCrop = selectedCropFilter == "All Crops" ||
                    (cropIdFromFilter != null && issue.targetCrops.contains(cropIdFromFilter))

            matchesSearch && matchesCrop
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Pest Diagnostics",
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Pest & Disease Diagnostic Assistant",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B)
                    )
                    Text(
                        text = "Identify symptoms, environmental stresses, and organic remedies",
                        fontSize = 11.sp,
                        color = Color(0xFF71717A)
                    )
                }
            }

            // Search and Filter Controls
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search symptoms (e.g. holes, rot, spots)", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF71717A), modifier = Modifier.size(18.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFDC2626),
                    unfocusedBorderColor = Color(0xFFE4E4E7)
                ),
                singleLine = true
            )

            // Crop Horizontal Selection Scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF52525B),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    ScrollableChipRow(
                        chips = cropsList,
                        selectedChip = selectedCropFilter,
                        onChipSelected = { selectedCropFilter = it }
                    )
                }
            }

            // Issues list
            if (filteredIssues.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No matching pests or diseases found.",
                        fontSize = 13.sp,
                        color = Color(0xFF71717A),
                        fontStyle = FontStyle.Italic
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    filteredIssues.forEach { issue ->
                        DiagnosticIssueItem(
                            issue = issue,
                            isExpanded = expandedIssueId == issue.id,
                            onToggleExpand = {
                                expandedIssueId = if (expandedIssueId == issue.id) null else issue.id
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScrollableChipRow(
    chips: List<String>,
    selectedChip: String,
    onChipSelected: (String) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(end = 12.dp)
    ) {
        items(chips) { chip ->
            val isSelected = chip == selectedChip
            Surface(
                modifier = Modifier.clickable { onChipSelected(chip) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) Color(0xFFFEF2F2) else Color(0xFFF4F4F5),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)) else null
            ) {
                Text(
                    text = chip,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Color(0xFF991B1B) else Color(0xFF52525B),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun DiagnosticIssueItem(
    issue: DiagnosticIssue,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
) {
    val severityColor = when (issue.severity) {
        "Critical" -> Color(0xFF991B1B)
        "High" -> Color(0xFFC2410C)
        "Medium" -> Color(0xFFB45309)
        else -> Color(0xFF15803D)
    }

    val severityBg = when (issue.severity) {
        "Critical" -> Color(0xFFFEE2E2)
        "High" -> Color(0xFFFFEDD5)
        "Medium" -> Color(0xFFFEF3C7)
        else -> Color(0xFFDCFCE7)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E4E7))
    ) {
        Column(modifier = Modifier.clickable { onToggleExpand() }.padding(12.dp)) {
            // Summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = issue.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF27272A)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = severityBg
                        ) {
                            Text(
                                text = issue.severity,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = severityColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = issue.category.replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        color = Color(0xFF71717A)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand details",
                    tint = Color(0xFF71717A)
                )
            }

            // Expanded detail section
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Divider(color = Color(0xFFE4E4E7), modifier = Modifier.padding(bottom = 10.dp))

                    Text(
                        text = issue.description,
                        fontSize = 12.sp,
                        color = Color(0xFF3F3F46),
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    // Symptoms
                    Text(
                        text = "Core Symptoms:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF18181B)
                    )
                    issue.symptoms.forEach { symptom ->
                        Row(modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                            Text("• ", fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                            Text(symptom, fontSize = 12.sp, color = Color(0xFF52525B))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Organic treatments
                    Text(
                        text = "Organic & Natural Remedies:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                    issue.organicTreatment.forEach { treatment ->
                        Row(modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                            Text("✓ ", fontSize = 12.sp, color = Color(0xFF22C55E), fontWeight = FontWeight.Bold)
                            Text(treatment, fontSize = 12.sp, color = Color(0xFF52525B))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Prevention
                    Text(
                        text = "Preventative Setup Guidelines:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8)
                    )
                    issue.prevention.forEach { prev ->
                        Row(modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                            Text("★ ", fontSize = 12.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                            Text(prev, fontSize = 12.sp, color = Color(0xFF52525B))
                        }
                    }
                }
            }
        }
    }
}
