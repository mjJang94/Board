package com.mj.board.viewmodel

import android.app.Application
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddViewModel(application: Application) : AndroidViewModel(application){

    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    var titleTextLength: MutableLiveData<String> = MutableLiveData("0/10")
    var contentTextLength: MutableLiveData<String> = MutableLiveData("0/50")



    var finishActivity: (() -> Unit) ?= null


    fun onTitleTextChange(editable: Editable?) {
        titleTextLength.value = "${editable?.length.toString()}/10"
    }

    fun onContenttextCgange(editable: Editable?){
        contentTextLength.value = "${editable?.length.toString()}/50"
    }

    fun backButtonClick() {
        finishActivity?.let { it() }
    }


    //입력정보 저장하기
    fun insertBoard(){
        GlobalScope.launch(Dispatchers.IO){

            val boardEntity = BoardEntity()
            boardEntity.title =

            repository.insertBoard(boardEntity)
        }
    }
}