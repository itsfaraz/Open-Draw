package com.designlife.opendraw.common.utils

import androidx.compose.ui.graphics.Color


fun List<Color>.toWeightedAverageColor(): Color {
    if (isEmpty()) return Color.Transparent
    if (size == 1) return first()

    var red = 0f
    var green = 0f
    var blue = 0f
    var alpha = 0f
    var totalWeight = 0f

    for (i in indices) {
        val weight = (i + 1).toFloat() // linear weight
        val color = this[i]

        red += color.red * weight
        green += color.green * weight
        blue += color.blue * weight
        alpha += color.alpha * weight

        totalWeight += weight
    }

    return Color(
        red = red / totalWeight,
        green = green / totalWeight,
        blue = blue / totalWeight,
        alpha = alpha / totalWeight
    )
}