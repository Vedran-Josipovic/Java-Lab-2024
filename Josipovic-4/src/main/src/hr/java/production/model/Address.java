package hr.java.production.model;

/**
 * Represents an address with a street, house number, city, and postal code.
 * This class uses the Builder pattern to create instances.
 */
public class Address {
    private String street, houseNumber, city, postalCode;

    /**
     * Private constructor used by the Builder class to create instances.
     */
    private Address() {
    }

    /**
     * Builder class for Address. Allows for the creation of Address instances using method chaining.
     */
    public static class Builder {
        private String street, houseNumber, city, postalCode;

        /**
         * Sets the street of the Address being built.
         *
         * @param street The street name.
         * @return The current Builder instance.
         */
        public Builder atStreet(String street) {
            this.street = street;
            return this;
        }

        /**
         * Sets the house number of the Address being built.
         *
         * @param houseNumber The house number.
         * @return The current Builder instance.
         */
        public Builder atHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        /**
         * Sets the city of the Address being built.
         *
         * @param city The city name.
         * @return The current Builder instance.
         */
        public Builder atCity(String city) {
            this.city = city;
            return this;
        }

        /**
         * Sets the postal code of the Address being built.
         *
         * @param postalCode The postal code.
         * @return The current Builder instance.
         */
        public Builder atPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Constructs an Address instance with the set parameters.
         *
         * @return A new Address instance.
         */
        public Address build() {
            Address address = new Address();
            address.street = this.street;
            address.houseNumber = this.houseNumber;
            address.city = this.city;
            address.postalCode = this.postalCode;
            return address;
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns a string representation of the Address instance.
     *
     * @return A string representation of this Address.
     */
    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' + ", houseNumber='" + houseNumber + '\'' + ", city='" + city + '\'' + ", postalCode='" + postalCode + '\'' + '}';
    }
}
