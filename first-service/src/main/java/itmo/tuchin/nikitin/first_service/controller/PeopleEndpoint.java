package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.entity.Color;
import itmo.tuchin.nikitin.first_service.entity.Country;
import itmo.tuchin.nikitin.first_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import se.ifmo.ru.firstservice.person.*;

import java.util.Arrays;
import java.util.List;

@Endpoint
@RequiredArgsConstructor
public class PeopleEndpoint {
    private static final String NAMESPACE_URI = "http://se/ifmo/ru/firstservice/person";
    private final PersonService personService;
    private final ModelMapper modelMapper;

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

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPersonRequest")
    @ResponsePayload
    public GetPersonResponse getPerson(@RequestPayload GetPersonRequest request) {
        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(modelMapper.map(personService.get(request.getId()), PersonResponse.class));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addPersonRequest")
    @ResponsePayload
    public AddPersonResponse addPerson(@RequestPayload AddPersonRequest request) {
        AddPersonResponse response = new AddPersonResponse();
        response.setPerson(modelMapper.map(personService.add(modelMapper.map(request.getPerson(), itmo.tuchin.nikitin.first_service.dto.PersonDTO.class)), PersonResponse.class));

        return response;
    }
}
