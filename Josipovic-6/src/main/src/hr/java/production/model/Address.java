package hr.java.production.model;

import hr.java.production.enumeration.Cities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an address with a street, house number, city, and postal code.
 * This class uses the Builder pattern to create instances.
 */
public class Address implements Serializable {
    private String street, houseNumber;
    private Cities city;

    /**
     * Private constructor used by the Builder class to create instances.
     */
    private Address() {
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

    public Cities getCity() {
        return city;
    }

    public void setCity(Cities city) {
        this.city = city;
    }

    public String getCityName() {
        return city.getName();
    }

    public String getCityPostalCode() {
        return city.getPostalCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getHouseNumber(), address.getHouseNumber()) && getCity() == address.getCity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getHouseNumber(), getCity());
    }

    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' + ", houseNumber='" + houseNumber + '\'' + ", city=" + city + '}';
    }

    /**
     * Builder class for Address. Allows for the creation of Address instances using method chaining.
     */
    public static class Builder {
        private String street, houseNumber;
        private Cities city;

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
         * @param city The city.
         * @return The current Builder instance.
         */
        public Builder atCity(Cities city) {
            this.city = city;
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
            return address;
        }
    }

}
