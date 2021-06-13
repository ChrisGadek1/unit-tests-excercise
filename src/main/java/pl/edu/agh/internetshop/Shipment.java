package pl.edu.agh.internetshop;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Shipment implements Findable {

    private boolean shipped;
    private Address senderAddress;
    private Address recipientAddress;

    public Shipment(Address senderAddress, Address recipientAddress) {
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        shipped = false;
    }

    public Shipment() {};

    public Address getSenderAddress() { return senderAddress; }

    public Address getRecipientAddress() { return recipientAddress; }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    @Override
    public String getCsvPath() {
        return "shipment.csv";
    }

    @Override
    public String[] toCSV() {
        int index, senderAddressIndex = senderAddress.isInDatabase(), recipientAddressIndex = recipientAddress.isInDatabase();
        if(senderAddressIndex == -1){
            senderAddressIndex = Findable.addToDatabase(senderAddress);
        }
        if(recipientAddressIndex == -1){
            recipientAddressIndex = Findable.addToDatabase(recipientAddress);
        }
        index = CSVProvider.getNextIndex(getCsvPath());
        String[] res = {Integer.toString(index), Integer.toString(senderAddressIndex), Integer.toString(recipientAddressIndex), Boolean.toString(shipped)};
        return res;
    }

    @Override
    public int isInDatabase() {
        int senderIndex, recipientIndex;
        if((senderIndex = senderAddress.isInDatabase()) == -1 || (recipientIndex = recipientAddress.isInDatabase()) == -1){
            return -1;
        }
        List<Integer> indexesList = Findable.getIDs(Shipment.class);
        Iterator shipmentIt = indexesList.iterator();
        while(shipmentIt.hasNext()){
            int currentIndex = (int)shipmentIt.next();
            String[] data = CSVProvider.getDataById(getCsvPath(), currentIndex);
            if(data[1].equals(Integer.toString(senderIndex)) && data[2].equals(Integer.toString(recipientIndex)) && data[3].equals(Boolean.toString(shipped))){
                return currentIndex;
            }
        }
        return -1;
    }

    @Override
    public Shipment getFromDatabaseById(int id) throws Exception {
        String[] data = CSVProvider.getDataById(getCsvPath(), id);
        if(data.length == 0){
            throw new Exception("brak podanego transportu w bazie");
        }
        String[] dataSender = CSVProvider.getDataById(Findable.getCsvPathByClass(Address.class), Integer.parseInt(data[1]));
        String[] dataRecipient = CSVProvider.getDataById(Findable.getCsvPathByClass(Address.class), Integer.parseInt(data[2]));

        Address a1 = new Address(dataSender[1], dataSender[2], dataSender[3], dataSender[4]);
        Address a2 = new Address(dataRecipient[1], dataRecipient[2], dataRecipient[3], dataRecipient[4]);
        Shipment s = new Shipment(a1,a2);
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return shipped == shipment.shipped &&
                Objects.equals(senderAddress, shipment.senderAddress) &&
                Objects.equals(recipientAddress, shipment.recipientAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipped, senderAddress, recipientAddress);
    }
}
