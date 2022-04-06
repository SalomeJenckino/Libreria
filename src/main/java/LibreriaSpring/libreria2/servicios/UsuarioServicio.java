package LibreriaSpring.libreria2.servicios;

import LibreriaSpring.libreria2.entidades.Usuario;
import LibreriaSpring.libreria2.enums.Role;
import LibreriaSpring.libreria2.errores.ErrorServicio;
import LibreriaSpring.libreria2.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private NotificacionServicio notificacionServicio;
    
    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave) throws ErrorServicio {

        validar(nombre, apellido, mail, clave);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);

        usuario.setAlta(new Date());
        usuario.setRol(Role.USER);
        
        usuarioRepositorio.save(usuario);
        
        //notificacionServicio.enviar("Bienvenido a Libreria Spring", "Libreria", usuario.getMail());
    }

    public void validar(String nombre, String apellido, String mail, String clave) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Nombre no puede ser vacio");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("Apellido no puede ser vacio");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("Mail no puede ser vacio");
        }

        if (clave == null || clave.isEmpty() || clave.length() < 6) {
            throw new ErrorServicio("Clave no puede ser vacio o tener menos de 6 caracteres");
        }
    }
    
    @Transactional
    public void modificar(String id, String nombre, String apellido, String mail, String clave) throws ErrorServicio {
        validar(nombre, apellido, mail, clave);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario");
        }

    }
    
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario");
        }
    }
    
    public Usuario buscarPorId(String id) throws ErrorServicio{
        Optional<Usuario> respuesta =  usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            return usuario;
        } else {
            throw new ErrorServicio("No se encontró el usuario");
        }   
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            permisos.add(new SimpleGrantedAuthority("ROLE_"+usuario.getRol()));
//            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
//            permisos.add(p1);
            
            //Con esta llamada guardo al usuario de la BD que se que esta autenticado
            //y lo meto en esta sesion web
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession();
            session.setAttribute("usuariosession", usuario);
            //en la variable de sesion usariosession yo tengo guardado mi objeto con todos los datos del usuario que esta logueado
            
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
