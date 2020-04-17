package fr.istic.iodeman.controllers;


import fr.istic.iodeman.dto.AuthenticationResponseDTO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConnectionController {

	final
	PersonUidResolver personUidResolver;

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtTokenUtil;

	public ConnectionController(PersonUidResolver personUidResolver, AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
		this.personUidResolver = personUidResolver;
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping("/authenticate")
	public AuthenticationResponseDTO login(@RequestParam("uid") String uid) throws Exception {

		if (!uid.equals("")) {
			Person person = personUidResolver.resolve(uid);

			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(person.getEmail(), person.getUid())
				);
			} catch (BadCredentialsException e) {
				throw new Exception("uid incorrect", e);
			}
			return new AuthenticationResponseDTO(person, jwtTokenUtil.generateToken(person.getEmail()));
		}
		return new AuthenticationResponseDTO();
	}

}
