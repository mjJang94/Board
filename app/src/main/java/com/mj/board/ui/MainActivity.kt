package com.mj.board.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.board.R
import com.mj.board.database.BoardEntity
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.databinding.BoardItemBinding
import com.mj.board.viewmodel.MainViewModel
import org.koin.android.ext.android.bind
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

        val adapter = MainAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.searchRcyUser.adapter = adapter
        binding.searchRcyUser.layoutManager = layoutManager
        // item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수 있고,
        // 그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있기 때문이다.
        // 특히 item이 자주 추가/삭제되면 오류가 날 수도 있기에 setHasFixedSize true를 설정한다.
        binding.searchRcyUser.setHasFixedSize(true)

        //LiveData 관찰
        viewmodel.mutableLiveData.observe(this, Observer {
            adapter.dataChange(it)
        })

        viewmodel.goToAddBoard = {
            startActivity(Intent(this, AddBoardActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        //모든 데이터 불러오기
        viewmodel.getAllData()
    }

    inner class MainAdapter(): RecyclerView.Adapter<MainAdapter.Holder>(){

        private var boardList :MutableList<BoardEntity> ?= null

        fun dataChange(data: MutableList<BoardEntity>){
            boardList?.clear()
            boardList = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = BoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return boardList?.size ?: 0
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.bind(boardList?.get(position))
        }

        inner class Holder(private var bind: BoardItemBinding) : RecyclerView.ViewHolder(bind.root){

            fun bind(boardEntity: BoardEntity?){

                boardEntity.let {
                    bind.txtTitle.text = boardEntity?.title
                    bind.txtTime.text = boardEntity?.time
                }
            }
        }
    }
}