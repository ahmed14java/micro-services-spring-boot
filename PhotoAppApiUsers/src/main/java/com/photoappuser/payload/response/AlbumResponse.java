package com.photoappuser.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse {

	private String albumId;
	private String userId;
	private String name;
	private String describtion;
}
