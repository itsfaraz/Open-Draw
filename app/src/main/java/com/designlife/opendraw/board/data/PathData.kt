package com.designlife.opendraw.board.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathData(
    val id : String,
    val penSize : Float,
    val color : Color,
    val path : List<Offset>
)