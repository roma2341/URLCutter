package study;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import study.models.LinkInfo;
import study.models.User;
import study.services.LinkInfosService;
import study.services.UsersService;
import study.utils.MyUtils;

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

	@RequestMapping(value = "/z{linkId}", method = RequestMethod.GET)
	public String onShortLink(@PathVariable Long linkId, HttpServletResponse response) throws IOException {
		String shortLink = linkInfosService.getLinkInfo(linkId).getData();
		if (MyUtils.isLink(shortLink))
	    return "redirect:"+shortLink;
		else {
			return "redirect:textview?link_id="+linkId;
		}
	}

	@RequestMapping(value ="/login", method = RequestMethod.GET)
    public String loginForm() {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "login";
    }
	@RequestMapping(value ="/textview", method = RequestMethod.GET)
    public String textview(Model model,@RequestParam("link_id") String linkId, HttpServletResponse response) {
		model.addAttribute("linkInfo", linkInfosService.getLinkInfo(Long.parseLong(linkId)));
        return "textview";
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
	@RequestMapping(value = "/removeuser", method = RequestMethod.GET)
	public String removeUser(@RequestParam("user_id") Long userId, HttpServletRequest request) {
		if(User.getCurrentUserId()==userId)
		{
			throw new IllegalArgumentException("You can't delete yourself");
		}
		usersService.removeUser(userId);
		return "redirect:/admin";
	}
	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
	/*@ExceptionHandler(IllegalArgumentException.class)
	void handleBadRequests(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again width different user");
	}*/
	
	@RequestMapping("/linkinfo")
    public String linkinfo(Model model) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "linkinfo";
    }
	@RequestMapping("/admin")
    public String admin(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
		Page<User> postsPage = usersService.getUsers(page, 5); 
		 model.addAttribute("users", postsPage.getContent());
		 model.addAttribute("pagesCount", postsPage.getTotalPages());
		 model.addAttribute("currentPage", page);
		 model.addAttribute("currentUser", User.getCurrentUser());
		 model.addAttribute("currentUserId", User.getCurrentUserId());
		 
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
