package hr.java.production.enumeration;

public enum StoreType {
    TECHNICAL_STORE(1),
    FOOD_STORE(2);

    private final int value;

    StoreType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StoreType{" +
                "value=" + value +
                '}';
    }
}
