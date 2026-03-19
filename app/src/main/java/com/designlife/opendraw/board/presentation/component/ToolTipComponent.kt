package com.designlife.opendraw.board.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.designlife.opendraw.R
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.ui.theme.ButtonPrimary
import com.designlife.opendraw.ui.theme.ColorNoteItem1
import com.designlife.opendraw.ui.theme.ColorNoteItem2
import com.designlife.opendraw.ui.theme.ColorNoteItem3
import com.designlife.opendraw.ui.theme.ColorNoteItem4
import com.designlife.opendraw.ui.theme.ColorNoteItem5
import com.designlife.opendraw.ui.theme.ColorNoteItem6
import com.designlife.opendraw.ui.theme.ColorNoteItem7
import com.designlife.opendraw.ui.theme.ColorNoteItem8
import com.designlife.opendraw.ui.theme.ColorPaletteItem1
import com.designlife.opendraw.ui.theme.ColorPaletteItem2
import com.designlife.opendraw.ui.theme.ColorPaletteItem3
import com.designlife.opendraw.ui.theme.ColorPaletteItem4
import com.designlife.opendraw.ui.theme.ColorPaletteItem5
import com.designlife.opendraw.ui.theme.ColorPaletteItem6
import com.designlife.opendraw.ui.theme.ColorPaletteItem7
import com.designlife.opendraw.ui.theme.ColorPaletteItem8
import com.designlife.opendraw.ui.theme.ColorPaletteItem9
import com.designlife.opendraw.ui.theme.ColorPaletteItem10
import com.designlife.opendraw.ui.theme.ColorPaletteItem11
import com.designlife.opendraw.ui.theme.ColorPaletteItem12
import com.designlife.opendraw.ui.theme.ColorPaletteItem13
import com.designlife.opendraw.ui.theme.ColorPaletteItem14
import com.designlife.opendraw.ui.theme.ColorPaletteItem15
import com.designlife.opendraw.ui.theme.ColorPaletteItem16
import com.designlife.opendraw.ui.theme.LightButtonPrimary
import com.designlife.opendraw.ui.theme.PrimaryButtonColor
import com.designlife.opendraw.ui.theme.PrimaryColorLight

@Composable
fun ToolTipComponent(
    selectedToolTip : ToolTipType,
    selectedShape : ShapeType,
    onTextEvent : () -> Unit,
    onBrushEvent : () -> Unit,
    onHighlighterEvent : () -> Unit,
    onEraserEvent : () -> Unit,
    onShapesEvent : () -> Unit,
    onNotesEvent : () -> Unit,
    onInsertEvent : () -> Unit,
    onRedoEvent : () -> Unit,
    onUndoEvent : () -> Unit,
    onBrushSelected : (size : Float,color : Color) -> Unit,
    onEraserSelected : (size : Float) -> Unit,
    onShapeSelected : (shapeType : ShapeType) -> Unit,
    onNoteColorSelected : (color : Color) -> Unit,
    onCanvasActionTypeEvent : (actionType : CanvasActionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentHeight()
            .padding(start = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(56.dp),
                shape = RoundedCornerShape(30),
                backgroundColor = Color.White
            ) {
                Column(modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentHeight()
                    .background(color = Color.White, shape = RoundedCornerShape(30)),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_TEXT,
                        icon = R.drawable.ic_text,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_TEXT)
                            onTextEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_BRUSH,
                        icon = R.drawable.ic_brush,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_BRUSH)
                            onBrushEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_HIGHLIGHTER,
                        icon = R.drawable.ic_highlighter,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_HIGHLIGHTER)
                            onHighlighterEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_ERASER,
                        icon = R.drawable.ic_eraser,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_ERASER)
                            onEraserEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_SHAPE,
                        icon = R.drawable.ic_shapes,
                        onClickEvent = {onShapesEvent()}
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_NOTE,
                        icon = R.drawable.ic_notes,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_NOTE)
                            onNotesEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_INSERT,
                        icon = R.drawable.ic_add,
                        onClickEvent = {
                            onCanvasActionTypeEvent(CanvasActionType.DRAW_IMAGE)
                            onInsertEvent()
                        }
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_REDO,
                        icon = R.drawable.ic_redo,
                        onClickEvent = {onRedoEvent()}
                    )

                    ToolTipItemComponent(
                        isSelected = selectedToolTip == ToolTipType.BOARD_UNDO,
                        icon = R.drawable.ic_undo,
                        onClickEvent = {onUndoEvent()}
                    )
                }
            }


            Column(modifier = Modifier
                .wrapContentHeight()
                .wrapContentHeight() ,
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_TEXT, isActive = ToolTipType.BOARD_TEXT == selectedToolTip){

                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_BRUSH, isActive = ToolTipType.BOARD_BRUSH == selectedToolTip){
                    BrushPaletteComponent(onBrushSelected = {size, color ->
                        onBrushSelected(size,color)
                        onCanvasActionTypeEvent(CanvasActionType.DRAW_BRUSH)
                    })
                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_HIGHLIGHTER, isActive = ToolTipType.BOARD_HIGHLIGHTER == selectedToolTip){

                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_ERASER, isActive = ToolTipType.BOARD_ERASER == selectedToolTip){
                    EraserComponent {
                        onEraserSelected(it)
                        onCanvasActionTypeEvent(CanvasActionType.DRAW_ERASER)
                    }
                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_SHAPE, isActive = ToolTipType.BOARD_SHAPE == selectedToolTip){
                    ShapeComponent(selectedShape) {type, canvasActionType ->
                        onCanvasActionTypeEvent(canvasActionType)
                        onShapeSelected(type)
                    }
                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_NOTE, isActive = ToolTipType.BOARD_NOTE == selectedToolTip){
                    NoteComponent {
                        onNoteColorSelected(it)
                        onCanvasActionTypeEvent(CanvasActionType.DRAW_NOTE)
                    }
                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_INSERT, isActive = ToolTipType.BOARD_INSERT == selectedToolTip){

                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_REDO, isActive = ToolTipType.BOARD_REDO == selectedToolTip){

                }

                ToolTipItemExtendedComponent(componentType = ToolTipType.BOARD_UNDO, isActive = ToolTipType.BOARD_UNDO == selectedToolTip){

                }
            }
        }

    }
}

