package cn.xor7.xiaohei.bwmanager.party

import cn.xor7.xiaohei.bwmanager.parties
import cn.xor7.xiaohei.bwmanager.plugin
import com.alessiodp.parties.api.interfaces.PartyPlayer
import org.bukkit.entity.Player
import java.util.UUID

data class TeamMember(val uuid: UUID, val name: String) {
    constructor(dataStr: String) : this(
        UUID.fromString(dataStr.split("/")[1]),
        dataStr.split("/")[0],
    )

    val player: Player? get() = plugin.server.getPlayer(uuid)
    val partyPlayer: PartyPlayer get() = parties.getPartyPlayer(uuid)!!
}