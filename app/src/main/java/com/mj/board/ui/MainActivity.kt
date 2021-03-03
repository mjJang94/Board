package com.mj.board.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewmodel : MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        init()
    }

    private fun init(){

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewmodel

        viewmodel.goToAddBoard = {
            startActivity(Intent(this, AddBoardActivity::class.java))
        }
    }

    private fun initLayout(){

    }
}