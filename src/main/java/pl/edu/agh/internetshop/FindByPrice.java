package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class FindByPrice implements FindMethod {
    private BigDecimal min;
    private BigDecimal max;

    @Override
    public List<Integer> find(List<Integer> list) {
        List<Integer> result = new LinkedList<>();
        for(int index: list){
            Order o = (Order)Findable.getFromDatabaseByClassAndId(Order.class, index);
            if(o.getPriceWithTaxes().compareTo(min) > 0 && o.getPriceWithTaxes().compareTo(max) < 0){
                result.add(index);
            }
        }
        return result;
    }

    public FindByPrice(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }
}
