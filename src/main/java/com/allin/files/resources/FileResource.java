package com.allin.files.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Component;

import com.allin.application.MediaStreamer;
import com.allin.files.model.Document;
import com.allin.files.model.SPath;
import com.allin.files.repository.SolrRepo;
import com.allin.files.solr.SolrService;

@Component
@Path("/")
public class FileResource {
	
	@Inject
	private SolrService solrService;
	
	@Inject
	private SolrRepo solrRepo;
	
	private final int chunk_size = 1024*1024;
	
	@GET
	@Path("/download/video/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getVideo(@PathParam("id") String id) throws SolrServerException, IOException{
		//File file = new File(doc.getPath());
		//System.out.println(doc.getPath()); 
		/*ResponseBuilder response = Response.ok().entity(file);
		response.header("Content-Disposition",  "filename="+doc.getName());
		return response.build();*/
		final Document doc = solrRepo.fetchById(id);
		 StreamingOutput fileStream =  new StreamingOutput() 
	        {
	            public void write(java.io.OutputStream output) throws IOException, WebApplicationException 
	            {
	                
	                    java.nio.file.Path path = Paths.get(doc.getPath());
	                    byte[] data = Files.readAllBytes(path);
	                    output.write(data);
	                    output.flush();
	                
	                
	            }
	        };
	        return Response
	                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
	                .header("content-disposition","filename = "+doc.getName())
	                .build();
    }
	
	@GET
	@Path("/play/video/{id}")
	@Produces("video/mp4")
	public Response playVideo(@PathParam("id") String id,@HeaderParam("Range") String range) throws SolrServerException, IOException{
		
		final Document doc = solrRepo.fetchById(id);
		final File asset = new File(doc.getPath());
		if (range == null) {
	        StreamingOutput streamer = new StreamingOutput() {
	            public void write(final OutputStream output) throws IOException, WebApplicationException {

	                final FileChannel inputChannel = new FileInputStream(asset).getChannel();
	                final WritableByteChannel outputChannel = Channels.newChannel(output);
	                try {
	                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
	                } finally {
	                    // closing the channels
	                    inputChannel.close();
	                    outputChannel.close();
	                }
	            }
	        };
	        
	        return Response.ok(streamer).header(HttpHeaders.CONTENT_LENGTH, asset.length()).build();
	    }
	
		    String[] ranges = range.split("=")[1].split("-");
		    final int from = Integer.parseInt(ranges[0]);
		    /**
		     * Chunk media if the range upper bound is unspecified. Chrome sends "bytes=0-"
		     */
		    int to = chunk_size + from;
		    if (to >= asset.length()) {
		        to = (int) (asset.length() - 1);
		    }
		    if (ranges.length == 2) {
		        to = Integer.parseInt(ranges[1]);
		    }
		    
		    final String responseRange = String.format("bytes %d-%d/%d", from, to, asset.length());
		    final RandomAccessFile raf = new RandomAccessFile(asset, "r");
		    raf.seek(from);
		    System.out.println("range from-"+from+" to-"+to +" date "+new Date().toString());
		    final int len = to - from + 1;
		    final MediaStreamer streamer = new MediaStreamer(len, raf);
		    Response.ResponseBuilder res = Response.ok(streamer).status(206)
		            .header("Accept-Ranges", "bytes")
		            .header("Content-Range", responseRange)
		            .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
		            .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));
	    return res.build();
	}
	
	@GET
	  @Path("/index")
	  @Produces(MediaType.TEXT_HTML )
	    public Response getIndexPage() {
		  	
	        return Response.ok().entity("Another Page").build();
	    }
	
	@GET
	@Path("/search/{query}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response search(@PathParam("query") String query){
		
		String response;
		try {
			response = solrService.getAll(query);
			return Response.ok().entity(response).build();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		return Response.serverError().build();
	}
	
	
}
