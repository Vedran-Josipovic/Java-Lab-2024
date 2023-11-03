package hr.java.production.enumeration;

/**
 * Source for enum city values: "https://sh.wikipedia.org/wiki/Popis_gradova_u_Hrvatskoj"
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

    @Override
    public String toString() {
        return "Cities{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
