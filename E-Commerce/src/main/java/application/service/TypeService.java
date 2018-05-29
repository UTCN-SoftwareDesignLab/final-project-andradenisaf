package application.service;

import application.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.TypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TypeService implements ITypeService {

    private TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public List<Type> findAll() {
        Iterable<Type> iterable = typeRepository.findAll();
        ArrayList<Type> types = new ArrayList<>();
        iterable.forEach(types::add);
        return types;
    }

    @Override
    public Type findById(Long id) {
        Optional<Type> optionalType = typeRepository.findById(id);
        Type type;
        try {
            type = optionalType.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return type;
    }

    @Override
    public Type findByName(String name) {
        Optional<Type> optionalType = Optional.ofNullable(typeRepository.findByType(name));
        Type type;
        try {
            type = optionalType.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return type;
    }
}
