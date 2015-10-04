package study.services;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import study.models.LinkInfo;
import study.models.User;
import study.models.User.Permissions;
import study.repositories.UserRepository;

@Service
public class UsersService {
	
	@Autowired
	private UserRepository usersRepo;
	
	@PostConstruct
	@Transactional
	public void createAdminUser() {
		register("admin", "admin@mail.com", "qwerty",Permissions.PERMISSIONS_ADMIN);
		register("user", "user@mail.com", "qwerty",Permissions.PERMISSIONS_USER);
	}
	
	@Transactional
	   public Page<User> getUsers(int page, int pageSize){
			return usersRepo.findAll(new PageRequest(page-1, pageSize)); // spring рахує сторінки з нуля
			
	   }
	
	@Transactional(readOnly = false)
	public void register(String login, String email, String pass) {
		String passHash = new BCryptPasswordEncoder().encode(pass);
		//String passHash = pass;
		
		User u = new User(login, email.toLowerCase(), passHash);

		// підпишемо користувача на самого себе

		usersRepo.save(u);
	}
	@Transactional(readOnly = false)
	public void register(String login, String email, String pass,Permissions permission) {
		String passHash = new BCryptPasswordEncoder().encode(pass);
		//String passHash = pass;
		User u = new User(login, email.toLowerCase(), passHash);
		u.setPermission(permission);
		// підпишемо користувача на самого себе

		usersRepo.save(u);
	}
	
}

