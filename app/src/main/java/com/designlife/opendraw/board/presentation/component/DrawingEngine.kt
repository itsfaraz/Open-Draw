package com.designlife.opendraw.board.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.board.presentation.state.CircleState
import com.designlife.opendraw.board.presentation.state.EllipseState
import com.designlife.opendraw.board.presentation.state.LineState
import com.designlife.opendraw.board.presentation.state.PolygonState
import com.designlife.opendraw.board.presentation.state.RectangleState
import com.designlife.opendraw.board.presentation.state.SquareState
import com.designlife.opendraw.board.presentation.state.StarState
import com.designlife.opendraw.board.presentation.state.TextState
import com.designlife.opendraw.ui.theme.HighlighterColor1
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DrawingEngine(
    modifier: Modifier = Modifier,
    isNewBoard : Boolean,
    paths: List<PathData>,
    currentPath: PathData?,
    actionType: CanvasActionType,
    circleShapeState: CircleState,
    squareShapeState: SquareState,
    lineShapeState: LineState,
    ellipseShapeState: EllipseState,
    rectangleShapeState: RectangleState,
    starShapeState: StarState,
    textState : TextState,
    polygonShapeState: PolygonState,
    onAction: (action: DrawingAction) -> Unit,
    onTextEditRequest: (currentText: String) -> Unit,  // ← tap on existing text
    onTextPlaceRequest: (position: Offset) -> Unit,    // ← tap on empty canvas
) {
    // ── Snapshot into gesture-safe refs ───────────────────────────────────────
    val currentCircle     by rememberUpdatedState(circleShapeState)
    val currentSquare     by rememberUpdatedState(squareShapeState)
    val currentLine       by rememberUpdatedState(lineShapeState)
    val currentEllipse    by rememberUpdatedState(ellipseShapeState)
    val currentRectangle  by rememberUpdatedState(rectangleShapeState)
    val currentStar       by rememberUpdatedState(starShapeState)
    val currentPolygon    by rememberUpdatedState(polygonShapeState)
    val currentText       by rememberUpdatedState(textState)
    val currentActionType by rememberUpdatedState(actionType)

    // ── Persistence flags — shape survives tool switches once activated ────────
    var circleEverPlaced    by remember { mutableStateOf(!isNewBoard) }
    var squareEverPlaced    by remember { mutableStateOf(!isNewBoard) }
    var lineEverPlaced      by remember { mutableStateOf(!isNewBoard) }
    var ellipseEverPlaced   by remember { mutableStateOf(!isNewBoard) }
    var rectangleEverPlaced by remember { mutableStateOf(!isNewBoard) }
    var starEverPlaced      by remember { mutableStateOf(!isNewBoard) }
    var polygonEverPlaced   by remember { mutableStateOf(!isNewBoard) }
    var textEverPlaced      by remember { mutableStateOf(!isNewBoard) }

    LaunchedEffect(actionType) {
        when (actionType) {
            CanvasActionType.DRAW_CIRCLE    -> circleEverPlaced    = true
            CanvasActionType.DRAW_SQUARE    -> squareEverPlaced    = true
            CanvasActionType.DRAW_LINE      -> lineEverPlaced      = true
            CanvasActionType.DRAW_ELLIPSE   -> ellipseEverPlaced   = true
            CanvasActionType.DRAW_RECTANGLE -> rectangleEverPlaced = true
            CanvasActionType.DRAW_STAR      -> starEverPlaced      = true
            CanvasActionType.DRAW_POLYGON   -> polygonEverPlaced   = true
            CanvasActionType.DRAW_TEXT      -> textEverPlaced      = true
            else -> Unit
        }
    }

    // ── TextMeasurer: pre-allocated, never inside draw lambda ─────────────────
    // rememberTextMeasurer() caches layout results internally — reuse it always
    val textMeasurer = rememberTextMeasurer()

    // ── Measure text layout ONCE per state change, not per frame ──────────────
    // TextLayoutResult gives us real pixel dimensions for centering + hit-test.
    // Recomputed only when text content, size, or scale changes — not on drag/rotate.
    val textLayout by remember(textState.text, textState.fontSize, textState.scale) {
        derivedStateOf {
            textMeasurer.measure(
                text  = textState.text,
                style = TextStyle(
                    fontSize = (textState.fontSize * textState.scale).sp,
                    color = textState.color,
                    fontWeight = FontWeight.Normal,
                ),
            )
        }
    }


    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(PrimaryColorHome1.value)
            .pointerInput(currentActionType) {
                awaitEachGesture {

                    // ── 1. Wait for first finger down ──────────────────────────
                    val firstDown = awaitFirstDown(requireUnconsumed = false)
                    firstDown.consume()

                    // Notify VM only for draw tools, not shape-manipulation tools
                    if (currentActionType.isDrawTool()) {
                        onAction(DrawingAction.OnNewPathStart)
                    }

                    // ── 2. Track all subsequent events until all fingers lift ──
                    var lastPosition = firstDown.position
                    val velocityTracker = VelocityTracker()

                    do {
                        val event: PointerEvent = awaitPointerEvent()
                        val pointerCount = event.changes.count { it.pressed }

                        when {
                            // ── CIRCLE: 2+ fingers → transform (pan/zoom/rotate)
                            currentActionType == CanvasActionType.DRAW_CIRCLE && pointerCount >= 2 -> {
                                val pan = event.calculatePan()
                                val zoom = event.calculateZoom()
                                val rotation = event.calculateRotation()

                                onAction(
                                    DrawingAction.OnCircleStateChange(
                                        currentCircle.copy(
                                            center = currentCircle.center + pan,
                                            radius = (currentCircle.radius * zoom)
                                                .coerceIn(50f, 800f),
                                            rotation = currentCircle.rotation +
                                                    (rotation * 180f / PI.toFloat())
                                        )
                                    )
                                )
                                // Consume all pointer changes to block scroll/parent
                                event.changes.fastForEach { it.consume() }
                            }

                            // ── CIRCLE: 1 finger → drag the circle
                            currentActionType == CanvasActionType.DRAW_CIRCLE && pointerCount == 1 -> {
                                val change = event.changes.firstOrNull() ?: continue
                                val dragAmount = change.position - lastPosition
                                lastPosition = change.position

                                onAction(
                                    DrawingAction.OnCircleStateChange(
                                        currentCircle.copy(
                                            center = currentCircle.center + dragAmount
                                        )
                                    )
                                )
                                change.consume()
                            }

                            // ── SQUARE: 2+ fingers → pan + zoom + rotate ──────
                            currentActionType == CanvasActionType.DRAW_SQUARE
                                    && pointerCount >= 2 -> {
                                onAction(
                                    DrawingAction.OnSquareStateChange(
                                        currentSquare.copy(
                                            center = currentSquare.center + event.calculatePan(),
                                            // zoom scales the side length
                                            size = (currentSquare.size * event.calculateZoom())
                                                .coerceIn(40f, 1600f),
                                            rotation = currentSquare.rotation +
                                                    (event.calculateRotation() * 180f / PI.toFloat()),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }

                            // ── SQUARE: 1 finger → drag ───────────────────────
                            currentActionType == CanvasActionType.DRAW_SQUARE
                                    && pointerCount == 1 -> {
                                val change = event.changes.firstOrNull() ?: continue
                                onAction(
                                    DrawingAction.OnSquareStateChange(
                                        currentSquare.copy(
                                            center = currentSquare.center +
                                                    (change.position - lastPosition)
                                        )
                                    )
                                )
                                lastPosition = change.position
                                change.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // LINE
                            // 1 finger: drag the whole line (pan both endpoints)
                            // 2 fingers: stretch/rotate (finger 0 → start,
                            //            finger 1 → end, giving full control)
                            // ════════════════════════════════════════════════════
                            currentActionType == CanvasActionType.DRAW_LINE
                                    && pointerCount >= 2 -> {
                                // Each finger independently anchors one endpoint —
                                // most natural way to resize/rotate a line
                                val sorted = event.changes
                                    .filter { it.pressed }
                                    .sortedBy { it.id.value }
                                val f0 = sorted.getOrNull(0)
                                val f1 = sorted.getOrNull(1)
                                val pan = event.calculatePan()

                                onAction(
                                    DrawingAction.OnLineStateChange(
                                        currentLine.copy(
                                            // Move the whole line by centroid pan
                                            pan   = currentLine.pan + pan,
                                            // Stretch: first finger pulls start toward it,
                                            // second finger pulls end toward it
                                            start = currentLine.start +
                                                    (f0?.let { it.position - it.previousPosition }
                                                        ?: Offset.Zero),
                                            end   = currentLine.end +
                                                    (f1?.let { it.position - it.previousPosition }
                                                        ?: Offset.Zero),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }

                            currentActionType == CanvasActionType.DRAW_LINE
                                    && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                val delta = c.position - lastPosition
                                onAction(
                                    DrawingAction.OnLineStateChange(
                                        currentLine.copy(
                                            pan = currentLine.pan + delta
                                        )
                                    )
                                )
                                lastPosition = c.position
                                c.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // ELLIPSE
                            // 2 fingers: pan + zoom each axis + rotate
                            // ════════════════════════════════════════════════════
                            currentActionType == CanvasActionType.DRAW_ELLIPSE
                                    && pointerCount >= 2 -> {
                                val zoom = event.calculateZoom()
                                onAction(
                                    DrawingAction.OnEllipseStateChange(
                                        currentEllipse.copy(
                                            center  = currentEllipse.center + event.calculatePan(),
                                            radiusX = (currentEllipse.radiusX * zoom).coerceIn(20f, 900f),
                                            radiusY = (currentEllipse.radiusY * zoom).coerceIn(20f, 900f),
                                            rotation = currentEllipse.rotation +
                                                    (event.calculateRotation() * 180f / PI.toFloat()),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }

                            currentActionType == CanvasActionType.DRAW_ELLIPSE
                                    && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                onAction(
                                    DrawingAction.OnEllipseStateChange(
                                        currentEllipse.copy(
                                            center = currentEllipse.center + (c.position - lastPosition)
                                        )
                                    )
                                )
                                lastPosition = c.position
                                c.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // RECTANGLE
                            // ════════════════════════════════════════════════════
                            currentActionType == CanvasActionType.DRAW_RECTANGLE
                                    && pointerCount >= 2 -> {
                                val zoom = event.calculateZoom()
                                onAction(
                                    DrawingAction.OnRectangleStateChange(
                                        currentRectangle.copy(
                                            center   = currentRectangle.center + event.calculatePan(),
                                            width    = (currentRectangle.width  * zoom).coerceIn(20f, 1800f),
                                            height   = (currentRectangle.height * zoom).coerceIn(20f, 1800f),
                                            rotation = currentRectangle.rotation +
                                                    (event.calculateRotation() * 180f / PI.toFloat()),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }

                            currentActionType == CanvasActionType.DRAW_RECTANGLE
                                    && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                onAction(
                                    DrawingAction.OnRectangleStateChange(
                                        currentRectangle.copy(
                                            center = currentRectangle.center + (c.position - lastPosition)
                                        )
                                    )
                                )
                                lastPosition = c.position
                                c.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // STAR
                            // 2 fingers: pan + zoom both radii proportionally + rotate
                            // ════════════════════════════════════════════════════
                            currentActionType == CanvasActionType.DRAW_STAR
                                    && pointerCount >= 2 -> {
                                val zoom = event.calculateZoom()
                                onAction(
                                    DrawingAction.OnStarStateChange(
                                        currentStar.copy(
                                            center      = currentStar.center + event.calculatePan(),
                                            outerRadius = (currentStar.outerRadius * zoom).coerceIn(30f, 900f),
                                            // innerRadius scales proportionally — keeps star proportions
                                            innerRadius = (currentStar.innerRadius * zoom).coerceIn(12f, 378f),
                                            rotation    = currentStar.rotation +
                                                    (event.calculateRotation() * 180f / PI.toFloat()),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }

                            currentActionType == CanvasActionType.DRAW_STAR
                                    && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                onAction(
                                    DrawingAction.OnStarStateChange(
                                        currentStar.copy(
                                            center = currentStar.center + (c.position - lastPosition)
                                        )
                                    )
                                )
                                lastPosition = c.position
                                c.consume()
                            }

                            // ── POLYGON ───────────────────────────────────────
                            // 2 fingers: pan + pinch scales radius + rotate
                            // sides never change via gesture — set from toolbar
                            currentActionType == CanvasActionType.DRAW_POLYGON
                                    && pointerCount >= 2 -> {
                                onAction(
                                    DrawingAction.OnPolygonStateChange(
                                        currentPolygon.copy(
                                            center   = currentPolygon.center + event.calculatePan(),
                                            radius   = (currentPolygon.radius * event.calculateZoom()).coerceIn(30f, 900f),
                                            rotation = currentPolygon.rotation + (event.calculateRotation() * 180f / PI.toFloat()),
                                        )
                                    )
                                )
                                event.changes.fastForEach { it.consume() }
                            }
                            currentActionType == CanvasActionType.DRAW_POLYGON
                                    && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                onAction(DrawingAction.OnPolygonStateChange(
                                    currentPolygon.copy(center = currentPolygon.center + (c.position - lastPosition))
                                ))
                                lastPosition = c.position ; c.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // TEXT
                            // ════════════════════════════════════════════════════

                            currentActionType == CanvasActionType.DRAW_TEXT && pointerCount >= 2 -> {
                                val zoom = event.calculateZoom()
                                onAction(DrawingAction.OnTextStateChange(
                                    currentText.copy(
                                        center   = currentText.center + event.calculatePan(),
                                        // Scale clamps: 0.3x original fontSize min, 8x max
                                        scale    = (currentText.scale * zoom).coerceIn(0.3f, 8f),
                                        rotation = currentText.rotation + (event.calculateRotation() * 180f / PI.toFloat()),
                                    )
                                ))
                                event.changes.fastForEach { it.consume() }
                            }

                            currentActionType == CanvasActionType.DRAW_TEXT && pointerCount == 1 -> {
                                val c = event.changes.firstOrNull() ?: continue
                                val delta = c.position - lastPosition

                                // Distinguish tap vs drag:
                                // If total movement is tiny it's a tap — handled at gesture end below.
                                // If movement crosses slop threshold, treat as drag immediately.
                                val isMeaningfulDrag = delta.getDistance() > 3f

                                if (isMeaningfulDrag) {
                                    onAction(DrawingAction.OnTextStateChange(
                                        currentText.copy(
                                            center = currentText.center + delta
                                        )
                                    ))
                                }
                                lastPosition = c.position
                                c.consume()
                            }

                            // ════════════════════════════════════════════════════
                            // FREEHAND (brush / highlighter / eraser)
                            // ════════════════════════════════════════════════════
                            currentActionType.isDrawTool() && pointerCount >= 1 -> {
                                val change = event.changes.firstOrNull() ?: continue

                                // Use historical positions for ultra-smooth, gap-free strokes.
                                // historicalData captures all intermediate touch samples between
                                // Compose recompositions (~60-120Hz screen vs 240Hz+ touch sensor).
                                val historicalPositions = change.historical
                                    .map { it.position }
                                    .plus(change.position)

                                historicalPositions.forEach { position ->
                                    velocityTracker.addPosition(
                                        change.uptimeMillis,
                                        position
                                    )
                                    onAction(DrawingAction.OnDraw(position))
                                }

                                change.consume()
                            }
                        }
                    } while (event.changes.any { it.pressed })


                    // ── Gesture ended — handle TEXT tap (place or edit) ───────

                    if (currentActionType == CanvasActionType.DRAW_TEXT) {
                        val totalMove = (lastPosition - firstDown.position).getDistance()
                        val wasTap = totalMove < 10f   // under 10px = tap, not drag

                        if (wasTap) {
                            // Check if the tap landed inside the current text bounds
                            val textHit = textEverPlaced &&
                                    isInsideTextBounds(firstDown.position, currentText, textLayout)

                            if (textHit) {
                                // Tap on existing text → open edit dialog with current content
                                onTextEditRequest(currentText.text)
                            } else {
                                // Tap on empty canvas → place text at tap position
                                onTextPlaceRequest(firstDown.position)
                            }
                        }
                    }

                    // ── 3. All fingers lifted ──────────────────────────────────
                    if (currentActionType.isDrawTool()) {
                        onAction(DrawingAction.OnPathEnd)
                    }
                }
            }
    ) {


        // ── Draw committed paths ───────────────────────────────────────────────
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                thickness = pathData.penSize
            )
        }

        // ═════════════════════════════════════════════════════════════════════
        // Shapes never appear here — they live in Layer 2 permanently
        // ═════════════════════════════════════════════════════════════════════
        when (currentActionType) {
            CanvasActionType.DRAW_BRUSH -> {
                currentPath?.let {
                    drawPath(path = it.path, color = it.color, thickness = it.penSize)
                }
            }

            CanvasActionType.DRAW_HIGHLIGHTER -> {
                currentPath?.let {
                    drawGradientPath(path = it.path, thickness = it.penSize)
                }
            }

            CanvasActionType.DRAW_CIRCLE -> {}
            CanvasActionType.DRAW_ERASER -> {}
            CanvasActionType.DRAW_NOTE -> {}
            CanvasActionType.DRAW_IMAGE -> {}
            CanvasActionType.DRAW_SQUARE -> {}
            CanvasActionType.DRAW_LINE -> {}
            CanvasActionType.DRAW_ELLIPSE -> {}
            CanvasActionType.DRAW_STAR -> {}
            CanvasActionType.DRAW_RECTANGLE -> {}
            CanvasActionType.DRAW_TEXT -> {}
            CanvasActionType.DRAW_POLYGON -> {}
            CanvasActionType.DRAW_UNDEFINED -> {}
        }

        // ═════════════════════════════════════════════════════════════════════
        // LAYER 3 — Active in-progress freehand stroke only
        // ═════════════════════════════════════════════════════════════════════
        if (circleEverPlaced) {
            rotate(
                degrees = circleShapeState.rotation,
                pivot   = circleShapeState.center,
            ) {
                drawCircle(
                    color  = HighlighterColor1,
                    radius = circleShapeState.radius,
                    center = circleShapeState.center,
                )
            }
        }

        if (squareEverPlaced) {
            val half = squareShapeState.size / 2f
            rotate(
                degrees = squareShapeState.rotation,
                pivot   = squareShapeState.center,
            ) {
                drawRect(
                    color   = HighlighterColor1,
                    topLeft = Offset(squareShapeState.center.x - half, squareShapeState.center.y - half),
                    size    = Size(squareShapeState.size, squareShapeState.size),
                )
            }
        }

        if (lineEverPlaced) {
            // pan is applied to both endpoints together
            drawLine(
                color       = HighlighterColor1,
                start       = lineShapeState.start + lineShapeState.pan,
                end         = lineShapeState.end   + lineShapeState.pan,
                strokeWidth = 6f,
                cap         = StrokeCap.Round,
            )
        }

        if (ellipseEverPlaced) {
            rotate(ellipseShapeState.rotation, ellipseShapeState.center) {
                drawOval(
                    color   = HighlighterColor1,
                    topLeft = Offset(
                        ellipseShapeState.center.x - ellipseShapeState.radiusX,
                        ellipseShapeState.center.y - ellipseShapeState.radiusY,
                    ),
                    size  = Size(
                        ellipseShapeState.radiusX * 2f,
                        ellipseShapeState.radiusY * 2f,
                    ),
                )
            }
        }

        if (rectangleEverPlaced) {
            val hw = rectangleShapeState.width  / 2f
            val hh = rectangleShapeState.height / 2f
            rotate(rectangleShapeState.rotation, rectangleShapeState.center) {
                drawRect(
                    color   = HighlighterColor1,
                    topLeft = Offset(
                        rectangleShapeState.center.x - hw,
                        rectangleShapeState.center.y - hh,
                    ),
                    size  = Size(rectangleShapeState.width, rectangleShapeState.height),
                )
            }
        }

        if (starEverPlaced) {
            rotate(starShapeState.rotation, starShapeState.center) {
                drawPath(
                    path  = buildStarPath(
                        center      = starShapeState.center,
                        outerRadius = starShapeState.outerRadius,
                        innerRadius = starShapeState.innerRadius,
                    ),
                    color = HighlighterColor1,
                )
            }
        }

        if (polygonEverPlaced) {
            rotate(polygonShapeState.rotation, polygonShapeState.center) {
                drawPath(
                    path  = buildPolygonPath(
                        center = polygonShapeState.center,
                        radius = polygonShapeState.radius,
                        sides  = polygonShapeState.sides,
                    ),
                    color = HighlighterColor1,
                )
            }
        }

        // ── TEXT — drawn with withTransform so pan+rotate+scale are ONE GPU op ─
        if (textEverPlaced) {
            val tw = textLayout.size.width.toFloat()
            val th = textLayout.size.height.toFloat()

            // topLeft is offset so the text CENTER sits exactly at textState.center.
            // This makes rotation pivot around the visual center of the text,
            // not its top-left corner — which would look completely wrong.
            val topLeft = Offset(
                x = textState.center.x - tw / 2f,
                y = textState.center.y - th / 2f,
            )

            withTransform({
                // All three transforms combined into one matrix multiplication —
                // cheaper than nesting rotate {} inside translate {} inside scale {}
                rotate(
                    degrees = textState.rotation,
                    pivot   = textState.center,    // rotate around text center
                )
            }) {
                drawText(
                    textLayoutResult = textLayout,
                    topLeft          = topLeft,
                    color            = textState.color,
                )
            }

            // ── Selection indicator: dashed border around text when TEXT tool active
            if (currentActionType == CanvasActionType.DRAW_TEXT) {
                val padding = 8f
                withTransform({
                    rotate(textState.rotation, textState.center)
                }) {
                    drawRect(
                        color   = Color.Gray.copy(alpha = 0.6f),
                        topLeft = Offset(topLeft.x - padding, topLeft.y - padding),
                        size    = Size(tw + padding * 2f, th + padding * 2f),
                        style   = Stroke(
                            width       = 1.5f,
                            pathEffect  = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                        ),
                    )
                }
            }
        }


    }
}

// ── Extension: which tools do freehand drawing? ───────────────────────────────
private fun CanvasActionType.isDrawTool(): Boolean = when (this) {
    CanvasActionType.DRAW_BRUSH,
    CanvasActionType.DRAW_HIGHLIGHTER,
    CanvasActionType.DRAW_ERASER -> true

    else -> false
}

// ── Path rendering helpers ────────────────────────────────────────────────────
internal fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    val smoothedPath = buildSmoothPath(path)
    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(width = thickness, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

internal fun DrawScope.drawGradientPath(
    path: List<Offset>,
    thickness: Float = 10f
) {
    val smoothedPath = buildSmoothPath(path)
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Magenta, Color.Blue, Color.Yellow, Color.DarkGray, Color.Cyan),
        start = Offset.Zero,
        end = Offset(size.width, size.height),
    )
    drawPath(
        path = smoothedPath,
        brush = gradientBrush,
        style = Stroke(width = thickness, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}

internal fun DrawScope.buildStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int = 5,
): Path = Path().apply {
    val totalVerts = points * 2
    val step       = (2.0 * PI / totalVerts).toFloat()
    val startAngle = (-PI / 2).toFloat()          // first point straight up

    repeat(totalVerts) { i ->
        val angle  = startAngle + step * i
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

/**
 * Builds a quadratic-bezier smoothed path from raw offset points.
 *
 * Uses a smoothness threshold to skip micro-jitter points that would
 * create jagged segments — critical for the fast hand movements of
 * young artists drawing expressively.
 */
private fun buildSmoothPath(points: List<Offset>): Path = Path().apply {
    if (points.isEmpty()) return@apply
    moveTo(points.first().x, points.first().y)

    // Smoothness = minimum pixel delta before we draw a new segment.
    // Lower value = more responsive but slightly more CPU per stroke.
    // 2f is ideal for stylus/finger on modern 240Hz+ touch screens.
    val smoothness = 5f

    for (i in 1..points.lastIndex) {
        val from = points[i - 1]
        val to = points[i]
        if (abs(from.x - to.x) >= smoothness || abs(from.y - to.y) >= smoothness) {
            quadraticTo(
                x1 = (from.x + to.x) / 2f,
                y1 = (from.y + to.y) / 2f,
                x2 = to.x,
                y2 = to.y
            )
        }
    }
}

private fun buildPolygonPath(
    center: Offset,
    radius: Float,
    sides: Int,
    rotationDeg: Float = -90f,   // -90° so flat edge sits on bottom for even-sided shapes
): Path = Path().apply {
    val step       = (2.0 * PI / sides).toFloat()
    val startAngle = rotationDeg * PI.toFloat() / 180f
    repeat(sides) { i ->
        val angle = startAngle + step * i
        val x = center.x + radius * cos(angle)
        val y = center.y + radius * sin(angle)
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

private fun isInsideTextBounds(
    touchPos: Offset,
    state: TextState,
    layout: TextLayoutResult,
): Boolean {
    val tw = layout.size.width.toFloat()
    val th = layout.size.height.toFloat()

    // Inverse-rotate touch around text center to get local-space position
    val angleRad = (-state.rotation * PI / 180.0).toFloat()
    val dx = touchPos.x - state.center.x
    val dy = touchPos.y - state.center.y
    val localX = dx * cos(angleRad) - dy * sin(angleRad)
    val localY = dx * sin(angleRad) + dy * cos(angleRad)

    // Now test against the unrotated text rect (centered at origin after inverse-rotate)
    val pad = 16f
    return localX in (-tw / 2f - pad)..(tw / 2f + pad) &&
            localY in (-th / 2f - pad)..(th / 2f + pad)
}