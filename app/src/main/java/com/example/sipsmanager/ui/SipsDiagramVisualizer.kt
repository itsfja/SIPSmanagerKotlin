package com.example.sipsmanager.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sipsmanager.types.PlantType

@Composable
fun SipsDiagramVisualizer(
    plant: PlantType,
    modifier: Modifier = Modifier
) {
    val layout = plant.earthboxLayout

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9FB), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE4E4E7), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Planter Top-Down Layout Diagram",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF27272A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF271B12), RoundedCornerShape(8.dp)) // Deep rich dark soil color
                .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                val w = size.width
                val h = size.height

                // Draw Water Tube (usually in top left)
                drawCircle(
                    color = Color(0xFF0284C7),
                    radius = 16f,
                    center = Offset(24f, 24f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = Offset(24f, 24f),
                    style = Stroke(width = 4f)
                )

                // Draw Fertilizer Band
                // "center" | "side" | "double-side"
                when (layout.fertilizerBand) {
                    "center" -> {
                        // Horizontal line in the middle
                        drawRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(0f, h / 2f - 6f),
                            size = Size(w, 12f)
                        )
                    }
                    "side" -> {
                        // Horizontal line near top edge (opposite tube, so let's put it near bottom edge)
                        drawRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(0f, h - 24f),
                            size = Size(w, 12f)
                        )
                    }
                    "double-side" -> {
                        // Two bands near both top and bottom edges
                        drawRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(0f, 20f),
                            size = Size(w, 12f)
                        )
                        drawRect(
                            color = Color(0xFFD97706),
                            topLeft = Offset(0f, h - 24f),
                            size = Size(w, 12f)
                        )
                    }
                }

                // Draw Planting Spots
                layout.diagramPoints.forEach { pt ->
                    // Map x (0-100) and y (0-100) to pixel offset
                    val px = (pt.x / 100f) * w
                    val py = (pt.y / 100f) * h

                    // Outer glowing target circle
                    drawCircle(
                        color = Color(0x3322C55E),
                        radius = 28f,
                        center = Offset(px, py)
                    )

                    // Solid seedling green point
                    drawCircle(
                        color = Color(0xFF22C55E),
                        radius = 14f,
                        center = Offset(px, py)
                    )

                    // Draw inner leaf vein star lines
                    drawLine(
                        color = Color.White,
                        start = Offset(px, py - 10f),
                        end = Offset(px, py + 10f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(px - 10f, py),
                        end = Offset(px + 10f, py),
                        strokeWidth = 3f
                    )
                }
            }
        }

        // Legend / Key Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF0284C7), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Water Tube", fontSize = 11.sp, color = Color(0xFF71717A))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFFD97706), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fertilizer Band (${layout.fertilizerBand})", fontSize = 11.sp, color = Color(0xFF71717A))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF22C55E), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Seedling (${layout.totalPlants} total)", fontSize = 11.sp, color = Color(0xFF71717A))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = layout.instructions,
            fontSize = 11.sp,
            color = Color(0xFF52525B),
            lineHeight = 15.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
        )
    }
}
