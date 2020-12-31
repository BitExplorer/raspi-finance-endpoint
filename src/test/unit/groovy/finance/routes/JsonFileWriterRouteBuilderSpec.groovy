package finance.routes

import finance.configurations.CamelProperties
import finance.processors.ExceptionProcessor
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.model.ModelCamelContext
import spock.lang.Specification

class JsonFileWriterRouteBuilderSpec extends Specification {

    protected ModelCamelContext camelContext
    protected ExceptionProcessor mockExceptionProcessor = GroovyMock(ExceptionProcessor)

    protected CamelProperties camelProperties = new CamelProperties(
            "true",
            "n/a",
            "n/a",
            "fileWriterRoute",
            "direct:routeFromLocal",
            "transactionToDatabaseRoute",
            "direct:routeFromLocal",
            "mock:toSavedFileEndpoint",
            "mock:toFailedJsonFileEndpoint",
            "mock:toFailedJsonParserEndpoint")

    void setup() {
        camelContext = new DefaultCamelContext()
        JsonFileWriterRouteBuilder router = new JsonFileWriterRouteBuilder(camelProperties, mockExceptionProcessor)
        camelContext.addRoutes(router)
        camelContext.start()
    }

    void cleanup() {
        camelContext.stop()
    }

    void 'test -- valid payload - 1 messages'() {
        given:
        MockEndpoint mockTestOutputEndpoint = MockEndpoint.resolve(camelContext, camelProperties.savedFileEndpoint)
        mockTestOutputEndpoint.expectedCount = 1
        ProducerTemplate producer = camelContext.createProducerTemplate()
        producer.setDefaultEndpointUri('direct:routeFromLocal')

        when:
        producer.sendBodyAndHeader('theData', 'guid', '123')

        then:
        mockTestOutputEndpoint.receivedExchanges.size() == 1
        mockTestOutputEndpoint.assertIsSatisfied()
        0 * _
    }

    void 'test -- invalid payload - 1 messages'() {
        given:
        MockEndpoint mockTestOutputEndpoint = MockEndpoint.resolve(camelContext, camelProperties.savedFileEndpoint)
        mockTestOutputEndpoint.expectedCount = 0
        ProducerTemplate producer = camelContext.createProducerTemplate()
        producer.setDefaultEndpointUri('direct:routeFromLocal')

        when:
        producer.sendBody('theDataWithoutHeader')

        then:
        mockTestOutputEndpoint.receivedExchanges.size() == 0
        1 * mockExceptionProcessor.process(_ as Exchange)
        mockTestOutputEndpoint.assertIsSatisfied()
        0 * _
    }
}
