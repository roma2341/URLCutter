package study.services;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scala.annotation.meta.getter;
import study.models.LinkInfo;
import study.models.User;
import study.repositories.LinkInfoRepository;
import study.repositories.UserRepository;
@Service
public class LinkInfosService {
	@Autowired
	private UserRepository usersRepo;
	
	@Autowired
	private LinkInfoRepository linksRepo;
	
	   /*private static final List<LinkInfo> INITIAL_LINKINFO = Arrays.asList(			   
	            new LinkInfo("NICO","http:\\google.com"),
	            new LinkInfo("ZIGZAG","http:\\bazara.net"),
	            new LinkInfo("UMKA","http:\\moc.com"));*/
	//ConnectionManager conManager;
	   //private List<LinkInfo> linksInfo = new ArrayList<LinkInfo>();     
	   
	   @Transactional
	   public Page<LinkInfo> getLinksInfo(int page, int pageSize){
			
			return linksRepo.findByAuthorId(User.getCurrentUserId(), new PageRequest(page-1, pageSize)); // spring рахує сторінки з нуля
			
	   }
	   @Transactional
	   public void addLinkInfo(LinkInfo singleLinkInfo)
	   {
		   User currentUser = usersRepo.findOne(User.getCurrentUserId());
		   singleLinkInfo.setAuthor(currentUser);
		   linksRepo.save(singleLinkInfo);
	   }
	   public void removeLinkInfo(Long id){
		   linksRepo.delete(id);
	   }
	   public LinkInfo getLinkInfo(Long id){
		   return linksRepo.findOne(id);
	   }
}
