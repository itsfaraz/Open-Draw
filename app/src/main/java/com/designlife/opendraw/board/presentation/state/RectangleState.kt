package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class RectangleState(
    val center: Offset = Offset(300f, 300f),
    val width: Float = 260f,
    val height: Float = 160f,
    val rotation: Float = 0f,
) : StateWrapper(ShapeType.RECTANGLE)
