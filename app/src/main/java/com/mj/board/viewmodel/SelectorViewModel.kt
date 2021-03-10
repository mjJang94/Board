package com.mj.board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectorViewModel(application: Application): AndroidViewModel(application) {

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    var info : MutableLiveData<MutableList<BoardEntity>> = MutableLiveData()


    fun getBoardList(){
        GlobalScope.launch(Dispatchers.IO){
            info.postValue(repository.getAllBoards())
        }
    }
}