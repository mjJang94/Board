package com.mj.board.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    //뒤로가기 리스너
    var finishActivity: (() -> Unit)? = null

    //수정하기 리스너
    var modifyClick: (() -> Unit)? = null

    var uid: MutableLiveData<String> = MutableLiveData()

    var date: MutableLiveData<String> = MutableLiveData()

    var time: MutableLiveData<String> = MutableLiveData()

    var title: MutableLiveData<String> = MutableLiveData()

    var content: MutableLiveData<String> = MutableLiveData()

    var color: MutableLiveData<String> = MutableLiveData()


    fun modifyClick() {
        modifyClick?.let { it() }
    }

    fun backButtonClick() {
        finishActivity?.let { it() }
    }
}