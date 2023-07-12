package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private val args by navArgs<GameFinishedFragmentArgs>()
    private val gameResult by lazy { args.gameResult }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTryAgain.setOnClickListener { retryGame() }
        showGameResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // PRIVATE FUNCTIONS
    private fun showGameResult() {
        binding.gameResult = args.gameResult
        setCorrectImage(gameResult.winner)
//        showRequiredAmountOfRightAnswers()
//        showYourAmountOfRightAnswers()
//        showRequiredPercentOfRightAnswers()
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
        findNavController().popBackStack()
    }
}