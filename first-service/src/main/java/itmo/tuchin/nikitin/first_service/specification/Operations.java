package itmo.tuchin.nikitin.first_service.specification;

public enum Operations {
    NONE(""),
    GREATER(">"),
    LESS("<"),
    GREATER_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),
    LIKE("~"),
    EQUAL("=");
    private final String value;
    Operations(final String s) { value = s; }
    public String toString() { return value; }
    public static Operations findByString(String value){
        for(Operations v : values()){
            if( v.value.equals(value)){
                return v;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
