package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coche")
public class CocheModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    @JsonBackReference
    private MarcaModel marca;

    @Column(name = "modelo",nullable = false)
    private String modelo;

    @Column(name = "caballos", nullable = false)
    private Integer caballos;

    @Column(name = "imagen", nullable = false)
    private String imagen;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto",columnDefinition = "longblob", nullable = true)
    private byte[] foto;

    @Column(name = "created_at")
    private LocalDateTime creates_at=LocalDateTime.now();
    @Column(name="updated_at")
    private LocalDateTime updated_at=LocalDateTime.now();

    public CocheModel(){

    }
    public CocheModel(MarcaModel marca, String modelo, Integer caballos){
        this.marca = marca;
        this.modelo = modelo;
        this.caballos = caballos;
    }

    @Override
    public String toString(){
        return "Coche{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ",caballos=" + caballos + '}';
    }
}
