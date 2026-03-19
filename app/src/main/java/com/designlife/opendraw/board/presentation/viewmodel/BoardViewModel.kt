package com.designlife.opendraw.board.presentation.viewmodel

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.opendraw.board.data.DrawingState
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.board.presentation.state.CircleState
import com.designlife.opendraw.board.presentation.state.EllipseState
import com.designlife.opendraw.board.presentation.state.LineState
import com.designlife.opendraw.board.presentation.state.PolygonState
import com.designlife.opendraw.board.presentation.state.RectangleState
import com.designlife.opendraw.board.presentation.state.SquareState
import com.designlife.opendraw.board.presentation.state.StarState
import com.designlife.opendraw.board.presentation.state.StateWrapper
import com.designlife.opendraw.board.presentation.state.TextState
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import com.designlife.opendraw.common.domain.mapper.toCircleState
import com.designlife.opendraw.common.domain.mapper.toEllipseState
import com.designlife.opendraw.common.domain.mapper.toLineState
import com.designlife.opendraw.common.domain.mapper.toPolygonState
import com.designlife.opendraw.common.domain.mapper.toRectangleState
import com.designlife.opendraw.common.domain.mapper.toShape
import com.designlife.opendraw.common.domain.mapper.toSquareState
import com.designlife.opendraw.common.domain.mapper.toStarState
import com.designlife.opendraw.common.domain.mapper.toText
import com.designlife.opendraw.common.domain.repository.BoardRepository
import com.designlife.opendraw.common.utils.AppUtils
import com.designlife.opendraw.common.utils.toWeightedAverageColor
import com.designlife.opendraw.ui.theme.HighlighterColor1
import com.designlife.opendraw.ui.theme.HighlighterColor2
import com.designlife.opendraw.ui.theme.HighlighterColor3
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Stack

