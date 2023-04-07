package eu.syplex.proxy.backend.database.sadu

import com.zaxxer.hikari.HikariDataSource
import de.chojo.sadu.databases.MariaDb
import de.chojo.sadu.datasource.DataSourceCreator
import eu.syplex.proxy.backend.database.connection.ConnectionProperty

object StaticDataLoader {

    fun connect(property: ConnectionProperty) {
        StaticQueryAdapter.start(createSource(property))
    }

    private fun createSource(property: ConnectionProperty): HikariDataSource {
        return DataSourceCreator.create(MariaDb.get())
            .configure { config ->
                config.host(property.host)
                config.port(property.port)
                config.database(property.database)
                config.user(property.username)
                config.password(property.password)
            }
            .create()
            .withMaximumPoolSize(3)
            .withMinimumIdle(1)
            .build()
    }

}