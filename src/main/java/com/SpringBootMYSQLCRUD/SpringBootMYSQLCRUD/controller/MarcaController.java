package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.controller;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.MarcaException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.MarcaModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping("/marcasview")
    public ModelAndView listado(Model modelo) throws UnsupportedEncodingException {
        List<MarcaModel> marcas = getAllMarcas();

        modelo.addAttribute("listaMarcas", marcas);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listadoMarcas.html");
        return modelAndView;
    }

    @Operation(summary = "Obtiene todas las marcas", description = "Obtiene una lista de todas las marcas", tags = {"marcas"})
    @ApiResponse(responseCode = "200", description = "Lista de marcas")
    @GetMapping("/marca")
    public List<MarcaModel> getAllMarcas(){
        return marcaService.getAllMarcas();
    }

    @Operation(summary = "Obtiene una marca", description = "Obtiene un género a partir de un id", tags = {"marcas"})
    @Parameter(name = "id", description = "Id de la marca", required = true,example = "1")
    @ApiResponse(responseCode = "200", description = "Marca encontrada")
    @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    @GetMapping("/marca/{id}")
    public ResponseEntity<MarcaModel> getMarcaById(@PathVariable Long id){
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(id);

        if(((Optional<?>) optionalMarcaModel).isPresent()){
            optionalMarcaModel = marcaService.getMarcaById(id);
            return new ResponseEntity<>(optionalMarcaModel.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}