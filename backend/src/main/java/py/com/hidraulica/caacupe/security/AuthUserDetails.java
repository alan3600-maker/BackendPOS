package py.com.hidraulica.caacupe.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthUserDetails extends User {

	private static final long serialVersionUID = 3247491051217535001L;
	private final Long id;

	public AuthUserDetails(Long id, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
