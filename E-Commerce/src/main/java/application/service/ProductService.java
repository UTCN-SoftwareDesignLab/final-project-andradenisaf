package application.service;

import application.entity.Product;
import application.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAll();
        return products;
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product;
        try {
            product = optionalProduct.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return product;
    }

    @Override
    public Product findByName(String name) {
        Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findByName(name));
        Product product;
        try {
            product = optionalProduct.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return product;
    }

    @Override
    public List<Product> findAllByCompany(String company) {
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAllByCompany(company);
        return products;
    }

    @Override
    public List<Product> findAllByType(Type type) {
        ArrayList<Product> products = (ArrayList<Product>) productRepository.findAllByType(type);
        return products;
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        if (!productRepository.existsById(product.getId())) {
            return null;
        }
        return productRepository.save(product);
    }

    @Override
    public boolean delete(Product product) {
        if (!productRepository.existsById(product.getId())) {
            return false;
        }
        productRepository.delete(product);
        return true;
    }
}
