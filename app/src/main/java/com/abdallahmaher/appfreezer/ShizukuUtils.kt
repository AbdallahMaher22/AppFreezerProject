package com.abdallahmaher.appfreezer
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuUtils {
    fun hasPermission(): Boolean {
        return try {
            Shizuku.pingBinder() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            false
        }
    }

    fun requestPermission() {
        try {
            if (Shizuku.pingBinder() && !hasPermission()) Shizuku.requestPermission(0)
        } catch (e: Throwable) { }
    }

    // هنا السحر: كود يرسل الأمر ويقرأ سبب الرفض لو حدث!
    fun togglePlayStore(disable: Boolean): String {
        val pkg = "com.android.vending"
        val cmd = if (disable) "pm disable-user --user 0 $pkg" else "pm enable $pkg"
        
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", cmd), null, null)
            val reader = BufferedReader(InputStreamReader(process.errorStream))
            val errorOutput = reader.readText()
            process.waitFor()
            
            if (errorOutput.isNotEmpty()) {
                errorOutput // إرجاع رسالة الخطأ لنقرأها
            } else {
                "SUCCESS"
            }
        } catch (e: Throwable) {
            e.message ?: "Unknown Error"
        }
    }
}
