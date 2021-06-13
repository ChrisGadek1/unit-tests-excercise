package pl.edu.agh.internetshop;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import pl.edu.agh.internetshop.util.FileOperations;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static pl.edu.agh.internetshop.util.CustomAssertions.assertBigDecimalCompareValue;

public class OrderTest {

    //test data
    Address a1 = new Address("Janusz", "Olkuska 48b", "12-123", "Gdansk");
    Address a2 = new Address("Mariusz", "Polna 4", "22-542", "Warszawa");
    Address a3 = new Address("Andrzej", "Szkolna 4", "54-914", "Wroclaw");
    Address a4 = new Address("Marian","Piastowska 4c/3", "23-546","Bialystok");

    Shipment s1 = new Shipment(a1, a2);
    Shipment s2 = new Shipment(a3, a4);

    MoneyTransfer m1 = new MoneyTransfer(new BigInteger("1239876543"), "konto Janusza", "proste zakupy", 5);
    MoneyTransfer m2 = new MoneyTransfer(new BigInteger("134583458039485"), "Marian", "rower", 1);

    Product p1 = new Product("rower", new BigDecimal("642.45"));
    Product p2 = new Product("kocyk", new BigDecimal("24.33"));
    Product p3 = new Product("koszulka", new BigDecimal("33.65"));

    Order o1 = new Order(new LinkedList<Product>(Arrays.asList(p1, p2)));
    Order o2 = new Order(p1);

    OrderTest(){
        m1.setCommitted(true);
        o1.setShipment(s1);
        o2.setShipment(s2);
        o1.setMoneyTransfer(m1);
        o2.setMoneyTransfer(m2);
    }



    @Test
    public void productCanBeObtainedThroughOrder() {
        // given
        List<Product> expectedProducts = new LinkedList<Product>();
        expectedProducts.add(mock(Product.class));

        Order order = new Order(expectedProducts);

        // when
        List<Product> actualProducts = order.getProducts();

        // then
        assertEquals(1, expectedProducts.size());
        assertSame(expectedProducts, actualProducts);
    }

    @Test
    public void multipleProductsOrderPrice() throws Exception {
        //given
        List<Product> products = new LinkedList<>();
        products.add(mock(Product.class));
        products.add(mock(Product.class));
        given(products.get(0).getPrice()).willReturn(new BigDecimal("1.87"));
        given(products.get(1).getPrice()).willReturn(new BigDecimal("1000.54"));
        Order order = new Order(products);
        BigDecimal expectedPrice = new BigDecimal("1002.41");

        //when
        BigDecimal actualsProductsPrice = order.getPrice();

        //then
        assertBigDecimalCompareValue(actualsProductsPrice, expectedPrice);
    }

    @Test
    public void multipleProductsOrderPriceWithTaxes() throws Exception{
        //given
        List<Product> products = new LinkedList<>();
        products.add(mock(Product.class));
        products.add(mock(Product.class));
        given(products.get(0).getPrice()).willReturn(new BigDecimal("4.54"));
        given(products.get(1).getPrice()).willReturn(new BigDecimal("14.64"));
        Order order = new Order(products);
        BigDecimal expectedPrice = new BigDecimal("23.59");
        //when
        BigDecimal actualsProductsPrice = order.getPriceWithTaxes();
        //then
        assertBigDecimalCompareValue(actualsProductsPrice, expectedPrice);
    }

    @Test
    public void shipmentCanBeSet() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();
        Shipment expectedShipment = mock(Shipment.class);

        // when
        order.setShipment(expectedShipment);

