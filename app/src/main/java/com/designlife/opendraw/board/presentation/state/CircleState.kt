package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class CircleState(
    val radius : Float = 100f,
    val rotation : Float = 0f,
    val center : Offset = Offset(300f,300f)
) : StateWrapper(type = ShapeType.CIRCLE)