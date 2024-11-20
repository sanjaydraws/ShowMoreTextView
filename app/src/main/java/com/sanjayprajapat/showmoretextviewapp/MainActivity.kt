package com.sanjayprajapat.showmoretextviewapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sanjayprajapat.showmoretextview.R
import com.sanjayprajapat.showmoretextview.ShowMoreTextView
import com.sanjayprajapat.showmoretextview.enums.TextState
import com.sanjayprajapat.showmoretextview.listener.StateChangeListener
import com.sanjayprajapat.showmoretextview.utils.getColorCompat
import com.sanjayprajapat.showmoretextview.utils.getFontFamilyMedium
import com.sanjayprajapat.showmoretextviewapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
        }
        binding?.showMoreText?.addOnStateChangeListener(object :StateChangeListener{
            override fun onStateChange(textState: TextState) {
                when(textState){
                    TextState.EXPANDED -> Toast.makeText(this@MainActivity, "Expanded",Toast.LENGTH_SHORT).show()
                    TextState.COLLAPSED -> Toast.makeText(this@MainActivity, "Collapsed",Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding?.showMoreText?.apply {
            setMoreTextSize(23f)
        }
        val targets = listOf(
            Triple(
                "globalchauffeurdrive@avis.co.in",
                getFontFamilyMedium("fonts/montserrat_semibold"),
                getColorCompat(com.sanjayprajapat.showmoretextviewapp.R.color.red_90)
            ),
            Triple(
                "+91 124 4724 888",
                getFontFamilyMedium("fonts/montserrat_semibold"),
                getColorCompat(com.sanjayprajapat.showmoretextviewapp.R.color.red_90)
            ),
        )
        val clickHandlers = listOf(
            { email: String ->
                // Open email client
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }

                // Check if an email client is available
                packageManager?.let {
                    emailIntent.resolveActivity(it)?.let {
                        startActivity(emailIntent)
                    }
                } ?: run {
                    // No email client found
                    Toast.makeText(this@MainActivity, "No email client installed.", Toast.LENGTH_SHORT).show()
                }
            },
            { phoneNumber: String ->
                // Open dialer for the phone number
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(dialIntent)
            }
        )
//
//
        binding?.showMoreText1?.applyMultipleClickWithFont(
            targets, clickHandlers
        )


    }

    //for multiple fontFamily With Click
    fun ShowMoreTextView.applyMultipleClickWithFont(
        targets: List<Triple<String?, Typeface?, Int?>>,
        clickHandlers: List<((String) -> Unit)?> = listOf() // Optional list of click handlers
    ) {
        val text = this.text.toString()
        val spannableString = SpannableString(text)

        // Enable text selection for clickable spans
        this.movementMethod = LinkMovementMethod.getInstance()
        this.highlightColor = Color.TRANSPARENT // Disable text highlight background color when clicked

        for ((index, target) in targets.withIndex()) {
            val (targetText, fontFamily, targetColor) = target
            if (targetText.isNullOrEmpty()) continue

            Log.d("TAG", "applyMultipleFontsAndColors: $targetText")
            val start = text.indexOf(targetText)
            if (start == -1) continue // Target text not found in the TextView's text

            val end = start + targetText.length

            // Apply color if provided
            targetColor?.let {
                val colorSpan = ForegroundColorSpan(it)
                spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Apply bold style
            val styleSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Apply custom font family if provided
            fontFamily?.let {
                val typefaceSpan = CustomTypefaceSpan(it)
                spannableString.setSpan(typefaceSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Add click handling using ClickableSpan
            val clickHandler = clickHandlers.getOrNull(index)
            clickHandler?.let {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        // Call the specific clickHandler for this targetText
                        it(targetText)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false // Remove underline from clickable text
                    }
                }
                spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        this.text = spannableString
    }

    class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("") {

        override fun updateDrawState(ds: android.text.TextPaint) {
            applyCustomTypeFace(ds, newType)
        }

        override fun updateMeasureState(paint: android.text.TextPaint) {
            applyCustomTypeFace(paint, newType)
        }

        private fun applyCustomTypeFace(paint: android.text.TextPaint, tf: Typeface) {
            val oldStyle: Int
            val old = paint.typeface
            oldStyle = old?.style ?: 0

            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }

            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.typeface = tf
        }
    }

}