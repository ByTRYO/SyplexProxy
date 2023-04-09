package eu.syplex.proxy.util

import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import eu.syplex.proxy.ProxyPlugin
import java.util.concurrent.TimeUnit


class JoinMeValidator(private val plugin: ProxyPlugin, private val proxyServer: ProxyServer, private val translator: ComponentTranslator) {
    private val currentServer = ArrayList<RegisteredServer>()
    private var cooldown = false

    fun createJoinMe(registeredServer: RegisteredServer) {
        currentServer.add(registeredServer)
        cooldown = true

        cancelUsability()
    }

    private fun cancelUsability() {
        proxyServer.scheduler.buildTask(plugin) {
            cooldown = false
            currentServer.clear()
        }.delay(translator.raw("joinme-delay").toLong(), TimeUnit.SECONDS).schedule()

    }

    fun getServer(): RegisteredServer {
        return currentServer[0]
    }

    fun isCooldownActive(): Boolean {
        return cooldown
    }
}