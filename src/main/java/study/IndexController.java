package study;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import study.models.LinkInfo;
import study.models.User;
import study.services.LinkInfosService;
import study.services.UsersService;

@Controller
public class IndexController {
	@Autowired
    private LinkInfosService linkInfosService;
	@Autowired
	private UsersService usersService;
	
	@RequestMapping("/")
	public String index(Model model) {
		
		// якщо користувач вже увійшов, то перекинути його з реєстрації на домашню сторінку		
		if(!User.isAnonymous()) {
			return "redirect:/home";
		}
		
		return "login";
	}
	
	@RequestMapping("/home")
    public String home(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
		Page<LinkInfo> postsPage = linkInfosService.getLinksInfo(page, 5); 
		 model.addAttribute("linksInfo", postsPage.getContent());
		 model.addAttribute("pagesCount", postsPage.getTotalPages());
		 model.addAttribute("currentPage", page);
		 model.addAttribute("currentUser", User.getCurrentUser());
		 
        return "home";
    }

	

	@RequestMapping(value ="/login", method = RequestMethod.GET)
    public String loginForm() {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "login";
    }
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestParam("signup_username") String login, 
						   @RequestParam("signup_email") String email, 
						   @RequestParam("signup_pass") String pass,HttpServletRequest request) {
		
		usersService.register(login, email, pass);

		return "redirect:/home";
	}
	@RequestMapping(value = "/removelink", method = RequestMethod.GET)
	public String removeLink(@RequestParam("link_id") Long linkId, HttpServletRequest request) {
		linkInfosService.removeLinkInfo(linkId);
		return "redirect:/home";
	}
	
	@RequestMapping("/linkinfo")
    public String linkinfo(Model model) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "linkinfo";
    }
	@RequestMapping("/admin")
    public String admin(Model model) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "admin";
    }
	
	@RequestMapping("/userinfo")
    public String userinfo(Model model) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "userinfo";
    }
	
	
	 @RequestMapping(value = "/post", method = RequestMethod.POST)
	    public String createLinkInfo(@RequestParam("data") String linkData) {
	        linkInfosService.addLinkInfo(new LinkInfo(linkData));
	        return "redirect:home";
	    }
}
