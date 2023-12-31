package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private lateinit var gameSettings: GameSettings

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    // lD
    private val _formattedTime = MutableLiveData<String>()
    val formattedTimeLd: LiveData<String> get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val questionLd: LiveData<Question> get() = _question

    private val _currentPercentOfRightAnswers = MutableLiveData<Int>()
    val currentPercentOfRightAnswersLd: LiveData<Int> get() = _currentPercentOfRightAnswers

    private val _correctAnswersInfo = MutableLiveData<String>()
    val correctAnswersInfoLd: LiveData<String> get() = _correctAnswersInfo

    private val _isEnoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val isEnoughCountOfRightAnswersLd: LiveData<Boolean> get() = _isEnoughCountOfRightAnswers

    private val _isEnoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val isEnoughPercentOfRightAnswersLd: LiveData<Boolean> get() = _isEnoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercentLd: LiveData<Int> get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResultLd: LiveData<GameResult> get() = _gameResult

    init {
        startGame()
    }

    override fun onCleared() {
        super.onCleared()
        timer = null
    }

    companion object {
        private const val MILLS_IN_ONE_SECOND = 1000L
        private const val SECONDS_IN_ONE_MINUTE = 60
    }

    // FUNCTIONS
    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    //  PRIVATE FUNCTIONS
    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }
    private fun updateProgress() {
        val percentOfRightAnswers = calculatePercentOfRightAnswers()
        _currentPercentOfRightAnswers.value = percentOfRightAnswers
        val progressInfo = String.format(
            application.resources.getString(R.string.your_and_minimum_correct_answers_amount),
            countOfRightAnswers,
            gameSettings.minCountOfCorrectAnswers
        )
        _correctAnswersInfo.value = progressInfo

        _isEnoughCountOfRightAnswers.value =
            countOfRightAnswers >= gameSettings.minCountOfCorrectAnswers
        _isEnoughPercentOfRightAnswers.value =
            percentOfRightAnswers >= gameSettings.minPercentOfCorrectAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfRightAnswers == 0) {
            return 0
        }
        return ((countOfRightAnswers.toDouble() / countOfQuestions) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = questionLd.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            isEnoughCountOfRightAnswersLd.value == true && isEnoughPercentOfRightAnswersLd.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfCorrectAnswers
    }

    private fun formatTime(millis: Long): String {
        val secondsTotal = millis / MILLS_IN_ONE_SECOND
        val minutes = secondsTotal / SECONDS_IN_ONE_MINUTE
        val minutesToSeconds = minutes * SECONDS_IN_ONE_MINUTE
        val secondsLeft = secondsTotal - minutesToSeconds
        return String.format("%02d:%02d", minutes, secondsLeft)
    }

    private fun startTimer() {
        val gameTimeInMillis = gameSettings.gameTimeInSeconds * MILLS_IN_ONE_SECOND
        timer = object : CountDownTimer(gameTimeInMillis, MILLS_IN_ONE_SECOND) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formatTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }
}