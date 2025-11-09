package com.example.ppm_proyecto.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ppm_proyecto.presentation.components.charts.BarChart
import com.example.ppm_proyecto.presentation.components.charts.PieChart
import androidx.compose.material3.CardDefaults

@Composable
fun StatisticsCard(
    presentCount: Int,
    absentCount: Int,
    lateCount: Int,
    attendancePercent: Int,
    title: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título de la tarjeta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val centerLabel = "$attendancePercent%"

            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Gráfico circular (izquierda)
                    PieChart(
                        absence = absentCount,
                        presence = presentCount,
                        late = lateCount,
                        centerText = centerLabel,
                        modifier = Modifier.weight(1f)
                    )

                    // Gráfico de barras (derecha)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(35.dp))
                        BarChart(
                            presentCount = presentCount,
                            absentCount = absentCount,
                            lateCount = lateCount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable

fun StatisticsCardPreview() {
    StatisticsCard(
        presentCount = 15,
        absentCount = 8,
        lateCount = 3,
        title = "Estadísticas de asistencia",
        attendancePercent = 65,

    )

}
