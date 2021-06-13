package pl.edu.agh.internetshop;
import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FindMethodsTest {
    //test data
    Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
    Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
    Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
    Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");
    Address a5 = new Address("Tomasz","Wolska 32a", "98-123","Rzeszow");

    Shipment s1 = new Shipment(a1, a2);
    Shipment s2 = new Shipment(a3, a4);
    Shipment s3 = new Shipment(a5, a2);
    Shipment s4 = new Shipment(a3, a1);

    MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
    MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);

    Product p1 = new Product("rower", new BigDecimal("64.45"));
    Product p2 = new Product("kocyk", new BigDecimal("24.33"));
    Product p3 = new Product("koszulka", new BigDecimal("33.65"));
    Product p4 = new Product("maska od plywania", new BigDecimal("111.65"));
    Product p5 = new Product("lyzka do jedzenia", new BigDecimal("10.21"));
    Product p6 = new Product("wiatrak", new BigDecimal("74.91"));

    Order o1 = new Order(new LinkedList<Product>(Arrays.asList(p1, p2, p3))); //sum 122.43
    Order o2 = new Order(p1); // sum 79.27
    Order o3 = new Order(new LinkedList<>(Arrays.asList(p2,p4,p5))); //149.19
    Order o4 = new Order(new LinkedList<>(Arrays.asList(p1, p6, p2))); //162.69
    Order o5 = new Order(new LinkedList<>(Arrays.asList(p3, p5, p6))); //118.77

    FindMethodsTest(){
        m1.setCommitted(true);

        o1.setShipment(s1);
        o2.setShipment(s2);
        o3.setShipment(s4);
        o4.setShipment(s3);
        o5.setShipment(s1);

        o1.setMoneyTransfer(m1);
        o2.setMoneyTransfer(m2);
        o3.setMoneyTransfer(m2);
        o4.setMoneyTransfer(m2);
        o5.setMoneyTransfer(m1);
    }

    @Test
    public void findByNameWorks() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);
        Findable.addToDatabase(o3);
        Findable.addToDatabase(o4);
        Findable.addToDatabase(o5);

        String testName = "Janusz";

        //when
        List<Order> filteredOrders = Finder.findByMethods(new LinkedList<>(Arrays.asList(new FindByName(testName))));

        //then
        assertEquals(3, filteredOrders.size());
        for(Order o: filteredOrders){
            assertEquals(o.getShipment().getRecipientAddress().getName().equals(testName) ||
                    o.getShipment().getSenderAddress().getName().equals(testName), true);
        }
    }

    @Test
    public void findByProductNameTest() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);
        Findable.addToDatabase(o3);
        Findable.addToDatabase(o4);
        Findable.addToDatabase(o5);

        String testProductName = "koszulka";

        //when
        List<Order> orders = Finder.findByMethods(new LinkedList<>(Arrays.asList(
                new FindByProductName(testProductName)
        )));

        //then
        assertEquals(2, orders.size());
        for(Order o: orders){
            Boolean flag = false;
            for(Product p: o.getProducts()){
                if(p.getName().equals(testProductName)) flag = true;
            }
            assertEquals(true, flag);
        }
    }

    @Test
    public void findByPrice() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);
        Findable.addToDatabase(o3);
        Findable.addToDatabase(o4);
        Findable.addToDatabase(o5);

        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("130.00");

        //when
        List<Order> orders = Finder.findByMethods(new LinkedList<>(Arrays.asList(
                new FindByPrice(minPrice, maxPrice)
        )));


        //then
        assertEquals(3, orders.size());
        for(Order o: orders){
            assertEquals(true, o.getPriceWithTaxes().compareTo(minPrice) > 0 && o.getPriceWithTaxes().compareTo(maxPrice) < 0);
        }
    }

    @Test
    public void findByPriceAndPersonName() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);
        Findable.addToDatabase(o3);
        Findable.addToDatabase(o4);
        Findable.addToDatabase(o5);

        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("130.00");

        String personName = "Janusz";

        //when
        List<Order> orders = Finder.findByMethods(new LinkedList<>(Arrays.asList(
                new FindByPrice(minPrice, maxPrice), new FindByName(personName)
        )));

        //then
        assertEquals(1, orders.size());
        for(Order o: orders){
            assertEquals(true, o.getPriceWithTaxes().compareTo(minPrice) > 0 && o.getPriceWithTaxes().compareTo(maxPrice) < 0);
        }
        for(Order o: orders){
            assertEquals(o.getShipment().getRecipientAddress().getName().equals(personName) ||
                    o.getShipment().getSenderAddress().getName().equals(personName), true);
        }
    }
}
