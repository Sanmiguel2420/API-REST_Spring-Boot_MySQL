package com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.service;

import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.CocheModelBadRequestException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.CocheModelException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.exceptions.CocheModelNotFoundException;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.CocheModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.model.MarcaModel;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.repository.ICocheRepository;
import com.SpringBootMYSQLCRUD.SpringBootMYSQLCRUD.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CocheService {

    @Autowired
    private ICocheRepository cocheRepository;

    public List<CocheModel> getAllCoches(){
        return cocheRepository.findAll();
    }

    public CocheModel createCoche(CocheModel cocheModel, MultipartFile file) throws IOException {
        if(cocheModel.getModelo() == null || cocheModel.getModelo().isEmpty()){
            throw new CocheModelBadRequestException("Debe introducir un modelo");
        }
        if(cocheModel.getCaballos() == null || cocheModel.getCaballos() <= 0){
            throw new CocheModelBadRequestException("Debe introducir numero de caballos y debe ser mayor que 0");
        }
        Long idMarca = cocheModel.getMarca().getId();
        if(idMarca == null || idMarca <= 0 || idMarca > 3){
            throw new CocheModelBadRequestException("Debe introducirse una marca entre 1 y 3");
        }

        String marca;
        if(idMarca == 1){
            marca = "Toyota";
        } else if(idMarca == 2){
            marca = "Nissan";
        } else {
            marca = "Mercedes";
        }
        CocheModel cocheModelsave = new CocheModel(new MarcaModel(idMarca,marca), cocheModel.getModelo(), cocheModel.getCaballos());

        if (!file.isEmpty()) {
            cocheModelsave.setImagen(file.getOriginalFilename());
            cocheModelsave.setFoto(ImageUtils.compressImage(file.getBytes()));

            Path dirImg = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dirImg.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = file.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(rutaCompleta,bytesImg);
            } catch (IOException e){
                throw new CocheModelException("Error de escritura");
            }



        } else{
            throw  new CocheModelBadRequestException("Debe introducirse el fichero imagen");
        }
        return cocheRepository.save(cocheModelsave);
    }

    public Optional<CocheModel> getCocheById(Long id) {
        return Optional.ofNullable(cocheRepository.findById(id).orElseThrow(
                ()->new CocheModelNotFoundException("No se ha encontrado el coche con id: " + id)
        ));
    }
    public CocheModel updateCoche(CocheModel cocheModel,MultipartFile file) throws IOException{
        if(cocheModel.getModelo() == null || cocheModel.getModelo().isEmpty()){
            throw new CocheModelBadRequestException("Debe introducir un modelo");
        }
        if(cocheModel.getCaballos() == null || cocheModel.getCaballos() <= 0){
            throw new CocheModelBadRequestException("Debe introducir numero de caballos y debe ser mayor que 0");
        }

        Long idMarca = cocheModel.getMarca().getId();

        if(idMarca == null || idMarca <= 0 || idMarca > 3){
            throw new CocheModelBadRequestException("Debe introducirse una marca entre 1 y 3");
        }

        String marca;
        if(idMarca == 1){
            marca = "Toyota";
        } else if(idMarca == 2){
            marca = "Nissan";
        } else {
            marca = "Mercedes";
        }

        CocheModel cocheModelsave = new CocheModel(new MarcaModel(idMarca, marca), cocheModel.getModelo(), cocheModel.getCaballos());

        if (!file.isEmpty()){
            cocheModel.setImagen(file.getOriginalFilename());
            cocheModel.setFoto(ImageUtils.compressImage(file.getBytes()));
            Path dirImg = Paths.get("src//main//resources//static//img");
            String rutaAbsoluta = dirImg.toFile().getAbsolutePath();

            try {
                byte[] bytesImg = file.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + file.getOriginalFilename());
                Files.write(rutaCompleta,bytesImg);
            } catch (IOException e){
                throw new CocheModelException("Error de escritura");
            }
        } else{
            throw  new CocheModelBadRequestException("Debe introducirse el fichero imagen");
        }
        return cocheRepository.save(cocheModel);
    }
    public void deleteCocheById(Long id){
        cocheRepository.deleteById(id);
    }

    public byte[] descargarFoto(Long id){
        CocheModel cocheModel = cocheRepository.findById(id).orElse(null);
        return cocheModel != null ? ImageUtils.decompressImage(cocheModel.getFoto()) : null;
    }

    public List<CocheModel> getCocheByModelo(String modelo){
        return cocheRepository.findByModeloContainingIgnoreCase(modelo);
    }

    public List<CocheModel> orderCochesByCaballos(){
        return cocheRepository.OrderByCaballos();
    }
}
