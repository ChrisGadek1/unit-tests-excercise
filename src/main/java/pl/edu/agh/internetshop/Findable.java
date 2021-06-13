package pl.edu.agh.internetshop;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Findable {
    String getCsvPath();
    static String getCsvPathByClass(Class<? extends Findable> f){
        try{
            return (String) f.getDeclaredMethod("getCsvPath").invoke(f.getDeclaredConstructor().newInstance());
        }
        catch (NoSuchMethodException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    static List<Integer> getIDs(Class<? extends Findable> f){
        return CSVProvider.getAllIndexes(getCsvPathByClass(f));
    }

    String[] toCSV();

    int isInDatabase();

    static int addToDatabase(Findable f){
        int res;
        String[] toCSV = f.toCSV();
        res = Integer.parseInt(toCSV[0]);
        CSVProvider.addNewLine(Findable.getCsvPathByClass(f.getClass()), toCSV);
        return res;
    }

    Findable getFromDatabaseById(int id) throws Exception;

    static Findable getFromDatabaseByClassAndId(Class<? extends Findable> f, int id) {
        try {
            return (Findable) f.getDeclaredMethod("getFromDatabaseById", int.class).invoke(f.getDeclaredConstructor().newInstance(), id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
