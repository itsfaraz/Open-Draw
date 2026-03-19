package com.designlife.opendraw.common.utils.serializer

import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
private val json = Json {
    prettyPrint       = true
    ignoreUnknownKeys = true
    encodeDefaults    = false
    coerceInputValues = true
}

// ─────────────────────────────────────────────────────────────────────────────
// List<InternalCanvasBoard.Shape>
// ─────────────────────────────────────────────────────────────────────────────

fun List<InternalCanvasBoard.Shape>.encodeToJson(): String =
    json.encodeToString(
        serializer = ListSerializer(InternalCanvasBoard.Shape.serializer()),
        value      = this,
    )

fun String.decodeToShapes(): List<InternalCanvasBoard.Shape> =
    json.decodeFromString(
        deserializer = ListSerializer(InternalCanvasBoard.Shape.serializer()),
        string       = this,
    )

// ─────────────────────────────────────────────────────────────────────────────
// Single InternalCanvasBoard.Shape
// ─────────────────────────────────────────────────────────────────────────────

fun InternalCanvasBoard.Shape.encodeToJson(): String =
    json.encodeToString(
        serializer = InternalCanvasBoard.Shape.serializer(),
        value      = this,
    )

fun String.decodeToShape(): InternalCanvasBoard.Shape =
    json.decodeFromString(
        deserializer = InternalCanvasBoard.Shape.serializer(),
        string       = this,
    )

// ─────────────────────────────────────────────────────────────────────────────
// List<InternalCanvasBoard.Text>
// ─────────────────────────────────────────────────────────────────────────────

fun List<InternalCanvasBoard.Text>.encodeTextsToJson(): String =
    json.encodeToString(
        serializer = ListSerializer(InternalCanvasBoard.Text.serializer()),
        value      = this,
    )

fun String.decodeToTexts(): List<InternalCanvasBoard.Text> =
    json.decodeFromString(
        deserializer = ListSerializer(InternalCanvasBoard.Text.serializer()),
        string       = this,
    )

// ─────────────────────────────────────────────────────────────────────────────
// Single InternalCanvasBoard.Text
// ─────────────────────────────────────────────────────────────────────────────

fun InternalCanvasBoard.Text.encodeToJson(): String =
    json.encodeToString(
        serializer = InternalCanvasBoard.Text.serializer(),
        value      = this,
    )

fun String.decodeToText(): InternalCanvasBoard.Text =
    json.decodeFromString(
        deserializer = InternalCanvasBoard.Text.serializer(),
        string       = this,
    )

// ─────────────────────────────────────────────────────────────────────────────
// Safe wrappers
// ─────────────────────────────────────────────────────────────────────────────

fun String.decodeToShapeOrNull(): InternalCanvasBoard.Shape? =
    runCatching { decodeToShape() }.getOrNull()

fun String.decodeToShapesOrEmpty(): List<InternalCanvasBoard.Shape> =
    runCatching { decodeToShapes() }.getOrElse { emptyList() }

fun String.decodeToTextOrNull(): InternalCanvasBoard.Text? =
    runCatching { decodeToText() }.getOrNull()

fun String.decodeToTextsOrEmpty(): List<InternalCanvasBoard.Text> =
    runCatching { decodeToTexts() }.getOrElse { emptyList() }