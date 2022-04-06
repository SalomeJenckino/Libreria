
package LibreriaSpring.libreria2.controladores;

import LibreriaSpring.libreria2.entidades.Autor;
import LibreriaSpring.libreria2.entidades.Usuario;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.servicios.AutorServicio;
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

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
@Controller
@RequestMapping("/autor")
public class AutorControlador {
    
    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping("/editar-autor")
    public String editarAutor(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion,ModelMap model){
        
        if (accion == null) {
            accion = "Crear";
        }
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }
        
        Autor autor = new Autor();
        if (id != null && !id.isEmpty()) {
            try {
                autor = autorServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        
        //Añado al modelo el autor
        model.put("editar", autor);
        model.put("accion", accion);
        
        return "editarAutor.html";
    }
    
    //Actualizar libros
    @PostMapping("/actualizar-autor")
    public String actualizarAutor(ModelMap modelo, HttpSession session, @RequestParam String id,
            @RequestParam String nombre){

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }

        try {
            if (id == null || id.isEmpty()) {
                autorServicio.guardarA(nombre);
            } else {
                autorServicio.modificarA(id, nombre);
            }
            return "inicio.html";
        } catch (ErrorServicio e) {
            Autor autor = new Autor();

            //mostrar los datos precargados
            autor.setId(id);
            autor.setNombre(nombre);
            
            modelo.put("accion", "Actualizar");
            modelo.put("error", e.getMessage());
            modelo.put("editar", autor);

            return "editarAutor.html";
        }
    }
    
    //Listar autores
    @GetMapping("/lista-autores")
    public String listaAutores(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }
            
        List<Autor> autores = autorServicio.buscarAutoresActivos();
        model.put("autores", autores);
        return "autores.html";
    }
    
    //Eliminar libro
    @GetMapping("eliminar-autor")
    public String eliminarAutor(ModelMap mod, @RequestParam(required = true) String id){      
        try {
            autorServicio.deshabilitar(id);         
        } catch (ErrorServicio ex) {
            mod.put("accion", "Eliminar");
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/autor/lista-autores";
    }
    
//    @GetMapping("/registroAutor")
//    public String registro(){
//        return "registroAutor.html";
//    }
//    
//    @PostMapping("/guardar")
//    public String guardar(ModelMap modelo,@RequestParam String nombre){
//        System.out.println("Nombre autor: "+nombre);
//        
//        try{
//            autorServicio.guardar(nombre);
//        }catch(ErrorServicio ex){
//            modelo.put("error", ex.getMessage());
//            return "registroAutor.html";
//        }
//      return "inicio.html";
//    }
    
}
