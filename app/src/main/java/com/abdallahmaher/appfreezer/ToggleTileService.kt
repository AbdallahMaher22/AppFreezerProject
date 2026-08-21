package com.abdallahmaher.appfreezer

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast

class ToggleTileService : TileService() {
    private var isFrozen = false

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        if (!ShizukuUtils.hasPermission()) {
            Toast.makeText(this, "Shizuku لا يملك صلاحية!", Toast.LENGTH_SHORT).show()
            return
        }

        // تنفيذ الأمر المخصص لمتجر Play
        isFrozen = !isFrozen
        val result = ShizukuUtils.togglePlayStore(isFrozen)

        if (result == "SUCCESS") {
            updateTileState()
        } else {
            isFrozen = !isFrozen // تراجع عن التغيير إذا فشل
            Toast.makeText(this, "فشل: $result", Toast.LENGTH_LONG).show()
            updateTileState()
        }
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        if (isFrozen) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "متوقف Play"
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "متجر Play"
        }
        tile.updateTile()
    }
}
