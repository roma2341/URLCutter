package study.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import study.models.User;
import study.repositories.UserRepository;

@Service
public class UsersService {
	
	@Autowired
	private UserRepository usersRepo;
	
	@PostConstruct
	@Transactional
	public void createAdminUser() {
		register("admin", "admin@mail.com", "qwerty");
	}
	
	@Transactional(readOnly = false)
	public void register(String login, String email, String pass) {
		String passHash = new BCryptPasswordEncoder().encode(pass);
		//String passHash = pass;
		
		User u = new User(login, email.toLowerCase(), passHash);

		// підпишемо користувача на самого себе

		usersRepo.save(u);
	}
	
}

