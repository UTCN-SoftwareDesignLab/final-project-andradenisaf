package application.repository;

import application.entity.Product;
import application.entity.Type;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findByName(String name);
    List<Product> findAllByCompany(String company);
    List<Product> findAllByType(Type type);

}
