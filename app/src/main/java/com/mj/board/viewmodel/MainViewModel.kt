package com.mj.board.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    var fixTitle : MutableLiveData<String> = MutableLiveData()
}