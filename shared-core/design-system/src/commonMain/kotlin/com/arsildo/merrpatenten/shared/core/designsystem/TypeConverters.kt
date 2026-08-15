package com.arsildo.merrpatenten.shared.core.designsystem

fun formatTimer(millisUntilFinished: Long): String {
    val totalSeconds = (millisUntilFinished / 1000).toInt()
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60
    val formattedMinutes = if (minutes < 10) "0$minutes" else "$minutes"
    val formattedSeconds = if (seconds < 10) "0$seconds" else "$seconds"
    return "$formattedMinutes:$formattedSeconds"
}

fun formatQuestion(question: String): String {
    val lines = question.lines().filter { it.isNotBlank() }
    return lines.joinToString(" ")
}
