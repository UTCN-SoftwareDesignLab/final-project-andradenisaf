import application.entity.Order;
import application.entity.User;
import application.repository.OrderRepository;
import application.service.OrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class OrderServiceTest {
    private OrderService orderService;

    private User user1 = new User();

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void before() {
        orderService = new OrderService(orderRepository);

        User user1 = new User();
        user1.setId(new Long(1));
        user1.setUsername("diana");
        user1.setPassword("dianapass");
        user1.setFullname("Diana Balaian");
        user1.setEmail("diana_balaian@yahoo.com");
        user1.setRole("customer");

        User user2 = new User();
        user2.setId(new Long(2));
        user2.setUsername("carina");
        user2.setPassword("carinapass");
        user2.setFullname("Carina Sav");
        user2.setEmail("carina_sav@yahoo.com");
        user2.setRole("employee");

        User user3 = new User();
        user3.setId(new Long(3));
        user3.setUsername("ligia");
        user3.setPassword("ligiapass");
        user3.setFullname("Ligia Bucovala");
        user3.setEmail("ligia_bucovala@yahoo.com");
        user3.setRole("customer");


        Order order1 = new Order();
        order1.setId(new Long(1));
        order1.setDate(new Date(2018, 5, 21));
        order1.setTotal(77.99);
        order1.setUser(new User());

        Order order2 = new Order();
        order2.setId(new Long(1));
        order2.setDate(new Date(2018, 5, 27));
        order2.setTotal(117.99);
        order2.setUser(new User());

        ArrayList<Order> orders = new ArrayList<Order>();
        orders.add(order1);
        orders.add(order2);

        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Optional<Order> optional = Optional.of(order1);
        Mockito.when(orderRepository.findById(new Long(1))).thenReturn(optional);
        Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(orders);
    }

    @Test
    public void testFindAll() {
        Iterable<Order> orders = orderService.findAll();
        ArrayList<Order> orderArrayList = (ArrayList<Order>) orderService.findAll();
        orders.forEach(orderArrayList::add);
        Assert.assertEquals(2, orderArrayList.size());
    }

    @Test
    public void testFindById() {
        Order order = orderService.findById(new Long(1));
        Assert.assertEquals(new Long(1), order.getId());
    }

    @Test
    public void testFindByUser() {
        Iterable<Order> orders = orderService.findByUser(user1);
        ArrayList<Order> orderArrayList = (ArrayList<Order>) orderService.findByUser(user1);
        orders.forEach(orderArrayList::add);
        Assert.assertEquals(2, orderArrayList.size());
    }
}
