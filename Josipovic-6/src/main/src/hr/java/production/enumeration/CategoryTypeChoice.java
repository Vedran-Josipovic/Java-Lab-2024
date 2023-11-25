package hr.java.production.enumeration;

public enum CategoryTypeChoice {
    FOOD(1), LAPTOP(2), OTHER(3);

    private final Integer choice;

    CategoryTypeChoice(Integer choice) {
        this.choice = choice;
    }

    public Integer getChoice() {
        return choice;
    }

    @Override
    public String toString() {
        return "CategoryType{" + "choice=" + choice + '}';
    }
}
