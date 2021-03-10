package com.mj.board.ui.widget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.board.R
import com.mj.board.database.BoardEntity
import com.mj.board.databinding.ActivitySelectorBinding
import com.mj.board.viewmodel.SelectorViewModel
import org.koin.android.ext.android.inject

class SelectorActivity : AppCompatActivity() {

    val viewModel: SelectorViewModel by inject()
    lateinit var binding: ActivitySelectorBinding

    var action: String? = ""
    var widgetId: Int? = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selector)
        binding.lifecycleOwner = this
        binding.selectorViewModel = viewModel

        val adapter = SelectAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rcySelect.adapter = adapter
        binding.rcySelect.layoutManager = layoutManager
        binding.rcySelect.setHasFixedSize(true)


        action = intent.getStringExtra("KEY")
        widgetId = intent.getIntExtra("WIDGET_ID", -1)

        viewModel.getBoardList()

        viewModel.info.observe(this, Observer { it ->
            adapter.dataChange(it)
        })
    }


    //rcy adapter
    inner class SelectAdapter : RecyclerView.Adapter<SelectAdapter.Holder>() {

        private var boardList: MutableList<BoardEntity>? = null

        fun dataChange(data: MutableList<BoardEntity>) {
            boardList?.clear()
            boardList = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.select_item, parent, false)

            return Holder(view)
        }

        override fun getItemCount(): Int {
            return boardList?.size ?: 0
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.bind(boardList?.get(position))
        }

        inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

            private var txtTitle: TextView = view.findViewById(R.id.txt_title)

            fun bind(boardEntity: BoardEntity?) {

                boardEntity.let {
                    txtTitle.text = boardEntity?.title

                }

                txtTitle.setOnClickListener {
                    val intent = Intent(action)
                    intent.putExtra("UID", boardEntity?.uid)
                    intent.putExtra("WIDGET_ID", widgetId)
                    sendBroadcast(intent)

                    this@SelectorActivity.finish()
                }
            }
        }
    }
}