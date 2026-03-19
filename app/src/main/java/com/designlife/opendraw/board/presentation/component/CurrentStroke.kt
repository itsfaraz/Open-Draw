package com.designlife.opendraw.board.presentation.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.DrawingType

data class CurrentStroke(
    val points: List<Offset>,
    val color: Color,
    val penSize: Float,
    val type: DrawingType,
    val blendMode: BlendMode = BlendMode.SrcOver
)