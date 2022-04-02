package com.sanjayprajapat.showmoretextview.listener

import com.sanjayprajapat.showmoretextview.enums.State

interface StateChangeListener {
    fun onStateChange(textState:State)
}