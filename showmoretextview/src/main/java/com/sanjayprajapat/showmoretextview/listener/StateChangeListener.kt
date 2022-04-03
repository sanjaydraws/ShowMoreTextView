package com.sanjayprajapat.showmoretextview.listener

import com.sanjayprajapat.showmoretextview.enums.TextState

interface StateChangeListener {
    fun onStateChange(textState:TextState)
}