package study.models;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
@Entity
public class LinkInfo {
	enum LinkType { LINK, TEXT }
	//public static int linkCounter = 0;

public LinkInfo(String data){
	this.data=data;
	//this.shortLink= String.format("%06d%n", this.id);	
	this.creationTime=new Date();
	System.out.println("LinkInfo(String data)");
}
/*public void init(){
	this.shortLink= String.format("%06d%n", id);	
}*/
public LinkInfo(){
System.out.println("LinkInfo()");
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

public int getCountMozilla() {
	return countMozilla;
}
public void setCountMozilla(int countMozilla) {
	this.countMozilla = countMozilla;
}
public int getCountChrome() {
	return countChrome;
}
public void setCountChrome(int countСrome) {
	this.countChrome = countСrome;
}
public int getCountIE() {
	return countIE;
}
public void setCountIE(int countIE) {
	this.countIE = countIE;
}
public int getCountWindows() {
	return countWindows;
}
public void setCountWindows(int countWindows) {
	this.countWindows = countWindows;
}
public int getCountLinux() {
	return countLinux;
}
public void setCountLinux(int countLinux) {
	this.countLinux = countLinux;
}
public void incCountMozilla(){
	countMozilla++;
}
public void incCountChrome(){
	countChrome++;
}
public void incCountIE(){
	countIE++;
}
public void incCountWindows(){
	countWindows++;
}
public void incCountLinux(){
	countLinux++;
}
public int getCountAll() {
	return countAll;
}
public void setCountAll(int countAll) {
	this.countAll = countAll;
}
public void incCountAll(){
	this.countAll++;
}

int countAll;
int countMozilla;
int countChrome;
int countIE;
int countWindows;
int	countLinux;


}
