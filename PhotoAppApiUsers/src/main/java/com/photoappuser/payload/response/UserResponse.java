package com.photoappuser.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	
	private Long userId;
	private String name;
	private String username;
	private String email;
	private List<AlbumResponse> albums;
}