@Composable
fun ToolTipItemComponent(
    isSelected: Boolean,
    @DrawableRes  icon : Int,
    onClickEvent : () -> Unit,
){
    IconButton (
        modifier = Modifier.Companion
            .graphicsLayer {
                val scale = if (isSelected) 1.2F else 1F
                scaleX = scale
                scaleY = scale
            }
            .padding(vertical = 10.dp)
            .size(44.dp)
            .background(color = PrimaryButtonColor, shape = RoundedCornerShape(100)),
        onClick = {
            onClickEvent()
        },
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(icon),
            contentDescription = "Tooltip Icon",
            tint = Color.White
        )
    }
}

@Composable
fun ToolTipItemExtendedComponent(
    componentType : ToolTipType,
    isActive : Boolean,
    extendedView : @Composable () -> Unit
){
    if (isActive){
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .width(400.dp)
                .wrapContentHeight()
                .background(color = Color.White, shape = RoundedCornerShape(20)),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            extendedView()
        }
    }else { // For Spacing and padding
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .wrapContentHeight()
                .height(44.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }
    }
}

@Composable
fun BrushPaletteComponent(
    onBrushSelected : (size : Float, color : Color) -> Unit
){
    val colorList = listOf<Color>(
        ColorPaletteItem1, ColorPaletteItem2, ColorPaletteItem3, ColorPaletteItem4,
        ColorPaletteItem5, ColorPaletteItem6, ColorPaletteItem7, ColorPaletteItem8,
        ColorPaletteItem9, ColorPaletteItem10, ColorPaletteItem11, ColorPaletteItem12,
        ColorPaletteItem13, ColorPaletteItem14, ColorPaletteItem15, ColorPaletteItem16,
    )

    var selectedSize = remember {
        mutableStateOf(1f)
    }

    var selectedColor = remember {
        mutableStateOf(colorList.get(0))
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 80.dp)
            .height(180.dp)
            .wrapContentWidth()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        selectedSize.value = 3f
                        onBrushSelected(3f, selectedColor.value)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .size(3.dp)
                            .background(color = if (selectedSize.value == 3f) PrimaryButtonColor else Color.Black, shape = RoundedCornerShape(100))
                    ) {  }
                }


                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        selectedSize.value = 5f
                        onBrushSelected(5f, selectedColor.value)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .size(5.dp)
                            .background(color = if (selectedSize.value == 5f) PrimaryButtonColor else  Color.Black, shape = RoundedCornerShape(100))
                    ) {  }
                }


                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        selectedSize.value = 7f
                        onBrushSelected(7f, selectedColor.value)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .size(7.dp)
                            .background(color = if (selectedSize.value == 7f) PrimaryButtonColor else Color.Black, shape = RoundedCornerShape(100))
                    ) {  }

                }


                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        selectedSize.value = 9f
                        onBrushSelected(9f, selectedColor.value)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .size(9.dp)
                            .background(color = if (selectedSize.value == 9f) PrimaryButtonColor else Color.Black, shape = RoundedCornerShape(100))
                    ) {  }
                }

                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        selectedSize.value = 11f
                        onBrushSelected(11f, selectedColor.value)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .size(11.dp)
                            .background(color = if (selectedSize.value == 11f) PrimaryButtonColor else Color.Black, shape = RoundedCornerShape(100))

                    ) {  }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ){
                items(colorList.size){index ->
                    val isSelected = colorList.get(index) == selectedColor.value
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                            .size(40.dp)
                            .clickable {
                                selectedColor.value = colorList.get(index)
                                onBrushSelected(selectedSize.value, selectedColor.value)
                            }
                            .background(
                                color = colorList.get(index),
                                shape = RoundedCornerShape(100)
                            ).border(width = if (isSelected) 2.dp else 0.dp, color = if (isSelected) LightButtonPrimary else Color.Transparent, shape = RoundedCornerShape(if (isSelected) 100 else 0)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {  }
                }
            }
        }
    }
}

