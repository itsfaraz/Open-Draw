package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class PolygonState(
    val center: Offset = Offset(300f, 300f),
    val radius: Float = 150f,
    val rotation: Float = 0f,
    val sides: Int = 5,          // 3=triangle, 4=rhombus, 5=pentagon, 6=hexagon …
) : StateWrapper(ShapeType.POLYGON)