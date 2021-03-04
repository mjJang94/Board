package com.mj.board.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application): AndroidViewModel(application){

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    //(LiveData)게시판 데이터
    var mutableLiveData : MutableLiveData<MutableList<BoardEntity>> = MutableLiveData()

    var goToAddBoard: (() -> Unit) ?= null


    //모든 저장 정보 불러오기
    fun getAllData(){
        GlobalScope.launch(Dispatchers.IO){

            mutableLiveData.postValue(repository.getAllBoards())
        }
    }

    //제목으로 검색하기
    fun findBoard(title: String){
        GlobalScope.launch(Dispatchers.IO){

            mutableLiveData.postValue(repository.findBoardsByTitle(title))
        }
    }

    //정보 수정하기
    fun modifyBoard(boardEntity: BoardEntity){
        GlobalScope.launch(Dispatchers.IO){
            repository.modifyBoard(boardEntity)
        }
    }
    //보드 삭제
    fun deleteBoard(boardEntity: BoardEntity){
        GlobalScope.launch(Dispatchers.IO){
            repository.deleteBoard(boardEntity)
        }
    }

    fun floatingBtnClickListener(){
        goToAddBoard?.let { it() }
    }

}