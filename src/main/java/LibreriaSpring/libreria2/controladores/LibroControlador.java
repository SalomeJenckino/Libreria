package LibreriaSpring.libreria2.controladores;

import LibreriaSpring.libreria2.entidades.Autor;
import LibreriaSpring.libreria2.entidades.Editorial;
import LibreriaSpring.libreria2.entidades.Libro;
import LibreriaSpring.libreria2.entidades.Usuario;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.repositorios.AutorRepositorio;
import LibreriaSpring.libreria2.repositorios.EditorialRepositorio;
import LibreriaSpring.libreria2.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    //Crear libros
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar-libro")
    public String editarLibro(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion, ModelMap model) {
        
        if (accion == null) {
            accion = "Crear";
        }
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }

        Libro libro = new Libro();
        if (id != null && !id.isEmpty()) {
            try {
                libro = libroServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        
        //Añado al modelo un libro
        model.put("editar", libro);
        model.put("accion", accion);

        List<Autor> autores = autorRepositorio.findAll();
        model.put("autores", autores);

        List<Editorial> editoriales = editorialRepositorio.findAll();
        model.put("editoriales", editoriales);

        return "editarLibro.html";
    }

    //Actualizar libros
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar-libro")
    public String actualizarLibro(ModelMap modelo, HttpSession session, @RequestParam String id,
            @RequestParam Long isbn,
            @RequestParam String titulo,
            @RequestParam Integer anio,
            @RequestParam Integer ejemplares,
            @RequestParam String idAutor,
            @RequestParam String idEditorial) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }

        try {
            if (id == null || id.isEmpty()) {
                libroServicio.guardarL(isbn, titulo, anio, ejemplares, idAutor, idEditorial);
            } else {
                libroServicio.modificarL(id, isbn, titulo, anio, ejemplares, idAutor, idEditorial);
            }
            return "inicio.html";
        } catch (ErrorServicio e) {
            Libro libro = new Libro();

            //mostrar los datos precargados
            libro.setId(id);
            libro.setIsbn(isbn);
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            
            modelo.put("accion", "Actualizar");
            
            List<Autor> autores = autorRepositorio.buscarAutoresActivos();
            modelo.put("autores", autores);

            List<Editorial> editoriales = editorialRepositorio.buscarEditorialesActivas();
            modelo.put("editoriales", editoriales);

            modelo.put("error", e.getMessage());

            modelo.put("editar", libro);

            return "editarLibro.html";
        }
    }

    //Listar libros
    @GetMapping("/lista-libros")
    public String listaLibros(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }
            
        List<Libro> libros = libroServicio.buscarLibrosActivos();
        model.put("libros", libros);
        return "libros.html";
    }
    
    //Eliminar libro
    @GetMapping("eliminar-libro")
    public String eliminarLibro(ModelMap mod, @RequestParam(required = true) String id){      
        try {
            libroServicio.deshabilitar(id);         
        } catch (ErrorServicio ex) {
            mod.put("accion", "Eliminar");
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/libro/lista-libros";
    }

}
