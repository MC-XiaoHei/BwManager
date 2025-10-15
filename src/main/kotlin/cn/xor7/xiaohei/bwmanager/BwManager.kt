package cn.xor7.xiaohei.bwmanager

import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import com.alessiodp.parties.api.Parties
import com.alessiodp.parties.api.interfaces.PartiesAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var parties: PartiesAPI
lateinit var plugin: BwManager

class BwManager : JavaPlugin() {
    override fun onEnable() {
        plugin = this
        parties = Parties.getApi()
        TeamInfo.loadTeamInfo()
    }
}