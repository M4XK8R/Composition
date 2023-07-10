package com.example.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.composition.GAME_FRAG_BACKSTACK_NAME
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBackAvoidGameFrag()
        binding.btnTryAgain.setOnClickListener { retryGame() }
        showGameResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_GAME_RESULT = "game_result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }

    // PRIVATE FUNCTIONS
    private fun showGameResult() {
        setCorrectImage(gameResult.winner)
        showRequiredAmountOfRightAnswers()
        showYourAmountOfRightAnswers()
        showRequiredPercentOfRightAnswers()
        showYourPercentOfRightAnswers()
    }

    private fun setCorrectImage(isWinner: Boolean) {
        if (isWinner) {
            binding.ivEmojiResult.setImageResource(R.drawable.smile)
        }
    }

    private fun showYourPercentOfRightAnswers() {
        val formatString = getString(R.string.right_answers_percentage)
        val yourPercentOfRightAnswers =
            ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
        binding.tvScorePercentage.text = String.format(formatString, yourPercentOfRightAnswers)
    }

    private fun showRequiredPercentOfRightAnswers() {
        val formatString = getString(R.string.required_answers_percentage)
        val requiredPercentOfRightAnswers = gameResult.gameSettings.minPercentOfRightAnswers
        binding.tvRequiredPercentage.text =
            String.format(formatString, requiredPercentOfRightAnswers)
    }

    private fun showYourAmountOfRightAnswers() {
        val formatString = getString(R.string.score)
        val yourAmountOfRightAnswers = gameResult.countOfRightAnswers
        binding.tvScore.text = String.format(formatString, yourAmountOfRightAnswers)
    }

    private fun showRequiredAmountOfRightAnswers() {
        val formatString = getString(R.string.required_answers_count)
        val requiredAmountOfRightAnswers = gameResult.gameSettings.minCountOfRightAnswers
        binding.tvRequiredAnswers.text = String.format(formatString, requiredAmountOfRightAnswers)
    }

    private fun getBackAvoidGameFrag() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GAME_FRAG_BACKSTACK_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }
}