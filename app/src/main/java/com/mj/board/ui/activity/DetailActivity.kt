package com.mj.board.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.application.Constant.BUTTON_NAME
import com.mj.board.application.Constant.COLOR
import com.mj.board.application.Constant.CONTENT
import com.mj.board.application.Constant.DATE
import com.mj.board.application.Constant.TIME
import com.mj.board.application.Constant.TITLE
import com.mj.board.application.Constant.UID
import com.mj.board.application.Constant.WIDGET_ID
import com.mj.board.databinding.ActivityAddBoardBinding
import com.mj.board.databinding.ActivityDetailBinding
import com.mj.board.util.ktx.dataBinding
import com.mj.board.viewmodel.DetailViewModel
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by inject()
    private val binding: ActivityDetailBinding by dataBinding(R.layout.activity_detail)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    override fun onResume() {
        super.onResume()

        viewModel.findById(viewModel.uid.value!!)
    }

    private fun init() {

        //넘겨받은 데이터 구성
        viewModel.uid.value = intent.getIntExtra(UID, -1)
        viewModel.widgetId.value = intent.getIntExtra(WIDGET_ID, -1)

        //수정하기 이동
        viewModel.modifyClick = {

            Intent(this, AddBoardActivity::class.java).apply {
                putExtra(WIDGET_ID, viewModel.widgetId.value!!)
                putExtra(UID, viewModel.uid.value!!)
                putExtra(BUTTON_NAME, "수정하기")
                putExtra(DATE, viewModel.date.value)
                putExtra(TIME, viewModel.time.value)
                putExtra(TITLE, viewModel.title.value)
                putExtra(CONTENT, viewModel.content.value)
                putExtra(COLOR, viewModel.color.value)
            }.also {
                startActivity(it)
            }

        }

        //뒤로가기
        viewModel.finishActivity = {
            finish()
        }
    }
}