class BoardViewModel(
    private val boardRepository: BoardRepository,
) : ViewModel(){

    private val _boardTitle = mutableStateOf("")
    val boardTitle = _boardTitle

    private val _boardId = mutableStateOf(System.currentTimeMillis().hashCode().toLong())

    private val _boardCreatedAt = mutableStateOf(0L)

    private val _boardCategory = mutableStateOf("Unknown")

    private val _exportedData = mutableStateOf("")
    val exportedData = _exportedData

    private val _fileName = mutableStateOf("Whiteboard_${AppUtils.getFormattedTimestamp(System.currentTimeMillis())}.json")
    val fileName = _fileName

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    private val _canvasActionType = mutableStateOf(CanvasActionType.DRAW_UNDEFINED)
    val canvasActionType = _canvasActionType
    private val _selectedToolTip = mutableStateOf(ToolTipType.IN_ACTIVE)
    val selectedToolTip = _selectedToolTip

    private val _selectedShape = mutableStateOf(ShapeType.UN_DEFINED)
    val selectedShape = _selectedShape

    val _highlighterColor : Color = listOf(HighlighterColor1, HighlighterColor2, HighlighterColor3).toWeightedAverageColor()

    private val _penSize = mutableStateOf(3F)

    private val _circleShapeState = MutableStateFlow(CircleState())
    val circleShapeState = _circleShapeState

    private val _squareShapeState = MutableStateFlow(SquareState())
    val squareShapeState = _squareShapeState

    private val _lineShapeState = MutableStateFlow(LineState())
    val lineShapeState = _lineShapeState

    private val _ellipseShapeState = MutableStateFlow(EllipseState())
    val ellipseShapeState = _ellipseShapeState

    private val _rectangleShapeState = MutableStateFlow(RectangleState())
    val rectangleShapeState = _rectangleShapeState

    private val _starShapeState = MutableStateFlow(StarState())
    val starShapeState = _starShapeState

    private val _polygonShapeState = MutableStateFlow(PolygonState())
    val polygonShapeState = _polygonShapeState

    private val _textState = MutableStateFlow(TextState())
    val textState = _textState

    private val _stateHistory = Stack<StateWrapper>()
    private val _stateUndoRedoHistory = Stack<StateWrapper>()

    fun onAction(action : DrawingAction){
        when(action){
            DrawingAction.OnNewPathStart -> {
                onNewPathStart()
            }
            DrawingAction.OnPathEnd -> {
                onPathEnd()
            }
            DrawingAction.OnClearCanvas -> {
                clearCanvas()
            }
            is DrawingAction.OnDraw -> {
                onDraw(action.offset)
            }
            is DrawingAction.OnBrushSelected -> {
                onBrushSelected(action.size,action.color)
            }
            is DrawingAction.OnCircleStateChange -> {
                _circleShapeState.value = action.circleState
                onHistoryState(action.circleState)
            }
            is DrawingAction.OnSquareStateChange -> {
                _squareShapeState.value = action.squareState
                onHistoryState(action.squareState)
            }
            is DrawingAction.OnLineStateChange -> {
                _lineShapeState.value = action.lineState
                onHistoryState(action.lineState)
            }
            is DrawingAction.OnEllipseStateChange -> {
                _ellipseShapeState.value = action.ellipseState
                onHistoryState(action.ellipseState)
            }
            is DrawingAction.OnRectangleStateChange -> {
                _rectangleShapeState.value = action.rectState
                onHistoryState(action.rectState)
            }
            is DrawingAction.OnStarStateChange -> {
                _starShapeState.value = action.starState
                onHistoryState(action.starState)
            }
            is DrawingAction.OnPolygonStateChange -> {
                _polygonShapeState.value = action.polygonState
                onHistoryState(action.polygonState)
            }
            is DrawingAction.OnEraserSelected -> {
                onEraserSelected(action.size)
            }
            is DrawingAction.OnToolTipChange -> {
                _selectedToolTip.value = action.type
                onToolTipChange(action.type)
            }
            is DrawingAction.OnShapeSelected -> {
                _selectedShape.value = action.type
                onCanvasActionShapeType(action.type)
            }
            is DrawingAction.OnCanvasActionTypeSelected -> {
                _canvasActionType.value = action.type
            }
            is DrawingAction.OnTextStateChange -> {
                _textState.value = action.textState
                onHistoryState(action.textState)
            }
            is DrawingAction.OnSaveBoard -> {
                onCanvasSave()
            }
            is DrawingAction.OnExportBoard -> {
                onExport()
            }
            is DrawingAction.OnImportBoard -> {
//                onImport()
            }
            is DrawingAction.OnCanvasBoardResume -> {
                _boardId.value = action.boardId
                onResumeCanvas(action.boardId)
                Log.d("FLOW","BoardViewModel :: onAction :: DrawingAction.OnCanvasBoardResume :: onResumeCanvas")
            }
            is DrawingAction.OnCanvasBoardCreate -> {
                _boardId.value = System.currentTimeMillis().hashCode().toLong()
                _boardTitle.value = action.title
                _boardCreatedAt.value = action.createdAt
            }
            is DrawingAction.OnUndoEvent -> {
                onUndo()
            }
            is DrawingAction.OnRedoEvent -> {
                onRedo()
            }
            is DrawingAction.Clear -> {

            }
        }
    }

    private fun onCanvasActionShapeType(type: ShapeType): Unit {
        _canvasActionType.value = when(type){
            ShapeType.CIRCLE -> {
                CanvasActionType.DRAW_CIRCLE
            }
            ShapeType.SQUARE -> {
                CanvasActionType.DRAW_SQUARE
            }
            ShapeType.RECTANGLE -> {
                CanvasActionType.DRAW_RECTANGLE
            }
            ShapeType.POLYGON -> {
                CanvasActionType.DRAW_POLYGON
            }
            ShapeType.STAR -> {
                CanvasActionType.DRAW_STAR
            }
            ShapeType.ELLIPSE -> {
                CanvasActionType.DRAW_ELLIPSE
            }
            ShapeType.LINE -> {
                CanvasActionType.DRAW_LINE
            }
            ShapeType.TEXT_FIELD -> {
                CanvasActionType.DRAW_TEXT
            }
            ShapeType.UN_DEFINED -> {
                CanvasActionType.DRAW_UNDEFINED
            }
        }

    }


    private fun onResumeCanvas(boardId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.getBoardById(boardId)?.let { canvasBoard ->
                Log.d("FLOW","BoardViewModel :: onResumeCanvas :: id ${canvasBoard.boardId}")
                _boardTitle.value = canvasBoard.boardTitle
                _boardCreatedAt.value = canvasBoard.createdAt
                _boardCategory.value = canvasBoard.category
                val pathDataList : List<PathData> = InternalCanvasBoard.fromStroke(boardId,canvasBoard.strokes)
                val shapes : List<InternalCanvasBoard.Shape> = canvasBoard.shapes
                val texts : List<InternalCanvasBoard.Text> = canvasBoard.texts


                pathDataList?.let { pathData ->
                    _state.update {
                        it.copy(paths = pathData)
                    }

//                    it.fastForEach { pathData ->
//                        recoverDrawingCanvas(pathData)
//                    }
                }

                shapes.fastForEach { state ->
                    fillShapeState(state)
                }
            }

        }
    }

    private fun recoverDrawingCanvas(pathData: PathData){
        Log.d("FLOW","BoardViewModel :: recoverDrawingCanvas :: path data ${pathData.hashCode()}")
        onBrushSelected(size = pathData.penSize, color = pathData.color)
        pathData.path.fastForEach {
            onDraw(offset = it)
        }
    }

    private fun fillShapeState(shape: InternalCanvasBoard.Shape){
        when(shape.getShapeEnum()){
            ShapeType.CIRCLE -> {
                _circleShapeState.value =  shape.toCircleState()
            }
            ShapeType.SQUARE -> {
                _squareShapeState.value = shape.toSquareState()
            }
            ShapeType.RECTANGLE -> {
                _rectangleShapeState.value = shape.toRectangleState()
            }
            ShapeType.POLYGON -> {
                _polygonShapeState.value = shape.toPolygonState()
            }
            ShapeType.STAR -> {
                _starShapeState.value = shape.toStarState()
            }
            ShapeType.ELLIPSE -> {
                _ellipseShapeState.value = shape.toEllipseState()
            }
            ShapeType.LINE -> {
                _lineShapeState.value = shape.toLineState()
            }
            else -> {

            }
        }
    }

    private fun onExport() {
        viewModelScope.launch(Dispatchers.IO) {
            val strokes = mutableListOf<InternalCanvasBoard.Strokes>()
            _state.value.paths.fastForEach { pathData ->
                Log.i("FLOW", "onExport: Stroke Color = ${pathData.color}, Pen Size = ${pathData.penSize}")
                strokes.add(PathData.toStroke(pathData))
            }


            val shapes : List<InternalCanvasBoard.Shape> =
                _stateHistory.filter { stateWrapper -> (stateWrapper.type != ShapeType.TEXT_FIELD) && (stateWrapper.type != ShapeType.UN_DEFINED) }
                    .map { stateWrapper -> getShapeState(stateWrapper) }

            val texts : List<InternalCanvasBoard.Text> = _stateHistory.filter { stateWrapper -> stateWrapper.type == ShapeType.TEXT_FIELD }.map { stateWrapper -> (stateWrapper as TextState).toText()}


            val internalCanvasBoard = InternalCanvasBoard(
                boardId = _boardId.value,
                boardTitle = _boardTitle.value,
                category = "Testing",
                strokes = strokes,
                createdAt = _boardCreatedAt.value,
                lastModifiedAt = System.currentTimeMillis(),
                shapes = shapes,
                texts = texts
            )
            val data = InternalCanvasBoard.toJson(internalCanvasBoard)
            _exportedData.value = data
        }
    }

    private fun onCanvasSave() {
        val strokes = mutableListOf<InternalCanvasBoard.Strokes>()
        _state.value.paths.fastForEach { pathData ->
            strokes.add(PathData.toStroke(pathData))
        }


        val shapes : List<InternalCanvasBoard.Shape> =
            _stateHistory.filter { stateWrapper -> (stateWrapper.type != ShapeType.TEXT_FIELD) && (stateWrapper.type != ShapeType.UN_DEFINED) }
                .map { stateWrapper -> getShapeState(stateWrapper) }

        val texts : List<InternalCanvasBoard.Text> = _stateHistory.filter { stateWrapper -> stateWrapper.type == ShapeType.TEXT_FIELD }.map { stateWrapper -> (stateWrapper as TextState).toText()}

        val internalCanvasBoard = InternalCanvasBoard(
            boardId = _boardId.value,
            createdAt = _boardCreatedAt.value,
            lastModifiedAt = System.currentTimeMillis(),
            boardTitle = _boardTitle.value,
            category = _boardCategory.value,
            strokes = strokes,
            shapes = shapes,
            texts = texts
        )
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.insertBoard(internalCanvasBoard)
        }
    }

    private fun getShapeState(wrapper: StateWrapper): InternalCanvasBoard.Shape {
        return when(wrapper.type){
            ShapeType.CIRCLE -> (wrapper as CircleState).toShape()
            ShapeType.SQUARE -> (wrapper as SquareState).toShape()
            ShapeType.RECTANGLE -> (wrapper as RectangleState).toShape()
            ShapeType.POLYGON -> (wrapper as PolygonState).toShape()
            ShapeType.STAR -> (wrapper as StarState).toShape()
            ShapeType.ELLIPSE -> (wrapper as EllipseState).toShape()
            ShapeType.LINE -> (wrapper as LineState).toShape()
            else ->(wrapper as CircleState).toShape()
        }
    }


    private fun onToolTipChange(type: ToolTipType) {
        when(type){
            ToolTipType.IN_ACTIVE -> {}
            ToolTipType.BOARD_TEXT -> {}
            ToolTipType.BOARD_HIGHLIGHTER-> {
                onHighlighterSelected()
            }
            ToolTipType.BOARD_INSERT -> {}
            ToolTipType.BOARD_NOTE -> {}
            ToolTipType.BOARD_UNDO -> {}
            ToolTipType.BOARD_REDO -> {}
            else -> {}
        }
    }

    private fun onHighlighterSelected() {
        onBrushSelected(25f,_highlighterColor)
    }

    private fun onEraserSelected(size: Float) {
        onBrushSelected(size, PrimaryColorHome1.value)
    }

    private fun onBrushSelected(size : Float, color: Color) {
        _state.update { it.copy(
            selectedColor = color
        ) }
        _penSize.value = size
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = null,
            paths = it.paths + currentPathData
        ) }
    }

    private fun onNewPathStart() {
        _state.update { it.copy(
            currentPath = PathData(
                id = System.currentTimeMillis(),
                color = it.selectedColor,
                path = emptyList(),
                penSize = _penSize.value
            )
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = currentPathData.copy(
                path = currentPathData.path + offset
            )
        ) }
    }
    private fun clearCanvas() {
        _state.update { it.copy(
            currentPath = null,
            paths = emptyList()
        ) }
        _canvasActionType.value = CanvasActionType.DRAW_UNDEFINED
        _selectedToolTip.value = ToolTipType.IN_ACTIVE
        _selectedShape.value = ShapeType.UN_DEFINED
        _circleShapeState.value = CircleState()
        _ellipseShapeState.value = EllipseState()
        _lineShapeState.value = LineState()
        _polygonShapeState.value = PolygonState()
        _rectangleShapeState.value = RectangleState()
        _squareShapeState.value = SquareState()
        _starShapeState.value = StarState()
        _textState.value = TextState()
        _stateHistory.clear()
        _stateUndoRedoHistory.clear()
    }


    fun onHistoryState(stateWrapper: StateWrapper){
        try {
            _stateHistory.push(stateWrapper)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun onUndo(){
        try {
            val stateWrapper = _stateHistory.pop()
            checkAndUndo(stateWrapper)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    private fun checkAndUndo(stateWrapper: StateWrapper) {
        when(stateWrapper.type){
            ShapeType.CIRCLE -> {
                _circleShapeState.value = CircleState()
            }
            ShapeType.SQUARE -> {
                _squareShapeState.value = SquareState()
            }
            ShapeType.RECTANGLE -> {
                _rectangleShapeState.value = RectangleState()
            }
            ShapeType.POLYGON -> {
                _polygonShapeState.value = PolygonState()
            }
            ShapeType.STAR -> {
                _starShapeState.value = StarState()
            }
            ShapeType.ELLIPSE -> {
                _ellipseShapeState.value = EllipseState()
            }
            ShapeType.LINE -> {
                _lineShapeState.value = LineState()
            }
            ShapeType.TEXT_FIELD -> {
                _textState.value = TextState()

            }
            ShapeType.UN_DEFINED -> {

            }
        }
        _stateUndoRedoHistory.push(stateWrapper)
    }

    fun onRedo(){
        try {
            val stateWrapper = _stateUndoRedoHistory.pop()
            checkAndRedo(stateWrapper)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun checkAndRedo(stateWrapper: StateWrapper) {
        when(stateWrapper.type){
            ShapeType.CIRCLE -> {
                _circleShapeState.value = stateWrapper as CircleState
            }
            ShapeType.SQUARE -> {
                _squareShapeState.value = stateWrapper as SquareState
            }
            ShapeType.RECTANGLE -> {
                _rectangleShapeState.value = stateWrapper as RectangleState
            }
            ShapeType.POLYGON -> {
                _polygonShapeState.value = stateWrapper as PolygonState
            }
            ShapeType.STAR -> {
                _starShapeState.value = stateWrapper as StarState
            }
            ShapeType.ELLIPSE -> {
                _ellipseShapeState.value = stateWrapper as EllipseState
            }
            ShapeType.LINE -> {
                _lineShapeState.value = stateWrapper as LineState
            }
            ShapeType.TEXT_FIELD -> {
                _textState.value = stateWrapper as TextState

            }
            ShapeType.UN_DEFINED -> {

            }
        }
        _stateHistory.push(stateWrapper)
    }

    override fun onCleared() {
        clearCanvas()
        super.onCleared()
    }

}