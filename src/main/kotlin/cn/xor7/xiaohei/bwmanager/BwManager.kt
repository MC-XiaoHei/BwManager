package cn.xor7.xiaohei.bwmanager

import cn.xor7.xiaohei.bwmanager.commands.ConfigCommand
import cn.xor7.xiaohei.bwmanager.commands.GameCommand
import cn.xor7.xiaohei.bwmanager.commands.SpectatorCommand
import cn.xor7.xiaohei.bwmanager.commands.TeamCommand
import cn.xor7.xiaohei.bwmanager.listeners.GameListener
import cn.xor7.xiaohei.bwmanager.listeners.GlobalListener
import cn.xor7.xiaohei.bwmanager.listeners.ReplayListener
import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import cn.xor7.xiaohei.bwmanager.replay.clients
import cn.xor7.xiaohei.bwmanager.replay.processes
import com.alessiodp.parties.api.Parties
import com.alessiodp.parties.api.interfaces.PartiesAPI
import com.andrei1058.bedwars.api.BedWars
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

lateinit var parties: PartiesAPI
lateinit var bedwars: BedWars
lateinit var plugin: BwManager

class BwManager : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        parties = Parties.getApi()
        bedwars = Bukkit.getServicesManager().getRegistration(BedWars::class.java).getProvider()
        TeamInfo.loadTeamInfo()
        GlobalListener.register()
        ReplayListener.register()
        GameListener.register()
        TeamCommand.register("bwteam")
        ConfigCommand.register("bwcfg")
        GameCommand.register("bwgame")
        SpectatorCommand.register("bwspe")
    }

    private fun Listener.register() {
        Bukkit.getPluginManager().registerEvents(this, this@BwManager)
    }

    private fun CommandExecutor.register(name: String) {
        getCommand(name).executor = this@register
    }

    override fun onDisable() {
        clients.forEach { it.stopSync() }
        processes.forEach { it.destroy() }
    }
}