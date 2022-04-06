
package LibreriaSpring.libreria2.repositorios;

import LibreriaSpring.libreria2.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String>{
    
    @Query("SELECT c FROM Autor c WHERE c.id = :id")
    public List<Autor> listaAutor(@Param("id") String id);
    
    @Query("SELECT c FROM Autor c WHERE c.nombre LIKE :nombre")
    public List<Autor> buscarPorNombre(@Param("nombre") String nombre);
    
    //metodo para guardar un libro
    @Query("SELECT c FROM Autor c WHERE c.alta = true")
    public List<Autor> buscarAutoresActivos();
}
