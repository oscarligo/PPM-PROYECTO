package com.example.ppm_proyecto.presentation.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import com.example.ppm_proyecto.presentation.theme.StatusAbsentRedText
import com.example.ppm_proyecto.presentation.theme.StatusPresentGreenText
import com.example.ppm_proyecto.presentation.theme.StatusWarningYellowText

@Composable
fun PieChart(
    absence: Int,
    presence: Int,
    late: Int,
    modifier: Modifier = Modifier,
    thickness: Dp = 24.dp,
    centerText: String? = null,
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            val presenceColor = StatusPresentGreenText
            val absenceColor = MaterialTheme.colorScheme.surfaceVariant

            Canvas(modifier = Modifier.size(140.dp)) {
                val stroke = Stroke(width = thickness.toPx())
                val inset = thickness.toPx() / 2f
                val arcSize = Size(
                    this.size.minDimension - thickness.toPx(),
                    this.size.minDimension - thickness.toPx()
                )

                val total = (absence + presence + late).coerceAtLeast(0)

                if (total <= 0) {
                    // Draw a neutral ring when there's no data
                    drawArc(
                        color = absenceColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(inset, inset),
                        size = arcSize,
                        style = stroke
                    )
                } else {
                    val presenceSweep = 360f * (presence.toFloat() / total.toFloat())
                    val absenceSweep = 360f - presenceSweep

                    var startAngle = -90f

                    // Draw presence first in primary color
                    if (presenceSweep > 0f) {
                        drawArc(
                            color = presenceColor,
                            startAngle = startAngle,
                            sweepAngle = presenceSweep,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = stroke
                        )
                        startAngle += presenceSweep
                    }

                    // Draw absence in a neutral/secondary color
                    if (absenceSweep > 0f) {
                        drawArc(
                            color = absenceColor,
                            startAngle = startAngle,
                            sweepAngle = absenceSweep,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = arcSize,
                            style = stroke
                        )
                    }
                }
            }

            if (centerText != null) {
                Text(
                    text = centerText,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

    }
}

@Composable
fun BarChart(
    items: List<Pair<String, Float>>, // Elemento 1: etiqueta, Elemento 2: valor
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    maxValue: Float? = null,
) {
    val maxV = maxValue ?: items.maxOfOrNull { it.second } ?: 1f
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (label, value) ->
            val ratio = if (maxV > 0f) (value / maxV).coerceIn(0f, 1f) else 0f

            // Map label to color: Presentes (green), Ausencias (red), Tardes (yellow)
            val normalized = label.trim().lowercase()
            val barColorThis = when {
                normalized.startsWith("pres") -> StatusPresentGreenText
                normalized.startsWith("ausen") || normalized.startsWith("absen") -> StatusAbsentRedText
                normalized.startsWith("tard") -> StatusWarningYellowText
                else -> barColor
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.width(96.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(22.dp)
                        .background(trackColor, RoundedCornerShape(12.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(ratio)
                            .background(barColorThis, RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}
