package com.bionic.edu.service;

import com.bionic.edu.dao.OrderDaoImpl;
import com.bionic.edu.entity.OrderDishes;
import com.bionic.edu.entity.Orders;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OrderDishesServiceImplTest {
    OrderDishesService orderDishesService;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
        orderDishesService = context.getBean(OrderDishesService.class);
    }

    @Test
    public void testFindById() throws Exception {
        OrderDishes orderDishes = orderDishesService.findById(1);
        assertNotNull(orderDishes);
        assertEquals(1, orderDishes.getId());
    }

    @Test
    public void testFindAll() throws Exception {
        List<OrderDishes> ordersDishes = orderDishesService.findAll();
        assertNotNull(ordersDishes);
        assertEquals(5, ordersDishes.size());
    }

    @Test
    public void testAdd() throws Exception {
        OrderDishes orderDishes = orderDishesService.findById(1);
        orderDishesService.save(orderDishes);
        int id = orderDishes.getId();
        assertNotNull(orderDishesService.findById(id));
    }

    @Test
    public void testUpdate() throws Exception {
        OrderDishes orderDishes = orderDishesService.findById(1);
        orderDishes.setPrice(10.00);
        orderDishesService.save(orderDishes);
        assertEquals(10.00, orderDishes.getPrice(), 0.00);
    }

    @Test
    public void testDelete() throws Exception {
        OrderDishes orderDishes = orderDishesService.findById(2);
        orderDishesService.save(orderDishes);
        int id = orderDishes.getId();
        orderDishesService.delete(id);
        assertNull(orderDishesService.findById(id));
    }

    @Test
    public void testGetAllFromOrder() throws Exception {
        Orders order = new OrderDaoImpl().findById(1);
        List<OrderDishes> orderDishesList = orderDishesService.getAllFromOrder(order);
        assertNotNull(orderDishesList);
        assertEquals(2, orderDishesList.size());
    }

    @Test
    public void testAddKitchenmadeToOrder() throws Exception {
        // todo
    }

    @Test
    public void testCreateListForKitchen() throws Exception {
        // todo
    }
}