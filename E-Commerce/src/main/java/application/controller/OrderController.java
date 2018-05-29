package application.controller;

import application.Constants;
import application.entity.Order;
import application.entity.OrderItem;
import application.entity.User;
import application.login.UserInfo;
import application.service.IOrderItemService;
import application.service.IOrderService;
import application.service.IUserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    private IOrderService orderService;
    private IOrderItemService orderItemService;
    private IUserService userService;

    @Autowired
    public OrderController(IOrderService orderService, IOrderItemService orderItemService, IUserService userService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
    }

    @RequestMapping(path = "/orders", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String orderList() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userInfo.getUser();

        List<Order> orders;

        if (user.getRole().equals(Constants.Roles.CUSTOMER)) {
            orders = orderService.findByUser(user);
        } else {
            orders = orderService.findAll();
        }

        JSONArray ordersJSON = new JSONArray();
        for (Order order : orders) {
            JSONObject orderJSON = order.toJSON();

            JSONArray orderItemsJSON = new JSONArray();
            List<OrderItem> orderItems = orderItemService.findByOrder(order);
            for (OrderItem orderItem: orderItems) {
                JSONObject orderItemJSON = orderItem.toJSON();
                orderItemsJSON.add(orderItemJSON);
            }
            orderJSON.put("items", orderItemsJSON);

            ordersJSON.add(orderJSON);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("orders", ordersJSON);

        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createOrder(@RequestBody String orderJSON) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(orderJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Order order = new Order();

        Long userId = (Long) jsonObject.get("user_id");

        User user = userService.findById(userId);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        order.setUser(user);

        String dateString = (String) jsonObject.get("date");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            order.setDate(date);
        }

        Double total = new Double((Long) jsonObject.get("total"));
        order.setTotal(total);

        order = orderService.create(order);

        return new ResponseEntity(order.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/orders/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        orderService.delete(order);
        return new ResponseEntity(HttpStatus.OK);
    }
}
