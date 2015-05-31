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

import java.text.ParseException;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
public class InventoryTest {
    
    public InventoryTest() {
    }

    @Test
    public void testGetQuantityForIdReturnsErrorForNegativeID() {
        int id = -100;
        int expResult = -1;
        int result = Inventory.getQuantityForId(id);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testGetQuantityForIdReturnsValidResultForPositiveID() {
        int id = 4;
        int result = Inventory.getQuantityForId(id);
        assertTrue(result >= 0);        
    }
     @Test
    public void testReportWhenNoOrdersThenReturnEmptyString() {
        OrderQueue orderQueue = new OrderQueue();
        String expResult = "";
        String result = orderQueue.report();
        assertEquals(expResult,result);        
    }
     @Test
    public void testReportWhenItemsInQueueThenReturnCorrectReport() throws OrderQueue.NoCustomerException, OrderQueue.NoPurchasesException, OrderQueue.NoTimeReceivedException, OrderQueue.NoTimeProcessedException, ParseException, org.json.simple.parser.ParseException {
        OrderQueue orderQueue = new OrderQueue();
        Order order = new Order("Cust1", "Name1");
        order.addPurchase(new Purchase("1", 8));
        orderQueue.add(order);
        Order order2 = new Order("Cust2", "Name2");
        order2.addPurchase(new Purchase("2", 4));
        orderQueue.add(order2);

        Order next = orderQueue.next();
        orderQueue.process(next);

        orderQueue.fulfill(next);

        JSONObject expResult = new JSONObject();
        JSONArray orders = new JSONArray();
        JSONObject o1 = new JSONObject();
        o1.put("customerId", "Cust1");
        o1.put("customerName", "Name1");
        o1.put("timeReceived", new Date().toString());
        o1.put("timeProcessed", new Date().toString());
        o1.put("timeFulfilled", new Date().toString());
        JSONArray pList = new JSONArray();
        JSONObject p1 = new JSONObject();
        p1.put("productId", 1);
        p1.put("quantity", 8);
        pList.add(p1);
        o1.put("purchases", pList);
        o1.put("notes", null);
        orders.add(o1);
        JSONObject o2 = new JSONObject();
        o2.put("customerId", "Cust2");
        o2.put("customerName", "Name2");
        o2.put("timeReceived", new Date().toString());
        o2.put("timeProcessed", null);
        o2.put("timeFulfilled", null);
        JSONArray pList2 = new JSONArray();
        JSONObject p2 = new JSONObject();
        p2.put("productId", 2);
        p2.put("quantity", 4);
        pList2.add(p2);
        o2.put("purchases", pList2);
        o2.put("notes", null);
        orders.add(o2);
        expResult.put("orders", orders);

        String resultString = orderQueue.report();
        JSONObject result = (JSONObject) JSONValue.parseWithException(resultString);
        assertEquals(expResult.toJSONString(), result.toJSONString());
    }
}
