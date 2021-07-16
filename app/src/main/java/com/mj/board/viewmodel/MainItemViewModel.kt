package com.mj.board.viewmodel

import androidx.lifecycle.ViewModel

class MainItemViewModel: ViewModel(){

//            private var cvRow: CardView = view.findViewById(R.id.cv_row)
//            private var txtDate: TextView = view.findViewById(R.id.txt_date)
//            private var txtTime: TextView = view.findViewById(R.id.txt_time)
//            private var txtTitle: TextView = view.findViewById(R.id.txt_title)
//            private var txtContent: TextView = view.findViewById(R.id.txt_content)
//
//            fun bind(boardEntity: BoardEntity?) {
//
//                //클릭시
//                cvRow.setOnClickListener {
//                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
//                    intent.putExtra(Constant.UID, boardEntity?.uid)
//                    startActivity(intent)
//                }
//
//                //롱클릭시
//                cvRow.setOnLongClickListener {
//
//                    showDialog(boardEntity!!)
//
//                    return@setOnLongClickListener  true
//                }
//
//
//                boardEntity.let {
//                    cvRow.setBackgroundColor(Color.parseColor(boardEntity?.color))
//                    txtDate.text = boardEntity?.date
//                    txtTime.text = boardEntity?.time
//                    txtTitle.text = boardEntity?.title
//                    txtContent.text = boardEntity?.content
//\
//                }
//            }
//        }

        fun showDialog(boardEntity: BoardEntity) {
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
}