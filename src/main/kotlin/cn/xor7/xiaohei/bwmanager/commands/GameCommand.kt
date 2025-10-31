package cn.xor7.xiaohei.bwmanager.commands

import cn.xor7.xiaohei.bwmanager.bedwars
import cn.xor7.xiaohei.bwmanager.listeners.ReplayListener
import cn.xor7.xiaohei.bwmanager.party.Team
import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import cn.xor7.xiaohei.bwmanager.party.TeamInfo.playerNumInTeam
import cn.xor7.xiaohei.bwmanager.party.TeamMember
import cn.xor7.xiaohei.bwmanager.replay.PcrcClient
import cn.xor7.xiaohei.bwmanager.runTaskLater
import com.andrei1058.bedwars.api.arena.GameState
import com.andrei1058.bedwars.api.arena.IArena
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
            "start" -> processStart(teamIds, sender, arena, arenaName)
            "stop" -> processStop(sender, arena, arenaName)

            else -> {
                sender.sendMessage("§c未知的操作：$action")
                return true
            }
        }
        return true
    }

    private fun processStart(
        teamIds: List<String>,
        sender: CommandSender,
        arena: IArena,
        arenaName: String,
    ) {
        if (teamIds.size < 2) {
            sender.sendMessage(USAGE)
            return
        }
        if (arena.status != GameState.waiting) {
            sender.sendMessage("§c竞技场 $arenaName 已经在游戏中")
            return
        }
        if (teamIds.size != arena.maxPlayers / arena.maxInTeam) {
            sender.sendMessage("§c当前竞技场 $arenaName 需要 ${arena.maxInTeam} 支队伍，但只提供了 ${teamIds.size} 支")
            return
        }
        if (playerNumInTeam > arena.maxInTeam) {
            sender.sendMessage("§c当前竞技场 $arenaName 每队最多 ${arena.maxInTeam} 人，但配置中每队有 $playerNumInTeam 人")
            return
        }
        teamIds
            .map {
                it.toIntOrNull() ?: run {
                    sender.sendMessage("§c无效的队号：$it")
                    return
                }
            }.map {
                TeamInfo.teamInfo[it] ?: run {
                    sender.sendMessage("§c找不到队号为 $it 的队伍")
                    return
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
                    return
                }
            }
            .map(Team::players)
            .flatten()
            .mapNotNull(TeamMember::player)
            .forEach { arena.addPlayer(it, true) }
        val client = PcrcClient(teamIds.joinToString(separator = "-"))
        ReplayListener.arenas[client.playerName] = arena
        ReplayListener.clients[client.playerName] = client
        client.start()
        runTaskLater(40L) {
            arena.changeStatus(GameState.starting)
        }
    }

    private fun processStop(
        sender: CommandSender,
        arena: IArena,
        arenaName: String,
    ) {
        if (arena.status == GameState.waiting) {
            sender.sendMessage("§c竞技场 $arenaName 尚未开始游戏")
            return
        }
        arena.disable()
        sender.sendMessage("§a竞技场 $arenaName 的游戏已被强制停止")
    }
}