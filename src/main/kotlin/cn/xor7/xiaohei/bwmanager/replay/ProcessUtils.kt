package cn.xor7.xiaohei.bwmanager.replay

import cn.xor7.xiaohei.bwmanager.plugin
import java.io.BufferedWriter
import java.io.File
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import kotlin.concurrent.thread

val processes = mutableListOf<ProcessController>()

fun startProcess(exePath: String, name: String, workDir: File? = null, args: List<String> = emptyList()): ProcessController {
    val command = mutableListOf<String>().apply {
        add(exePath)
        addAll(args)
    }
    val pb = ProcessBuilder(command)
    if (workDir != null) pb.directory(workDir)

    pb.redirectOutput(ProcessBuilder.Redirect.PIPE)
    pb.redirectError(ProcessBuilder.Redirect.PIPE)
    pb.redirectInput(ProcessBuilder.Redirect.PIPE)

    val proc = pb.start()

    thread(name = "proc-stdout-${proc.hashCode()}") {
        proc.inputStream.bufferedReader(Charsets.UTF_8).forEachLine { plugin.logger.info("[replay-$name] $it") }
    }
    thread(name = "proc-stderr-${proc.hashCode()}") {
        proc.errorStream.bufferedReader(Charsets.UTF_8).forEachLine { plugin.logger.severe("[replay-$name] $it") }
    }

    return ProcessController.fromProcess(proc).also { processes += it }
}

data class ProcessController(
    val process: Process,
    private val stdinWriter: BufferedWriter
) {
    fun writeLine(line: String) {
        stdinWriter.apply {
            write(line)
            newLine()
            flush()
        }
    }

    fun writeRaw(text: String) {
        stdinWriter.apply {
            write(text)
            flush()
        }
    }

    fun closeStdin() {
        try { stdinWriter.close() } catch (_: Exception) { }
    }

    fun waitForExit(): Int = process.waitFor()

    fun destroy() {
        process.destroy()
    }

    companion object {
        fun fromProcess(process: Process): ProcessController {
            val writer = BufferedWriter(OutputStreamWriter(process.outputStream, StandardCharsets.UTF_8))
            return ProcessController(process, writer)
        }
    }
}