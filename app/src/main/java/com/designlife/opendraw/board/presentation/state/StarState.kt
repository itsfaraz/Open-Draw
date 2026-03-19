package com.designlife.opendraw.board.presentation.state

import androidx.compose.ui.geometry.Offset
import com.designlife.opendraw.board.domain.enums.ShapeType

data class StarState(
    val center: Offset = Offset(300f, 300f),
    val outerRadius: Float = 150f,
    val innerRadius: Float = 63f,   // ~0.42 * outer looks natural
    val rotation: Float = 0f,
) : StateWrapper( ShapeType.STAR)