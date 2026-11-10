package com.arsildo.merrpatenten.shared.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Car: ImageVector
    get() {
        if (_Car != null) {
            return _Car!!
        }
        _Car = ImageVector.Builder(
            name = "Car",
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
                moveTo(19f, 17f)
                horizontalLineToRelative(2f)
                curveToRelative(0.6f, 0f, 1f, -0.4f, 1f, -1f)
                verticalLineToRelative(-3f)
                curveToRelative(0f, -0.9f, -0.7f, -1.7f, -1.5f, -1.9f)
                curveTo(18.7f, 10.6f, 16f, 10f, 16f, 10f)
                reflectiveCurveToRelative(-1.3f, -1.4f, -2.2f, -2.3f)
                curveToRelative(-0.5f, -0.4f, -1.1f, -0.7f, -1.8f, -0.7f)
                horizontalLineTo(5f)
                curveToRelative(-0.6f, 0f, -1.1f, 0.4f, -1.4f, 0.9f)
                lineToRelative(-1.4f, 2.9f)
                arcTo(3.7f, 3.7f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 12f)
                verticalLineToRelative(4f)
                curveToRelative(0f, 0.6f, 0.4f, 1f, 1f, 1f)
                horizontalLineToRelative(2f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(7f, 17f)
                moveToRelative(-2f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(9f, 17f)
                horizontalLineToRelative(6f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(17f, 17f)
                moveToRelative(-2f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 0f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, 0f)
            }
        }.build()

        return _Car!!
    }

@Suppress("ObjectPropertyName")
private var _Car: ImageVector? = null
