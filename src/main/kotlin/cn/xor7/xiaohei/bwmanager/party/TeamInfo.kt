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

        println("已从CSV加载 ${teamInfo.size} 个团队信息。")
        teamInfo.forEach { (t, u) -> println("团队 $t (队长 ${u.leader}): ${u.players.joinToString()}") }
    }

}