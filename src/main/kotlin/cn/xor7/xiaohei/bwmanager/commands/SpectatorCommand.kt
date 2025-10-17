package cn.xor7.xiaohei.bwmanager.commands

import cn.xor7.xiaohei.bwmanager.bedwars
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player

object SpectatorCommand : CommandExecutor {
    override fun onCommand(
        sender: org.bukkit.command.CommandSender,
        command: org.bukkit.command.Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.size !in 1..2) {
            sender.sendMessage("§c用法：/bwspe <arena> [player]")
            return true
        }
        val arenaName = args[0]
        val player = args.getOrNull(1)?.let {
            Bukkit.getPlayer(it) ?: run {
                sender.sendMessage("§c找不到玩家 $it")
                return true
            }
        } ?: sender as? Player ?: run {
            sender.sendMessage("§c请指定玩家，或由玩家本人执行此命令")
            return true
        }
        val arena = bedwars.arenaUtil.getArenaByName(arenaName) ?: run {
            sender.sendMessage("§c找不到名为 $arenaName 的竞技场")
            return true
        }
        arena.addSpectator(player, false, arena.reSpawnLocation)
        return true
    }
}