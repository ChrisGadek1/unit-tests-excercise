package pl.edu.agh.internetshop;


import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ShipmentTest {
    
    @Test
    public void senderAddressCanBeObtainedThroughShipment() {
    	// given
    	Address senderAddress = mock(Address.class);
		Address recipientAddress = mock(Address.class);
		Shipment shipment = new Shipment(senderAddress, recipientAddress);
    	
    	// when
		Address actualSenderAddress = shipment.getSenderAddress();
		
    	// then
        assertSame(senderAddress, actualSenderAddress);
    }

    @Test
    public void recipientAddressCanBeObtainedThroughShipment() {
    	// given
    	Address senderAddress = mock(Address.class);
		Address recipientAddress = mock(Address.class);
		Shipment shipment = new Shipment(senderAddress, recipientAddress);
    	
    	// when
		Address actualRecipientAddress = shipment.getRecipientAddress();
		
    	// then
        assertSame(recipientAddress, actualRecipientAddress);
    }

    private Shipment getShipmentWithMockedAddresses()
	{
		Address senderAddress = mock(Address.class);
		Address recipientAddress = mock(Address.class);
		return new Shipment(senderAddress, recipientAddress);
	}
    
    @Test
    public void shipmentStatusCanBeSet() throws Exception {
    	// given
    	Shipment shipment = getShipmentWithMockedAddresses();
    	
    	// when
    	shipment.setShipped(true);
    	
    	// then
        assertTrue(shipment.isShipped());
    }
    
    @Test
    public void shipmentStatusIsNotSetByDefault() throws Exception {
    	// given
    	Shipment shipment = getShipmentWithMockedAddresses();
    	
    	// when
        boolean productShipped = shipment.isShipped();

        // then
        assertFalse(productShipped);
    }

    @Test
    public void addsUnpresentAddresesToDatabase() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");

        Shipment s1 = new Shipment(a1,a2);

        File f = FileOperations.cleanFile("shipment.csv");

        File f1 = FileOperations.cleanFile("address.csv");

        //when
        String[] data = s1.toCSV();

        //then
        assertEquals(1, a1.isInDatabase());
        assertEquals(2, a2.isInDatabase());
    }

    @Test
    public void toCSVTest() throws Exception{
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

        String[] mockedRes1 = {"1", "1", "2", "false"};
        String[] mockedRes2 = {"1", "3", "4", "true"};

        //when
        String[] data1 = s1.toCSV();
        String[] data2 = s2.toCSV();

        //then
        assertArrayEquals(mockedRes1, data1);
        assertArrayEquals(mockedRes2, data2);
    }

    @Test
    public void isInDatabaseTest() throws Exception{
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

        //then
        assertEquals(1, s1.isInDatabase());
        assertEquals(-1, s2.isInDatabase());
    }

    @Test
    public void shipmentObjectFromDatabaseTest() throws Exception{
        //given
        Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
        Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
        Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
        Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");

        File f = FileOperations.cleanFile("shipment.csv");
        File f1 = FileOperations.cleanFile("address.csv");

        Shipment s1 = new Shipment(a1, a2);
        Shipment s2 = new Shipment(a3, a4);

        Findable.addToDatabase(s1);
        Findable.addToDatabase(s2);

        //when
        Shipment gotS1 = (Shipment) Findable.getFromDatabaseByClassAndId(Shipment.class, 1);
        Shipment gotS2 = (Shipment) Findable.getFromDatabaseByClassAndId(Shipment.class, 2);

        //then
        assertEquals(gotS1.equals(s1), true);
        assertEquals(gotS2.equals(s2), true);
    }
}
