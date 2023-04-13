package eu.syplex.proxy.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import eu.syplex.proxy.util.ComponentTranslator
import eu.syplex.proxy.config.news.NewsFetcher
import eu.syplex.proxy.util.Placeholder


class NewsCommand(private val translator: ComponentTranslator, private val news: NewsFetcher) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val args = invocation.arguments()

        if (sender !is Player) {
            sender.sendMessage(translator.fromConfig("no-player"))
            return
        }

        if (args.isNotEmpty()) {
            sender.sendMessage(translator.fromConfigWithReplacement("invalid-usage", Placeholder.command, "news"))
            return
        }

        val list = news.getNews()
        list.forEach { str ->
            sender.sendMessage(str)
        }

    }
}