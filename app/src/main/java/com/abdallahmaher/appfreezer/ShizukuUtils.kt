package com.abdallahmaher.appfreezer
import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

object ShizukuUtils {
    fun hasPermission(): Boolean {
        return try {
            if (Shizuku.pingBinder()) {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
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
        val cmd = if (disable) {
            arrayOf("pm", "disable-user", "--user", "0", packageName)
        } else {
            arrayOf("pm", "enable", packageName)
        }
        
        return try {
            if (!Shizuku.pingBinder()) return false
            val process = Shizuku.newProcess(cmd, null, null)
            val exitCode = process.waitFor()
            exitCode == 0
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
}
