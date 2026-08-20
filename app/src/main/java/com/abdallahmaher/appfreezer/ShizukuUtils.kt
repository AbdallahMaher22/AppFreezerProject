package com.abdallahmaher.appfreezer
import rikka.shizuku.Shizuku
object ShizukuUtils {
    fun hasPermission(): Boolean {
        if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) return false
        return Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission() {
        if (!hasPermission()) {
            Shizuku.requestPermission(0)
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
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
