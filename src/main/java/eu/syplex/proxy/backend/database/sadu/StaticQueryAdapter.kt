package eu.syplex.proxy.backend.database.sadu

import de.chojo.sadu.base.QueryFactory
import de.chojo.sadu.wrapper.stage.QueryStage
import java.util.logging.Logger
import javax.sql.DataSource

object StaticQueryAdapter {

    private val logger = Logger.getLogger(StaticQueryAdapter::class.simpleName)
    private lateinit var factory: QueryFactory

    fun <T> builder(clazz: Class<T>): QueryStage<T> {
        return factory.builder(clazz)
    }

    fun builder(): QueryStage<Void> {
        return factory.builder()
    }

    fun start(dataSource: DataSource) {
        factory = QueryFactory(dataSource)
        logger.info("Static SADU query adapter started")
    }


}