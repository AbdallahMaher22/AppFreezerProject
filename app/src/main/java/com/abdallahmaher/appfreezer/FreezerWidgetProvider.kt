package com.abdallahmaher.appfreezer

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class FreezerWidgetProvider : AppWidgetProvider() {
    companion object {
        private var isFrozen = false
        const val ACTION_TOGGLE = "com.abdallahmaher.appfreezer.ACTION_TOGGLE"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TOGGLE) {
            val prefs = AppPreferences(context)
            val selectedApps = prefs.getSelectedApps()
            if (selectedApps.isNotEmpty() && ShizukuUtils.hasPermission()) {
                isFrozen = !isFrozen
                selectedApps.forEach { pkg ->
                    ShizukuUtils.toggleApp(pkg, isFrozen)
                }
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
            views.setTextViewText(R.id.widgetText, "معطلة")
        } else {
            views.setInt(R.id.widgetLayout, "setBackgroundResource", R.drawable.widget_bg_normal)
            views.setTextViewText(R.id.widgetText, "تجميد")
        }

        val intent = Intent(context, FreezerWidgetProvider::class.java).apply {
            action = ACTION_TOGGLE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
