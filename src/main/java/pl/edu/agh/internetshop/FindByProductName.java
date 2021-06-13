package pl.edu.agh.internetshop;

import java.util.LinkedList;
import java.util.List;

public class FindByProductName implements FindMethod {
    private String productName;

    @Override
    public List<Integer> find(List<Integer> list) {
        List<Integer> result = new LinkedList<>();
        for(int index: list){
            Order o = (Order)Findable.getFromDatabaseByClassAndId(Order.class, index);
            boolean flag = false;
            for(Product p: o.getProducts()){
                if(p.getName().equals(productName)){
                    flag = true;
                }
            }
            if(flag){
                result.add(index);
            }
        }
        return result;
    }

    public FindByProductName(String productName) {
        this.productName = productName;
    }
}
