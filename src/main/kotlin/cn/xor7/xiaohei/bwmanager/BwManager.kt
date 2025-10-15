package cn.xor7.xiaohei.bwmanager

import cn.xor7.xiaohei.bwmanager.commands.ConfigCommand
import cn.xor7.xiaohei.bwmanager.commands.GameCommand
import cn.xor7.xiaohei.bwmanager.commands.TeamCommand
import cn.xor7.xiaohei.bwmanager.listeners.LobbyListener
import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import com.alessiodp.parties.api.Parties
import com.alessiodp.parties.api.interfaces.PartiesAPI
import com.andrei1058.bedwars.api.BedWars
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
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
        Bukkit.getPluginManager().registerEvents(LobbyListener, this)
        TeamCommand.register("bwteam")
        ConfigCommand.register("bwcfg")
        GameCommand.register("bwgame")
    }

    fun CommandExecutor.register(name: String) {
        getCommand(name).executor = this@register
    }
}