package service;

import client.PeopleClient;

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

    public float getPercentage(String color) {
        List<String> colors = getColors();
        assert color != null : "hair-color";
        if (color.isEmpty() | !colors.contains(color.toUpperCase())) {
            throw new IllegalArgumentException("hair-color");
        }
        int total = peopleClient.getTotal();
        int countByColor = peopleClient.getCountByHairColor(color);
        float percent = (float) countByColor / total * 100;
        return percent;
    }

    public int getCount(String nationality, String color) {
        List<String> colors = getColors();
        assert color != null : "hair-color";
        if (color.isEmpty() | !colors.contains(color.toUpperCase())) {
            throw new IllegalArgumentException("hair-color");
        }
        List<String> countries = getCountries();
        assert nationality != null : "nationality";
        if (nationality.isEmpty() | !countries.contains(nationality.toUpperCase())) {
            throw new IllegalArgumentException("nationality");
        }
        return peopleClient.getCountByNationalityAndHairColor(nationality, color);
    }
}
