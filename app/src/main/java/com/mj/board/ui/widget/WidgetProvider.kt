package com.mj.board.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import com.mj.board.R
import com.mj.board.application.Constant.SCHEME
import com.mj.board.database.BoardEntity
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class WidgetProvider : AppWidgetProvider() {

    lateinit var boardEntity: BoardEntity

    val MY_ACTION = "android.action.MY_ACTION"
    val SELECT = "android.action.SELECT"
    var uid : Int ?= -1
    var widgetId : Int ?= -1
    var txtTitle : TextView ?= null


    //    위젯 갱신 주기에 따라 위젯을 갱신할때 호출
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->
            val views: RemoteViews = addViews(context, appWidgetId)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)


        if (intent?.action == MY_ACTION) {

            val widgetId = intent.getIntExtra("WIDGET_ID", -1)

            val i = Intent(Intent.ACTION_VIEW, Uri.parse(SCHEME))
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("WIDGET_ID", widgetId)
            i.putExtra("KEY", SELECT)
            context?.startActivity(i)
        }

        if (intent?.action == SELECT){
            uid = intent.getIntExtra("UID", -1)
            widgetId = intent.getIntExtra("WIDGET_ID", -1)

            val repository = Repository(context?.applicationContext!!)

            runBlocking {
                boardEntity = repository.findBoardByUid(uid!!)
            }

            val view = RemoteViews(context.packageName, R.layout.widget_screen)

            //텍스트 변경
            view.setTextViewText(R.id.txt_widget, boardEntity.title)
            //배경색 변경
            view.setInt(R.id.ll_widget_back, "setBackgroundColor", Color.parseColor(boardEntity.color))

            //위젯 뷰 업데이트
            AppWidgetManager.getInstance(context).updateAppWidget(widgetId!!, view)

        }
    }

    //    위젯이 처음 생성될때 호출되며, 동일한 위젯의 경우 처음 호출
    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

    }

    //    위젯의 마지막 인스턴스가 제거될때 호출
    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    //    위젯이 사용자에 의해 제거될때 호출
    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    private fun setMyAction(context: Context?, appWidgetId: Int): PendingIntent {
        val intent = Intent()
        intent.action = MY_ACTION
        intent.putExtra("WIDGET_ID", appWidgetId)
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun addViews(context: Context?, appWidgetId: Int): RemoteViews {
        val views = RemoteViews(context?.packageName, R.layout.widget_screen)
        views.setOnClickPendingIntent(R.id.txt_widget, setMyAction(context, appWidgetId))
        return views
    }
}