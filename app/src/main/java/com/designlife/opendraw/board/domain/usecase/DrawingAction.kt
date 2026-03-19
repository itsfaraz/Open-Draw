package com.designlife.opendraw.board.domain.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.presentation.state.CircleState
import com.designlife.opendraw.board.presentation.state.EllipseState
import com.designlife.opendraw.board.presentation.state.LineState
import com.designlife.opendraw.board.presentation.state.PolygonState
import com.designlife.opendraw.board.presentation.state.RectangleState
import com.designlife.opendraw.board.presentation.state.SquareState
import com.designlife.opendraw.board.presentation.state.StarState
import com.designlife.opendraw.board.presentation.state.TextState

sealed interface DrawingAction{
    data object OnNewPathStart : DrawingAction
    data class OnDraw(val offset: Offset) : DrawingAction
    data object OnPathEnd : DrawingAction
    data object OnClearCanvas : DrawingAction
    data class OnBrushSelected(val size : Float, val color: Color) : DrawingAction
    data class OnCircleStateChange(val circleState: CircleState) : DrawingAction
    data class OnSquareStateChange(val squareState: SquareState) : DrawingAction

    data class OnLineStateChange(val lineState: LineState) : DrawingAction

    data class OnEllipseStateChange(val ellipseState: EllipseState) : DrawingAction

    data class OnRectangleStateChange(val rectState: RectangleState) : DrawingAction

    data class OnPolygonStateChange(val polygonState: PolygonState) : DrawingAction
    data class OnStarStateChange(val starState: StarState) : DrawingAction
    data class OnEraserSelected(val size : Float) : DrawingAction
    data class OnToolTipChange(val type: ToolTipType) : DrawingAction
    data class OnShapeSelected(val type: ShapeType) : DrawingAction
    data class OnCanvasActionTypeSelected(val type : CanvasActionType) : DrawingAction
    data object OnSaveBoard : DrawingAction
    data object OnExportBoard : DrawingAction
    data class OnImportBoard(val jsonData: String) : DrawingAction
    data class OnCanvasBoardResume(val boardId : Long) : DrawingAction
    data class OnTextStateChange(val textState: TextState) : DrawingAction
    data class OnCanvasBoardCreate(val boardId : Long, val title : String, val createdAt: Long) : DrawingAction

    data object OnUndoEvent : DrawingAction
    data object OnRedoEvent : DrawingAction
    data object Clear : DrawingAction
}