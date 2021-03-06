package finance.routes

import finance.configurations.CamelProperties
import finance.processors.ExceptionProcessor
import io.micrometer.core.annotation.Timed
import org.apache.camel.Exchange
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

//@ConditionalOnProperty(name = ["camel.enabled"], havingValue = "true", matchIfMissing = true)
@Component
open class JsonFileWriterRouteBuilder(
    private var camelProperties: CamelProperties, private var exceptionProcessor: ExceptionProcessor
) : RouteBuilder() {

    @Timed
    @Throws(Exception::class)
    override fun configure() {

        from(camelProperties.jsonFileWriterRoute)
            .autoStartup(camelProperties.autoStartRoute)
            //.routeId(camelProperties.jsonFileWriterRouteId)
            .routeId(JsonFileWriterRouteBuilder::class.java.simpleName.toString().replace("Builder", ""))
            .setHeader(Exchange.FILE_NAME, header("guid"))
            .choice()
            .`when`(header("CamelFileName").isNotNull)
            .log(LoggingLevel.INFO, "wrote processed data to file.")
            .to(camelProperties.savedFileEndpoint)
            .log(LoggingLevel.INFO, "message saved payload to file.")
            .otherwise()
            .throwException(RuntimeException("filename is not set."))
            .endChoice()
            .end()
    }
}