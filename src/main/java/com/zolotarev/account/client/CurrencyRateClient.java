package com.zolotarev.account.client;

import com.zolotarev.account.domain.Currency;
import com.zolotarev.exception.ExternalException;
import com.zolotarev.exception.RequestException;
import com.zolotarev.util.Contract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.zolotarev.constant.Messages.*;

/**
 * Used for getting data about current currency rates from an external service
 */
@Component
public class CurrencyRateClient {

    //Used Reactive WebClient instead RestTemplate, because RestTemplate may be deprecated in next releases (see RestTemplate docs). And WebClient more convenient imho
    private final WebClient client;

    public CurrencyRateClient(@Value("${api.currency_rate.url:http://localhost:8080/api/currency-rates/}") String currencyRateUrl) {
        client = WebClient.create(currencyRateUrl);
    }

    /**
     * Getting rate by currency pair
     * @param first First currency from pair. Mustn't be null
     * @param second Second currency from pair. Mustn't be null
     * @return Mono of currency rate. Contains {@link RequestException} in case if request data is invalid,<br />
     * or {@link ExternalException} in case if something went wrong in an external service
     */
    public Mono<BigDecimal> getRate(Currency first, Currency second) {
        Contract.requiresNotNull(first, CURRENCY_MUST_BE_NOT_NULL);
        Contract.requiresNotNull(second, CURRENCY_MUST_BE_NOT_NULL);

        final String pair = buildCurrencyPairPath(first, second);

        return client.get()
                .uri(builder -> builder.path("/{pair}").build(pair))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RequestException("Invalid request by currency pair[" + pair + "]")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ExternalException("External error by currency pair[" + pair + "]")))
                .bodyToMono(BigDecimal.class);
    }

    private String buildCurrencyPairPath(Currency first, Currency second) {
        return first.name() + "-" + second.name();
    }

}
