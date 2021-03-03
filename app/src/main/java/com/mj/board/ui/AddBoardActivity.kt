package com.mj.board.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.databinding.ActivityAddBoardBinding
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.viewmodel.AddViewModel
import com.mj.board.viewmodel.MainViewModel
import org.koin.android.ext.android.inject

class AddBoardActivity : AppCompatActivity() {

    private val viewmodel : AddViewModel by inject()
    private lateinit var binding: ActivityAddBoardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_board)

        init()

    }

    private fun init(){

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_board)
        binding.lifecycleOwner = this
        binding.addViewModel = viewmodel

        viewmodel.finishActivity = {
            this.finish()
        }
    }
}