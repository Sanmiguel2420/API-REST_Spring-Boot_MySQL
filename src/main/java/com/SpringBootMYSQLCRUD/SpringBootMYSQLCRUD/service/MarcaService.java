package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.service;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.MarcaNotFoundException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.MarcaModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.repository.IMarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    @Autowired
    private IMarcaRepository marcaRepository;

    public List<MarcaModel> getAllMarcas(){
        return marcaRepository.findAll();
    }

    public Optional<MarcaModel> getMarcaById(Long id){
        return Optional.ofNullable(marcaRepository.findById(id).orElseThrow(
                () -> new MarcaNotFoundException("No se ha encontrado la marca con id: " + id)
        ));
    }
}
