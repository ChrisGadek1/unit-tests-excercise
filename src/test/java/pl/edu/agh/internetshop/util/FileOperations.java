package pl.edu.agh.internetshop.util;

import java.io.File;
import java.io.PrintWriter;

public class FileOperations{
    public static File cleanFile(String path) throws Exception {
        File f1 = new File(path);
        if(!f1.exists()){
            f1.createNewFile();
        }
        PrintWriter writer1 = new PrintWriter(f1);
        writer1.print("");
        writer1.close();
        return f1;
    }

    public static void cleanAllFiles() throws Exception{
        File f = FileOperations.cleanFile("address.csv");
        File f1 = FileOperations.cleanFile("orders.csv");
        File f2 = FileOperations.cleanFile("shipment.csv");
        File f3 = FileOperations.cleanFile("moneyTransfer.csv");
        File f4 = FileOperations.cleanFile("orderProducts.csv");
        File f5 = FileOperations.cleanFile("product.csv");
    }
}
