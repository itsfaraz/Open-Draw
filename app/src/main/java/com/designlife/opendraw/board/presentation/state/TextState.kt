package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.ShapeType

data class TextState(
    val text: String = "",
    val center: Offset = Offset(300f, 300f),
    val fontSize: Float = 40f,          // sp
    val color: Color = Color.Black,
    val rotation: Float = 0f,           // degrees
    val scale: Float = 1f,              // pinch zoom scales fontSize visually
) : StateWrapper(ShapeType.TEXT_FIELD)