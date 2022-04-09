package com.sanjayprajapat.showmoretextview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sanjayprajapat.showmoretextview.databinding.ActivityMainBinding
import com.sanjayprajapat.showmoretextview.enums.TextState
import com.sanjayprajapat.showmoretextview.listener.StateChangeListener


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


    }
}