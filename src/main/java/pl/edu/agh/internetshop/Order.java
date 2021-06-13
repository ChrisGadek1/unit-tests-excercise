package pl.edu.agh.internetshop;

import java.math.BigDecimal;
import java.util.*;


public class Order implements Findable{
    private static final BigDecimal TAX_VALUE = BigDecimal.valueOf(1.23);
	private UUID id;
    private List<Product> products;
    private boolean paid;
    private Shipment shipment;
    private ShipmentMethod shipmentMethod;
    private PaymentMethod paymentMethod;
    private MoneyTransfer moneyTransfer;
    private String manyToManyPath = "orderProducts.csv";

    public MoneyTransfer getMoneyTransfer() {
        return moneyTransfer;
    }

    public void setMoneyTransfer(MoneyTransfer moneyTransfer) {
        this.moneyTransfer = moneyTransfer;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private Address address;

    public Order(List<Product> products) {
        this.products = products;
        id = UUID.randomUUID();
        paid = false;
    }

    public Order(Product product) {
        this(List.of(product));
    }

    public Order() {}

    public UUID getId() {
        return id;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSent() {
        return shipment != null && shipment.isShipped();
    }

    public boolean isPaid() { return paid; }

    public Shipment getShipment() {
        return shipment;
    }

    public BigDecimal getPrice() {
        BigDecimal sum = new BigDecimal("0");
        for(Product p: products){
            sum = sum.add(p.getPrice());
        }
        return sum;
    }

    public BigDecimal getPriceWithTaxes() {
        return getPrice().multiply(TAX_VALUE).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public ShipmentMethod getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(ShipmentMethod shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public void send() {
        boolean sentSuccessful = getShipmentMethod().send(shipment, shipment.getSenderAddress(), shipment.getRecipientAddress());
        shipment.setShipped(sentSuccessful);
    }

    public void pay(MoneyTransfer moneyTransfer) {
        moneyTransfer.setCommitted(getPaymentMethod().commit(moneyTransfer));
        paid = moneyTransfer.isCommitted();
        this.moneyTransfer = moneyTransfer;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public String getCsvPath() {
        return "orders.csv";
    }

    @Override
    public String[] toCSV() {
        Iterator productIt = products.iterator();
        List<Integer> productsIndexes = new LinkedList<>();
        int shipmentIndex, moneyTransferIndex;
        while(productIt.hasNext()){
            Product product = (Product) productIt.next();
            int productsIndex;
            if((productsIndex = product.isInDatabase()) == -1){
                productsIndex = Findable.addToDatabase(product);
            }
            productsIndexes.add(productsIndex);
        }
        if((shipmentIndex = shipment.isInDatabase()) == -1){
            shipmentIndex = Findable.addToDatabase(shipment);
        }
        if((moneyTransferIndex = moneyTransfer.isInDatabase()) == -1){
            moneyTransferIndex = Findable.addToDatabase(moneyTransfer);
        }
        int orderIndex = CSVProvider.getNextIndex(getCsvPath());
        Iterator productsIndexesIt = productsIndexes.iterator();
        while(productsIndexesIt.hasNext()){
            int currentProductIndex = (int)productsIndexesIt.next();
            String[] manyToManyData = {Integer.toString(CSVProvider.getNextIndex(manyToManyPath)), Integer.toString(orderIndex), Integer.toString(currentProductIndex)};
            CSVProvider.addNewLine(manyToManyPath, manyToManyData);
        }
        String[] res = {Integer.toString(orderIndex), Integer.toString(moneyTransferIndex), Integer.toString(shipmentIndex)};
        return res;
    }

    @Override
    public int isInDatabase() {

        //orders are never the same, so there could be more than one order
        //with same attributes
        return -1;
    }

    @Override
    public Order getFromDatabaseById(int id) throws Exception {
        String[] data = CSVProvider.getDataById(getCsvPath(), id);
        if(data.length == 0){
            throw new Exception("brak podanego zamowienia w bazie");
        }
        MoneyTransfer m = (MoneyTransfer) Findable.getFromDatabaseByClassAndId(MoneyTransfer.class, Integer.parseInt(data[1]));
        Shipment s = (Shipment) Findable.getFromDatabaseByClassAndId(Shipment.class, Integer.parseInt(data[2]));
        int orderId = Integer.parseInt(data[1]);
        List<Product> products = new LinkedList<>();
        List<Integer> manyToManyIndexes = CSVProvider.getAllIndexes("orderProducts.csv");
        for(int index: manyToManyIndexes){
            String[] dataManyToMany = CSVProvider.getDataById("orderProducts.csv", index);
            if(Integer.parseInt(dataManyToMany[1]) == orderId){
                products.add((Product)Findable.getFromDatabaseByClassAndId(Product.class, Integer.parseInt(dataManyToMany[2])));
            }
        }
        Order o = new Order(products);
        o.setMoneyTransfer(m);
        o.setShipment(s);
        return o;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(products, order.products) &&
                Objects.equals(shipment, order.shipment) &&
                Objects.equals(moneyTransfer, order.moneyTransfer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, shipment, moneyTransfer);
    }
}
