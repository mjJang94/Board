package com.mj.board.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mj.board.application.Constant
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    var searchKeyword: MutableLiveData<String> = MutableLiveData()

    var goToAddBoard: (() -> Unit) ?= null

    var filterBoard: (() -> Unit) ?= null

    //필터 조건
    // 0 최신순, 1 색상별, 2 날짜별
    var filterValue : MutableLiveData<Int> = MutableLiveData()

    var isFirstQuery: MutableLiveData<Boolean> = MutableLiveData(true)

    //(LiveData)게시판 데이터
    var boardData : MutableLiveData<MutableList<BoardEntity>> = MutableLiveData()


    fun floatingBtnClickListener(){
        goToAddBoard?.let { it() }
    }

    //검색버튼 클릭
    fun searchButtonClickListener(){
        filterBoard?.let { it() }
    }

    //최신순 정보 불러오기
    fun getOrderByLatestData(){
        launch(Dispatchers.IO){
            boardData.postValue(repository.getByLatestData())
        }
    }

    //색상별 정보 불러오기
    fun getOrderByColorData(){
        launch(Dispatchers.IO){
            boardData.postValue(repository.getByColorData())
        }
    }

    //제목으로 검색하기
    fun findBoardByKeyword(keyword: String){
        launch(Dispatchers.IO){
            boardData.postValue(repository.findBoardsByTitle(keyword))
        }
    }

    //삭제
    fun deleteBoard(entity: BoardEntity){
        launch(Dispatchers.IO) {
            repository.deleteBoard(entity)

            //카테고리별로 나누기
            if(filterValue.value == Constant.ORDER_BY_LATEST) {
                getOrderByLatestData()
            }else{
                getOrderByColorData()
            }
        }
    }
}