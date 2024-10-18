package service;

import client.PeopleClient;

import java.util.List;

public class PeopleService {

    private final PeopleClient peopleClient;

    public PeopleService() {
        peopleClient = new PeopleClient();
    }

    public float getPercentage(String color) {
        List<String> colors = peopleClient.getColors();
        assert color != null;
        if (color.isEmpty() | !colors.contains(color.toUpperCase())) {
            throw new IllegalArgumentException("hair-color");
        }
        int total = peopleClient.getTotal();
        int countByColor = peopleClient.getCountByHairColor(color);
        float percent = (float) countByColor / total * 100;
        return percent;
    }

    public int getCount(String nationality, String color) {
        List<String> colors = peopleClient.getColors();
        assert color != null;
        if (color.isEmpty() | !colors.contains(color.toUpperCase())) {
            throw new IllegalArgumentException("hair-color");
        }
        List<String> countries = peopleClient.getCountries();
        assert nationality != null;
        if (nationality.isEmpty() | !countries.contains(nationality.toUpperCase())) {
            throw new IllegalArgumentException("nationality");
        }
        return peopleClient.getCountByNationalityAndHairColor(nationality, color);
    }
}
