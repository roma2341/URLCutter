package study;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.bitwalker.useragentutils.UserAgent;
import study.models.LinkInfo;
import study.models.User;
import study.services.LinkInfosService;
import study.services.UsersService;
import study.utils.MyUtils;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

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
	@RequestMapping(value = "/link_statistic_img{linkId}", method = RequestMethod.GET)
	public @ResponseBody void getImage(HttpServletResponse response)
	{
	    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

	    response.setContentType("image/png");
	    OutputStream out;
	    try
	    {
	        out = response.getOutputStream();
	        ImageIO.write(image, "png", out);
	        out.close();
	    }
	    catch (IOException ex)
	    {
	        //Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
	    	System.out.println("Error occured during getImage call:"+ex);
	    }
	}
	/*{
		Slice s1 = Slice.newSlice(15,Color.newColor("CACACA"),"Mac","Mac");
		PieChart pieChart = GCharts.newPieChart(s1);
		pieChart.setTitle("Google Pie Chart", Color.BLACK, 15);
		pieChart.setSize(720, 360);
		pieChart.setThreeD(true);
		model.addAttribute("pieUrl", pieChart.toURLString());
	}*/

	@RequestMapping(value = "/z{linkId}", method = RequestMethod.GET)
	public String onShortLink(Model model,@PathVariable Long linkId,HttpServletRequest request, HttpServletResponse response) throws IOException {
		String shortLink = linkInfosService.getLinkInfo(linkId).getData();
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		String browserName = userAgent.getBrowser().getName();
		String osName = userAgent.getOperatingSystem().getName();
		System.out.println(browserName); //+ " " + userAgent.getBrowserVersion());
		//Browser detect
		LinkInfo linkInfo = linkInfosService.getLinkInfo(linkId);
		linkInfo.incCountAll();
		if (browserName.contains("Chrome") || browserName.contains("Chrome Mobile")) 
			linkInfo.incCountChrome();
		else if (browserName.contains("Firefox"))
			linkInfo.incCountMozilla();
		else if (browserName.contains("Internet Explorer") || browserName.contains("IE Mobile"))
			linkInfo.incCountIE();
		//
			if(osName.contains("Windows"))
				linkInfo.incCountWindows();
			else if (osName.contains("Linux"))
				linkInfo.incCountLinux();
		System.out.println("chrome:"+linkInfo.getCountChrome());
		System.out.println("windows:"+linkInfo.getCountWindows());
		linkInfosService.updateLinkInfo(linkInfo,linkId);
			
		
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
		if (User.getCurrentUser().getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
				linkInfosService.getLinkInfo(linkId).getAuthor().getId()==User.getCurrentUserId())
		linkInfosService.removeLinkInfo(linkId);
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;
		//return "redirect:/home";
	}
	@RequestMapping(value = "/removeuser", method = RequestMethod.GET)
	public String removeUser(@RequestParam("user_id") Long userId, HttpServletRequest request) {
		if(User.getCurrentUserId()==userId)
		{
			throw new IllegalArgumentException("You can't delete yourself");
		}
		usersService.removeUser(userId);
		String referer = request.getHeader("Referer");
	    return "redirect:"+ referer;
		//return "redirect:/admin";
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
    public String linkinfo(Model model,@RequestParam("link_id") Long linkId) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
		LinkInfo currentLinkInfo = linkInfosService.getLinkInfo(linkId);
		model.addAttribute("linksInfo", currentLinkInfo);
		int mozillaPercent = 0,chromePercent=0,iePercent=0,windowsPercent=0,linuxPercent=0;
		if (currentLinkInfo.getCountAll()>0)
		{
		mozillaPercent = currentLinkInfo.getCountMozilla()*100/currentLinkInfo.getCountAll();
		chromePercent = currentLinkInfo.getCountChrome()*100/currentLinkInfo.getCountAll();
		iePercent = currentLinkInfo.getCountIE()*100/currentLinkInfo.getCountAll();		
		windowsPercent = currentLinkInfo.getCountWindows()*100/currentLinkInfo.getCountAll();
		linuxPercent = currentLinkInfo.getCountLinux()*100/currentLinkInfo.getCountAll();
		}
		
		Slice s1 = Slice.newSlice(mozillaPercent,Color.GREEN,"Mozilla");
		Slice s2 = Slice.newSlice(chromePercent,Color.BLUE,"Chrome");
		Slice s3 = Slice.newSlice(iePercent,Color.RED,"IE");
		
		Slice s4 = Slice.newSlice(windowsPercent,Color.GREEN,"Windows");
		Slice s5 = Slice.newSlice(linuxPercent,Color.BLUE,"Linux");
		/*int countAll;
		int countMozilla;
		int countChrome;
		int countIE;
		int countWindows;
		int	countLinux;*/
		PieChart pieChartBrowser = GCharts.newPieChart(s1,s2,s3);
		pieChartBrowser.setTitle("Browser statistic", Color.BLACK, 15);
		pieChartBrowser.setSize(450, 240);
		pieChartBrowser.setThreeD(true);
		PieChart pieChartOS = GCharts.newPieChart(s4,s5);
		pieChartOS.setTitle("OS statistic", Color.BLACK, 15);
		pieChartOS.setSize(450, 240);
		pieChartOS.setThreeD(true);
		
		model.addAttribute("pieUrlBrowser", pieChartBrowser.toURLString());
		model.addAttribute("pieUrlOS", pieChartOS.toURLString());
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
	
	@RequestMapping("/linklist")
    public String linklist(Model model,@RequestParam("user_id") Long userId, @RequestParam(value = "page", defaultValue = "1") int page) {
		Page<LinkInfo> postsPage = linkInfosService.getLinksInfoByUserId(page, 5,userId); 
		 model.addAttribute("linksInfo", postsPage.getContent());
		 model.addAttribute("pagesCount", postsPage.getTotalPages());
		 model.addAttribute("currentPage", page);	 
		model.addAttribute("user_id", userId);
        return "linklist";
    }

	
	@RequestMapping("/userinfo")
    public String userinfo(Model model) {
		// model.addAttribute("linksInfo", postsService.getLinksInfo());
        return "userinfo";
    }
	
	
	 @RequestMapping(value = "/post", method = RequestMethod.POST)
	    public String createLinkInfo(@RequestParam("data") String linkData,HttpServletRequest request) {
		 if (linkData.length()>0)
	        linkInfosService.addLinkInfo(new LinkInfo(linkData));
		 String referer = request.getHeader("Referer");
		    return "redirect:"+ referer;
	      //  return "redirect:home";
	    }
	 
}
