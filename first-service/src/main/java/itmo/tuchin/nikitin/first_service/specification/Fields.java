package itmo.tuchin.nikitin.first_service.specification;

public enum Fields {
    NAME("name"),
    HEIGHT("height"),
    CREATION_DATE("creationDate"),
    EYE_COLOR("eyeColor"),
    HAIR_COLOR("hairColor"),
    NATIONALITY("nationality"),
    ID("id"),
    COORDINATES_X("coordinates.x"),
    COORDINATES_Y("coordinates.y"),
    LOCATION_X("location.x"),
    LOCATION_Y("location.y"),
    LOCATION_NAME("location.name");
    private final String value;
    Fields(final String s) { value = s; }
    public String toString() { return value; }
    public static Fields findByString(String value){
        for(Fields v : values()){
            if( v.value.equals(value)){
                return v;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
