package cn.xor7.xiaohei.bwmanager.party

import cn.xor7.xiaohei.bwmanager.parties
import cn.xor7.xiaohei.bwmanager.plugin
import com.alessiodp.parties.api.interfaces.PartyPlayer
import org.bukkit.entity.Player
import java.util.*

data class TeamMember(val uuid: UUID, val name: String) {
    constructor(dataStr: String) : this(
        getUuidFromDataStr(dataStr),
        dataStr.split("/")[0],
    )

    val player: Player? get() = plugin.server.getPlayer(uuid)
    val partyPlayer: PartyPlayer get() = parties.getPartyPlayer(uuid)!!

    fun getDisplayInfo(): String = if (player != null) {
        "§a${name}"
    } else {
        "§c${name}"
    }
}

private fun getUuidFromDataStr(dataStr: String): UUID {
    val split = dataStr.split("/")
    return if (split.size == 1) {
        UUID.nameUUIDFromBytes("OfflinePlayer:${split[0]}".toByteArray())
    } else {
        UUID.fromString(split[1])
    }
}