package com.designlife.opendraw.common.domain.mapper

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.board.domain.enums.ShapeType
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
import com.designlife.opendraw.common.presentation.Shape
import com.designlife.opendraw.home.domain.converters.ColorConverter
import kotlin.div

// ─────────────────────────────────────────────────────────────────────────────
// STATE → Shape  (serialise / save)
// ─────────────────────────────────────────────────────────────────────────────

fun CircleState.toShape(): InternalCanvasBoard.Shape {
    val tl = Offset(center.x - radius, center.y - radius)
    val br = Offset(center.x + radius, center.y + radius)
    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_CIRCLE,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )
}

fun EllipseState.toShape() : InternalCanvasBoard.Shape{
    val tl = Offset(center.x - radiusX, center.y - radiusY)
    val br = Offset(center.x + radiusX, center.y + radiusY)
    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_ELLIPSE,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )
}

fun LineState.toShape() : InternalCanvasBoard.Shape{
    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_LINE,
        topLeft     = start.toPair(),
        bottomRight = end.toPair(),
    )
}

fun PolygonState.toShape() : InternalCanvasBoard.Shape{
    val tl = Offset(center.x - radius, center.y - radius)
    val br = Offset(center.x + radius, center.y + radius)
    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_POLYGON,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )
}

fun RectangleState.toShape() : InternalCanvasBoard.Shape{

    val hw = width  / 2f
    val hh = height / 2f
    val tl = Offset(center.x - hw, center.y - hh)
    val br = Offset(center.x + hw, center.y + hh)

    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_RECTANGLE,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )

}

fun SquareState.toShape() : InternalCanvasBoard.Shape{
    val half = size / 2f
    val tl   = Offset(center.x - half, center.y - half)
    val br   = Offset(center.x + half, center.y + half)

    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_SQUARE,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )
}

fun StarState.toShape() : InternalCanvasBoard.Shape{
    // Bounding box uses outerRadius for both axes
    val tl = Offset(center.x - outerRadius, center.y - outerRadius)
    val br = Offset(center.x + outerRadius, center.y + outerRadius)

    return InternalCanvasBoard.Shape(
        type        = Shape.TYPE_STAR,
        topLeft     = tl.toPair(),
        bottomRight = br.toPair(),
    )

}


fun TextState.toText(): InternalCanvasBoard.Text   {
    // Text bounding box is approximate — exact pixel size needs TextMeasurer.
    // We store center as topLeft and use extras for all text-specific data.
    // Callers that have a TextLayoutResult should use toShapeWithSize() instead.
    return InternalCanvasBoard.Text(
        text = text,
        color   = ColorConverter.serializeColor(color),
        position = center.toPair(),
        size = fontSize
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Shape → STATE  (deserialise / load)
// ─────────────────────────────────────────────────────────────────────────────



fun InternalCanvasBoard.Shape.toCircleState(): CircleState {
    check(type == Shape.TYPE_CIRCLE) { "Expected CIRCLE, got $type" }
    return CircleState(
        center   = centerOffset(),
        radius   = bboxRadius(),
        rotation = 0f,              // schema has no rotation field — defaults to 0,
    )
}

fun InternalCanvasBoard.Shape.toEllipseState(): EllipseState {
    check(type == Shape.TYPE_ELLIPSE) { "Expected ELLIPSE, got $type" }
    return EllipseState(
        center   = centerOffset(),
        radiusX  = bboxRadiusX(),
        radiusY  = bboxRadiusY(),
        rotation = 0f,
    )
}

fun InternalCanvasBoard.Shape.toLineState(): LineState {
    check(type == Shape.TYPE_LINE) { "Expected LINE, got $type" }
    // For LINE: topLeft = start endpoint, bottomRight = end endpoint.
    // pan starts at Zero — it accumulates only via gesture after restore.
    return LineState(
        start = topLeftOffset(),
        end   = bottomRightOffset(),
        pan   = Offset.Zero,
    )
}

fun InternalCanvasBoard.Shape.toPolygonState(): PolygonState {
    check(type == Shape.TYPE_POLYGON) { "Expected POLYGON, got $type" }
    // Schema has no sides field — default to pentagon (5).
    // If you later extend the schema, read it from a query param or subtype.
    return PolygonState(
        center   = centerOffset(),
        radius   = bboxRadius(),
        rotation = 0f,
        sides    = 5,
    )
}

fun InternalCanvasBoard.Shape.toRectangleState(): RectangleState {
    check(type == Shape.TYPE_RECTANGLE) { "Expected RECTANGLE, got $type" }
    return RectangleState(
        center   = centerOffset(),
        width    = bboxWidth(),
        height   = bboxHeight(),
        rotation = 0f,
    )
}

fun InternalCanvasBoard.Shape.toSquareState(): SquareState {
    check(type == Shape.TYPE_SQUARE) { "Expected SQUARE, got $type" }
    // Use the shorter side so a slightly-off bbox still restores as a square
    return SquareState(
        center   = centerOffset(),
        size     = minOf(bboxWidth(), bboxHeight()),
        rotation = 0f,
    )
}

fun InternalCanvasBoard.Shape.toStarState(): StarState {
    check(type == Shape.TYPE_STAR) { "Expected STAR, got $type" }
    val outer = bboxRadius()
    return StarState(
        center      = centerOffset(),
        outerRadius = outer,
        innerRadius = outer * 0.42f,    // natural-looking ratio, schema has no inner field
        rotation    = 0f,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// InternalCanvasBoard.Text → TextState
// ─────────────────────────────────────────────────────────────────────────────

fun InternalCanvasBoard.Text.toTextState(): TextState {
    return TextState(
        text     = text,
        center   = Offset(position.first, position.second),
        fontSize = size,
        color    = Color.Black,
        rotation = 0f,
        scale    = 1f,
    )
}
private fun Offset.toPair() = Pair(x, y)
/** Center of the bounding box as Compose Offset */
private fun InternalCanvasBoard.Shape.centerOffset(): Offset = Offset(
    x = (topLeft.first  + bottomRight.first)  / 2f,
    y = (topLeft.second + bottomRight.second) / 2f,
)

/** Full width of the bounding box */
private fun InternalCanvasBoard.Shape.bboxWidth(): Float =
    (bottomRight.first - topLeft.first).coerceAtLeast(0f)

/** Full height of the bounding box */
private fun InternalCanvasBoard.Shape.bboxHeight(): Float =
    (bottomRight.second - topLeft.second).coerceAtLeast(0f)

/** Circumradius — half the shorter side (used by circle, polygon, star) */
private fun InternalCanvasBoard.Shape.bboxRadius(): Float =
    minOf(bboxWidth(), bboxHeight()) / 2f

/** Half-width (used by ellipse as radiusX) */
private fun InternalCanvasBoard.Shape.bboxRadiusX(): Float = bboxWidth()  / 2f

/** Half-height (used by ellipse as radiusY) */
private fun InternalCanvasBoard.Shape.bboxRadiusY(): Float = bboxHeight() / 2f

/** topLeft as Compose Offset */
private fun InternalCanvasBoard.Shape.topLeftOffset(): Offset =
    Offset(topLeft.first, topLeft.second)

/** bottomRight as Compose Offset */
private fun InternalCanvasBoard.Shape.bottomRightOffset(): Offset =
    Offset(bottomRight.first, bottomRight.second)
