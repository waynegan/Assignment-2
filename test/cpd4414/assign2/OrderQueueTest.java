/*
 * Copyright 2015 Len Payne <len.payne@lambtoncollege.ca>.
 * Updated 2015 Mark Russell <mark.russell@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cpd4414.assign2;

import cpd4414.assign2.Order;
import cpd4414.assign2.OrderQueue;
import cpd4414.assign2.OrderQueue.NoCustomerException;
import cpd4414.assign2.Purchase;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class OrderQueueTest {
    
    public OrderQueueTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testWhenCustomerExistsAndPurchasesExistThenTimeReceivedIsNow() throws NoCustomerException, OrderQueue.NoPurchasesException {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("CUST00001", "ABC Cafeteria");
        order.addPurchase(new Purchase("1", 8));
        order.addPurchase(new Purchase("2", 4));
        orderQueue.add(order);
        
        long expResult = new Date().getTime();
        long result = order.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    @Test
     public void testWhenNoCustomerExistsThenThrowAnException() throws  OrderQueue.NoPurchasesException  {
        boolean didThrow = false;
         OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("", "");
        order.addPurchase(new Purchase("1", 8));
        order.addPurchase(new Purchase("2", 4));
       try{ orderQueue.add(order);
       }catch(OrderQueue.NoCustomerException ex){
       didThrow=true;
       }
       
        assertTrue(didThrow);
    }
      @Test
     public void testWhenNoPurchasesThenThrowAnException() throws OrderQueue.NoCustomerException {
        boolean didThrow = false;
         OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("Something", "Order");
        
       try{ orderQueue.add(order);
       }catch(OrderQueue.NoPurchasesException ex){
       didThrow=true;
       }
       
        assertTrue(didThrow);
    } 
      
    @Test 
    public void testGetNextWhenOrdersInSystemThenGetNextAvaluable() throws NoCustomerException, OrderQueue.NoPurchasesException{
    OrderQueue orderQueue= new OrderQueue();
    Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("SomeID",8)) ;
    orderQueue.add(order);
    Order order2 = new Order ("SomeValues","OtherValues");
    order2.addPurchase(new Purchase("SomeID",4)) ;
    orderQueue.add(order2);
    
    Order result = orderQueue.next(); 
    assertEquals (result,order);
    assertNull(result.getTimeProcessed());
    
    
    
    }
        
    @Test 
    public void testGetNextWhenNoOrdersInSystemThenReturnNull() throws NoCustomerException, OrderQueue.NoPurchasesException{
    OrderQueue orderQueue= new OrderQueue();
    
    Order result = orderQueue.next(); 
   
    assertNull(result);
    
    
    
    }
    @Test
    public void testProcessWhenTimeReceivedIsSetThenSetTimeProcessedToNow() throws NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException{
    OrderQueue orderQueue= new OrderQueue();
      Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("1",8)) ;
    orderQueue.add(order);
    Order order2 = new Order ("SomeValues","OtherValues");
    order2.addPurchase(new Purchase("2",4)) ;
    orderQueue.add(order2);
    Order next = orderQueue.next();
    orderQueue.process(next);
      long expResult = new Date().getTime();
        long result = next.getTimeProcessed().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
    
     @Test
    public void testProcessWhenTimeReceivedNotSetThenThrowException() throws NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException{
        boolean didThrow = false;
        OrderQueue orderQueue= new OrderQueue();
      Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("1",8)) ;
        try{
    orderQueue.process(order);}
        catch(OrderQueue.NoTimeReceivedException ex){
        didThrow=true;
        }
  assertTrue(didThrow);
    }
    
     @Test
    public void testFulfillWhenTimeReceivedIsSetAndTimeProcessedIsSetAndItemsInStockThenSetTimeFulfilledToNow() throws NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException, OrderQueue.NoTimeProcessedException{
    OrderQueue orderQueue= new OrderQueue();
      Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("1",8)) ;
    orderQueue.add(order);
    Order order2 = new Order ("SomeValues","OtherValues");
    order2.addPurchase(new Purchase("2",4)) ;
    orderQueue.add(order2);
    Order next = orderQueue.next();
    orderQueue.process(next);
    orderQueue.fulfill(next);
      long expResult = new Date().getTime();
        long result = next.getTimeReceived().getTime();
        assertTrue(Math.abs(result - expResult) < 1000);
    }
     @Test
    public void testFulfillWhenTimeReceivedNotSetThenThrowException() throws NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException, OrderQueue.NoTimeProcessedException{
        boolean didThrow = false;
        OrderQueue orderQueue= new OrderQueue();
      Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("1",8)) ;
        try{
    orderQueue.fulfill(order);}
        catch(OrderQueue.NoTimeReceivedException ex){
        didThrow=true;
        }
  assertTrue(didThrow);
    }
    @Test
    public void testFulfillWhenTimeProcessedNotSetThenThrowException() throws NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException{
        boolean didThrow = false;
        OrderQueue orderQueue= new OrderQueue();
      Order order = new Order ("SomeValues","OtherValues");
    order.addPurchase(new Purchase("1",8)) ;
    orderQueue.add(order);
        try{
    orderQueue.fulfill(order);}
        catch(OrderQueue.NoTimeProcessedException ex){
        didThrow=true;
        }
  assertTrue(didThrow);
    }
}
