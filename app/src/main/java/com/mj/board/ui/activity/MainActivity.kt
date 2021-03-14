package com.mj.board.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.application.Constant.ORDER_BY_COLOR
import com.mj.board.application.Constant.ORDER_BY_LATEST
import com.mj.board.database.BoardEntity
import com.mj.board.databinding.ActivityMainBinding
import com.mj.board.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.isFirstQuery.value == false) orderByData()
    }

    private fun init() {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        val adapter = MemoRcyAdapter()
        val layoutManager = GridLayoutManager(this, 2)
        binding.rcyBoard.adapter = adapter
        binding.rcyBoard.layoutManager = layoutManager
        binding.rcyBoard.setHasFixedSize(true)
        binding.rcyBoard.addItemDecoration(SpacesItemDecoration(2, 15))


        //플로팅 버튼 클릭시
        viewModel.goToAddBoard = {
            val intent = Intent(this, AddBoardActivity::class.java)
            intent.putExtra(Constant.BUTTON_NAME, "등록하기")
            startActivity(intent)
        }

        //보드 데이터 변경시
        viewModel.boardData.observe(this, Observer { data ->
            adapter.dataChange(data)
        })

        //검색 키워드가 입력될 떄
        viewModel.searchKeyword.observe(this, Observer { keyword ->

            GlobalScope.launch(Dispatchers.Main) {

                delay(1000)

                if (keyword.isNullOrEmpty()) {
                    orderByData()
                } else {
                    viewModel.findBoardByKeyword("%" + viewModel.searchKeyword.value!! + "%")
                }
            }
        })

        makeSpinner()
        initAdmob()
    }

    private fun makeSpinner(){

        //검색 조건 스피너
        val items = resources.getStringArray(R.array.filter_array)
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, items)

        binding.spinnerFilter.adapter = spinnerAdapter
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                when(position){

                    0 -> {
                        viewModel.isFirstQuery.value = false
                        viewModel.filterValue.value = ORDER_BY_LATEST
                        viewModel.getOrderByLatestData()
                    }

                    1 -> {
                        viewModel.isFirstQuery.value = false
                        viewModel.filterValue.value = ORDER_BY_COLOR
                        viewModel.getOrderByColorData()
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun initAdmob(){

        MobileAds.initialize(this, object : OnInitializationCompleteListener{
            override fun onInitializationComplete(status: InitializationStatus?) {
            }
        })

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun orderByData(){

        if (viewModel.filterValue.value == ORDER_BY_LATEST){
            viewModel.getOrderByLatestData()
        }else{
            viewModel.getOrderByColorData()
        }
    }



    //rcy adapter
    inner class MemoRcyAdapter : RecyclerView.Adapter<MemoRcyAdapter.Holder>() {

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

                //클릭시
                cvRow.setOnClickListener {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(Constant.UID, boardEntity?.uid)
                    startActivity(intent)
                }

                //롱클릭시
                cvRow.setOnLongClickListener {

                    showDialog(boardEntity!!)

                    return@setOnLongClickListener  true
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

        private fun showDialog(boardEntity: BoardEntity){
            val builder =
                AlertDialog.Builder(this@MainActivity)
            builder.setTitle("이 메모를 삭제할까요?")
            builder.setPositiveButton(
                "삭제"
            ) { _, _ ->
                viewModel.deleteBoard(boardEntity)
            }
            builder.setNegativeButton(
                "취소"
            ) { _, _ ->
            }

            builder.show()
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