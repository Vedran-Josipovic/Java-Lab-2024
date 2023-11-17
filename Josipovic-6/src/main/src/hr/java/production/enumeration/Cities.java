package hr.java.production.enumeration;

/**
 * Represents cities in Croatia with their corresponding postal codes.
 * The source for city values is: "<a href="https://sh.wikipedia.org/wiki/Popis_gradova_u_Hrvatskoj">Wikipedia</a>"
 */
public enum Cities {
    ZAGREB("Zagreb", "10000"),
    SPLIT("Split", "21000"),
    RIJEKA("Rijeka", "51000"),
    OSIJEK("Osijek", "31000"),
    ZADAR("Zadar", "23000"),
    SLAVONSKI_BROD("Slavonski Brod", "35000"),
    VELIKA_GORICA("Velika Gorica", "10410");

    private String name, postalCode;

    /**
     * Constructs a new city with the specified name and postal code.
     *
     * @param name       The name of the city.
     * @param postalCode The postal code of the city.
     */
    Cities(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns a string representation of the city.
     *
     * @return A string representation of the city, including its name and postal code.
     */
    @Override
    public String toString() {
        return "Cities{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
