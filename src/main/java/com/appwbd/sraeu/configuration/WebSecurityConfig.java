package com.appwbd.sraeu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeRequests()
                .antMatchers("/eventos/showEventos", "/asistents/showAsistentes", "/lugares/showLugares").hasAuthority("CONSULTAR")
                .antMatchers("/eventos/eventoForm", "/asistents/asistenteForm", "/lugares/lugarForm").hasAnyAuthority("REGISTRAR", "MODIFICAR")
                .antMatchers("/eventos/removeEvento", "/asistentes/removeAsistente", "/lugares/removeLugar").hasAuthority("ELIMINAR")
                .antMatchers("/usuarios/*").hasAuthority("MANTENIMIENTOU")
                .anyRequest().permitAll()
            .and().formLogin()
                .loginPage("/login").permitAll()
            .and().logout().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception{
        return authenticationManager();
    }
}
