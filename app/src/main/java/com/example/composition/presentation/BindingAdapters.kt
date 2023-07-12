package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("resultEmoji")
fun bindResultEmoji(imageView: ImageView, isWinner: Boolean) {
    val imageResId = if (isWinner) R.drawable.smile else R.drawable.upset
    imageView.setImageResource(imageResId)

}

@BindingAdapter("requiredAmountOfRightAnswers")
fun bindRequiredAmountOfRightAnswers(textView: TextView, requiredAmount: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_correct_answers_amount),
        requiredAmount
    )
}

@BindingAdapter("yourScore")
fun bindYourScore(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.score),
        score
    )
}

@BindingAdapter("minPercentOfRightAnswers")
fun bindMinPercentOfRightAnswers(textView: TextView, requiredPercentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage_of_correct_answers),
        requiredPercentage
    )
}

@BindingAdapter("yourPercentageOfCorrectAnswers")
fun bindYourPercentageOfCorrectAnswers(textView: TextView, yourPercentageOfCorrectAnswers: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.percentage_of_correct_answers),
        yourPercentageOfCorrectAnswers
    )
}

