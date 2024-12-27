package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.entity.Color;
import itmo.tuchin.nikitin.first_service.entity.Country;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import se.ifmo.ru.firstservice.person.ColorEnum;
import se.ifmo.ru.firstservice.person.CountryEnum;
import se.ifmo.ru.firstservice.person.GetColorResponse;
import se.ifmo.ru.firstservice.person.GetCountryResponse;

import java.util.Arrays;
import java.util.List;

@Endpoint
public class EnumEndpoint {
    private static final String NAMESPACE_URI = "http://se/ifmo/ru/firstservice/person";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getColorRequest")
    @ResponsePayload
    public GetColorResponse getColors() {
        GetColorResponse response = new GetColorResponse();
        List<ColorEnum> data = response.getData();
        data.addAll(Arrays.stream(Color.values()).map(value -> ColorEnum.fromValue(value.name())).toList());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry() {
        GetCountryResponse response = new GetCountryResponse();
        List<CountryEnum> data = response.getData();
        data.addAll(Arrays.stream(Country.values()).map(value -> CountryEnum.fromValue(value.name())).toList());

        return response;
    }
}
