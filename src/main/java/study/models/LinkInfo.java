package study.models;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
@Entity
public class LinkInfo {
	enum LinkType { LINK, TEXT }
	//public static int linkCounter = 0;

public LinkInfo(String data){
	this.data=data;
	//this.shortLink= String.format("%06d%n", this.id);	
	this.creationTime=new Date();
}
/*public void init(){
	this.shortLink= String.format("%06d%n", id);	
}*/
public LinkInfo(){
	
}

public Date getCreationTime() {
	return creationTime;
}
public void setCreationTime(Date createdAt) {
	this.creationTime = createdAt;
}
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}
public LinkType getLinkType() {
	return linkType;
}
public void setLinkType(LinkType linkType) {
	this.linkType = linkType;
}
/*public String getShortLink() {
	return shortLink;
}
public void setShortLink(String shortLink) {
	this.shortLink = shortLink;
}*/
@Id
@GeneratedValue
private Long id;

public Long getId() {
	return id;
}
@NotNull
@ManyToOne
private User author;

public User getAuthor() {
	return author;
}
public void setAuthor(User author) {
	this.author = author;
}
@NotBlank
@Size(min = 1, max = 2048)
private String data;

/*@NotBlank
@Size(min = 1, max = 2048)
private String shortLink;*/

@NotNull
private Date creationTime;
private LinkType linkType = LinkType.TEXT;

}
