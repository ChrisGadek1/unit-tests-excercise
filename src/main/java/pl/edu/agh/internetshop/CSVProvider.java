package pl.edu.agh.internetshop;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVProvider {
    private static String convertToCSV(String[] data) {
        return Stream.of(data).collect(Collectors.joining(",")).concat("\n");
    }

    public static void addNewLine(String path, String[] data){
        File csvOutputFile;
        BufferedWriter buffWriter;
        try{
            csvOutputFile = new File(path);
            buffWriter = new BufferedWriter(new FileWriter(csvOutputFile, true) );
            String s = convertToCSV(data);
            buffWriter.append(s);
            buffWriter.close();
        }
        catch (IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    private static String[] convertFromCSV(String s){
        return s.split(",");
    }

    public static String[] getDataById(String path, int id){
        File csvOutputFile;
        BufferedReader bufferedReader;
        try{
            csvOutputFile = new File(path);
            bufferedReader = new BufferedReader(new FileReader(csvOutputFile));
            String read;
            while((read = bufferedReader.readLine()) != null){
                if(Integer.parseInt(read.split(",")[0]) == id){
                    return convertFromCSV(read);
                }
            }
            bufferedReader.close();
            throw new Exception("nie istnieje element o podanym id");
        }
        catch (IOException e){
            System.out.println(e.getStackTrace());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] empty = {};
        return empty;
    }

    public static void deleteById(String path, int id){
        File csvOutputFile;
        BufferedReader bufferedReader;
        try{
            csvOutputFile = new File(path);
            bufferedReader = new BufferedReader(new FileReader(csvOutputFile));
            String res = "", read;
            while((read = bufferedReader.readLine()) != null){
                if(Integer.parseInt(read.split(",")[0]) != id){
                    res += read + "\n";
                }
            }
            bufferedReader.close();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csvOutputFile));
            bufferedWriter.write(res);
            bufferedWriter.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int getNextIndex(String path){
        File csvOutputFile;
        BufferedReader bufferedReader;
        try{
            csvOutputFile = new File(path);
            bufferedReader = new BufferedReader(new FileReader(csvOutputFile));
            String prev = "0,", next;
            while((next = bufferedReader.readLine()) != null){
                prev = next;
            }
            return Integer.parseInt(prev.split(",")[0]) + 1;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Integer> getAllIndexes(String path){
        File csvOutputFile;
        BufferedReader bufferedReader;
        List<Integer> res = new LinkedList<>();
        try{
            csvOutputFile = new File(path);
            bufferedReader = new BufferedReader(new FileReader(csvOutputFile));
            String read;
            while((read = bufferedReader.readLine()) != null){
                res.add(Integer.parseInt(read.split(",")[0]));
            }
            return res;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
