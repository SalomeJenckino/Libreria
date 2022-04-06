package LibreriaSpring.libreria2.servicios;

import LibreriaSpring.libreria2.entidades.Autor;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired  //con autowired le estamos diciendo al SERVIDOR DE APLICACIONES que la variable autorRepositorio la inicialice él
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void guardarA(String nombre) throws ErrorServicio {

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);

        autorRepositorio.save(autor);   //le decimos al repositorio que lo guarde(persista) en la base de datos
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede estar vacio");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el libro");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(true);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el autor");
        }
    }
    
    @Transactional
    public void modificarA(String id, String nombre) throws ErrorServicio {
        validar(nombre);
        
        Autor respuesta = autorRepositorio.getOne(id);
        if (respuesta != null) {
            Autor autor = respuesta;
            autor.setNombre(nombre);
        } else {
            throw new ErrorServicio("No se encontró el autor");
        }
    }
    
    public Autor buscarPorId(String id) throws ErrorServicio{
        Optional<Autor> respuesta = autorRepositorio.findById(id);
         if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            return autor;
        } else {
            throw new ErrorServicio("No se encontró el autor");
        }
    }
    
    public List<Autor> buscarAutoresActivos(){
        return autorRepositorio.buscarAutoresActivos();
    }
}
