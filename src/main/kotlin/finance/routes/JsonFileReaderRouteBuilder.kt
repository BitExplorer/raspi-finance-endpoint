package finance.routes

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import finance.configurations.CamelProperties
import finance.processors.ExceptionProcessor
import finance.processors.JsonTransactionProcessor
import io.micrometer.core.annotation.Timed
import org.apache.camel.InvalidPayloadException
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

//@ConditionalOnProperty(name = ["camel.enabled"], havingValue = "true", matchIfMissing = true)
@Component
open class JsonFileReaderRouteBuilder(
    private var camelProperties: CamelProperties,
    private var jsonTransactionProcessor: JsonTransactionProcessor,
    private var exceptionProcessor: ExceptionProcessor
) : RouteBuilder() {

    @Timed
    @Throws(Exception::class)
    override fun configure() {

        onException(JsonParseException::class.java)
            .log(LoggingLevel.ERROR, "Jason parsing issue :: \${exception.message}")
            .process(exceptionProcessor)
            .to(camelProperties.failedJsonParserEndpoint)
            .handled(true)
            .end()

        onException(InvalidFormatException::class.java)
            .log(LoggingLevel.ERROR, "Invalid format :: \${exception.message}")
            .process(exceptionProcessor)
            .to(camelProperties.failedJsonParserEndpoint)
            .handled(true)
            .end()

        onException(UnrecognizedPropertyException::class.java)
            .log(LoggingLevel.ERROR, "Unrecognized Property :: \${exception.message}")
            .process(exceptionProcessor)
            .to(camelProperties.failedJsonParserEndpoint)
            .handled(true)
            .end()

        onException(InvalidPayloadException::class.java)
            .log(LoggingLevel.ERROR, "invalid payload :: \${exception.message}")
            .process(exceptionProcessor)
            .to(camelProperties.failedJsonParserEndpoint)
            .handled(true)
            .end()


        from(camelProperties.jsonFileReaderRoute)
            .autoStartup(camelProperties.autoStartRoute)

            //TODO: change how the routeId is set
            .routeId(JsonFileReaderRouteBuilder::class.java.simpleName.toString().replace("Builder", ""))
            //.routeId(camelProperties.jsonFileReaderRouteId)
            .log("fname = \${header.CamelFileName}")
            .choice()
            .`when`(header("CamelFileName").endsWith(".json"))
            .log(LoggingLevel.INFO, "fileName - new: \$simple{file:onlyname.noext}_\$simple{date:now:yyyy-MM-dd}.json")
            .process(jsonTransactionProcessor)
            .to(camelProperties.transactionToDatabaseRoute)
            .log(LoggingLevel.INFO, "JSON file processed successfully.")
            .otherwise()
            .to(camelProperties.failedJsonFileEndpoint)
            .log(LoggingLevel.ERROR, "Not a JSON file, NOT processed successfully.")
            .endChoice()
            .end()
    }
}