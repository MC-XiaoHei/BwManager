package cn.xor7.xiaohei.bwmanager.listeners

import cn.xor7.xiaohei.bwmanager.replay.PcrcClient
import com.andrei1058.bedwars.api.arena.GameState
import com.andrei1058.bedwars.api.arena.IArena
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object ReplayListener : Listener {
    val clients = mutableMapOf<String, PcrcClient>()
    val arenas = mutableMapOf<String, IArena>()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val name = player.name
        val arena = arenas[name] ?: return
        arena.addSpectator(player, false, arena.reSpawnLocation)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val name = player.name
        val client = clients[name]
        val arena = arenas[name]
        if (client == null || arena == null) return
        client.stop()
        clients -= name
        arenas -= name
    }

    @EventHandler
    fun onGameStateChange(event: GameStateChangeEvent) {
        if (event.newState in arrayOf(GameState.starting, GameState.playing)) {
            return
        }
        arenas.filter { event.arena.world.uid == it.value.world.uid }.keys.forEach { name ->
            val client = clients[name] ?: return
            client.stop()
            clients -= name
            arenas -= name
        }
    }
}