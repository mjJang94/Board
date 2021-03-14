package com.mj.board.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.mj.board.R
import com.mj.board.application.Constant
import com.mj.board.application.Constant.ACTION
import com.mj.board.application.Constant.CHANGE
import com.mj.board.application.Constant.DETAIL
import com.mj.board.application.Constant.DETAIL_SCHEME
import com.mj.board.application.Constant.MY_ACTION
import com.mj.board.application.Constant.SELECT
import com.mj.board.application.Constant.SELECT_SCHEME
import com.mj.board.application.Constant.UID
import com.mj.board.application.Constant.WIDGET_ID
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.runBlocking


class WidgetProvider : AppWidgetProvider() {

    val PREFERENCES_NAME = "preference"
    val KEY_SUFFIX = "uid"

    //위젯 갱신 주기에 따라 위젯을 갱신할때 호출
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->

            //preference key값 만들기
            val key = appWidgetId.toString() + KEY_SUFFIX
            //preference에서 uid 뽑기
            val uid = getPreference(context).getInt(key, -1)

            //preference 에 저장된 값이 없다면
            if (uid == -1) {
                val views: RemoteViews = addViews(context, appWidgetId)

                appWidgetManager?.updateAppWidget(appWidgetId, views)

                //있으면 조회해서 데이터 세팅
            } else {
                appWidgetManager?.updateAppWidget(appWidgetId, initView(context, appWidgetId, uid))
            }
        }
    }

    //Broadcast 메시지가 왔을 때
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == MY_ACTION) {

            val widgetId = intent.getIntExtra(WIDGET_ID, -1)

            val i = Intent(Intent.ACTION_VIEW, Uri.parse(SELECT_SCHEME))
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra(WIDGET_ID, widgetId)
            i.putExtra(ACTION, SELECT)
            context?.startActivity(i)
        }

        //선택한 데이터로 노출
        if (intent?.action == SELECT) {
            val uid = intent.getIntExtra(UID, -1)
            val widgetId = intent.getIntExtra(WIDGET_ID, -1)

            //preference key값 만들기
            val key = widgetId.toString() + KEY_SUFFIX

            //preference에서 uid 저장
            val editor = getPreference(context).edit()
            editor.putInt(key, uid)
            editor.apply()

            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, initView(context, widgetId, uid))

        }


        //위젯을 통한 변경이 아닌 앱을 통해 변경한경우 데이터 갱신
        if (intent?.action == CHANGE) {
            val uid = intent.getIntExtra(UID, -1)
            val arrIds = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context!!, WidgetProvider::class.java))

            for (widgetId in arrIds) {

                val key = widgetId.toString() + KEY_SUFFIX
                val tmpUid = getPreference(context).getInt(key, -1)

                if (uid == tmpUid) {
                    AppWidgetManager.getInstance(context).updateAppWidget(widgetId, initView(context, widgetId, uid))
                }
            }
        }

        //상세 화면으로 이동
        if (intent?.action == DETAIL) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(DETAIL_SCHEME))
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra(WIDGET_ID, intent.getIntExtra(WIDGET_ID, -1))
            i.putExtra(UID, intent.getIntExtra(UID, -1))
            context?.startActivity(i)
        }
    }

    // preference 객테 생성 메소드
    private fun getPreference(context: Context?): SharedPreferences {

        return context!!.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // 저장된 데이터가 있을경우 view 세팅
    private fun initView(context: Context?, widgetId: Int, uid: Int): RemoteViews {

        var boardEntity: BoardEntity

        val repository = Repository(context?.applicationContext!!)

        runBlocking {
            boardEntity = repository.findBoardByUid(uid)
        }

        val view = RemoteViews(context.packageName, R.layout.widget_screen)
        //선택하기 텍스트 뷰 숨기기
        view.setViewVisibility(R.id.txt_widget_select_memo, View.GONE)
        //선택한 메모 뷰 보이기
        view.setViewVisibility(R.id.txt_widget_select_title, View.VISIBLE)
        //선택한 메모 텍스트 변경
        view.setTextViewText(R.id.txt_widget_select_title, boardEntity.title)
        //선택한 메모 배경색 변경
        view.setInt(R.id.ll_widget_back, "setBackgroundColor", Color.parseColor(boardEntity.color))
        //선택한 메모 클릭
        view.setOnClickPendingIntent(R.id.txt_widget_select_title, setContentClickAction(context, widgetId, boardEntity))

        return view
    }

    // 메모 선택 메소드
    private fun setSelectAction(context: Context?, appWidgetId: Int): PendingIntent {
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = MY_ACTION
        intent.putExtra(WIDGET_ID, appWidgetId)
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    // 위젯 클릭 메소드
    private fun setContentClickAction(context: Context?, appWidgetId: Int, boardEntity: BoardEntity): PendingIntent {
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = DETAIL
        intent.putExtra(WIDGET_ID, appWidgetId)
        intent.putExtra(UID, boardEntity.uid)

        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //처음 위젯이 생성될때 기본 뷰 생성
    private fun addViews(context: Context?, appWidgetId: Int): RemoteViews {
        val views = RemoteViews(context?.packageName, R.layout.widget_screen)
        views.setOnClickPendingIntent(R.id.txt_widget_select_memo, setSelectAction(context, appWidgetId))
        views.setTextViewText(R.id.txt_widget_select_memo, "클릭하여\n메모선택")
        return views
    }
}