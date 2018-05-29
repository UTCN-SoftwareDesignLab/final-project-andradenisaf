package application.controller;

import application.Constants;
import application.entity.*;
import application.login.UserInfo;
import application.service.*;
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

import java.util.Date;
import java.util.List;

@RestController
public class CartController {

    private ICartService cartService;
    private IUserService userService;
    private ICartItemService cartItemService;
    private IProductService productService;
    private IOrderService orderService;
    private IOrderItemService orderItemService;

    @Autowired
    public CartController(ICartService cartService,
                          IUserService userService,
                          ICartItemService cartItemService,
                          IProductService productService,
                          IOrderService orderService,
                          IOrderItemService orderItemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @RequestMapping(path = "/cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity orderList() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userInfo.getUser();

        Cart cart;

        if (user.getRole().equals(Constants.Roles.CUSTOMER)) {
            cart = cartService.findByUser(user);
            if (cart == null) {
                new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<CartItem> cartItems = cartItemService.findByCart(cart);

        JSONArray cartJSON = new JSONArray();
        for (CartItem cartItem : cartItems) {
            JSONObject cartItemJSON = cartItem.toJSON();
            cartJSON.add(cartItemJSON);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("cart", cartJSON);

        return new ResponseEntity(responseJSON.toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/cart/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addItemToCart(@RequestBody String jsonString) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userInfo.getUser();

        Cart cart;

        if (user.getRole().equals(Constants.Roles.CUSTOMER)) {
            cart = cartService.findByUser(user);
            if (cart == null) {
                new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Long productId = (Long) jsonObject.get("product_id");
        Long quantity = (Long) jsonObject.get("quantity");

        Product product = productService.findById(productId);
        if (product == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cartItem = cartItemService.create(cartItem);

        return new ResponseEntity(cartItem.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/cart/item/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteItem(@PathVariable("id") Long cartItemId) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        if (cartItem == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        cartItemService.delete(cartItem);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "cart/checkout", method = RequestMethod.POST)
    public ResponseEntity checkout() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userInfo.getUser();

        Cart cart;

        if (user.getRole().equals(Constants.Roles.CUSTOMER)) {
            cart = cartService.findByUser(user);
            if (cart == null) {
                new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<CartItem> cartItems = cartItemService.findByCart(cart);

        Order order = new Order();

        order.setUser(user);
        order.setDate(new Date());

        orderService.create(order);

        Double price = 0.0;
        for (CartItem cartItem: cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItemService.create(orderItem);
            price += orderItem.getProduct().getPrice() * orderItem.getQuantity();
            cartItemService.delete(cartItem);
        }

        order.setTotal(price);

        orderService.update(order);

        return new ResponseEntity(HttpStatus.OK);
    }
}
