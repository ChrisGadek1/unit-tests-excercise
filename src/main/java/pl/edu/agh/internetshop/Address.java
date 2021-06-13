package pl.edu.agh.internetshop;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Address implements Findable {
    private String name;
    private String streetAndHomeNr;
    private String postalCode;
    private String city;

    public Address(String name, String streetAndHomeNr, String postalCode, String city) {
        this.name = name;
        this.streetAndHomeNr = streetAndHomeNr;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Address() {}
    public String getName() {
        return name;
    }

    public String getStreetAndHomeNr() {
        return streetAndHomeNr;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String getCsvPath() {
        return "address.csv";
    }

    @Override
    public String[] toCSV() {
        int index = CSVProvider.getNextIndex(getCsvPath());
        String[] res = {Integer.toString(index), name, streetAndHomeNr,postalCode, city};
        return res;
    }

    @Override
    public int isInDatabase() {
        List<Integer> listOfIndexes = Findable.getIDs(Address.class);
        Iterator indexIt = listOfIndexes.iterator();
        while(indexIt.hasNext()){
            int currentIndex = (int)indexIt.next();
            String[] data = CSVProvider.getDataById(getCsvPath(), currentIndex);
            if(data[1].equals(name) && data[2].equals(streetAndHomeNr) && data[3].equals(postalCode) && data[4].equals(city)){
                return currentIndex;
            }
        }
        return -1;
    }

    @Override
    public Address getFromDatabaseById(int id) throws Exception {
        String[] data = CSVProvider.getDataById(getCsvPath(), id);
        if(data.length == 0){
            throw new Exception("brak podanego adresu w bazie");
        }
        Address a = new Address(data[1], data[2], data[3], data[4]);
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(name, address.name) &&
                Objects.equals(streetAndHomeNr, address.streetAndHomeNr) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, streetAndHomeNr, postalCode, city);
    }
}
