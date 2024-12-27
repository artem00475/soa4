package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import se.ifmo.ru.firstservice.person.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Endpoint
@RequiredArgsConstructor
public class PeopleEndpoint {
    private static final String NAMESPACE_URI = "http://se/ifmo/ru/firstservice/person";
    private final PersonService personService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getColorRequest")
    @ResponsePayload
    public GetColorResponse getColors() {
        GetColorResponse response = new GetColorResponse();
        ColorsResponse colorsResponse = new ColorsResponse();
        List<ColorEnum> data = colorsResponse.getColor();
        data.addAll(Arrays.stream(ColorEnum.values()).toList());
        response.setData(colorsResponse);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry() {
        GetCountryResponse response = new GetCountryResponse();
        CountriesResponse countriesResponse = new CountriesResponse();
        List<CountryEnum> data = countriesResponse.getCountry();
        data.addAll(Arrays.stream(CountryEnum.values()).toList());
        response.setData(countriesResponse);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPersonRequest")
    @ResponsePayload
    public GetPersonResponse getPerson(@RequestPayload GetPersonRequest request) {
        GetPersonResponse response = new GetPersonResponse();
        response.setPerson(personService.get(request.getId()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addPersonRequest")
    @ResponsePayload
    public AddPersonResponse addPerson(@RequestPayload AddPersonRequest request) {
        AddPersonResponse response = new AddPersonResponse();
        response.setPerson(personService.add(request.getPerson()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePersonRequest")
    @ResponsePayload
    public void updatePerson(@RequestPayload UpdatePersonRequest request) {
        personService.update(request.getId(), request.getPerson());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "patchPersonRequest")
    @ResponsePayload
    public void patchPerson(@RequestPayload PatchPersonRequest request) {
        personService.patch(request.getId(), request.getPerson());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePersonRequest")
    @ResponsePayload
    public void deletePerson(@RequestPayload DeletePersonRequest request) {
        personService.delete(request.getId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "heightOperationRequest")
    @ResponsePayload
    public HeightOperationResponse aggregateHeight(@RequestPayload HeightOperationRequest request) {
        HeightOperationResponse response = new HeightOperationResponse();
        response.setValue(switch (request.getFunction()) {
            case "count" -> personService.countHeight(request.getHeight());
            case "average" -> personService.averageHeight();
            default -> "0";
        });

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPeopleRequest")
    @ResponsePayload
    public GetPeopleResponse getPeople(@RequestPayload GetPeopleRequest request) {
        Map<String, String> map = new HashMap<>();
        request.getQueryParams().forEach(query -> {
            String[] pair = query.split("=");
            map.put(pair[0], pair[1]);
        });
        return personService.getAll(
                request.getLimit(),
                request.getOffset(),
                map
        );
    }
}
