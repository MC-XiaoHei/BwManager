package cn.xor7.xiaohei.bwmanager.party

import cn.xor7.xiaohei.bwmanager.parties
import com.alessiodp.parties.api.interfaces.Party
import org.bukkit.entity.Player
import java.util.UUID

val teamUidMap = mutableMapOf<UUID, Team>()

data class Team(
    val id: String,
    val leader: TeamMember,
    val players: List<TeamMember>,
) {
    val partyName = "bw-team-$id"
    val party: Party

    init {
        parties.getParty(partyName)?.delete()
        if (!parties.createParty(partyName, null)) throw IllegalStateException("无法创建团队 $partyName")
        party = parties.getParty(partyName)!!
        players.map(TeamMember::partyPlayer).forEach(party::addMember)
        teamUidMap[party.id] = this
    }
}

fun Player.getTeam(): Team? = teamUidMap[parties.getPartyPlayer(this.uniqueId)?.partyId]