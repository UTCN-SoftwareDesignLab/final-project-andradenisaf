package application.service;

import application.entity.Product;
import application.entity.Type;

import java.util.List;

public interface IProductService {

    List<Product> findAll();
    Product findById(Long id);
    Product findByName(String name);
    List<Product> findAllByType(Type type);
    List<Product> findAllByCompany(String company);
    Product create(Product product);
    Product update(Product product);
    boolean delete(Product product);
}
