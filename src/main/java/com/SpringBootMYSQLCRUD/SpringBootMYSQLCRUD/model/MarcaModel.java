package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "marca")
public class MarcaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca", nullable = false, length = 50)
    private String marca;

    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CocheModel> coches;

    public MarcaModel(){

    }
    public MarcaModel(Long id, String marca){
        this.id = id;
        this.marca = marca;
    }

}
