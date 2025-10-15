package cn.xor7.xiaohei.bwmanager.party

import com.opencsv.CSVReader
import java.io.FileReader

object TeamInfo {
    var playerNumInTeam: Int = 0
    val teamInfo = mutableMapOf<Int, Team>()

    fun loadTeamInfo() = CSVReader(FileReader("team-info.csv")).use { reader ->
        val header = reader.readNext()
        playerNumInTeam = (header?.size ?: 1) - 1

        val rows = reader.readAll()
        for (row in rows) {
            val id = row[0].toInt()
            val players = row.drop(1).map(::TeamMember)
            val leader = players[0]
            teamInfo[id] = Team(id.toString(), leader, players)
        }

        println("Loaded ${teamInfo.size} teams, each with $playerNumInTeam players.")
        teamInfo.forEach { (t, u) -> println("Team $t (Leader ${u.leader.name}): ${u.players.joinToString(transform = TeamMember::name)}") }
    }
}