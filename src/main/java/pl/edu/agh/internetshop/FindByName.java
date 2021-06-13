package pl.edu.agh.internetshop;

import java.util.LinkedList;
import java.util.List;

public class FindByName implements FindMethod {
    private String name;
    @Override
    public List<Integer> find(List<Integer> list) {
        List<Integer> result = new LinkedList<>();
        for(int index: list){
            Order o = (Order)Findable.getFromDatabaseByClassAndId(Order.class, index);
            if(o.getShipment().getSenderAddress().getName().equals(name) ||
                o.getShipment().getRecipientAddress().getName().equals(name)){
                result.add(index);
            }
        }
        return result;
    }

    public FindByName(String name) {
        this.name = name;
    }
}
