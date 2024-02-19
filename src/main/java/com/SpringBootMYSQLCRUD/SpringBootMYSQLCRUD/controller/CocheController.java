package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.controller;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.CocheModelBadRequestException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.CocheModelException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.CocheModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.MarcaModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.service.CocheService;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.service.MarcaService;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.util.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class CocheController {

    @Autowired
    private CocheService cocheService;

    @Autowired
     private MarcaService marcaService;

    @Operation(summary = "Obtiene todos los coches", description = "Obtiene una lista de todos los coches", tags = {"coches"})
    @ApiResponse(responseCode = "200", description = "Lista de coches")
    @GetMapping("/coche")
    public List<CocheModel> getAllCoches(){
        return cocheService.getAllCoches();
    }

    @Operation(summary = "Te permite añadir un coche",description = "Añade un nuevo coche a la BD", tags = {"coches"})
    @ApiResponse(responseCode = "201", description = "Coche añadido con exito")
    @ApiResponse(responseCode = "404", description = "Hubo un error")
    @PostMapping(value = "/coche", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CocheModel> createCoche(
            @RequestParam Long id_marca,
            @RequestParam String modelo,
            @RequestParam Integer caballos,
            @RequestPart(name = "imagen", required = false)MultipartFile imagen) throws CocheModelException, IOException {

        MarcaModel marcaModel = new MarcaModel();
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(id_marca);
        if(((Optional<?>)optionalMarcaModel).isPresent()){
            marcaModel = optionalMarcaModel.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        CocheModel createdCoche = cocheService.createCoche(new CocheModel(marcaModel, modelo, caballos), imagen);
        return new ResponseEntity<>(createdCoche, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene un coche", description = "Obtiene un coche a partir de su ID", tags = {"coches"})
    @Parameter(name = "id", description = "Id del coche", required = true,example = "1")
    @ApiResponse(responseCode = "200", description = "coche encontrado")
    @ApiResponse(responseCode = "404", description = "coche no encontrado")
    @GetMapping("/coche/{id}")
    public ResponseEntity<CocheModel> getCocheById(@PathVariable Long id){
        Optional<CocheModel> optionalCocheModel = cocheService.getCocheById(id);

        if(((Optional<?>)optionalCocheModel).isPresent()){
            optionalCocheModel = cocheService.getCocheById(id);
            return new ResponseEntity<>(optionalCocheModel.get(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Actualiza un coche", description = "Actualiza un coche a partir de su ID", tags = {"coches"})
    @Parameter(name = "id", description = "Id del coche", required = true,example = "1")
    @ApiResponse(responseCode = "200", description = "coche actualizado")
    @ApiResponse(responseCode = "404", description = "coche no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos de coche no validos")
    @PutMapping(value = "/coche/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CocheModel> updateCoche (
            @PathVariable Long id,
            @RequestParam Long id_marca,
            @RequestParam String modelo,
            @RequestParam Integer caballos,
            @RequestPart(name = "imagen", required = false)MultipartFile imagen) throws IOException{

        MarcaModel marcaModel = new MarcaModel();
        Optional<MarcaModel> optionalMarcaModel = marcaService.getMarcaById(id_marca);
        if(((Optional<?>)optionalMarcaModel).isPresent()){
            marcaModel = optionalMarcaModel.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<CocheModel> optionalCocheModel = cocheService.getCocheById(id);

        if(((Optional<?>)optionalCocheModel).isPresent()){
            CocheModel existingCoche = optionalCocheModel.get();
            existingCoche.setMarca(marcaModel);
            existingCoche.setModelo(modelo);
            existingCoche.setCaballos(caballos);
            existingCoche.setUpdated_at(LocalDateTime.now());
            existingCoche.setFoto(ImageUtils.compressImage(imagen.getBytes()));

            CocheModel updatedCoche = cocheService.updateCoche(existingCoche,imagen);
            return new ResponseEntity<>(updatedCoche, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Elimina un coche", description = "Elimina un coche a partir de su ID", tags = {"coches"})
    @Parameter(name = "id", description = "Id del coche", required = true,example = "1")
    @ApiResponse(responseCode = "200", description = "coche eliminado")
    @ApiResponse(responseCode = "404", description = "coche no encontrado")
    @DeleteMapping("/coche/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable Long id){
        Optional<CocheModel> optionalCocheModel = cocheService.getCocheById(id);

        if(optionalCocheModel.isPresent()){
            cocheService.deleteCocheById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Muestra foto", description = "Obtiene la foto de un coche dado un id", tags = {"coches"})
    @Parameter(name = "id", description = "ID del coche", required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "Foto del coche")
    @ApiResponse(responseCode = "404", description = "Coche no encontrado")
    @GetMapping(value = "/{id}/foto", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> descargarFoto(@PathVariable Long id){
        byte[] foto = cocheService.descargarFoto(id);
        if(foto != null){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(foto);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cochesview")
    public ModelAndView Listado(Model modelo) throws UnsupportedEncodingException{
        List<CocheModel> coches = getAllCoches();

        modelo.addAttribute("listaCoches", coches);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("listado.html");
        return modelAndView;
    }

    @GetMapping("/coche/mod")
    public ResponseEntity<List<CocheModel>> getCochesByModel(@RequestParam String modelo){
        List<CocheModel> coches = cocheService.getCocheByModelo(modelo);
        if(!coches.isEmpty()){
            return new ResponseEntity<>(coches, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/coche/orderbycaballos")
    public ResponseEntity<List<CocheModel>> orderByCaballos(){
        List<CocheModel> coches = getAllCoches();
        Collections.sort(coches, Comparator.comparing(CocheModel::getCaballos).reversed());

        return new ResponseEntity<>(coches,HttpStatus.OK);
    }
}
