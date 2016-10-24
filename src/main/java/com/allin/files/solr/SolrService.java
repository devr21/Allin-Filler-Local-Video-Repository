package com.allin.files.solr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.allin.files.model.Document;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
@Configuration
public class SolrService {

	private static final String SOLR_URL = "http://localhost:8983/solr/allinfiles/select?q=";
	private static ObjectMapper mapper = new ObjectMapper();
	public Document getById(String id) throws IOException{
		
		/*String response = getAll(id);
		
		SolrResponse res = */
		
		return null;
	}
	
	public String getAll(String query) throws IOException{
		
		URL url = new URL(SOLR_URL+query.replace(" ", "%20"));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
}
