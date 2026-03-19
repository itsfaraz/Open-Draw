package com.designlife.opendraw.board.domain.usecase

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.board.domain.enums.ToolTipType

sealed interface DrawingAction{
    data object OnNewPathStart : DrawingAction
    data class OnDraw(val offset: Offset) : DrawingAction
    data object OnPathEnd : DrawingAction
    data object OnClearCanvas : DrawingAction
    data class OnBrushSelected(val size : Float, val color: Color) : DrawingAction

    data class OnEraserSelected(val size : Float) : DrawingAction
    data class OnToolTipChange(val type: ToolTipType) : DrawingAction
    data class OnShapeSelected(val type: ShapeType) : DrawingAction
    data class OnCanvasActionTypeSelected(val type : CanvasActionType) : DrawingAction
    data object OnSaveBoard : DrawingAction
    data object OnExportBoard : DrawingAction
    data class OnImportBoard(val jsonData: String) : DrawingAction
    data class OnCanvasBoardResume(val boardId : Long) : DrawingAction
    data class OnCanvasBoardCreate(val boardId : Long, val title : String, val createdAt: Long) : DrawingAction

}