package com.photoappuser.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.photoappuser.payload.response.AlbumResponse;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "albums-ws" , fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumServiceClient {

	@GetMapping("/users/{id}/albums")
	public List<AlbumResponse> getAlbums(@PathVariable String id);
}

@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumServiceClient>{

	@Override
	public AlbumServiceClient create(Throwable cause) {
		
		return new AlbumServiceClientFallback(cause);
	}
}

class AlbumServiceClientFallback implements AlbumServiceClient {
	
	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final Throwable cause;
	
	public AlbumServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public List<AlbumResponse> getAlbums(String id) {
		if(cause instanceof FeignException && ((FeignException) cause).status() == 404 ) {
			LOG.error("404 error took place when get Albums was called with userId: " + id + ". Error Message: " + cause.getLocalizedMessage());
		}else {
			LOG.error("Other error took places: " + cause.getLocalizedMessage());
		}
		return new ArrayList<>();
	}
	
}

