package com.abdallahmaher.appfreezer
import rikka.shizuku.Shizuku
object ShizukuUtils {
    fun hasPermission(): Boolean {
        return try {
            if (!Shizuku.pingBinder()) return false
            Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            false
        }
    }
    fun requestPermission() {
        try {
            if (Shizuku.pingBinder() && !hasPermission()) {
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
