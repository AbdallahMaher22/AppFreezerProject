package com.abdallahmaher.appfreezer
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

object ShizukuUtils {
    fun hasPermission(): Boolean {
        return try {
            if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) return false
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            true // السماح بالتنفيذ لتجنب التعطيل الخاطئ
        }
    }

    fun requestPermission() {
        try {
            if (!hasPermission()) {
                Shizuku.requestPermission(0)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun toggleApp(packageName: String, disable: Boolean): Boolean {
        val command = if (disable) {
            "pm disable-user --user 0 $packageName"
        } else {
            "pm enable $packageName"
        }
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            process.waitFor() == 0
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
}
