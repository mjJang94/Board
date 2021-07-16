package com.mj.board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SelectorViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    //viewmodel 내부에서 Repo 인스턴스 생성
    private val repository = Repository(application)

    var info: MutableLiveData<MutableList<BoardEntity>> = MutableLiveData()


    fun getBoardList() {
        launch(Dispatchers.IO) {
            info.postValue(repository.getByLatestData())
        }
    }
}