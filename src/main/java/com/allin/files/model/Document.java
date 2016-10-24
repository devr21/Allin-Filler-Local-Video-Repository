package com.allin.files.model;

import java.util.Date;
import java.util.UUID;

public class Document {

	private static int count = 1;
	private UUID id;
	private String name;
	private String path;
	private String extension;
	private String nameWithExtension;
	private Date createdAt;
	private Date updatedAt;

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName(){
		this.name = !extension.isEmpty() || extension != null?nameWithExtension:String.valueOf(count++);
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getNameWithExtension() {
		return nameWithExtension;
	}
	public void setNameWithExtension(String nameWithExtension) {
		this.nameWithExtension = nameWithExtension;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String toJson(){
		
		String json = "{";
		json += "\"";
		json += "name";
		json += "\":";
		json += "\"";
		json += name+"\",";
		json += "\"path\":\""+path+"\",\"extension\":\""+extension+"\",\"nameWithExtension\":\""+nameWithExtension;
		json += "\",\"createdAt\":\""+createdAt.toString()+"\",\"updatedAt\":\""+updatedAt+"\"}";
		
		return json;
	}
	
	
}
