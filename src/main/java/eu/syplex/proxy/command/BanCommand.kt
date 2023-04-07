package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ProxyServer
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.Placeholder

class BanCommand(private val proxyServer: ProxyServer, private val translator: ComponentTranslator) : SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty() || args.size == 1) {
            source.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "ban <Name> <ID>"))
            return
        }

        val optionalPlayer = proxyServer.getPlayer(args[0])

        if (!optionalPlayer.isPresent) {
            source.sendMessage(translator.fromConfig("not-online"))
            return
        }

        val target = optionalPlayer.get()

        TODO("Implement backend first")

    }
}