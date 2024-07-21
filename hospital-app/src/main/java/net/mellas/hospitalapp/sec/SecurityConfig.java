package net.mellas.hospitalapp.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
private DataSource dataSource;


   /* @Bean
    public DataSource dataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
                .build();
    }*/
      @Bean
   public InMemoryUserDetailsManager inMemoryUserDetailsManager (PasswordEncoder passwordEncoder){
    String encodedPassword = passwordEncoder.encode("1234");
       return new InMemoryUserDetailsManager(
               User.withUsername("user1").password(encodedPassword).roles("USER").build(),
               User.withUsername("user2").password(encodedPassword).roles("USER").build(),
               User.withUsername("admin").password(encodedPassword).roles("USER","ADMIN").build()

       );

   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       return  httpSecurity
               .formLogin(Customizer.withDefaults())
               .authorizeHttpRequests(ar->ar.requestMatchers("/deletePatient/**","/editPatient/**","/save/**","/formPatients/**").hasRole("ADMIN"))
               .authorizeHttpRequests(ar->ar.requestMatchers("/index/**").hasRole("USER"))
               .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
               //.exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler)
                      // .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.MULTI_STATUS)))
               .build();

   }

@Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
