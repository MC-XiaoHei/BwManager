package cn.xor7.xiaohei.bwmanager

import org.bukkit.scheduler.BukkitRunnable

fun runTaskLater(delayTicks: Long, task: () -> Unit) {
    object : BukkitRunnable() {
        override fun run() {
            task()
        }
    }.runTaskLater(plugin, delayTicks)
}