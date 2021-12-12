package  it.unibo.parkingmanagergui.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@Configuration
@EnableWebSecurity
class WebConfigSecurity : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/pm/alarm", "/pm/test" )
            .permitAll()
            .antMatchers("/pm").hasRole("MANAGER").
            anyRequest().authenticated()
            .and()
            .formLogin()
            .defaultSuccessUrl("/pm", true)
            .permitAll()
            .and()
            .logout().deleteCookies("remove").invalidateHttpSession(false)
            .logoutSuccessUrl("/")

    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**")
    }
    @Bean
    public override fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("MANAGER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}