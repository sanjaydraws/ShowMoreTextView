package com.sanjayprajapat.showmoretextview.listener

import com.sanjayprajapat.showmoretextview.enums.TextState

/**
 * @author : Sanjay Prajapat
 * Created On: 09-4-2022
 * Github:https://github.com/sanjaydraws
 * Blog: https://dev.to/sanjayprajapat
 * https://sanjayprajapat.hashnode.dev/
 ***/


interface StateChangeListener {
    fun onStateChange(textState:TextState)
}