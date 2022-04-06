package LibreriaSpring.libreria2.servicios;

import LibreriaSpring.libreria2.entidades.Autor;
import LibreriaSpring.libreria2.entidades.Editorial;
import LibreriaSpring.libreria2.entidades.Libro;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.repositorios.AutorRepositorio;
import LibreriaSpring.libreria2.repositorios.EditorialRepositorio;
import LibreriaSpring.libreria2.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void guardarL(Long isbn, String titulo, Integer anio, Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {
        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);

        validar(isbn, titulo, anio, ejemplares, autor, editorial);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(ejemplares);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(true);

        libroRepositorio.save(libro);

    }

    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Autor autor, Editorial editorial) throws ErrorServicio {
        if (isbn == null) {
            throw new ErrorServicio("Isbn no puede ser vacio");
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Titulo no puede ser vacio");
        }

        if (anio == null) {
            throw new ErrorServicio("Año no puede ser vacio");
        }

        if (ejemplares == null) {
            throw new ErrorServicio("Ejemplares no puede ser vacio");
        }

        if (autor == null) {
            throw new ErrorServicio("Autor no puede ser vacio");
        }

        if (editorial == null) {
            throw new ErrorServicio("Editorial no puede ser vacio");
        }
    }
    
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el libro");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(true);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el libro");
        }
    }
    
    @Transactional
    public void modificarL(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {

        Autor autor = autorRepositorio.getOne(idAutor);
        Editorial editorial = editorialRepositorio.getOne(idEditorial);

        validar(isbn, titulo, anio, ejemplares, autor, editorial);

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            
            //libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el usuario");
        }
    }
    
    public Libro buscarPorId(String id) throws ErrorServicio{
        Optional<Libro> respuesta = libroRepositorio.findById(id);
         if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            return libro;
        } else {
            throw new ErrorServicio("No se encontró el libro");
        }
    }
    
    public List<Libro> buscarLibrosActivos(){
        return libroRepositorio.buscarLibrosActivos();
    }
   
}
