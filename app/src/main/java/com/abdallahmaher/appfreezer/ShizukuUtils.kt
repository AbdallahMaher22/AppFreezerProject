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
            false // هذا السطر هو مفتاح الحل، يجب أن يكون false لطلب الصلاحية الحقيقية بدلاً من التظاهر بوجودها
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
            if (!Shizuku.pingBinder()) return false
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            process.waitFor() == 0
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
}
