package com.mj.board.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.mj.board.R
import com.mj.board.application.Constant.SCHEME
import com.mj.board.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WidgetProvider : AppWidgetProvider() {
    private val MY_ACTION = "android.action.MY_ACTION"

    //    위젯 갱신 주기에 따라 위젯을 갱신할때 호출
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds?.forEach { appWidgetId ->
            val views: RemoteViews = addViews(context)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)


        if (intent?.action == MY_ACTION) {
            Toast.makeText(context, "asdsadasd", Toast.LENGTH_SHORT).show()
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

    private fun setMyAction(context: Context?): PendingIntent {
        val intent = Intent()
        intent.action = MY_ACTION
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildURIIntent(context: Context?): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(SCHEME))
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun addViews(context: Context?): RemoteViews {
        val views = RemoteViews(context?.packageName, R.layout.widget_screen)
        views.setOnClickPendingIntent(R.id.txt_widget, buildURIIntent(context))
        return views
    }
}