@Composable
fun EraserComponent(
    selectedEraser : (size : Float) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 80.dp)
            .height(180.dp)
            .wrapContentWidth()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .size(5.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(100))
                    .clickable {
                        selectedEraser(5f)
                    }
            ) {  }

            Column(
                modifier = Modifier
                    .size(10.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(100))
                    .clickable {
                        selectedEraser(10f)
                    }
            ) {  }

            Column(
                modifier = Modifier
                    .size(15.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(100))
                    .clickable {
                        selectedEraser(15f)
                    }
            ) {  }

            Column(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(100))
                    .clickable {
                        selectedEraser(20f)
                    }
            ) {  }

            Column(
                modifier = Modifier
                    .size(25.dp)
                    .background(color = Color.Black, shape = RoundedCornerShape(100))
                    .clickable {
                        selectedEraser(25f)

                    }
            ) {  }
        }
    }

}

@Composable
fun ShapeComponent(
    selectedShape: ShapeType,
    onShapeSelected : (type : ShapeType, canvasActionType : CanvasActionType) -> Unit
){
    Column(
        modifier = Modifier
            .padding(horizontal = 60.dp)
            .height(180.dp)
            .wrapContentWidth()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyRow (
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.CIRCLE) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.CIRCLE, CanvasActionType.DRAW_CIRCLE)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(45.dp), painter = painterResource(R.drawable.ic_circle), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))
            }
            item {     Column(
                modifier = Modifier
                    .graphicsLayer{
                        val shape = if (selectedShape == ShapeType.RECTANGLE) 1.3F else 1.0F
                        scaleX = shape
                        scaleY = shape
                    }
                    .size(60.dp)
                    .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                    .clickable {
                        onShapeSelected(ShapeType.RECTANGLE, CanvasActionType.DRAW_RECTANGLE)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Icon(modifier = Modifier.size(40.dp), painter = painterResource(R.drawable.ic_rectangle), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.SQUARE) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.SQUARE, CanvasActionType.DRAW_SQUARE)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(40.dp),painter = painterResource(R.drawable.ic_square), contentDescription = "Shape Icon", tint = Color.Blue) }
                    Spacer(modifier = Modifier.width(10.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.LINE) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.LINE, CanvasActionType.DRAW_LINE)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(45.dp),painter = painterResource(R.drawable.ic_line), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.STAR) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.STAR, CanvasActionType.DRAW_STAR)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(45.dp),painter = painterResource(R.drawable.ic_star), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))
            }
            item {

                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.ELLIPSE) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.ELLIPSE, CanvasActionType.DRAW_ELLIPSE)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(45.dp),painter = painterResource(R.drawable.ic_ellipse), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))
            }
            item {
                Column(
                    modifier = Modifier
                        .graphicsLayer{
                            val shape = if (selectedShape == ShapeType.POLYGON) 1.3F else 1.0F
                            scaleX = shape
                            scaleY = shape
                        }
                        .size(60.dp)
                        .border(width = 3.dp, color = Color.LightGray, shape = CircleShape)
                        .clickable {
                            onShapeSelected(ShapeType.POLYGON, CanvasActionType.DRAW_POLYGON)
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Icon(modifier = Modifier.size(40.dp),painter = painterResource(R.drawable.ic_polygon), contentDescription = "Shape Icon", tint = Color.Blue) }
                Spacer(modifier = Modifier.width(10.dp))

            }
        }
    }

}

@Composable
fun NoteComponent(
    onNoteColorSelected : (color : Color) -> Unit
) {

    val colorList = listOf<Color>(
        ColorNoteItem1, ColorNoteItem2, ColorNoteItem3, ColorNoteItem4,
        ColorNoteItem5, ColorNoteItem6, ColorNoteItem7, ColorNoteItem8
    )

    var selectedColor = remember {
        mutableStateOf(colorList.get(0))
    }

    Column(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ){
            items(colorList.size){index ->
                val isSelected = colorList.get(index).equals(selectedColor.value)
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                        .size(40.dp)
                        .clickable {
                            selectedColor.value = colorList.get(index)
                            onNoteColorSelected(colorList.get(index))
                        }
                        .background(
                            color = colorList.get(index),
                            shape = RoundedCornerShape(100)
                        )
                        .border(width = if (isSelected) 2.dp else 0.dp ,color = if (isSelected) Color.Black else Color.Transparent,shape = RoundedCornerShape(if (isSelected) 100 else 0)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                }
            }
        }
    }
}