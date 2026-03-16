package com.designlife.opendraw.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.board.presentation.component.DrawingCanvas
import com.designlife.opendraw.board.presentation.component.ToolTipComponent
import com.designlife.opendraw.board.presentation.viewmodel.BoardViewModel
import com.designlife.opendraw.ui.theme.PrimaryColorHome1

class BoardFragment : Fragment() {

    lateinit var viewModel: BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[BoardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {

                val selectedToolTip = viewModel.selectedToolTip.value

                val state by viewModel.state.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = PrimaryColorHome1.value),
                    contentAlignment = Alignment.TopStart
                ) {

                    DrawingCanvas(
                        isHighlighterSelected = selectedToolTip == ToolTipType.BOARD_HIGHLIGHTER,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = PrimaryColorHome1.value),
                        paths = state.paths,
                        currentPath = state.currentPath,
                        onAction = viewModel::onAction,
                    )

                    Column(
                        modifier = Modifier.wrapContentSize().background(color = Color.Transparent),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ToolTipComponent(
                            selectedToolTip = selectedToolTip,
                            onTextEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_TEXT){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_TEXT))
                                }
                            },
                            onBrushEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_BRUSH){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_BRUSH))
                                }
                            },
                            onHighlighterEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_HIGHLIGHTER){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_HIGHLIGHTER))
                                }
                            },
                            onEraserEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_ERASER){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_ERASER))
                                }
                            },
                            onShapesEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_SHAPE){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_SHAPE))
                                }
                            },
                            onNotesEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_NOTE){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_NOTE))
                                }
                            },
                            onInsertEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_INSERT))
                            },
                            onRedoEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_UNDO))
                            },
                            onUndoEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_REDO))
                            },
                            onBrushSelected = { size,color -> viewModel.onAction(DrawingAction.OnBrushSelected(size,color)) },
                            onEraserSelected = {size ->  viewModel.onAction(DrawingAction.OnEraserSelected(size))},
                            onShapeSelected = {type -> },
                            onNoteColorSelected = {color -> }
                        )
                    }

                }


            }
        }
    }
}