package cn.xor7.xiaohei.bwmanager.party

import cn.xor7.xiaohei.bwmanager.parties
import cn.xor7.xiaohei.bwmanager.plugin
import com.alessiodp.parties.api.interfaces.PartyPlayer
import org.bukkit.entity.Player
import java.util.UUID

@JvmInline
value class TeamMember(val uuid: UUID) {
    constructor(uuidStr: String) : this(UUID.fromString(uuidStr))
    val player: Player? get() = plugin.server.getPlayer(uuid)
    val partyPlayer: PartyPlayer get() = parties.getPartyPlayer(uuid)!!
}