package eu.syplex.proxy.util.news

import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.loader.ConfigurationLoader
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader
import org.yaml.snakeyaml.DumperOptions
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class NewsLoader(private val dataDirectory: Path) {
    private val dataFile: File = File(dataDirectory.toFile(), "news.yml")
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
            val inputStream = javaClass.getResourceAsStream("/news.yml")
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
