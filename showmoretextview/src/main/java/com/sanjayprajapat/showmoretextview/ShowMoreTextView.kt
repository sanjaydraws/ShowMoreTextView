
package com.sanjayprajapat.showmoretextview

import android.content.Context
import android.graphics.Rect
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.doOnLayout
import androidx.core.view.isInvisible
import com.sanjayprajapat.showmoretextview.enums.TextState
import com.sanjayprajapat.showmoretextview.listener.StateChangeListener
import com.sanjayprajapat.showmoretextview.utils.safeToFloat
import com.sanjayprajapat.showmoretextview.utils.safeToInt
import com.sanjayprajapat.showmoretextview.utils.toSp

/**
 * @author : Sanjay Prajapat
 * Created On: 09-4-2022
 * Github:https://github.com/sanjaydraws
 * Blog: https://dev.to/sanjayprajapat
 * https://sanjayprajapat.hashnode.dev/
 ***/

class ShowMoreTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    companion object {
        private const val MAX_LINE_DEFAULT = 3
        private const val DEFAULT_SHOW_MORE_TEXT_SIZE = 14f

    }


    private var showMoreMaxLine:Int? = MAX_LINE_DEFAULT
    private var showMoreText:String? = context.getString(R.string.show_more)
    private var showMoreColor:Int? = ContextCompat.getColor(context, R.color.show_more_color)
    private var showMoreTextSize: Float? = DEFAULT_SHOW_MORE_TEXT_SIZE

    private var stateChangeListener:StateChangeListener? = null

    /**
     * this is Original text
     * */
    private var expendedText:CharSequence? =""
    /**
     *
     * */
    private var collapsedText:CharSequence? =""


    /**
     * default text state is collapsed
     * */
    var textState:TextState = TextState.COLLAPSED
        private set(value){
            field = value
            text = when(value){
                TextState.EXPANDED -> expendedText
                TextState.COLLAPSED -> collapsedText
            }
            stateChangeListener?.onStateChange(value)
        }

    val isTextExpanded
        get() = textState == TextState.EXPANDED

    val isTextCollapsed
        get() = textState == TextState.COLLAPSED



    init {
        setupAttrs(context, attrs, defStyleAttr)
        setUpListener()
    }
    private fun setupAttrs(context:Context?, attrs:AttributeSet?, defStyleAttr: Int?){
        val typedArray = context?.obtainStyledAttributes(attrs,R.styleable.ShowMoreTextView,defStyleAttr?:0,0)
        showMoreMaxLine = typedArray?.getInt(R.styleable.ShowMoreTextView_showMoreMaxLine,showMoreMaxLine?:0)
        showMoreColor = typedArray?.getColor(R.styleable.ShowMoreTextView_showMoreTextColor,showMoreColor?:0)
        showMoreText = typedArray?.getString(R.styleable.ShowMoreTextView_showMoreText)?:showMoreText
        showMoreTextSize = typedArray?.getDimension(R.styleable.ShowMoreTextView_showMoreTextSize, showMoreTextSize?.toSp(context).safeToFloat()) ?: showMoreTextSize

    }

    private fun setUpListener(){
        super.setOnClickListener{
//            Toast.makeText(context,"click",Toast.LENGTH_LONG).show()
            switchTextState()
        }
    }

    private fun switchTextState(){
        when(textState) {
            TextState.EXPANDED -> doOnCollapse()
            TextState.COLLAPSED -> doOnExpand()
        }
    }

    private fun doOnCollapse(){
        if(isTextCollapsed || collapsedText.isNullOrEmpty())
            return
        textState = TextState.COLLAPSED
    }

    private fun doOnExpand(){
        if(isTextExpanded || expendedText.isNullOrEmpty())
            return
        textState = TextState.EXPANDED
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)

        /**
         * if view has been laid out and has not requested a layout , the action will be performed straight away
         * otherwise the action will be performed after the view is next laid out
         * */
        doOnLayout {
            post{
                setUpShowMoreTextView()
            }
        }
    }
    private fun setUpShowMoreTextView(){
        if(ifNeedToSkipSetup()){
            return
        }
        expendedText = text
        val adjustCutCount = getAdjustCutCount(maxLine = showMoreMaxLine, showMoreText) // 6
        val maxTextIndex = layout.getLineVisibleEnd(showMoreMaxLine?.minus(1).safeToInt())
        val originalSubText = expendedText?.substring(0, maxTextIndex - 1 - adjustCutCount)

        collapsedText = buildSpannedString {
            append(originalSubText)
            color(showMoreColor.safeToInt()) { append(showMoreText) }
            // Apply custom text size for the "Show More" part
            if (showMoreTextSize != null) {
                val sizeSpan = AbsoluteSizeSpan(showMoreTextSize!!.toInt()) // Apply text size here
                setSpan(sizeSpan, originalSubText?.length.safeToInt(), originalSubText?.length.safeToInt() + (showMoreText?.length ?: 0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        text = collapsedText
    }


    private fun ifNeedToSkipSetup():Boolean =   isInvisible || lineCount <= (showMoreMaxLine?:0) || isTextExpanded || text == null || text == collapsedText

    private fun  getAdjustCutCount(maxLine:Int?, readMoreText:String?):Int{
        val lastLineStartIndex = layout.getLineVisibleEnd(maxLine?.minus(2)?:0) + 1
        val lastLineEndIndex = layout.getLineVisibleEnd(maxLine?.minus(1)?:0)
        val lastLineText = text.substring(lastLineStartIndex, lastLineEndIndex)//available, but the majority have suffered alteration in

        val bounds = Rect()
        paint.getTextBounds(lastLineText, 0 , lastLineText.length, bounds)//Rect(1, -22 - 647, 6)
        var adjustCutCount = -1
        do {
            adjustCutCount++
            val subText = lastLineText.substring(0, lastLineText.length- adjustCutCount)
            val replacedText = subText + readMoreText
            paint.getTextBounds(replacedText, 0, replacedText.length, bounds)
            val replacedTextWidth = bounds.width()
        }while (replacedTextWidth>width)
        return adjustCutCount
    }

    public  fun addOnStateChangeListener(stateChangeListener: StateChangeListener) {
        this.stateChangeListener = stateChangeListener
    }


    /**
     * Updates the text size for the "Show More" part programmatically.
     * @param size The new text size in SP (scale-independent pixels).
     */
    fun setMoreTextSize(size:Float?){
        showMoreTextSize = size?.toSp(context).safeToFloat()
        setUpShowMoreTextView()
    }
}