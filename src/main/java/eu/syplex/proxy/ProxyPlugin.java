package eu.syplex.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "proxy", name = "Proxy", version = "1.0-SNAPSHOT", description = "Proxy functionalities", authors = {"Merry, ByTRYO"})
public class ProxyPlugin {

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public ProxyPlugin(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        logger.info("ProxyPlugin started successfully!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Register stuff here
    }
}
