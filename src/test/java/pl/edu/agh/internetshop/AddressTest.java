package pl.edu.agh.internetshop;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.creation.SuspendMethod;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {
    
    @Test
    public void addressIsInitialized() {
    	// given
    	String name = "Adam Smith";
        String streetAndHomeNr = "1234 Main Street";
        String postalCode = "01003";
        String city = "New York";
        
        // when
        Address address = new Address(name, streetAndHomeNr, postalCode, city);
        
        // then    	
        assertEquals(name, address.getName());
        assertEquals(streetAndHomeNr, address.getStreetAndHomeNr());
        assertEquals(postalCode, address.getPostalCode());
        assertEquals(city, address.getCity());
    }

    @Test
    public void toCsvTest() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdańsk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");

        String[] mockedData1 = {"1", "Janusz", "Olkuska 48b", "12-123", "Gdańsk"};
        String[] mockedData2 = {"1", "Mariusz", "Polna 4", "22-542", "Warszawa"};

        File f = FileOperations.cleanFile("address.csv");

        //when
        String data1[] = a1.toCSV();
        String data2[] = a2.toCSV();

        //then
        assertArrayEquals(mockedData1, data1);
        assertArrayEquals(mockedData2, data2);

        //clean
        f.delete();
    }

    @Test
    public void isInDatabaseTest() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
        Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
        Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");


        File f = FileOperations.cleanFile("address.csv");

        String[] line1 = {"1", "Janusz", "Olkuska 48b", "12-123", "Gdansk"};
        String[] line2 = {"2","Mariusz", "Polna 4", "22-542", "Warszawa"};

        CSVProvider.addNewLine("address.csv", line1);
        CSVProvider.addNewLine("address.csv", line2);
        //when
        int res1 = a1.isInDatabase();
        int res2 = a2.isInDatabase();
        int res3 = a3.isInDatabase();

        //then
        System.out.println(FileUtils.readLines(f));

        assertEquals(1, res1);
        assertEquals(2, res2);
        assertEquals(-1, res3);

        //clean
        f.delete();
    }

    @Test
    public void addressObjectFromDatabaseTest() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
        Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
        Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");

        File f = FileOperations.cleanFile("address.csv");

        Findable.addToDatabase(a1);
        Findable.addToDatabase(a2);
        Findable.addToDatabase(a3);
        Findable.addToDatabase(a4);

        //when
        Address gotA1 = (Address)Findable.getFromDatabaseByClassAndId(Address.class, 1);
        Address gotA2 = (Address)Findable.getFromDatabaseByClassAndId(Address.class, 2);
        Address gotA3 = (Address)Findable.getFromDatabaseByClassAndId(Address.class, 3);
        Address gotA4 = (Address)Findable.getFromDatabaseByClassAndId(Address.class, 4);

        //then
        assertEquals(a1.equals(gotA1), true);
        assertEquals(a2.equals(gotA2), true);
        assertEquals(a3.equals(gotA3), true);
        assertEquals(a4.equals(gotA4), true);
    }
}