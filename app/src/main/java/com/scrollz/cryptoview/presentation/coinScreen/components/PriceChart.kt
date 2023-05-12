package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.ui.theme.BrightGray
import com.scrollz.cryptoview.ui.theme.Gold

@Composable
fun PriceChart() {
    val height = LocalConfiguration.current.screenWidthDp.dp * 3/4

    val infos = listOf(
        27590.61, 27499.68, 27709.15, 27854.17,
        27681.76, 27603.03, 27678.87, 27592.32,
        27584.57, 27580.85, 27491.04, 27496.57,
        27512.24, 27537.81, 27555.8, 27452.07,
        27441.6, 27484.68, 27478.29, 27504.62,
        27415.15, 27242.93, 27191.63, 27219.93
    )
    val highValue = remember(infos) { infos.max() }
    val lowValue = remember(infos) { infos.min() }

    Surface(
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)) {
                val spacePerHour = size.width / (infos.size - 1)
                val chartHeight = size.height - 32.dp.toPx()

                val strokePath = Path().apply {
                    for (i in infos.indices) {
                        val nextI = infos.getOrNull(i + 1) ?: infos.last()

                        val leftRatio = (infos[i] - lowValue) / (highValue - lowValue)
                        val rightRatio = (nextI - lowValue) / (highValue - lowValue)

                        val x1 = i * spacePerHour
                        val y1 = chartHeight - (leftRatio * chartHeight).toFloat()
                        val x2 = (i + 1) * spacePerHour
                        val y2 = chartHeight - (rightRatio * chartHeight).toFloat()

                        if(i == 0) {
                            moveTo(x1, y1)
                        }
                        quadraticBezierTo(
                            x1, y1, (x1 + x2) / 2f, (y1 + y2) / 2f
                        )
                    }
                }
                val horizontalGridY = listOf(0f, chartHeight * 1/4, chartHeight/2,
                    chartHeight * 3/4, chartHeight)
                for (y in horizontalGridY) {
                    drawLine(
                        color = BrightGray.copy(alpha = 0.1f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
                drawPath(
                    path = strokePath,
                    color = Gold,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
                val fillPath = strokePath
                    .apply {
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Gold.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        endY = size.height
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.height(32.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    LabelItem(text = "27756")
                }
                LabelItem(text = "27503")
                Row(
                    modifier = Modifier.height(32.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    LabelItem(text = "27247")
                }
            }
        }
    }
}

@Composable
fun LabelItem(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
    )
}