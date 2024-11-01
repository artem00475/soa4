package service;

import client.PeopleClient;

import java.util.LinkedList;
import java.util.List;

public class PeopleService {

    private List<String> colors;
    private List<String> countries;

    private final PeopleClient peopleClient;

    public PeopleService() {
        peopleClient = new PeopleClient();
    }

    private List<String> getColors() {
        if (colors == null) {
            colors = peopleClient.getColors();
        }
        return colors;
    }

    private List<String> getCountries() {
        if (countries == null) {
            countries = peopleClient.getCountries();
        }
        return countries;
    }

    public double getPercentage(String color) {
        if (color == null || color.isEmpty() || !getColors().contains(color.toUpperCase())) {
            throw new IllegalArgumentException("hair-color");
        }
        int total = peopleClient.getTotal();
        return total == 0 ? 0.0 : (double) peopleClient.getCountByHairColor(color) / total * 100;
    }

    public int getCount(String nationality, String color) {
        List<String> errors = new LinkedList<>();
        if (color == null || color.isEmpty() || !getColors().contains(color.toUpperCase())) {
            errors.add("hair-color");
        }
        if (nationality == null || nationality.isEmpty() || !getCountries().contains(nationality.toUpperCase())) {
            errors.add("nationality");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(",", errors));
        }
        return peopleClient.getCountByNationalityAndHairColor(nationality, color);
    }
}
