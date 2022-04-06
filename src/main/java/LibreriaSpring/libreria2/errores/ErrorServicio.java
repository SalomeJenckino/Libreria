
package LibreriaSpring.libreria2.errores;

public class ErrorServicio extends Exception{
    //constructor del error
    public ErrorServicio(String msj){
        super(msj);
    }
    // creamos esta clase para diferenciar los errores de las reglas de negocio con
    //los errores del sistema
}
