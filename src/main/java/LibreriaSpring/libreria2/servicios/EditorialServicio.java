
package LibreriaSpring.libreria2.servicios;

import LibreriaSpring.libreria2.entidades.Editorial;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void guardarE(String nombre) throws ErrorServicio{
               
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        
        editorialRepositorio.save(editorial);
    }
    
    public void validar(String nombre)throws ErrorServicio{
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre de la editorial no puede estar vacia");
        }
    }
    
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró la editorial");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró la editorial");
        }
    }
    
    @Transactional
    public void modificarE(String id, String nombre)throws ErrorServicio{
        validar(nombre);
        
        Editorial respuesta = editorialRepositorio.getOne(id);
        if (respuesta!=null) {
            Editorial editorial = respuesta;
            editorial.setNombre(nombre);
        }else {
            throw new ErrorServicio("No se encontró la editorial");
        }
    }
    
    public Editorial buscarPorId(String id) throws ErrorServicio{
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
         if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            return editorial;
        } else {
            throw new ErrorServicio("No se encontró la editorial");
        }
    }
    
    public List<Editorial> buscarEditorialesActivas(){
        return editorialRepositorio.buscarEditorialesActivas();
    }
}
