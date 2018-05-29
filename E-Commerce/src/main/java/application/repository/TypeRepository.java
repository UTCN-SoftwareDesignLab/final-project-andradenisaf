package application.repository;

import application.entity.Type;
import org.springframework.data.repository.CrudRepository;

public interface TypeRepository extends CrudRepository<Type, Long> {

    Type findByType(String type);
}
