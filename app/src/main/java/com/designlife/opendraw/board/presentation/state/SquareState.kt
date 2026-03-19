package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class SquareState(
    val size: Float = 200f,          // side length
    val rotation: Float = 0f,        // degrees
    val center: Offset = Offset(300f, 300f),
) : StateWrapper(ShapeType.SQUARE)
