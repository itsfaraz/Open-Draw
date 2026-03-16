package com.designlife.opendraw.board.domain.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.ToolTipType

sealed interface DrawingAction{
    data object OnNewPathStart : DrawingAction
    data class OnDraw(val offset: Offset) : DrawingAction
    data object OnPathEnd : DrawingAction
    data object OnClearCanvas : DrawingAction
    data class OnBrushSelected(val size : Float, val color: Color) : DrawingAction

    data class OnEraserSelected(val size : Float) : DrawingAction
    data class OnToolTipChange(val type: ToolTipType) : DrawingAction

}