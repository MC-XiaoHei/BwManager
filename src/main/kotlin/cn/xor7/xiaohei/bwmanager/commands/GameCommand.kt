package cn.xor7.xiaohei.bwmanager.commands

import cn.xor7.xiaohei.bwmanager.bedwars
import cn.xor7.xiaohei.bwmanager.party.Team
import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import cn.xor7.xiaohei.bwmanager.party.TeamInfo.playerNumInTeam
import cn.xor7.xiaohei.bwmanager.party.TeamMember
import cn.xor7.xiaohei.bwmanager.replay.PcrcClient
import com.andrei1058.bedwars.api.arena.GameState
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object GameCommand : CommandExecutor {
    const val USAGE = "§c用法：/bwgame <start|stop> <arena> [<teamId1> <teamId2> [teamId3...]]"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.size < 2) {
            sender.sendMessage(USAGE)
            return true
        }
        val action = args[0]
        val arenaName = args[1]
        val arena = bedwars.arenaUtil.getArenaByName(arenaName) ?: run {
            sender.sendMessage("§c找不到名为 $arenaName 的竞技场")
            return true
        }
        val teamIds = args.drop(2)
        when (action) {
            "start" -> {
                if (teamIds.size < 2) {
                    sender.sendMessage(USAGE)
                    return true
                }
                if (arena.status != GameState.waiting) {
                    sender.sendMessage("§c竞技场 $arenaName 已经在游戏中")
                    return true
                }
                if (teamIds.size != arena.maxPlayers / arena.maxInTeam) {
                    sender.sendMessage("§c当前竞技场 $arenaName 需要 ${arena.maxInTeam} 支队伍，但只提供了 ${teamIds.size} 支")
                    return true
                }
                if (playerNumInTeam > arena.maxInTeam) {
                    sender.sendMessage("§c当前竞技场 $arenaName 每队最多 ${arena.maxInTeam} 人，但配置中每队有 $playerNumInTeam 人")
                    return true
                }
                teamIds
                    .map {
                        it.toIntOrNull() ?: run {
                            sender.sendMessage("§c无效的队号：$it")
                            return true
                        }
                    }.map {
                        TeamInfo.teamInfo[it] ?: run {
                            sender.sendMessage("§c找不到队号为 $it 的队伍")
                            return true
                        }
                    }
                    .also { teams ->
                        val unreadyPlayers = teams.map { it.getUnreadyPlayers() }
                            .flatten()
                        if (unreadyPlayers.isNotEmpty()) {
                            sender.sendMessage("§c以下玩家未上线，无法开始游戏：")
                            unreadyPlayers.forEach {
                                sender.sendMessage("§c * ${it.getDisplayInfo()}")
                            }
                            return true
                        }
                    }
                    .map(Team::players)
                    .flatten()
                    .mapNotNull(TeamMember::player)
                    .forEach { arena.addPlayer(it, true) }
                PcrcClient(teamIds.joinToString(separator = "."))
            }

            "stop" -> {

                if (arena.status == GameState.waiting) {
                    sender.sendMessage("§c竞技场 $arenaName 尚未开始游戏")
                    return true
                }
                arena.disable()
                sender.sendMessage("§a竞技场 $arenaName 的游戏已被强制停止")
            }

            else -> {
                sender.sendMessage("§c未知的操作：$action")
                return true
            }
        }
        return true
    }
}