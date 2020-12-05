package finance.controllers

import finance.domain.Parameter
import finance.services.ParmApiService
import org.jetbrains.annotations.NotNull
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Path
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import spock.lang.Shared
import spock.lang.Specification

class ParameterControllerSpec extends Specification {
    static final String BASE_URL = "http://url.com"

    @Shared
    NetworkBehavior behavior = NetworkBehavior.create()

    @Shared
    ParmApiService mockParmService

    static class MockParmService implements ParmApiService {

        BehaviorDelegate<ParmApiService> delegate

        MockParmService(BehaviorDelegate<ParmApiService> delegate) {
            this.delegate = delegate
        }

        @Override
        Call<List<Parameter>> selectParm(@NotNull @Path("parmName") String parmName) {
            delegate.returningResponse().selectParm(parmName)
        }
    }

    void setupSpec() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build()
        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).networkBehavior(behavior).build()
        BehaviorDelegate<ParmApiService> delegate = mockRetrofit.create(ParmApiService)

        mockParmService = new MockParmService(delegate)
    }

    void "test - should succeed"() {
        given:
        def response = mockParmService.selectParm('test').execute()

        expect:
        response.successful
    }

    void 'test -- selectParm - should fail on 500'() {
        given:
        behavior.setFailurePercent(100)

        when:
        mockParmService.selectParm('test').execute()

        then:
        IOException ex = thrown(IOException)
    }
}
