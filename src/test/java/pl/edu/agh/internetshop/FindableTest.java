package pl.edu.agh.internetshop;
import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

public class FindableTest {
    @Test
    public void getCsvPathTest(){
        //given
        String mockedOrdersPath = "orders.csv";
        String mockedAddressPath = "address.csv";
        String mockedProductPath = "product.csv";

        //when
        String ordersPath = Findable.getCsvPathByClass(Order.class);
        String addressPath = Findable.getCsvPathByClass(Address.class);
        String productPath = Findable.getCsvPathByClass(Product.class);
        //then

        assertEquals(mockedAddressPath, addressPath);
        assertEquals(mockedOrdersPath, ordersPath);
        assertEquals(mockedProductPath, productPath);
    }

    @Test
    public void getIdsTest() throws Exception{
        //given
        String[] s1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] s2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] s3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] s4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        File f = FileOperations.cleanFile("address.csv");

        CSVProvider.addNewLine("address.csv", s1);
        CSVProvider.addNewLine("address.csv", s2);
        CSVProvider.addNewLine("address.csv", s3);
        CSVProvider.addNewLine("address.csv", s4);

        List<Integer> mockedList = new LinkedList<>(Arrays.asList(1,2,3,4));

        //when
        List<Integer> list = Findable.getIDs(Address.class);

        //then
        assertArrayEquals(mockedList.toArray(), list.toArray());

        //clean
        f.delete();
    }

    @Test
    public void addProductToDatabaseTest() throws Exception{
        //given
        Product p1 = new Product("rower", new BigDecimal("642.45"));
        Product p2 = new Product("kocyk", new BigDecimal("24.33"));
        Product p3 = new Product("koszulka", new BigDecimal("33.65"));

        File f = FileOperations.cleanFile("product.csv");

        //when
        Findable.addToDatabase(p1);
        Findable.addToDatabase(p2);

        //then
        assertEquals(1, p1.isInDatabase());
        assertEquals(2, p2.isInDatabase());
        assertEquals(-1, p3.isInDatabase());
    }

    @Test
    public void addAddressToDatabase() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
        Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");

        File f = FileOperations.cleanFile("address.csv");

        //when
        Findable.addToDatabase(a1);
        Findable.addToDatabase(a2);

        //then
        assertEquals(1, a1.isInDatabase());
        assertEquals(2, a2.isInDatabase());
        assertEquals(-1, a3.isInDatabase());
    }

    @Test
    public void addMoneyTransferToDatabase() throws Exception{
        //given
        MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
        m1.setCommitted(true);
        MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);
        m2.setCommitted(true);
        MoneyTransfer m3 = new MoneyTransfer(new BigInteger("3475983479"), "Marian i żona Halina", "passat 1.9 tdi", 2);

        File f = FileOperations.cleanFile("moneyTransfer.csv");

        //when
        Findable.addToDatabase(m1);
        Findable.addToDatabase(m2);

        //then
        assertEquals(1, m1.isInDatabase());
        assertEquals(2, m2.isInDatabase());
        assertEquals(-1, m3.isInDatabase());
    }

    @Test
    public void addShipmentToDatabase() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
        Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
        Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");

        File f = FileOperations.cleanFile("shipment.csv");
        File f1 = FileOperations.cleanFile("address.csv");

        Shipment s1 = new Shipment(a1, a2);
        Shipment s2 = new Shipment(a3, a4);
        s2.setShipped(true);

        //when
        Findable.addToDatabase(s1);
        Findable.addToDatabase(s2);

        //then
        assertEquals(1, s1.isInDatabase());
        assertEquals(2, s2.isInDatabase());
    }
}
