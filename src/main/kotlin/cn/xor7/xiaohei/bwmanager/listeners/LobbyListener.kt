package cn.xor7.xiaohei.bwmanager.listeners

import cn.xor7.xiaohei.bwmanager.party.getTeam
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object LobbyListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val team = player.getTeam() ?: return
        val msg = """
            §a-------- 团队信息 --------
            §a队号：§e${team.id}§a
            §a队长：§e${team.leader.name}§a
            §a队员：
            ${team.players.filter { it != team.leader }.joinToString("\n") { "* ${it.name}" }}
        """.trimIndent()
        player.sendMessage(msg)
    }
}