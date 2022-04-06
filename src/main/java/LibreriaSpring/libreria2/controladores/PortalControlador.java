package LibreriaSpring.libreria2.controladores;

import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) { //con required false yo indico que no necesariamente va haber un parametro de error, solo hay errod cuando ingreso mal los datos, sino ese paramentro esta vacio
        if (error != null) {
            model.put(error, "Usuario o clave incorrectos");
        }
        return "login.html";
    }
    
    @GetMapping("/registroUsuario")
    public String registro() {
        return "registroUsuario.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo ,@RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave) {
        try {
//            System.out.println("Nombre: "+nombre);
//            System.out.println("Apellido: "+apellido);
//            System.out.println("Mail: "+mail);
//            System.out.println("Clave: "+clave);
            usuarioServicio.registrar(nombre, apellido, mail, clave);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            //Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex); lo reemplazo por el modelo.put
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave", clave);
            return "registroUsuario.html";
        }
        return "index.html";
    }
}
