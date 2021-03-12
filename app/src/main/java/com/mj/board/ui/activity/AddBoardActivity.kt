package com.mj.board.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.application.Constant.EMPTY
import com.mj.board.application.Constant.WHITE
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
        viewModel.widgetId.value = intent.getIntExtra(Constant.WIDGET_ID, -1)
        viewModel.uid.value = intent.getIntExtra(Constant.UID, -1)
        viewModel.title.value = intent.getStringExtra(Constant.TITLE)
        viewModel.content.value = intent.getStringExtra(Constant.CONTENT)
        viewModel.time.value = intent.getStringExtra(Constant.TIME)
        viewModel.date.value = intent.getStringExtra(Constant.DATE)
        viewModel.boardColor.value = intent.getStringExtra(Constant.COLOR) ?: "#ffcc80"
//        binding.llColor.setBackgroundColor(Color.parseColor(viewModel.boardColor.value))


        viewModel.finishActivity = {
            this.finish()
        }

        viewModel.insertComplete = {
            Toast.makeText(this, getString(R.string.common_save_complete), Toast.LENGTH_SHORT).show()
            afterSave()
        }

        binding.llColor.setOnClickListener {
            openColorPicker()
        }
    }

    private fun openColorPicker() {
        val colorPicker = ColorPicker(this)
        colorPicker.dialogViewLayout.setBackgroundColor(Color.parseColor(WHITE))

        val colors: ArrayList<String> = ArrayList()
        colors.add("#ffcc80")
        colors.add("#ffe082")
        colors.add("#fff59d")
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
        colors.add("#ffffff")
        colorPicker.setColors(colors)
            .setTitle(EMPTY)
            .disableDefaultButtons(true)
            .setColumns(5)
            .setRoundColorButton(true)
            .addListenerButton(getString(R.string.common_confirm)) { _, position, _ ->
                colorPicker.dismissDialog()

                if (position >= 0) {
                    viewModel.boardColor.value = colors[position]
//                    binding.llColor.setBackgroundColor(Color.parseColor(colors[position]))
//                    binding.clMemo.setBackgroundColor(Color.parseColor(colors[position]))
                }
            }

            .show() // dialog 생성
    }

    private fun afterSave() {
        binding.etTitle.setText(EMPTY)
        binding.etContent.setText(EMPTY)
        binding.etTitle.clearFocus()
        binding.etContent.clearFocus()
//        binding.llColor.setBackgroundColor(Color.parseColor(viewModel.boardColor.value))


        //등록일경우는 아래 로직 제외
        if (viewModel.uid.value != -1) {

            //해당되는 id의 위젯이 있다면 내용 갱신
            if (viewModel.widgetId.value != -1) {
                val intent = Intent(Constant.SELECT)
                intent.putExtra(Constant.UID, viewModel.uid.value!!)
                intent.putExtra(Constant.WIDGET_ID, viewModel.widgetId.value!!)
                sendBroadcast(intent)
            }
            val intent = Intent(Constant.CHANGE)
            intent.putExtra(Constant.UID, viewModel.uid.value!!)
            sendBroadcast(intent)
        }
        finish()
    }
}