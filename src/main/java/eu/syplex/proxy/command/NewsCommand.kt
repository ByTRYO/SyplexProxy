package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.util.news.NewsFetcher


class NewsCommand(private val translator: ComponentTranslator, private val news: NewsFetcher) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            sender.sendMessage(translator.fromConfig("invalid-usage"))
            return
        }

        val list = news.getNews()
        list.forEach { str ->
            sender.sendMessage(str)
        }

    }
}