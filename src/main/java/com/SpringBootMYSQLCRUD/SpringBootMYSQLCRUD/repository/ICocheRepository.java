package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.repository;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.CocheModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICocheRepository extends JpaRepository<CocheModel, Long> {

    List<CocheModel> findByModeloContainingIgnoreCase(String modelo);

    List<CocheModel> OrderByCaballos();


}
