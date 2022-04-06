
package LibreriaSpring.libreria2.repositorios;

import LibreriaSpring.libreria2.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT c FROM Usuario c WHERE c.id = :id")
    public List<Usuario> buscarPorId(@Param("id") String id);
    
    @Query ("SELECT c FROM Usuario c WHERE c.mail = :mail")
    public Usuario buscarPorMail(@Param("mail") String mail);
    
    @Query("SELECT c FROM Usuario c WHERE c.nombre = :nombre")
    public List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
}
