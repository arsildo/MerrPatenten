package com.arsildo.merrpatenten.shared.core.designsystem.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val CloudExclamationPoint: ImageVector
    get() {
        if (_CloudExclamationPoint != null) {
            return _CloudExclamationPoint!!
        }
        _CloudExclamationPoint = ImageVector.Builder(
            name = "CloudExclamationPoint",
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
                moveTo(12f, 12f)
                verticalLineToRelative(4f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(12f, 20f)
                horizontalLineToRelative(0.01f)
            }
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(8.128f, 16.949f)
                arcTo(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 15.71f, 8f)
                horizontalLineToRelative(1.79f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 9f)
                horizontalLineToRelative(-1.642f)
            }
        }.build()

        return _CloudExclamationPoint!!
    }

@Suppress("ObjectPropertyName")
private var _CloudExclamationPoint: ImageVector? = null
