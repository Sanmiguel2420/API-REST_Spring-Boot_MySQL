package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.repository;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.MarcaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaRepository extends JpaRepository<MarcaModel, Long> {
}
