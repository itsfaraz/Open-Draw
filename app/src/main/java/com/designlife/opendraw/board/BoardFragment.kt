package com.designlife.opendraw.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.presentation.component.ToolTipComponent
import com.designlife.opendraw.ui.theme.PrimaryColorHome1

class BoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {

                var selectedToolTip = remember {
                    mutableStateOf(ToolTipType.IN_ACTIVE)
                }

                Column(
                    modifier = Modifier.fillMaxSize().background(color = PrimaryColorHome1.value),
                    horizontalAlignment = Alignment.Start
                ) {
                    ToolTipComponent(
                        selectedToolTip = selectedToolTip.value,
                        onTextEvent = {

                        },
                        onBrushEvent = {
                            if (selectedToolTip.value == ToolTipType.BOARD_BRUSH){
                                selectedToolTip.value = ToolTipType.IN_ACTIVE
                            }else{
                                selectedToolTip.value = ToolTipType.BOARD_BRUSH
                            }
                        },
                        onHighlighterEvent = {},
                        onEraserEvent = {
                            if (selectedToolTip.value == ToolTipType.BOARD_ERASER){
                                selectedToolTip.value = ToolTipType.IN_ACTIVE
                            }else{
                                selectedToolTip.value = ToolTipType.BOARD_ERASER
                            }
                        },
                        onShapesEvent = {
                            if (selectedToolTip.value == ToolTipType.BOARD_SHAPE){
                                selectedToolTip.value = ToolTipType.IN_ACTIVE
                            }else{
                                selectedToolTip.value = ToolTipType.BOARD_SHAPE
                            }
                        },
                        onNotesEvent = {
                            if (selectedToolTip.value == ToolTipType.BOARD_NOTE){
                                selectedToolTip.value = ToolTipType.IN_ACTIVE
                            }else{
                                selectedToolTip.value = ToolTipType.BOARD_NOTE
                            }
                        },
                        onInsertEvent = {},
                        onRedoEvent = {},
                        onUndoEvent = {},
                        onBrushSelected = { size,color -> },
                        onEraserSelected = {size -> },
                        onShapeSelected = {type -> },
                        onNoteColorSelected = {color -> }
                    )
                }
            }
        }
    }
}