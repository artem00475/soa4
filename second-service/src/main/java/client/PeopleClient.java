package client;

import dto.EnumDTO;
import dto.TotalDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class PeopleClient {
    private static final String REST_URI
            = "https://localhost:8080";

    private Client getClient() {
        return ClientBuilder.newBuilder()
                .hostnameVerifier((s, session) -> true)
                .build();
    }

    public int getTotal() {
        Client client = getClient();
        Response responseTotal = client.target(REST_URI)
                .path("people")
                .queryParam("limit", 1)
                .request()
                .get();
        int total = responseTotal.readEntity(TotalDTO.class).getTotal();
        client.close();
        return total;
    }

    public int getCountByHairColor(String color) {
        Client client = getClient();
        Response responseTotalByColor = client.target(REST_URI)
                .path("people")
                .queryParam("hairColor", color)
                .request()
                .get();
        int totalByColor = responseTotalByColor.readEntity(TotalDTO.class).getTotal();
        client.close();
        return totalByColor;
    }

    public int getCountByNationalityAndHairColor(String nationality, String color) {
        Client client = getClient();
        Response responseCount = client.target(REST_URI)
                .path("people")
                .queryParam("limit", 1)
                .queryParam("nationality", nationality)
                .queryParam("hairColor", color)
                .request()
                .get();
        int count = responseCount.readEntity(TotalDTO.class).getTotal();
        client.close();
        return count;
    }

    public List<String> getColors() {
        Client client = getClient();
        Response responseCount = client.target(REST_URI)
                .path("color")
                .request()
                .get();
        List<String> colors = responseCount.readEntity(EnumDTO.class).getData();
        client.close();
        return colors;
    }

    public List<String> getCountries() {
        Client client = getClient();
        Response responseCount = client.target(REST_URI)
                .path("country")
                .request()
                .get();
        List<String> countries = responseCount.readEntity(EnumDTO.class).getData();
        client.close();
        return countries;
    }
}
