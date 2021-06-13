package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Find;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;


public class ProductTest {

	
    private static final String NAME = "Mr. Sparkle";
    private static final BigDecimal PRICE = BigDecimal.valueOf(1);

	@Test
    public void productNameCanBeSet() throws Exception{
        //given
    	
        // when
        Product product = new Product(NAME, PRICE);
        
        // then
        assertEquals(NAME, product.getName());
    }
    
    @Test
    public void productPriceCanBeSet() throws Exception{
        //given
    	
        // when
        Product product = new Product(NAME, PRICE);
        
        // then
        assertBigDecimalCompareValue(product.getPrice(), PRICE);
    }

    @Test
    public void toCSVTest() throws Exception{
	    //given
        Product p1 = new Product("rower", new BigDecimal("642.45"));
        Product p2 = new Product("kocyk", new BigDecimal("24.33"));

        File f = FileOperations.cleanFile("product.csv");

        String[] mockedRes1 = {"1", "rower", "642.45"};
        String[] mockedRes2 = {"1", "kocyk", "24.33"};

        //when
        String[] res1 = p1.toCSV();
        String[] res2 = p2.toCSV();

        //then
        assertArrayEquals(mockedRes1, res1);
        assertArrayEquals(mockedRes2, res2);

        //clean
        f.delete();
    }

    @Test
    public void isInDatabaseTest() throws Exception{
        //given
        Product p1 = new Product("rower", new BigDecimal("642.45"));
        Product p2 = new Product("kocyk", new BigDecimal("24.33"));
        Product p3 = new Product("koszulka", new BigDecimal("33.65"));

        File f = FileOperations.cleanFile("product.csv");

        String[] mockedRes1 = {"1", "rower", "642.45"};
        String[] mockedRes2 = {"2", "kocyk", "24.33"};

        CSVProvider.addNewLine("product.csv", mockedRes1);
        CSVProvider.addNewLine("product.csv", mockedRes2);

        //when
        int res1 = p1.isInDatabase();
        int res2 = p2.isInDatabase();
        int res3 = p3.isInDatabase();

        //then
        assertEquals(1, res1);
        assertEquals(2, res2);
        assertEquals(-1, res3);

        //clean
        f.delete();
    }

    @Test
    public void productObjectFromDatabaseTest() throws Exception{
        //given
        Product p1 = new Product("rower", new BigDecimal("642.45"));
        Product p2 = new Product("kocyk", new BigDecimal("24.33"));
        Product p3 = new Product("koszulka", new BigDecimal("33.65"));

        File f = FileOperations.cleanFile("product.csv");

        Findable.addToDatabase(p1);
        Findable.addToDatabase(p2);
        Findable.addToDatabase(p3);

        //when
        Product gotP1 = (Product) Findable.getFromDatabaseByClassAndId(Product.class, 1);
        Product gotP2 = (Product) Findable.getFromDatabaseByClassAndId(Product.class, 2);
        Product gotP3 = (Product) Findable.getFromDatabaseByClassAndId(Product.class, 3);

        //then
        assertEquals(p1.equals(gotP1), true);
        assertEquals(p2.equals(gotP2), true);
        assertEquals(p3.equals(gotP3), true);
    }
}