package application.controller;

import application.entity.Product;
import application.entity.Type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.IProductService;
import application.service.ITypeService;

import java.util.*;

@RestController
public class ProductController {

    private IProductService productService;
    private ITypeService typeService;

    @Autowired
    public ProductController(IProductService productService, ITypeService typeService) {
        this.productService = productService;
        this.typeService = typeService;
    }

    @RequestMapping(path = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String productList(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "company", required = false) String company,
                           @RequestParam(value = "type", required = false) String type) {
        List<Product> products = new ArrayList<Product>();
        if (name != null) {
            Product product = productService.findByName(name);
            products.add(product);
        } else if (company != null) {
            products = productService.findAllByCompany(company);
        } else if (type != null) {
            Type type1 = typeService.findByName(type);
            if (type1 != null) {
                products = productService.findAllByType(type1);
            }
        } else {
            products = productService.findAll();
        }

        JSONArray productsJSON = new JSONArray();
        for (Product product : products) {
            JSONObject productJSON = product.toJSON();
            productsJSON.add(productJSON);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("products", productsJSON);

        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/products", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProduct(@RequestBody String productJSON) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(productJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();

        String name = (String) jsonObject.get("name");
        product.setName(name);

        String company = (String) jsonObject.get("company");
        product.setCompany(company);

        Long typeID = (Long) jsonObject.get("type");
        Type type = typeService.findById(typeID);
        product.setType(type);

        Integer quantity = (int) (long) jsonObject.get("quantity");
        product.setQuantity(quantity);

        Double price = new Double((Long) jsonObject.get("price"));
        product.setPrice(price);

        String image = (String) jsonObject.get("image");
        product.setImage(image);

        product = productService.create(product);

        return new ResponseEntity(product.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/product/sell/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sellProduct(@PathVariable("id") Long id, @RequestBody String jsonString) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Integer quantity = (int) (long) jsonObject.get("quantity");
        if (quantity != null) {
            Integer productQuantity = product.getQuantity();
            Integer newQuantity = productQuantity - quantity;
            product.setQuantity(newQuantity);
            productService.update(product);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(path = "/products/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProduct(@PathVariable("id") Long id, @RequestBody String productJSON) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(productJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String name = (String) jsonObject.get("name");
        if (name != null) {
            product.setName(name);
        }

        String company = (String) jsonObject.get("c ompany");
        if (company != null) {
            product.setCompany(company);
        }

        Long typeId = (Long) jsonObject.get("type");
        if (typeId != null) {
            Type type = typeService.findById(typeId);
            if (type != null) {
                product.setType(type);
            }
        }

        Integer quantity = (Integer) jsonObject.get("quantity");
        if (quantity != null) {
            product.setQuantity(quantity);
        }

        Double price = (Double) jsonObject.get("price");
        if (price != null) {
            product.setPrice(price);
        }

        String image = (String) jsonObject.get("image");
        if (image != null) {
            product.setImage(image);
        }

        product = productService.update(product);

        return new ResponseEntity(product.toJSON().toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        productService.delete(product);
        return new ResponseEntity(HttpStatus.OK);
    }
}
