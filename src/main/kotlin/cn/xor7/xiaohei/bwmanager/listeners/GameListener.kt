package cn.xor7.xiaohei.bwmanager.listeners

import cn.xor7.xiaohei.bwmanager.plugin
import com.andrei1058.bedwars.api.events.team.TeamEliminatedEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object GameListener : Listener {
    @EventHandler
    fun onTeamEliminated(event: TeamEliminatedEvent) {
        plugin.logger.info("[${event.arena.arenaName}] Team ${event.team.name} has been eliminated!")
    }
}