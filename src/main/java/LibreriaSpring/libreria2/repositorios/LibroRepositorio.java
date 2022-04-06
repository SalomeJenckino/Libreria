
package LibreriaSpring.libreria2.repositorios;

import LibreriaSpring.libreria2.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String>{
    
    @Query("SELECT c FROM Libro c WHERE c.id = :id")
    public List<Libro> buscarPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Libro c WHERE c.isbn = :isbn")
    public List<Libro> buscarPorIsbn(@Param("isbn") long isbn);
    
    @Query("SELECT c FROM Libro c WHERE c.titulo LIKE :titulo")
    public List<Libro> buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT c FROM Libro c WHERE c.autor.nombre LIKE :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Libro c WHERE c.editorial.nombre LIKE :nombre")
    public List<Libro> buscarPorEditorial(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Libro c")
    public List<Libro> buscarAll();
    
    //metodo para guardar un libro
    @Query("SELECT c FROM Libro c WHERE c.alta = true")
    public List<Libro> buscarLibrosActivos();

}
