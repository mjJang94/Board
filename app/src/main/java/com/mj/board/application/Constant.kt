package com.mj.board.application

object Constant {

    const val UID = "UID"
    const val DATE = "DATE"
    const val TIME = "TIME"
    const val TITLE = "TITLE"
    const val CONTENT = "CONTENT"
    const val COLOR = "COLOR"
    const val BUTTON_NAME = "BUTTON_NAME"

    const val ACTION = "ACTION"
    const val WIDGET_ID = "WIDGET_ID"

    const val WHITE = "#ffffff"
    const val EMPTY = ""

    const val SELECT_SCHEME = "board://list"
    const val DETAIL_SCHEME = "board://detail"

    //브로드캐스트 리시버 액션

    const val MY_ACTION = "android.action.MY_ACTION"
    const val DETAIL = "android.action.DETAIL"
    const val SELECT = "android.action.SELECT"
    const val CHANGE = "android.action.CHANGE"


    //필터조건 0 최신순, 1 색상별
    const val ORDER_BY_LATEST = 0
    const val ORDER_BY_COLOR = 0
}