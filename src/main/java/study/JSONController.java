package study;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

import study.models.LinkInfo;

@Controller
@RequestMapping("/json")
public class JSONController {
	@JsonView(View.Summary.class)
	@RequestMapping(value = "/test",method = RequestMethod.GET)
	public @ResponseBody LinkInfo testJSON(){
return new LinkInfo("Hello user");

}
	
}
