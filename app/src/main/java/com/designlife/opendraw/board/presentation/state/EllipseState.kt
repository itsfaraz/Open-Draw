package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class EllipseState(
    val center: Offset = Offset(300f, 300f),
    val radiusX: Float = 150f,   // horizontal radius
    val radiusY: Float = 90f,    // vertical radius
    val rotation: Float = 0f,
) : StateWrapper(ShapeType.ELLIPSE)