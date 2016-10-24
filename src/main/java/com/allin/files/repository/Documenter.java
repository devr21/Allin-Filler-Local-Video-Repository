package com.allin.files.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;

import com.allin.files.model.Document;

public class Documenter {
	
	private static final String DIRECTORY = "D:\\Music";
	private SolrRepo repo = new SolrRepo();
	
	
	public static void main(String[] args) {
		
		try {
			new Documenter().startOff();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void startOff() throws SolrServerException, IOException {
		File folder = new File(DIRECTORY);
		File[] listOfFiles = folder.listFiles();
		List<Document> docList = new ArrayList<Document>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        Document doc = createDoc(listOfFiles[i]);
	        docList.add(doc);
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName()+" ");
	      }
	    }
	    repo.updateDocuments(docList);
	}

	private Document createDoc(File file) {
		
		Document doc = new Document();
		doc.setId(UUID.randomUUID());
		doc.setNameWithExtension(file.getName());
		doc.setPath(stripSlashes(file.getAbsolutePath()));
		//System.out.println(file.getPath());
		doc.setExtension(getExtension(doc.getNameWithExtension()));
		doc.setCreatedAt(new Date());
		doc.setUpdatedAt(new Date());
		doc.setName();
		
		return doc;
	}

	private String getExtension(String val) {
		
		return val.substring(val.lastIndexOf(".")+1, val.length());
	}

	private String stripSlashes(String path) {
		
		path = path.replace("\\", "/");
		path = path.replace("\"", "'");
		return path;
	}

	
	
}
