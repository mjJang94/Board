package com.mj.board.viewmodel

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mj.board.application.Constant.EMPTY
import com.mj.board.application.Constant.WHITE
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import com.mj.board.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddViewModel(application: Application) : AndroidViewModel(application) {

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    //색상 데이터
    var boardColor: MutableLiveData<String> = MutableLiveData()

    var widgetId: MutableLiveData<Int> = MutableLiveData()

    var uid: MutableLiveData<Int> = MutableLiveData()

    var date: MutableLiveData<String> = MutableLiveData()

    var time: MutableLiveData<String> = MutableLiveData()

    var title: MutableLiveData<String> = MutableLiveData()

    var content: MutableLiveData<String> = MutableLiveData()

    var buttonName: MutableLiveData<String> = MutableLiveData()

    //데이터 삽입 완료 콜백
    var insertComplete: (() -> Unit)? = null

    //뒤로가기 클릭 콜백
    var finishActivity: (() -> Unit)? = null


    fun onTitleTextChange(editable: Editable?) {
        title.value = editable.toString()
    }

    fun onContentTextChange(editable: Editable?) {
        content.value = editable.toString()
    }

    fun backButtonClick() {
        finishActivity?.let { it() }
    }


    //입력정보 저장하기
    //입력정보 수정하기
    fun saveBoard() {

        if (buttonName.value == "등록하기") {

            GlobalScope.launch(Dispatchers.IO) {

                val boardEntity = BoardEntity(
                    null,
                    title.value,
                    content.value,
                    boardColor.value,
                    Util.getTodayDate(),
                    Util.getTime()
                )

                repository.insertBoard(boardEntity)

                withContext(Dispatchers.Main) {
                    title.value = EMPTY
                    content.value = EMPTY
                    boardColor.value = WHITE
                    insertComplete?.let { it() }
                }
            }
        } else {

            GlobalScope.launch(Dispatchers.IO) {
                val boardEntity = BoardEntity(
                    uid.value,
                    title.value,
                    content.value,
                    boardColor.value,
                    Util.getTodayDate(),
                    Util.getTime()
                )

                repository.modifyBoard(boardEntity)

                withContext(Dispatchers.Main) {
                    title.value = EMPTY
                    content.value = EMPTY
                    boardColor.value = WHITE
                    insertComplete?.let { it() }
                }
            }
        }
    }
}