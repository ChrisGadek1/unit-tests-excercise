package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Product implements Findable {
	
	public static final int PRICE_PRECISION = 2;
	public static final RoundingMode ROUND_STRATEGY = RoundingMode.HALF_UP;
	
    private String name;
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price.setScale(PRICE_PRECISION, ROUND_STRATEGY);
    }

    public Product () {}

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getCsvPath() {
        return "product.csv";
    }

    @Override
    public String[] toCSV() {
        int index = CSVProvider.getNextIndex(getCsvPath());
        String[] res = {Integer.toString(index), name, price.toString()};
        return res;
    }

    @Override
    public int isInDatabase() {
        List<Integer> listOfIndexes = Findable.getIDs(Product.class);
        Iterator indexIt = listOfIndexes.iterator();
        while(indexIt.hasNext()){
            int currentIndex = (int)indexIt.next();
            String[] data = CSVProvider.getDataById(getCsvPath(), currentIndex);
            if(data[1].equals(name) && data[2].equals(price.toString())){
                return currentIndex;
            }
        }
        return -1;
    }

    @Override
    public Product getFromDatabaseById(int id) throws Exception {
        String[] data = CSVProvider.getDataById(getCsvPath(), id);
        if(data.length == 0){
            throw new Exception("brak podanego produktu w bazie");
        }
        Product p = new Product(data[1], new BigDecimal(data[2]));
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
