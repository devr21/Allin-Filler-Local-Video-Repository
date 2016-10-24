package com.allin.files.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.inject.Named;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

import com.allin.files.model.Document;

@Named
public class SolrRepo {

	private static String SOLR_BASE_URL = "http://localhost:8983/solr";
	private static String SOLR_CORE_NAME = "allinfiles";
	private static String SOLR_CORE_URL = SOLR_BASE_URL + "/" + SOLR_CORE_NAME;
	private SolrClient solr = new HttpSolrClient.Builder(SOLR_CORE_URL).build();
	
	public void updateDocuments(List<Document> docs) throws SolrServerException, IOException{
		solr.add(docListToSolrInputDocList(docs));
		UpdateResponse res = solr.commit();
	}

	public Document fetchById(String id) throws SolrServerException, IOException{
		
		return map(fetch(id));
	}
	
	private SolrDocument fetch(String id) throws SolrServerException, IOException{
		SolrQuery sq = new SolrQuery();
		sq.setQuery("id:"+id);
		SolrDocument sdoc = solr.query(sq).getResults().get(0);
		return sdoc;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new SolrRepo().fetch("a5bac041-93eb-4d5d-8e79-c9f4edf335f4"));
			
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Document map(SolrDocument sdoc){
		if(sdoc == null)
		System.out.println("null");
		Document doc = new Document();
		/*doc.setCreatedAt(sdoc.getFieldValue("createdAt").toString());
		doc.setUpdatedAt(Date.valueOf(sdoc.getFieldValue("updatedAt").toString()));*/
		doc.setExtension(sdoc.getFieldValue("extension").toString());
		doc.setId(UUID.fromString(sdoc.getFieldValue("id").toString()));
		doc.setName(sdoc.getFieldValue("name").toString());
		doc.setNameWithExtension(sdoc.getFieldValue("nameWithExtension").toString());
		doc.setPath(sdoc.getFieldValue("path").toString());
		return doc;
	}
	
	private List<SolrInputDocument> docListToSolrInputDocList(List<Document> docs) {
		
		List<SolrInputDocument> sdocs = new ArrayList<SolrInputDocument>();
		
		for(Document doc:docs){
		SolrInputDocument sdoc = new SolrInputDocument();
		sdoc.addField("id", doc.getId().toString());
		sdoc.addField("name", doc.getName());
		sdoc.addField("path", doc.getPath());
		sdoc.addField("extension", doc.getExtension());
		sdoc.addField("nameWithExtension", doc.getNameWithExtension());
		sdoc.addField("createdAt", doc.getCreatedAt());
		sdoc.addField("updatedAt", doc.getUpdatedAt());
		sdocs.add(sdoc);
		}
		return sdocs;
	}
	
}
