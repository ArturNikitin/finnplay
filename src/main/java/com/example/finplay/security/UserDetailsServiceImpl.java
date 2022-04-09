package com.example.finplay.security;

import com.example.finplay.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		log.info("Logging in with user " + email);
		var user = userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User " + email + "Not found"));
		return MyUserDetails.of(user.getEmail(), user.getPassword(), Set.of("ROLE_USER"));
	}

	@Value(staticConstructor = "of")
	public static class MyUserDetails implements UserDetails {
		String username;
		String password;
		Set<String> roles;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}
