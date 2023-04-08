package eu.syplex.proxy.config

import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader
import org.yaml.snakeyaml.DumperOptions
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class ConfigLoader(private val dataDirectory: Path) {
    private val dataFile: File = File(dataDirectory.toFile(), "config.yml")
    private val loader: ConfigurationLoader<ConfigurationNode>

    lateinit var configurationNode: ConfigurationNode

    init {
        loader = YAMLConfigurationLoader.builder()
            .setFile(dataFile)
            .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
            .build()
        loadOrCreate()
    }

    private fun loadOrCreate() {
        if (!dataDirectory.toFile().exists()) dataDirectory.toFile().mkdirs()
        if (!dataFile.exists()) {
            val inputStream = javaClass.getResourceAsStream("/config.yml")
            if (inputStream != null) {
                try {
                    Files.copy(inputStream, dataFile.toPath())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        configurationNode = loader.load()
    }
}
