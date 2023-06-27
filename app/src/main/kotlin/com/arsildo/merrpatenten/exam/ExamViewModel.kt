package com.arsildo.merrpatenten.exam

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsildo.merrpatenten.BuildConfig
import com.arsildo.merrpatenten.data.ExamResult
import com.arsildo.merrpatenten.data.Question
import com.arsildo.merrpatenten.data.local.ExamResultsDAO
import com.arsildo.merrpatenten.data.local.PreferencesRepository
import com.arsildo.merrpatenten.data.local.QuestionnaireRepository
import com.arsildo.merrpatenten.utils.EXAM_DURATION_DEBUG
import com.arsildo.merrpatenten.utils.EXAM_DURATION_RELEASE
import com.arsildo.merrpatenten.utils.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.utils.formatTimer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Random
import javax.inject.Inject

data class ExamUiState(
    val isCompleted: Boolean = false,
    val saveStats: Boolean = false,
    val errors: Int = 0,
    var questions: List<Question> = emptyList()
)

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val questionnaireRepository: QuestionnaireRepository,
    private val examResultsDAO: ExamResultsDAO,
) : ViewModel() {

    private val generatedQuestions = generateQuestions()

    var timer by mutableStateOf("00:00")
        private set

    private lateinit var countDownTimer: CountDownTimer

    private val _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = combine(
        _uiState,
        preferencesRepository.getSaveStats,
    ) { state, stats ->
        ExamUiState(
            isCompleted = state.isCompleted,
            errors = state.errors,
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

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(
            if (BuildConfig.DEBUG) EXAM_DURATION_DEBUG else EXAM_DURATION_RELEASE,
            1_000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                if (uiState.value.isCompleted) cancel()
                else timer = formatTimer(millisUntilFinished)
            }

            override fun onFinish() {
                concludeExam()
                cancel()
            }

        }.start()
    }

    fun completeExam() {
        _uiState.update { it.copy(isCompleted = true) }
    }

    private fun concludeExam() {
        _uiState.update {
            it.copy(
                isCompleted = true,
                errors = countErrors()
            )
        }
        countDownTimer.cancel()
        insertResult()
        Timber.tag("TimberLog").d("Exam Concluded")
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
            for (index in 0 until QUESTIONS_IN_EXAM) {
                val randomNumber = Random().nextInt(questions.size)
                Timber.tag("TimberLog").d("Generated question $randomNumber")
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

    private fun insertResult() {
        if (uiState.value.saveStats) viewModelScope.launch {
            withContext(Dispatchers.IO) {
                examResultsDAO.insertResult(
                    ExamResult(
                        errors = uiState.value.errors,
                        time = timer,
                    )
                )
                examResultsDAO.limitResults()
            }
        }
    }

}
