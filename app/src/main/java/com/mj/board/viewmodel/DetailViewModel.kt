package com.mj.board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mj.board.application.Constant
import com.mj.board.application.Constant.WHITE
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    
    //viewmodel 내부에서 Repo 인스턴스 생성
    val repository = Repository(application)

    //뒤로가기 리스너
    var finishActivity: (() -> Unit)? = null

    //수정하기 리스너
    var modifyClick: (() -> Unit)? = null

    var widgetId: MutableLiveData<Int> = MutableLiveData()

    var uid: MutableLiveData<Int> = MutableLiveData()

    var date: MutableLiveData<String> = MutableLiveData()

    var time: MutableLiveData<String> = MutableLiveData()

    var title: MutableLiveData<String> = MutableLiveData()

    var content: MutableLiveData<String> = MutableLiveData()

    var color: MutableLiveData<String> = MutableLiveData(WHITE)


    fun modifyClick() {
        modifyClick?.let { it() }
    }

    fun backButtonClick() {
        finishActivity?.let { it() }
    }

    fun findById(id: Int){

        GlobalScope.launch(Dispatchers.IO){

            val info = repository.findBoardByUid(id)

            withContext(Dispatchers.Main){
                title.value = info.title
                content.value = info.content
                time.value = info.time
                date.value = info.date
                color.value = info.color
            }
        }
    }
}