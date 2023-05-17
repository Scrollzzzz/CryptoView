package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import com.scrollz.cryptoview.domain.model.Tick
import com.scrollz.cryptoview.presentation.common.ErrorBox
import com.scrollz.cryptoview.presentation.common.LoadingBox
import com.scrollz.cryptoview.presentation.common.Status
import com.scrollz.cryptoview.utils.toPriceFormat

@Composable
fun PriceChart(
    status: Status,
    ticks: List<Tick>
) {
    val height = LocalConfiguration.current.screenWidthDp.dp * 3/4

    Surface(
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        Crossfade(
            targetState = status,
            animationSpec = tween(700)
        ) { status ->
            when (status) {
                is Status.Loading -> {
                    LoadingBox(backgroundColor = MaterialTheme.colorScheme.surface)
                }
                is Status.Error -> {
                    ErrorBox(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        text = "Error"
                    )
                }
                is Status.Normal -> {
                    if (ticks.size < 6) {
                        ErrorBox(
                            backgroundColor = MaterialTheme.colorScheme.background,
                            text = "Error"
                        )
                    } else {
                        val highValue = remember(ticks) { ticks.maxOf { it.price } }
                        val lowValue = remember(ticks) { ticks.minOf { it.price } }
                        val firstValue = remember(ticks) { ticks.first().price }
                        val lastValue = remember(ticks) { ticks.last().price }
                        val chartColor = if (lastValue >= firstValue) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.inversePrimary
                        val supportColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.1f)
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 32.dp)
                            ) {
                                val spacePerHour = size.width / (ticks.size - 1)
                                val chartHeight = size.height - 32.dp.toPx()

                                val strokePath = Path().apply {

                                    for (i in ticks.indices) {
                                        val nextI = ticks.getOrNull(i + 1)?.price ?: ticks.last().price

                                        val leftRatio = (ticks[i].price - lowValue) / (highValue - lowValue)
                                        val rightRatio = (nextI - lowValue) / (highValue - lowValue)

                                        val x1 = i * spacePerHour
                                        val y1 = chartHeight - (leftRatio * chartHeight).toFloat()
                                        val x2 = (i + 1) * spacePerHour
                                        val y2 = chartHeight - (rightRatio * chartHeight).toFloat()

                                        if (i == 0) {
                                            moveTo(x1, y1)
                                        }
                                        quadraticBezierTo(
                                            x1, y1, (x1 + x2) / 2f, (y1 + y2) / 2f
                                        )
                                    }
                                }
                                val horizontalGridY = listOf(
                                    0f, chartHeight * 1 / 4, chartHeight / 2,
                                    chartHeight * 3 / 4, chartHeight
                                )
                                for (y in horizontalGridY) {
                                    drawLine(
                                        color = supportColor,
                                        start = Offset(0f, y),
                                        end = Offset(size.width, y),
                                        strokeWidth = 0.5.dp.toPx()
                                    )
                                }
                                drawPath(
                                    path = strokePath,
                                    color = chartColor,
                                    style = Stroke(
                                        width = 1.dp.toPx(),
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
                                            chartColor.copy(alpha = 0.3f),
                                            Color.Transparent
                                        ),
                                        endY = size.height
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 4.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.height(32.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    LabelItem(text = highValue.toPriceFormat())
                                }
                                LabelItem(text = ((highValue + lowValue) / 2).toPriceFormat())
                                Row(
                                    modifier = Modifier.height(32.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    LabelItem(text = lowValue.toPriceFormat())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabelItem(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(25),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
        )
    }
}