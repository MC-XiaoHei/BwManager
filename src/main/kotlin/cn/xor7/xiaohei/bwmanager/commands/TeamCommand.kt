package cn.xor7.xiaohei.bwmanager.commands

import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object TeamCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§c用法: /bwteam <[team-id]|list|info>")
            return false
        }
        when (args[0]) {
            "info" -> {
                sender.sendMessage("§a-------- 团队信息 --------")
                sender.sendMessage("§a团队数量：§e${TeamInfo.teamInfo.size}§a")
                sender.sendMessage("§a每队人数：§e${TeamInfo.playerNumInTeam}§a")
            }

            "list" -> {
                sender.sendMessage("§a-------- 团队列表 --------")
                TeamInfo.teamInfo.forEach { (id, team) ->
                    sender.sendMessage("§a队号 §e$id§a 队长 §e${team.leader.name}§a 队员 ${team.players.joinToString(transform = cn.xor7.xiaohei.bwmanager.party.TeamMember::name)}")
                }
            }

            else -> {
                val teamId = args[0].toIntOrNull()
                if (teamId == null) {
                    sender.sendMessage("§c无效的队伍号: ${args[0]}")
                    return false
                }
                val team = TeamInfo.teamInfo[teamId]
                if (team == null) {
                    sender.sendMessage("§c未找到队伍 $teamId")
                    return false
                }
                sender.sendMessage(team.getDisplayInfo())
            }
        }
        return true
    }
}