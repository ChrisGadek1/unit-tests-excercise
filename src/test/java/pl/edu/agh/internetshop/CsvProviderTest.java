package pl.edu.agh.internetshop;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvProviderTest {
    @Test
    public void convertToCSVTest() throws Exception{
        //given
        String[] case1 = {"this", "should", "be","done"};
        String[] case2 = {"to", "be", "or","not", "to", "be"};

        String mockedResult1 = "this,should,be,done\n";
        String mockedResult2 = "to,be,or,not,to,be\n";

        //when
        Method m1 = CSVProvider.class.getDeclaredMethod("convertToCSV", String[].class);
        m1.setAccessible(true);
        Method m2 = CSVProvider.class.getDeclaredMethod("convertToCSV", String[].class);
        m2.setAccessible(true);
        String result1 = (String) m1.invoke(null, (Object)case1);
        String result2 = (String) m2.invoke(null, (Object)case2);

        //then
        assertEquals(result1, mockedResult1);
        assertEquals(result2, mockedResult2);
    }

    @Test
    public void addCSVLineTest() throws Exception{
        //given
        File f = FileOperations.cleanFile("test.csv");
        File f1 = FileOperations.cleanFile("test1.csv");

        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(f1));
        buffWriter.append("1,this,should,look,like,this\n");
        buffWriter.append("2,without,any,problems,I,hope\n");
        buffWriter.close();

        String[] data1 = {"1","this","should","look","like","this"};
        String[] data2 = {"2","without","any","problems","I","hope"};

        //when
        CSVProvider.addNewLine("test.csv", data1);
        CSVProvider.addNewLine("test.csv", data2);

        //then
        assertEquals(FileUtils.readLines(f1), FileUtils.readLines(f));

        //clean
        f.delete();
        f1.delete();
    }

    @Test
    public void deleteElementByIdTest() throws Exception{
        //given
        String[] s1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] s2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] s3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] s4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        String[] mockedResult = {"2,Elzbieta,Krawczyk,Olkusz,False","4,Andrzej,Nowak,Braciejowka,True"};

        File f = FileOperations.cleanFile("test.csv");

        CSVProvider.addNewLine("test.csv", s1);
        CSVProvider.addNewLine("test.csv", s2);
        CSVProvider.addNewLine("test.csv", s3);
        CSVProvider.addNewLine("test.csv", s4);

        //when
        CSVProvider.deleteById("test.csv", 1);
        CSVProvider.deleteById("test.csv", 3);

        //then
        assertArrayEquals(mockedResult, FileUtils.readLines(f).toArray());

        //clean
        f.delete();
    }

    @Test
    public void getByIdTest() throws Exception{
        //given
        String[] s1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] s2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] s3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] s4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        File f = FileOperations.cleanFile("test.csv");

        CSVProvider.addNewLine("test.csv", s1);
        CSVProvider.addNewLine("test.csv", s2);
        CSVProvider.addNewLine("test.csv", s3);
        CSVProvider.addNewLine("test.csv", s4);

        //when
        String[] result1 = CSVProvider.getDataById("test.csv", 2);
        String[] result2 = CSVProvider.getDataById("test.csv", 3);

        //then
        assertArrayEquals(result1, s2);
        assertArrayEquals(result2, s3);

        //clean
        f.delete();
    }

    @Test
    public void fromCSVConvertTest() throws Exception{
        //given
        String s1 = "1,Janusz,Kowalski,Sieniczno,True";
        String s2 = "2,Elzbieta,Krawczyk,Olkusz,False";
        String s3 = "3,Mariusz,Wolski,Kosmolow,True";
        String s4 = "4,Andrzej,Nowak,Braciejowka,True";

        String[] mockRes1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] mockRes2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] mockRes3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] mockRes4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        //when
        Method m1 = CSVProvider.class.getDeclaredMethod("convertFromCSV", String.class);
        m1.setAccessible(true);
        String[] res1 = (String[])m1.invoke(null, (Object)s1);
        String[] res2 = (String[])m1.invoke(null, (Object)s2);
        String[] res3 = (String[])m1.invoke(null, (Object)s3);
        String[] res4 = (String[])m1.invoke(null, (Object)s4);

        //then
        assertArrayEquals(res1, mockRes1);
        assertArrayEquals(res2, mockRes2);
        assertArrayEquals(res3, mockRes3);
        assertArrayEquals(res4, mockRes4);
    }

    @Test
    public void getNextIndexTest() throws Exception{
        //given
        String[] s1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] s2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] s3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] s4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        File f = FileOperations.cleanFile("test.csv");

        int mockedId1 = 1, mockedId2 = 5;

        //when
        int nextId1 = CSVProvider.getNextIndex("test.csv");

        CSVProvider.addNewLine("test.csv", s1);
        CSVProvider.addNewLine("test.csv", s2);
        CSVProvider.addNewLine("test.csv", s3);
        CSVProvider.addNewLine("test.csv", s4);

        int nextId2 = CSVProvider.getNextIndex("test.csv");
        //then
        assertEquals(mockedId1, nextId1);
        assertEquals(mockedId2, nextId2);
        //clean
        f.delete();
    }

    @Test
    public void getAllIndexesTest() throws Exception{
        //given
        String[] s1 = {"1","Janusz","Kowalski","Sieniczno","True"};
        String[] s2 = {"2","Elzbieta","Krawczyk","Olkusz","False"};
        String[] s3 = {"3","Mariusz","Wolski","Kosmolow","True"};
        String[] s4 = {"4","Andrzej","Nowak","Braciejowka","True"};

        File f = FileOperations.cleanFile("test.csv");

        List<Integer> mockedList = new LinkedList<>(Arrays.asList(1,2,3,4));

        CSVProvider.addNewLine("test.csv", s1);
        CSVProvider.addNewLine("test.csv", s2);
        CSVProvider.addNewLine("test.csv", s3);
        CSVProvider.addNewLine("test.csv", s4);

        //when
        List<Integer> list = CSVProvider.getAllIndexes("test.csv");

        //then
        assertArrayEquals(mockedList.toArray(), list.toArray());

        //clean
        f.delete();
    }
}
