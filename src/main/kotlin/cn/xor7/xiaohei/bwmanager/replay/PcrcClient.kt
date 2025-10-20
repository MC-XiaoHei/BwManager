package cn.xor7.xiaohei.bwmanager.replay

import cn.xor7.xiaohei.bwmanager.plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class PcrcClient(private val name: String) {
    private val serverPort = Bukkit.getServer().port
    private val pcrcFolder = Bukkit.getServer()
        .worldContainer
        .resolve("pcrc")
    private val workingDir = pcrcFolder.resolve(name)
    private val pcrcProcess: ProcessController
    val playerName = "_r_$name"

    init {
        val configFile = workingDir.resolve("config.json")
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.writeText(buildConfigJson())
        }
        pcrcProcess = startProcess(
            exePath = pcrcFolder.resolve("PCRC.exe").absolutePath,
            name = name,
            workDir = workingDir,
        )
        pcrcProcess.writeLine("start")
    }

    fun stop(): BukkitTask = object : BukkitRunnable() {
        override fun run() {
            processes -= pcrcProcess
            pcrcProcess.writeLine("stop")
            pcrcProcess.writeLine("exit")
            pcrcProcess.waitForExit()
        }
    }.runTask(plugin)


    private fun buildConfigJson() = """{
      "__1__": "-------- Base --------",
      "language": "en_us",
      "recording_temp_file_directory": "PCRC_recording_temp",
      "recording_storage_directory": "PCRC_recordings",
      "debug_mode": false,
      "debug_packet": false,
    
      "__2__": "-------- Account and Server --------",
      "authenticate_type": "offline",
      "username": "$playerName",
      "password": "secret",
      "store_token": false,
      "address": "localhost",
      "port": $serverPort,
      "server_name": "$name",
      "initial_version": "1.12.2",
    
      "__3__": "-------- PCRC Control --------",
      "file_size_limit_mb": 2048,
      "file_buffer_size_mb": 8,
      "time_recorded_limit_hour": 12,
      "delay_before_afk_second": 15,
      "afk_ignore_spectator": false,
      "record_packets_when_afk": true,
      "auto_relogin": true,
      "auto_relogin_attempts": 5,
      "chat_spam_protect": true,
      "command_prefix": "!!PCRC",
    
      "__4__": "-------- PCRC Features --------",
      "daytime": 4000,
      "weather": false,
      "with_player_only": false,
      "remove_items": false,
      "remove_bats": true,
      "remove_phantoms": true,
      "on_joined_commands": [],
    
      "__5__": "-------- PCRC Whitelist --------",
      "enabled": false,
      "whitelist": []
    }"""
}