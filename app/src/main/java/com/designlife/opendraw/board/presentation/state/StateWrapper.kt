package com.designlife.opendraw.board.presentation.state

import com.designlife.opendraw.board.domain.enums.ShapeType

// State Wrapper
sealed class StateWrapper(
    val type : ShapeType = ShapeType.UN_DEFINED
)