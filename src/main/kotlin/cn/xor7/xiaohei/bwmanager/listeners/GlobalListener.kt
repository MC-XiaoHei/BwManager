package cn.xor7.xiaohei.bwmanager.listeners

import cn.xor7.xiaohei.bwmanager.party.getTeam
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object GlobalListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val team = player.getTeam() ?: return
        player.sendMessage(team.getDisplayInfo())
    }
}