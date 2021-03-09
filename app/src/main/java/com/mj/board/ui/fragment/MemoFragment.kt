package com.mj.board.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.database.BoardEntity
import com.mj.board.databinding.FragmentMemoBinding
import com.mj.board.ui.activity.AddBoardActivity
import com.mj.board.ui.activity.DetailActivity
import com.mj.board.viewmodel.MemoFragViewModel
import org.koin.android.ext.android.inject


open class MemoFragment : Fragment() {

    val viewModel: MemoFragViewModel by inject()
    lateinit var binding: FragmentMemoBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo, container, false)
        binding.lifecycleOwner = requireActivity()
        binding.memoFragmentViewModel = viewModel

        val adapter = MemoRcyAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcyBoard.adapter = adapter
        binding.rcyBoard.layoutManager = layoutManager
        // item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수 있고,
        // 그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있기 때문이다.
        // 특히 item이 자주 추가/삭제되면 오류가 날 수도 있기에 setHasFixedSize true를 설정한다.
        binding.rcyBoard.setHasFixedSize(true)
        binding.rcyBoard.addItemDecoration(SpacesItemDecoration(2, 15))


        viewModel.goToAddBoard = {
            val intent = Intent(requireActivity(), AddBoardActivity::class.java)
            intent.putExtra(Constant.BUTTON_NAME, "등록하기")
            startActivity(intent)
        }

        viewModel.boardData.observe(requireActivity(), Observer {data ->
            adapter.dataChange(data)
        })

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel.getAllData()
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
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
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
                AlertDialog.Builder(requireContext())
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