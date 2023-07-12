package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import kotlinx.coroutines.newFixedThreadPoolContext

// GAME FRAGMENT
@BindingAdapter("onOptionClickListenerBa")
fun bindOnOptionClickListener(textView: TextView, optionClickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        optionClickListener.onOptionClick(textView.text.toString().toInt())
    }
}

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

@BindingAdapter("numberToStringAdapter")
fun bindNumberToString(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("textViewColorAdapter")
fun bindTextViewColorAdapter(textView: TextView, isStateGood: Boolean) {
    textView.setTextColor(getColorByState(textView.context, isStateGood))
}

@BindingAdapter("progressBarColorAdapter")
fun bindProgressBarColor(progressBar: ProgressBar, isStateGood: Boolean) {
    val color = getColorByState(progressBar.context, isStateGood)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

private fun getColorByState(context: Context, isStateGood: Boolean): Int {
    val colorResId = if (isStateGood) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

// GAME FINISHED FRAGMENT
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

