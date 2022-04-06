package LibreriaSpring.libreria2.controladores;

import LibreriaSpring.libreria2.entidades.Usuario;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.servicios.UsuarioServicio;
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
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        //Esto evita que cualquier usuario pueda modificar otro usuario que no le pertenece pegando el id de otro usuario en la url
        Usuario login = (Usuario) session.getAttribute("usuariosession"); //se recupera el usuario de la session
        if (login == null || !login.getId().equals(id)) {
            return "redirec:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            model.addAttribute("perfil", usuario);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "editarUsuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap model, HttpSession session, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave) {
        Usuario usuario = null;
        try {
            usuario = usuarioServicio.buscarPorId(id);
            usuarioServicio.modificar(id, nombre, apellido, mail, clave);
            session.setAttribute("usuariosession", usuario); //esta llamada pisa el usuario viejo que los datos actualizados
            return "redirect:/inicio";
        } catch (ErrorServicio e) {
            model.put("error", e.getMessage());
            model.put("perfil", usuario);
        }
        return "registroUsuario.html";
    }
}
