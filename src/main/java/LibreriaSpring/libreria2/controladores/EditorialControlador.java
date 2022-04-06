package LibreriaSpring.libreria2.controladores;

import LibreriaSpring.libreria2.entidades.Editorial;
import LibreriaSpring.libreria2.entidades.Usuario;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.servicios.EditorialServicio;
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

@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/editar-editorial")
    public String editarEditorial(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String accion,ModelMap model){
        
        if (accion == null) {
            accion = "Crear";
        }
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }
        
        Editorial editorial = new Editorial();
        if (id != null && !id.isEmpty()) {
            try {
                editorial = editorialServicio.buscarPorId(id);
            } catch (ErrorServicio ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        
        //Añado al modelo la editorial
        model.put("editar", editorial);
        model.put("accion", accion);
        
        return "editarEditorial.html";
    }
    
    //Actualizar editoriales
    @PostMapping("/actualizar-editorial")
    public String actualizarEditorial(ModelMap modelo, HttpSession session, @RequestParam String id,
            @RequestParam String nombre){

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }

        try {
            if (id == null || id.isEmpty()) {
               editorialServicio.guardarE(nombre);
            } else {
               editorialServicio.modificarE(id, nombre);
            }
            return "inicio.html";
        } catch (ErrorServicio e) {
            Editorial editorial = new Editorial();

            //mostrar los datos precargados
            editorial.setId(id);
            editorial.setNombre(nombre);
            
            modelo.put("accion", "Actualizar");
            modelo.put("error", e.getMessage());
            modelo.put("editar", editorial);

            return "editarEditorial.html";
        }
    }
    
    //Listar editoriales
    @GetMapping("/lista-editoriales")
    public String listaEditoriales(HttpSession session, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null) {
            return "login.html";
        }
            
        List<Editorial> editoriales = editorialServicio.buscarEditorialesActivas();
        model.put("editoriales", editoriales);
        return "editoriales.html";
    }
    
    //Eliminar editorial
    @GetMapping("eliminar-editorial")
    public String eliminarEditorial(ModelMap mod, @RequestParam(required = true) String id){      
        try {
            editorialServicio.deshabilitar(id);         
        } catch (ErrorServicio ex) {
            mod.put("accion", "Eliminar");
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/editorial/lista-editoriales";
    }

//    @GetMapping("/registroEditorial")
//    public String registroE() {
//        return "registroEditorial.html";
//    }
//    
//    @PostMapping("/guardarE")
//    public String guardarE(ModelMap modelo,@RequestParam String nombre){
//        System.out.println("Nombre autor: "+nombre);
//        
//        try{
//            editorialServicio.guardarE(nombre);
//        }catch(ErrorServicio ex){
//            modelo.put("error", ex.getMessage());
//            return "registroEditorial.html";
//        }
//      return "inicio.html";
//    }

}
