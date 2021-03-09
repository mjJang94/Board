package com.mj.board.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.databinding.ActivityAddBoardBinding
import com.mj.board.viewmodel.AddViewModel
import org.koin.android.ext.android.inject
import petrov.kristiyan.colorpicker.ColorPicker


class AddBoardActivity : AppCompatActivity() {

    private val viewModel: AddViewModel by inject()
    private lateinit var binding: ActivityAddBoardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_board)
        binding.lifecycleOwner = this
        binding.addViewModel = viewModel

        viewModel.buttonName.value = intent.getStringExtra(Constant.BUTTON_NAME)

        //넘겨받은 데이터 구성
        viewModel.uid.value = intent.getIntExtra(Constant.UID, -1)
        viewModel.title.value = intent.getStringExtra(Constant.TITLE)
        viewModel.content.value = intent.getStringExtra(Constant.CONTENT)
        viewModel.time.value = intent.getStringExtra(Constant.TIME)
        viewModel.date.value = intent.getStringExtra(Constant.DATE)
        viewModel.boardColor.value = intent.getStringExtra(Constant.COLOR) ?: "#ffffff"
        binding.llColor.setBackgroundColor(Color.parseColor(viewModel.boardColor.value))

        viewModel.finishActivity = {
            this.finish()
        }

        viewModel.insertComplete = {
            Toast.makeText(this, "저장 완료!", Toast.LENGTH_SHORT).show()
            afterSave()
        }

        binding.llColor.setOnClickListener {
            openColorPicker()
        }
    }

    private fun openColorPicker() {
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
            .disableDefaultButtons(true)
            .setColumns(5)
            .setRoundColorButton(true)
            .addListenerButton("확인") { _, position, _ ->
                colorPicker.dismissDialog()

                if (position >= 0) {
                    viewModel.boardColor.value = colors[position]
                    binding.llColor.setBackgroundColor(Color.parseColor(colors[position]))
                }
            }
            .show() // dialog 생성
    }

    private fun afterSave() {
        binding.etTitle.setText("")
        binding.etContent.setText("")
        binding.etTitle.clearFocus()
        binding.etContent.clearFocus()
        binding.llColor.setBackgroundColor(Color.parseColor(viewModel.boardColor.value))

        finish()
    }
}