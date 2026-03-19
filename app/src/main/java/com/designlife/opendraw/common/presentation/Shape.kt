package com.designlife.opendraw.common.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * Canonical serializable representation of every canvas shape.
 *
 * Bounding box convention (all in canvas pixels, pre-rotation):
 *
 *  topLeft     = (center.x - halfW,  center.y - halfH)
 *  bottomRight = (center.x + halfW,  center.y + halfH)
 *
 * For shapes without a natural bounding box (Line), topLeft = start, bottomRight = end.
 *
 * Extra floats are packed into  rather than making the schema
 * unbounded. Each shape type documents what it puts there.
 *
 * Color is stored as a packed ARGB Long string — human-readable in JSON and
 * survives all Color space conversions.
 */
@Serializable
data class Shape(
    /** One of: CIRCLE, ELLIPSE, LINE, POLYGON, RECTANGLE, SQUARE, STAR, TEXT */
    val type: String,

    /**
     * Axis-aligned bounding box before rotation.
     * Pair<x, y> — avoids a custom Offset serializer at the schema boundary.
     */
    val topLeft: Pair<Float, Float>,
    val bottomRight: Pair<Float, Float>,

    /** Rotation in degrees, applied around the shape's centroid. */
    val rotation: Float = 0f,

    /**
     * Color packed as ARGB Long.
     * Use [Shape.colorAsCompose] to convert back.
     */
    val colorArgb: Long = Color.Black.value.toLong(),

    /**
     * Shape-specific extras — fully documented per type:
     *
     * CIRCLE    extra1 = radius  (redundant with bbox but avoids float rounding on restore)
     * ELLIPSE   extra1 = radiusX, extra2 = radiusY
     * LINE      extra1 = panX,    extra2 = panY
     * POLYGON   extra1 = radius,  extra2 = sides (as Float)
     * RECTANGLE extra1 = width,   extra2 = height
     * SQUARE    extra1 = size
     * STAR      extra1 = outerRadius, extra2 = innerRadius
     * TEXT      extra1 = fontSize, extra2 = scale — text content in [text]
     */
    val extra1: Float = 0f,
    val extra2: Float = 0f,
    val extra3: Float = 0f,
    val extra4: Float = 0f,

    /** Only non-empty for TEXT shapes. */
    val text: String = "",
) {
    companion object {
        const val TYPE_CIRCLE    = "CIRCLE"
        const val TYPE_ELLIPSE   = "ELLIPSE"
        const val TYPE_LINE      = "LINE"
        const val TYPE_POLYGON   = "POLYGON"
        const val TYPE_RECTANGLE = "RECTANGLE"
        const val TYPE_SQUARE    = "SQUARE"
        const val TYPE_STAR      = "STAR"
        const val TYPE_TEXT      = "TEXT"
    }
}

/** Convenience: unpack the stored ARGB Long back to Compose Color */
val Shape.colorAsCompose: Color
    get() = Color(colorArgb.toULong())

/** Convenience: unpack topLeft to Compose Offset */
val Shape.topLeftOffset: Offset
    get() = Offset(topLeft.first, topLeft.second)

/** Convenience: unpack bottomRight to Compose Offset */
val Shape.bottomRightOffset: Offset
    get() = Offset(bottomRight.first, bottomRight.second)

/** Centroid of the bounding box */
val Shape.center: Offset
    get() = Offset(
        (topLeft.first  + bottomRight.first)  / 2f,
        (topLeft.second + bottomRight.second) / 2f,
        )
