package hr.java.production.model;

public class Address {
    private String street, houseNumber, city, postalCode;

    private Address() {
    }

    public static class Builder {
        private String street, houseNumber, city, postalCode;

        public Builder atStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder atHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder atCity(String city) {
            this.city = city;
            return this;
        }

        public Builder atPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

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

    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' + ", houseNumber='" + houseNumber + '\'' + ", city='" + city + '\'' + ", postalCode='" + postalCode + '\'' + '}';
    }
}
