package com.abdallahmaher.appfreezer
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
class ToggleTileService : TileService() {
    private var isCurrentlyDisabled = false 
    override fun onClick() {
        super.onClick()
        val prefs = AppPreferences(this)
        val selectedApps = prefs.getSelectedApps()
        if (selectedApps.isEmpty() || !ShizukuUtils.hasPermission()) return
        isCurrentlyDisabled = !isCurrentlyDisabled
        selectedApps.forEach { packageName ->
            ShizukuUtils.toggleApp(packageName, isCurrentlyDisabled)
        }
        updateTileState()
    }
    private fun updateTileState() {
        val tile = qsTile ?: return
        if (isCurrentlyDisabled) {
            tile.state = Tile.STATE_ACTIVE
            tile.label = "معطلة"
        } else {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "تجميد التطبيقات"
        }
        tile.updateTile()
    }
}
