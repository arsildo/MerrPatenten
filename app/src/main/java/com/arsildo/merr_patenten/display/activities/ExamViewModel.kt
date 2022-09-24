package com.arsildo.merr_patenten.display.activities

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.arsildo.merr_patenten.logic.database.Database
import com.arsildo.merr_patenten.logic.database.DatabaseExtension
import com.arsildo.merr_patenten.logic.database.DatabaseModel
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit


class ExamViewModel : ViewModel() {

    val isExamCompleted = mutableStateOf(false)
    val countDownTimer = mutableStateOf("40:00")

    val generatedQuestions: List<DatabaseModel> = generateQuestions()

    init {
        startCountDown()

    }

    private fun generateQuestions(): MutableList<DatabaseModel> {
        val dataList = Database().dataListP1.plus(DatabaseExtension().dataListP2)
        val randomQuestions = mutableListOf<DatabaseModel>()
        for (index in 0..39) {
            val randomNumber = Random().nextInt(dataList.size)
            randomQuestions.add(index, dataList[randomNumber])
        }
        return randomQuestions
    }

    private fun startCountDown() {
        object : CountDownTimer(144000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countDownTimer.value = formatCountDownTimer(millisUntilFinished)
                if (isExamCompleted.value) cancel()
            }

            override fun onFinish() {
                isExamCompleted.value = true
                cancel()
            }

        }.start()
    }

    private fun formatCountDownTimer(millisUntilFinished: Long): String {
        return String.format(
            Locale.getDefault(),
            format = "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
        )
    }

}