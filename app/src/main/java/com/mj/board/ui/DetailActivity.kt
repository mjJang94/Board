package com.mj.board.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.application.Constant.BUTTON_NAME
import com.mj.board.application.Constant.COLOR
import com.mj.board.application.Constant.CONTENT
import com.mj.board.application.Constant.DATE
import com.mj.board.application.Constant.TIME
import com.mj.board.application.Constant.TITLE
import com.mj.board.application.Constant.UID
import com.mj.board.databinding.ActivityDetailBinding
import com.mj.board.viewmodel.DetailViewModel
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by inject()
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this
        binding.detailViewModel = viewModel

        //넘겨받은 데이터 구성
        viewModel.uid.value = intent.getStringExtra(UID)
        viewModel.title.value = intent.getStringExtra(TITLE)
        viewModel.content.value = intent.getStringExtra(CONTENT)
        viewModel.time.value = intent.getStringExtra(TIME)
        viewModel.date.value = intent.getStringExtra(DATE)
        viewModel.color.value = intent.getStringExtra(COLOR)

        //수정하기
        viewModel.modifyClick = {

            val intent = Intent(this, AddBoardActivity::class.java)
            intent.putExtra(UID, viewModel.uid.value)
            intent.putExtra(BUTTON_NAME, "수정하기")
            intent.putExtra(DATE, viewModel.date.value)
            intent.putExtra(TIME, viewModel.time.value)
            intent.putExtra(TITLE, viewModel.title.value)
            intent.putExtra(CONTENT, viewModel.content.value)
            intent.putExtra(COLOR, viewModel.color.value)
            startActivity(intent)
        }

        //뒤로가기
        viewModel.finishActivity = {
            finish()
        }
    }
}