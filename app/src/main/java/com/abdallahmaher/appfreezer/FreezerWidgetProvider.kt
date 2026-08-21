package com.abdallahmaher.appfreezer

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast

class FreezerWidgetProvider : AppWidgetProvider() {
    companion object {
        private var isFrozen = false
        const val ACTION_TOGGLE = "com.abdallahmaher.appfreezer.ACTION_TOGGLE"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) updateWidget(context, appWidgetManager, id)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TOGGLE) {
            
            if (!ShizukuUtils.hasPermission()) {
                Toast.makeText(context, "Shizuku لا يملك صلاحية!", Toast.LENGTH_SHORT).show()
                return
            }

            // تنفيذ الأمر على متجر Play مباشرة
            isFrozen = !isFrozen
            val result = ShizukuUtils.togglePlayStore(isFrozen)

            if (result == "SUCCESS") {
                val stateMsg = if (isFrozen) "تم إيقاف متجر Play" else "تم تفعيل متجر Play"
                Toast.makeText(context, stateMsg, Toast.LENGTH_SHORT).show()
            } else {
                // لو فشل، سيطبع لنا السبب من نظام الهاتف!
                isFrozen = !isFrozen // تراجع عن تغيير اللون
                Toast.makeText(context, "فشل: $result", Toast.LENGTH_LONG).show()
            }

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, FreezerWidgetProvider::class.java)
            appWidgetManager.getAppWidgetIds(thisWidget).forEach { id ->
                updateWidget(context, appWidgetManager, id)
            }
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        if (isFrozen) {
            views.setInt(R.id.widgetLayout, "setBackgroundResource", R.drawable.widget_bg_active)
            views.setTextViewText(R.id.widgetText, "متوقف\nPlay")
        } else {
            views.setInt(R.id.widgetLayout, "setBackgroundResource", R.drawable.widget_bg_normal)
            views.setTextViewText(R.id.widgetText, "متجر\nPlay")
        }

        val intent = Intent(context, FreezerWidgetProvider::class.java).apply { action = ACTION_TOGGLE }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
