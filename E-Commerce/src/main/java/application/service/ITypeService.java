package application.service;

import application.entity.Type;

import java.util.List;

public interface ITypeService {

    List<Type> findAll();
    Type findById(Long id);
    Type findByName(String name);
}

