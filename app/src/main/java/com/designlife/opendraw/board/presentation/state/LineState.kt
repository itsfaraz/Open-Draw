package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class LineState(
    val start: Offset = Offset(200f, 300f),
    val end: Offset = Offset(500f, 300f),
    // pan offset applied to both points together
    val pan: Offset = Offset.Zero,
) : StateWrapper(ShapeType.LINE)