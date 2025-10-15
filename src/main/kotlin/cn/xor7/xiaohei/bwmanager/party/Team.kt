package cn.xor7.xiaohei.bwmanager.party

import cn.xor7.xiaohei.bwmanager.parties
import com.alessiodp.parties.api.interfaces.Party

data class Team(
    val id: String,
    val leader: TeamMember,
    val players: List<TeamMember>,
) {
    val partyId = "bw-team-$id"
    val party: Party

    init {
        parties.getParty(partyId)?.delete()
        if (!parties.createParty(partyId, null)) throw IllegalStateException("无法创建团队 $partyId")
        party = parties.getParty(partyId)!!
        players.map(TeamMember::partyPlayer).forEach(party::addMember)
    }
}