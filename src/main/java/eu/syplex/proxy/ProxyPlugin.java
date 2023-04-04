package eu.syplex.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.syplex.proxy.command.PingCommand;
import eu.syplex.proxy.command.YouTubeCommand;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "proxy", name = "Proxy", version = "1.0", authors = {"Merry, ByTRYO"})
public class ProxyPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public ProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("Proxy has been enabled successfully");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();

        CommandMeta pingCommandMeta = commandManager.metaBuilder("ping").plugin(this).build();
        commandManager.register(pingCommandMeta, new PingCommand());

        CommandMeta youtubeCommandMeta = commandManager.metaBuilder("youtube").aliases("yt").plugin(this).build();
        commandManager.register(youtubeCommandMeta, new YouTubeCommand());
    }
}
