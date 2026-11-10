package com.arsildo.merrpatenten.shared.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Vibrate: ImageVector
    get() {
        if (_Vibrate != null) {
            return _Vibrate!!
        }
        _Vibrate = ImageVector.Builder(
            name = "Vibrate",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveToRelative(2f, 8f)
                lineToRelative(2f, 2f)
                lineToRelative(-2f, 2f)
                lineToRelative(2f, 2f)
                lineToRelative(-2f, 2f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveToRelative(22f, 8f)
                lineToRelative(-2f, 2f)
                lineToRelative(2f, 2f)
                lineToRelative(-2f, 2f)
                lineToRelative(2f, 2f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(9f, 5f)
                lineTo(15f, 5f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 6f)
                lineTo(16f, 18f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15f, 19f)
                lineTo(9f, 19f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 18f)
                lineTo(8f, 6f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 5f)
                close()
            }
        }.build()

        return _Vibrate!!
    }

@Suppress("ObjectPropertyName")
private var _Vibrate: ImageVector? = null
