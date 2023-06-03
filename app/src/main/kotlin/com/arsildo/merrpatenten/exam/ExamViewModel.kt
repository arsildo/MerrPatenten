package com.arsildo.merrpatenten.exam

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.data.Question
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import com.arsildo.merrpatenten.data.local.QuestionnaireRepository
import com.arsildo.merrpatenten.utils.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.utils.formatTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Random
import javax.inject.Inject

data class ExamUiState(
    val isCompleted: Boolean = false,
    val saveStats: Boolean = false,
    val timer: String = "00:00",
    val errors: Int = 0,
    var questions: List<Question> = emptyList()
)

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val questionnaireRepository: QuestionnaireRepository,
) : ViewModel() {

    private val generatedQuestions = generateQuestions()

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = combine(
        _uiState,
        preferencesRepository.getSaveStats,
    ) { state, stats ->
        ExamUiState(
            isCompleted = state.isCompleted,
            errors = state.errors,
            timer = state.timer,
            saveStats = stats,
            questions = generatedQuestions
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExamUiState()
    )

    val trueCheckedPositions = mutableStateListOf<Boolean>()
    val falseCheckedPositions = mutableStateListOf<Boolean>()
    val responseList = mutableStateListOf<String>()
    val mistakePositions = mutableStateListOf<Int>()

    init {
        fillExamWithDefaults()
        startCountDown()
    }

    private fun startCountDown(): CountDownTimer = object : CountDownTimer(360000, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            if (!uiState.value.isCompleted) updateTimer(millisUntilFinished)
            else concludeExam()
        }

        override fun onFinish() {
            cancel()
            concludeExam()
        }

    }.start()

    private fun updateTimer(millis: Long) {
        _uiState.update { it.copy(timer = formatTimer(millis)) }
    }

    fun concludeExam() {
        _uiState.update {
            it.copy(
                isCompleted = true,
                errors = countErrors()
            )
        }
        Timber.tag("TimberLog").d("Exam Ended")
    }

    fun checkTrueAtPosition(position: Int) {
        trueCheckedPositions[position] = !trueCheckedPositions[position]
        if (trueCheckedPositions[position]) {
            falseCheckedPositions[position] = false
            responseList.set(index = position, "Saktë")
        } else responseList.set(index = position, "")
        Timber.tag("TimberLog").d("CHECKED true at $position")
    }

    fun checkFalseAtPosition(position: Int) {
        falseCheckedPositions[position] = !falseCheckedPositions[position]
        if (falseCheckedPositions[position]) {
            trueCheckedPositions[position] = false
            responseList.set(index = position, "Gabim")
        } else responseList.set(index = position, "")
        Timber.tag("TimberLog").d("CHECKED false at $position")
    }

    private fun generateQuestions(): List<Question> {
        val questionnaire = questionnaireRepository.getAll()
        val randomQuestions = mutableListOf<Question>()
        viewModelScope.launch {
            val questions = questionnaire.stateIn(this).value
            for (index in 0..39) {
                val randomNumber = Random().nextInt(questions.size)
                randomQuestions.add(index, questions[randomNumber])
            }
        }
        Timber.tag("TimberLog").d("GENERATED QUESTIONS")
        return randomQuestions
    }

    private fun countErrors(): Int {
        var errors = 0
        for (index in 0 until QUESTIONS_IN_EXAM) {
            if (responseList[index] != generatedQuestions[index].answer) {
                errors++
                mistakePositions.add(index, 1)
            } else mistakePositions.add(index, 0)
        }
        Timber.tag("TimberLog").d("$errors errors Made")
        return errors
    }

    private fun fillExamWithDefaults() {
        for (index in 0 until QUESTIONS_IN_EXAM) {
            trueCheckedPositions.add(index, false)
            falseCheckedPositions.add(index, false)
            responseList.add(index, "")
            mistakePositions.add(index, 1)
        }
    }

}
