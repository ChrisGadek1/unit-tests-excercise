package pl.edu.agh.internetshop;

import java.util.LinkedList;
import java.util.List;

public class Finder {

    public static List<Order> findByMethods(List<FindMethod> findList){
        List<Integer> allOrdersIndexes = CSVProvider.getAllIndexes(Findable.getCsvPathByClass(Order.class));
        List<Integer> filteredIndexes = allOrdersIndexes;
        for(FindMethod fm: findList){
            filteredIndexes = fm.find(filteredIndexes);
        }
        List<Order> resultList = new LinkedList<>();
        for(int index: filteredIndexes){
            resultList.add((Order)Findable.getFromDatabaseByClassAndId(Order.class, index));
        }
        return resultList;
    }

}
