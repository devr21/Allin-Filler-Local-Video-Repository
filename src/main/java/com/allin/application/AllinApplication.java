package com.allin.application;

import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;

import com.allin.files.resources.FileResource;

public class AllinApplication extends ResourceConfig{

	public AllinApplication(){
		packages("com.allin.files.resources");
		register(FileResource.class);
		register(EntityFilteringFeature.class);
		EncodingFilter.enableFor(this, GZipEncoder.class);
	}
	
}
