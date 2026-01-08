package py.com.hidraulica.caacupe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import py.com.hidraulica.caacupe.security.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> {
		}) // habilita CORS usando CorsConfigurationSource
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// ✅ preflight CORS
						.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

						// Públicos
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api/v1/health",
								"/api/auth/**")
						.permitAll()

						// Admin
						.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

						// Operaciones de caja: ventas/facturas/clientes/productos/servicios
						.requestMatchers("/api/v1/ventas/**", "/api/v1/facturas/**").hasAnyRole("ADMIN", "CAJERO")
						.requestMatchers("/api/v1/clientes/**", "/api/v1/productos/**", "/api/v1/servicios/**", "/api/v1/categorias/**", "/api/v1/marcas/**", "/api/v1/proveedores/**")
						.hasAnyRole("ADMIN", "CAJERO")

						// Taller
						.requestMatchers("/api/v1/ordenes-trabajo/**").hasAnyRole("ADMIN", "TALLER")

						// Stock (entrada/salida) - por ahora caja/admin
						.requestMatchers("/api/v1/movimientos-stock/**", "/api/v1/stocks/**", "/api/v1/depositos/**")
						.hasAnyRole("ADMIN", "CAJERO")

						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			org.springframework.security.core.userdetails.UserDetailsService uds, PasswordEncoder encoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(encoder);
		return new ProviderManager(provider);
	}
}
