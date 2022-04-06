
package LibreriaSpring.libreria2;

import LibreriaSpring.libreria2.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter{
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(usuarioServicio)        //configuracion del manejador de seguridad de Spring Security al cual le decimos cual es el servicio que debe utilizar para poedr autenticar un usuario
                .passwordEncoder(new BCryptPasswordEncoder());   //una vez autenticado el usuario con el usuarioServicio dice cual es el enconder que va utilizar para compara las contraseñas
                
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()                                    //en estas tres lineas digo que 
                            .antMatchers("/css/*","/js/*","/img/*")     //los recursos que estan en esas 3 carpetas
                            .permitAll()                                //los pueda acceder cualquier usuario sin necesidad de estar logueado
                .and().formLogin()
                            .loginPage("/login")
                                .loginProcessingUrl("/logincheck")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/inicio")
                                .permitAll()
                            .and().logout()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .permitAll();
        http.csrf().disable();
    }
}
