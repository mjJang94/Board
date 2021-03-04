package com.mj.board.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.databinding.ActivityAddBoardBinding
import com.mj.board.viewmodel.AddViewModel
import org.koin.android.ext.android.inject
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener


class AddBoardActivity : AppCompatActivity() {

    private val viewmodel: AddViewModel by inject()
    private lateinit var binding: ActivityAddBoardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_board)

        init()

    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_board)
        binding.lifecycleOwner = this
        binding.addViewModel = viewmodel

        viewmodel.finishActivity = {
            this.finish()
        }

        viewmodel.insertComplete = {
            Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show()
            afterSave()
        }

        binding.llColor.setOnClickListener {
            openColorPicker()
        }
    }

    fun openColorPicker() {
        val colorPicker = ColorPicker(this)
        val colors: ArrayList<String> = ArrayList()
        colors.add("#ffffff")
        colors.add("#ffab91")
        colors.add("#F48FB1")
        colors.add("#ce93d8")
        colors.add("#b39ddb")
        colors.add("#9fa8da")
        colors.add("#90caf9")
        colors.add("#81d4fa")
        colors.add("#80deea")
        colors.add("#80cbc4")
        colors.add("#c5e1a5")
        colors.add("#e6ee9c")
        colors.add("#fff59d")
        colors.add("#ffe082")
        colors.add("#ffcc80")
        colorPicker.setColors(colors)
            .setTitle("")
            .setDefaultColorButton(Color.parseColor(viewmodel.boardColor.value))
            .disableDefaultButtons(true)
            .setColumns(5)
            .setDefaultColorButton(Color.parseColor("#ffffff"))
            .setRoundColorButton(true)
            .addListenerButton("선택") { _, position, _ ->
                colorPicker.dismissDialog()
                viewmodel.boardColor.value = colors[position]
                binding.llColor.setBackgroundColor(Color.parseColor(colors[position]))
            }
            .show() // dialog 생성
    }

    private fun afterSave() {
        binding.etTitle.setText("")
        binding.etContent.setText("")
        binding.etTitle.clearFocus()
        binding.etContent.clearFocus()
        binding.llColor.setBackgroundColor(Color.parseColor(viewmodel.boardColor.value))
    }
}