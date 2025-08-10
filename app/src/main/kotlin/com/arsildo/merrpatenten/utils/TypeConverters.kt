package com.arsildo.merrpatenten.utils

import java.util.Locale
import java.util.concurrent.TimeUnit

fun formatTimer(millisUntilFinished: Long): String {
    return String.format(
        Locale.getDefault(),
        format = "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
    )
}

fun formatQuestion(question: String): String {
    val lines = question.lines().filter { it.isNotBlank() }
    return lines.joinToString(" ")
}