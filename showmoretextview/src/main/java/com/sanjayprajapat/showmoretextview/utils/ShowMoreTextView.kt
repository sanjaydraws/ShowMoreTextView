package com.sanjayprajapat.showmoretextview.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.sanjayprajapat.showmoretextview.R
import com.sanjayprajapat.showmoretextview.enums.TextState
import com.sanjayprajapat.showmoretextview.listener.StateChangeListener


class ShowMoreTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    companion object {
        private const val MAX_LINE_DEFAULT = 3
    }

    private var showMoreMaxLine:Int? = MAX_LINE_DEFAULT
    private var showMoreText:String? = context.getString(R.string.show_more)
    private var showMoreColor:Int? = ContextCompat.getColor(context, R.color.show_more_color)

    private var stateChangeListener:StateChangeListener? = null

    private var expendedText:CharSequence =""
    private var collapsedText:CharSequence =""


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



    private fun setupAttrs(context:Context?, attrs:AttributeSet?, defStyleAttr: Int?){
        val typedArray = context?.obtainStyledAttributes(attrs,R.styleable.ShowMoreTextView,defStyleAttr?:0,0)
        showMoreMaxLine = typedArray?.getInt(R.styleable.ShowMoreTextView_showMoreMaxLine,showMoreMaxLine?:0)
        showMoreColor = typedArray?.getColor(R.styleable.ShowMoreTextView_showMoreTextColor,showMoreColor?:0)
        showMoreText = typedArray?.getString(R.styleable.ShowMoreTextView_showMoreText)?:showMoreText

    }

    private fun setUpListener(){
        super.setOnClickListener{
            switchText()
        }
    }

    private fun switchText(){
        when(textState) {
            TextState.EXPANDED -> doCollapse()
            TextState.COLLAPSED -> doExpand()
        }
    }

    fun doCollapse(){

    }

    fun doExpand(){

    }


}