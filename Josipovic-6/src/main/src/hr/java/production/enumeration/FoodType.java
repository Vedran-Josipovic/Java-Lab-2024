package hr.java.production.enumeration;

public enum FoodType {
    PIZZA(1),
    CHICKEN_NUGGETS(2);

    private final Integer choice;

    FoodType(Integer value) {
        this.choice = value;
    }

    public Integer getChoice() {
        return choice;
    }

    @Override
    public String toString() {
        return "FoodType{" +
                "value=" + choice +
                '}';
    }
}
