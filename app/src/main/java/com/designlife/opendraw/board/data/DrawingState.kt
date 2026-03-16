package com.designlife.opendraw.board.data

import androidx.compose.ui.graphics.Color

data class DrawingState(
    val selectedColor : Color = Color.White,
    val currentPath : PathData? = null,
    val paths : List<PathData> = emptyList()
)