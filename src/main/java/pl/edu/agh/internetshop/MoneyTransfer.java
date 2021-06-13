package pl.edu.agh.internetshop;


import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MoneyTransfer implements Findable {
    private BigInteger accountNumber;
    private String ownerDetails;
    private String description;
    private int amount;
    private boolean committed;

    public MoneyTransfer(BigInteger accountNumber, String ownerDetails, String description, int amount) {
        this.accountNumber = accountNumber;
        this.ownerDetails = ownerDetails;
        this.description = description;
        this.amount = amount;
        committed = false;
    }

    public MoneyTransfer() {}

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerDetails() {
        return ownerDetails;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }

    @Override
    public String getCsvPath() {
        return "moneyTransfer.csv";
    }

    @Override
    public String[] toCSV() {
        int index = CSVProvider.getNextIndex(getCsvPath());
        String[] res = {Integer.toString(index), accountNumber.toString(), ownerDetails, description, Integer.toString(amount), Boolean.toString(committed)};
        return res;
    }

    @Override
    public int isInDatabase() {
        List<Integer> listOfIndexes = Findable.getIDs(MoneyTransfer.class);
        Iterator indexIt = listOfIndexes.iterator();
        while(indexIt.hasNext()){
            int currentIndex = (int)indexIt.next();
            String[] data = CSVProvider.getDataById(getCsvPath(), currentIndex);
            if(data[1].equals(accountNumber.toString()) && data[2].equals(ownerDetails) && data[3].equals(description) && data[4].equals(Integer.toString(amount)) && data[5].equals(Boolean.toString(committed))){
                return currentIndex;
            }
        }
        return -1;
    }

    @Override
    public MoneyTransfer getFromDatabaseById(int id) throws Exception {
        String[] data = CSVProvider.getDataById(getCsvPath(), id);
        if(data.length == 0){
            throw new Exception("brak podanej tranzakcji bazie");
        }
        MoneyTransfer m = new MoneyTransfer(new BigInteger(data[1]), data[2], data[3], Integer.parseInt(data[4]));
        if(data[5].equals("true")){
            m.setCommitted(true);
        }
        return m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTransfer that = (MoneyTransfer) o;
        return amount == that.amount &&
                committed == that.committed &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(ownerDetails, that.ownerDetails) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, ownerDetails, description, amount, committed);
    }
}
