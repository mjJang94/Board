package com.mj.board.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.board.R
import com.mj.board.database.BoardEntity
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val viewmodel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewmodel

        val adapter = MainRcyAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        binding.rcyBoard.adapter = adapter
        binding.rcyBoard.layoutManager = layoutManager
        // item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수 있고,
        // 그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있기 때문이다.
        // 특히 item이 자주 추가/삭제되면 오류가 날 수도 있기에 setHasFixedSize true를 설정한다.
        binding.rcyBoard.setHasFixedSize(true)

        binding.rcyBoard.addItemDecoration(SpacesItemDecoration(2, 15))

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

    //rcy adapter
    inner class MainRcyAdapter : RecyclerView.Adapter<MainRcyAdapter.Holder>() {

        private var boardList: MutableList<BoardEntity>? = null

        fun dataChange(data: MutableList<BoardEntity>) {
            boardList?.clear()
            boardList = data
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)
            val lp: GridLayoutManager.LayoutParams =
                view.layoutParams as GridLayoutManager.LayoutParams
            lp.height = parent.measuredHeight / 3
            lp.width = parent.measuredWidth / 2
            view.layoutParams = lp
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return boardList?.size ?: 0
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

            holder.bind(boardList?.get(position))
        }

        inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

            private var cvRow: CardView = view.findViewById(R.id.cv_row)
            private var txtDate: TextView = view.findViewById(R.id.txt_date)
            private var txtTime: TextView = view.findViewById(R.id.txt_time)
            private var txtTitle: TextView = view.findViewById(R.id.txt_title)
            private var txtContent: TextView = view.findViewById(R.id.txt_content)

            fun bind(boardEntity: BoardEntity?) {

                cvRow.setOnClickListener {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("DATE", boardEntity?.date)
                    intent.putExtra("TIME", boardEntity?.time)
                    intent.putExtra("TITLE", boardEntity?.title)
                    intent.putExtra("CONTENT", boardEntity?.content)
                    intent.putExtra("COLOR", boardEntity?.color)
                    startActivity(intent)
                }

                boardEntity.let {
                    cvRow.setBackgroundColor(Color.parseColor(boardEntity?.color))
                    txtDate.text = boardEntity?.date
                    txtTime.text = boardEntity?.time
                    txtTitle.text = boardEntity?.title
                    txtContent.text = boardEntity?.content

                }
            }
        }
    }

    //rcy decoration
    inner class SpacesItemDecoration(
        val spanCount: Int,
        var spacing: Int
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val position: Int = parent.getChildAdapterPosition(view)
            val column = position % spanCount

            outRect.left = column * spacing
            outRect.right = column * spacing
            outRect.top = spacing

        }
    }
}