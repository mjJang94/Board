package com.mj.board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MemoFragViewModel(application: Application) : AndroidViewModel(application){

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    var goToAddBoard: (() -> Unit) ?= null

    //(LiveData)게시판 데이터
    var boardData : MutableLiveData<MutableList<BoardEntity>> = MutableLiveData()


    fun floatingBtnClickListener(){
        goToAddBoard?.let { it() }
    }


    //모든 저장 정보 불러오기
    fun getAllData(){
        GlobalScope.launch(Dispatchers.IO){

            boardData.postValue(repository.getAllBoards())
        }
    }

    //제목으로 검색하기
    fun findBoard(title: String){
        GlobalScope.launch(Dispatchers.IO){

            boardData.postValue(repository.findBoardsByTitle(title))
        }
    }

    //삭제
    fun deleteBoard(entity: BoardEntity){
        GlobalScope.launch(Dispatchers.IO) {
            repository.deleteBoard(entity)
            boardData.postValue(repository.getAllBoards())
        }
    }
}