        // then
        assertSame(expectedShipment, order.getShipment());
    }

    @Test
    public void shipmentIsNotSetByDefault() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();

        // when shipment is checked
        // then
        assertNull(order.getShipment());
    }

    @Test
    public void priceIsCalculatedForSingleProduct() throws Exception {
        // given
        BigDecimal expectedProductPrice = BigDecimal.valueOf(1000);
        Product product = mock(Product.class);
        given(product.getPrice()).willReturn(expectedProductPrice);
        Order order = new Order(product);

        // when
        BigDecimal actualProductPrice = order.getPrice();

        // then
        assertBigDecimalCompareValue(expectedProductPrice, actualProductPrice);
    }

    private Order getOrderWithCertainProductPrice(double productPriceValue) {
        BigDecimal productPrice = BigDecimal.valueOf(productPriceValue);
        Product product = mock(Product.class);
        given(product.getPrice()).willReturn(productPrice);
        return new Order(product);
    }

    @Test
    public void priceWithTaxesIsCalculatedWithoutRoundUp() {
        // given

        // when
        Order order = getOrderWithCertainProductPrice(2); // 2 PLN

        // then
        assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(2.46)); // 2.44 PLN
    }

    @Test
    public void priceWithTaxesIsCalculatedWithRoundDown() {
        // given

        // when
        Order order = getOrderWithCertainProductPrice(0.01); // 0.01 PLN

        // then
        assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.01)); // 0.01 PLN

    }

    @Test
    public void priceWithTaxesIsCalculatedWithRoundUp() {
        // given

        // when
        Order order = getOrderWithCertainProductPrice(0.03); // 0.03 PLN

        // then
        assertBigDecimalCompareValue(order.getPriceWithTaxes(), BigDecimal.valueOf(0.04)); // 0.04 PLN

    }

    @Test
    public void shipmentMethodCanBeSet() {
        // given
        Order order = getOrderWithMockedProduct();
        ShipmentMethod surface = mock(SurfaceMailBus.class);

        // when
        order.setShipmentMethod(surface);

        // then
        assertSame(surface, order.getShipmentMethod());
    }

    @Test
    public void orderCanBeSent() {
        // given
        Order order = getOrderWithMockedProduct();
        SurfaceMailBus surface = mock(SurfaceMailBus.class);
        Shipment shipment = mock(Shipment.class);
        given(shipment.isShipped()).willReturn(true);

        // when
        order.setShipmentMethod(surface);
        order.setShipment(shipment);
        order.send();

        // then
        assertTrue(order.isSent());
    }

    @Test
    public void orderIsNotSentByDefault() {
        // given
        Order order = getOrderWithMockedProduct();

        // when sent status is checked
        // then
        assertFalse(order.isSent());
    }

    @Test
    public void orderHasGeneratedId() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();

        // when id is checked
        // then
        assertNotNull(order.getId());
    }

    @Test
    public void paymentMethodCanBeSet() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();
        PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);

        // when
        order.setPaymentMethod(paymentMethod);

        // then
        assertSame(paymentMethod, order.getPaymentMethod());
    }

    @Test
    public void orderCanBePaid() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();
        PaymentMethod paymentMethod = mock(MoneyTransferPaymentTransaction.class);
        given(paymentMethod.commit(any(MoneyTransfer.class))).willReturn(true);
        MoneyTransfer moneyTransfer = mock(MoneyTransfer.class);
        given(moneyTransfer.isCommitted()).willReturn(true);

        // when
        order.setPaymentMethod(paymentMethod);
        order.pay(moneyTransfer);

        // then
        assertTrue(order.isPaid());
    }

    @Test
    public void orderIsNotPaidByDefault() throws Exception {
        // given
        Order order = getOrderWithMockedProduct();

        // when paid status is checked
        // then
        assertFalse(order.isPaid());
    }

    @Test
    public void addsUnpresentRelatedDataToDatabase() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        //when
        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);

        //then
        assertEquals(1, a1.isInDatabase());
        assertEquals(2, a2.isInDatabase());
        assertEquals(3, a3.isInDatabase());
        assertEquals(4, a4.isInDatabase());
        assertEquals(1, p1.isInDatabase());
        assertEquals(2, p2.isInDatabase());
        assertEquals(1, s1.isInDatabase());
        assertEquals(2, s2.isInDatabase());
        assertEquals(1, m1.isInDatabase());
        assertEquals(2, m2.isInDatabase());
        assertEquals(-1, p3.isInDatabase());
    }

    @Test
    public void manyToManyRelationWorks() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        String[][] mockedResult = {{"1","1","1"},{"2","1","2"},{"3","2","1"}};
        //when
        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);

        //then
        List<Integer> indexes = CSVProvider.getAllIndexes("orderProducts.csv");
        int i = 0;
        for(int index: indexes){
            assertArrayEquals(mockedResult[i++], CSVProvider.getDataById("orderProducts.csv", index));
        }
    }

    @Test
    public void orderObjectFromDatabaseTest() throws Exception{
        //given
        FileOperations.cleanAllFiles();

        Findable.addToDatabase(o1);
        Findable.addToDatabase(o2);

        //when
        Order gotO1 = (Order) Findable.getFromDatabaseByClassAndId(Order.class, 1);
        Order gotO2 = (Order) Findable.getFromDatabaseByClassAndId(Order.class, 2);

        //then
        assertEquals(gotO1.equals(o1), true);
        assertEquals(gotO2.equals(o2), true);
    }

    private Order getOrderWithMockedProduct() {
        Product product = mock(Product.class);
        return new Order(product);
    }
}
