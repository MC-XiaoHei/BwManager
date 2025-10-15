package cn.xor7.xiaohei.bwmanager.commands

import cn.xor7.xiaohei.bwmanager.party.TeamInfo
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object ConfigCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.isNotEmpty() && args[0] == "reload") {
            if (args.size == 1) {
                sender.sendMessage("§c确定要重载队伍信息吗？游玩中使用可能会导致未知问题！！！")
                sender.sendMessage("§c输入 /bwcfg reload confirm 来确认执行！")
                return true
            } else if (args.size == 2 && args[1] == "confirm") {
                TeamInfo.loadTeamInfo()
                sender.sendMessage("§a已重载队伍信息！")
                return true
            }
        }
        sender.sendMessage("§c用法：/bwcfg reload")
        return true
    }
}