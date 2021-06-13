package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;


public class MoneyTransferTest {

	private static final BigInteger ACCOUNT_NUMBER = new BigInteger("27114020040000300201355387");
	private static final String OWNER_DETAILS = "example owner details";
	private static final String DESCRIPTION = "example description";
	private static final int AMOUNT = 5;
	
    @Test
    public void moneyTransferIsInitialized() {
    	// given

        // when
    	MoneyTransfer moneyTransfer = new MoneyTransfer(ACCOUNT_NUMBER, OWNER_DETAILS, DESCRIPTION, AMOUNT);
        
        // then
    	assertEquals(ACCOUNT_NUMBER, moneyTransfer.getAccountNumber());
    	assertEquals(OWNER_DETAILS, moneyTransfer.getOwnerDetails());
    	assertEquals(DESCRIPTION, moneyTransfer.getDescription());
    	assertEquals(AMOUNT, moneyTransfer.getAmount());
    }

    @Test
    public void commitFlagIsNotSetByDefault() throws Exception {
    	// given
    	MoneyTransfer moneyTransfer = new MoneyTransfer(ACCOUNT_NUMBER, OWNER_DETAILS, DESCRIPTION, AMOUNT);
    	
    	// when
		boolean committed = moneyTransfer.isCommitted();

		// then
		assertFalse(committed);
    }
    
    @Test
    public void commitFlagIsSetWithCommitting() throws Exception {
    	// given
    	MoneyTransfer moneyTransfer = new MoneyTransfer(ACCOUNT_NUMBER, OWNER_DETAILS, DESCRIPTION, AMOUNT);
    	
    	// when
    	moneyTransfer.setCommitted(true);
		boolean committed = moneyTransfer.isCommitted();

		// then
		assertTrue(committed);
    }

    @Test
	public void toCSVTest() throws Exception{
    	//given
		MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
		m1.setCommitted(true);
		MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);
		m2.setCommitted(true);
		MoneyTransfer m3 = new MoneyTransfer(new BigInteger("3475983479"), "Marian i żona Halina", "passat 1.9 tdi", 2);

		String[] mockedData1 = {"1","1239876543", "konto Janusza", "proste zakupy", "5", "true"};
		String[] mockedData2 = {"1", "134583458039485", "Marian", "rower", "1", "true"};
		String[] mockedData3 = {"1", "3475983479", "Marian i żona Halina", "passat 1.9 tdi", "2", "false"};

		File f = FileOperations.cleanFile("moneyTransfer.csv");

		//when
		String[] res1 = m1.toCSV();
		String[] res2 = m2.toCSV();
		String[] res3 = m3.toCSV();

		//then
		assertArrayEquals(mockedData1, res1);
		assertArrayEquals(mockedData2, res2);
		assertArrayEquals(mockedData3, res3);

		//clean
		f.delete();
	}

	@Test
	public void isInDataBaseTest() throws Exception{
		//given
		MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
		m1.setCommitted(true);
		MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);
		m2.setCommitted(true);
		MoneyTransfer m3 = new MoneyTransfer(new BigInteger("3475983479"), "Marian i żona Halina", "passat 1.9 tdi", 2);

		String[] mockedData1 = {"1","1239876543", "konto Janusza", "proste zakupy", "5", "true"};
		String[] mockedData2 = {"2", "134583458039485", "Marian", "rower", "1", "true"};

		File f = FileOperations.cleanFile("moneyTransfer.csv");

		CSVProvider.addNewLine("moneyTransfer.csv", mockedData1);
		CSVProvider.addNewLine("moneyTransfer.csv", mockedData2);

		//when
		int res1 = m1.isInDatabase();
		int res2 = m2.isInDatabase();
		int res3 = m3.isInDatabase();

		//then
		assertEquals(1, res1);
		assertEquals(2, res2);
		assertEquals(-1, res3);
	}

	@Test
	public void moneyTransferObjectFromDatabaseTest() throws Exception{
    	//given
		MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
		m1.setCommitted(true);
		MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);
		m2.setCommitted(true);
		MoneyTransfer m3 = new MoneyTransfer(new BigInteger("3475983479"), "Marian i żona Halina", "passat 1.9 tdi", 2);

		File f = FileOperations.cleanFile("moneyTransfer.csv");

		Findable.addToDatabase(m1);
		Findable.addToDatabase(m2);
		Findable.addToDatabase(m3);

		//when
		MoneyTransfer gotM1 = (MoneyTransfer) Findable.getFromDatabaseByClassAndId(MoneyTransfer.class, 1);
		MoneyTransfer gotM2 = (MoneyTransfer) Findable.getFromDatabaseByClassAndId(MoneyTransfer.class, 2);
		MoneyTransfer gotM3 = (MoneyTransfer) Findable.getFromDatabaseByClassAndId(MoneyTransfer.class, 3);

		//then
		assertEquals(m1.equals(gotM1), true);
		assertEquals(m2.equals(gotM2), true);
		assertEquals(m3.equals(gotM3), true);
	}
}