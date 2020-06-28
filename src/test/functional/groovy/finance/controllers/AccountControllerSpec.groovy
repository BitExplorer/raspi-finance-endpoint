package finance.controllers

import finance.Application
import finance.domain.Account
import finance.domain.Transaction
import finance.helpers.AccountBuilder
import finance.helpers.CategoryBuilder
import finance.helpers.TransactionBuilder
import finance.services.AccountService
import finance.services.CategoryService
import finance.services.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

@ActiveProfiles("stage-account")
@SpringBootTest(classes = Application, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerSpec extends Specification {

    @LocalServerPort
    protected int port

    TestRestTemplate restTemplate = new TestRestTemplate()

    @Shared
    HttpHeaders headers

    @Autowired
    TransactionService transactionService

    @Autowired
    AccountService accountService

    @Autowired
    CategoryService categoryService

    @Shared
    Account account


    def setupSpec() {
        headers = new HttpHeaders()
        account = AccountBuilder.builder().build()
    }


    private String createURLWithPort(String uri) {
        println "port = ${port}"

        return "http://localhost:" + port + uri
    }

    def "test findAccount endpoint accountNameOwner found"() {
        given:
        accountService.insertAccount(account)
        HttpEntity entity = new HttpEntity<>(null, headers)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/account/select/" + account.accountNameOwner), HttpMethod.GET,
                entity, String.class)
        then:
        assert response.statusCode == HttpStatus.OK
        0 * _

        cleanup:
        accountService.deleteByAccountNameOwner(account.accountNameOwner)
    }

    def "test findAccount endpoint accountNameOwner not found"() {
        given:
        HttpEntity entity = new HttpEntity<>(null, headers)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/account/select/" + UUID.randomUUID().toString()), HttpMethod.GET,
                entity, String.class)
        then:
        assert response.statusCode == HttpStatus.NOT_FOUND
        0 * _
    }

    def "test deleteAccount endpoint"() {
        given:
        accountService.insertAccount(account)

        HttpEntity entity = new HttpEntity<>(null, headers)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/account/delete/" + account.accountNameOwner), HttpMethod.DELETE,
                entity, String.class)
        then:
        assert response.statusCode == HttpStatus.OK
        0 * _

        cleanup:
        accountService.deleteByAccountNameOwner(account.accountNameOwner)
    }

    def "test insertAccount endpoint bad data"() {
        given:
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity entity = new HttpEntity<>("foo", headers)

        when:
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/account/insert/"), HttpMethod.POST,
                entity, String.class)
        then:
        //thrown HttpMessageNotReadableException
        assert response.statusCode == HttpStatus.BAD_REQUEST
        0 * _

    }
}