package com.photoappuser.dto;

import com.photoappuser.model.Role;
import com.photoappuser.payload.response.AlbumResponse;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserDto implements Serializable{

    private static final long serialVersionUID = 1434247734708714636L;

    private Long id;

    private String name;

    private String username;

    private String email;

    private String password;

    private String emailVerificationToken;

    private Boolean emailVerificationStatus = false;

    private Set<Role> roles = new HashSet<>();
    
    private List<AlbumResponse> albums;
}
