package com.photoappuser.services;

import com.photoappuser.dto.UserDto;
import com.photoappuser.jwt.JwtProvider;
import com.photoappuser.model.Role;
import com.photoappuser.model.RoleName;
import com.photoappuser.model.User;
import com.photoappuser.payload.response.AlbumResponse;
import com.photoappuser.repository.RoleRepository;
import com.photoappuser.repository.UserRepository;

import feign.FeignException;

import org.hibernate.PropertyNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService , UserDetailsService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private AlbumServiceClient albumServiceClient;

    @Override
    public UserDto createUser(UserDto userDto) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDto , User.class);
        // Creating user's account
//        user.setEmailVerificationToken(jwtProvider.generateVerificationToken(user.getUsername()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setEmailVerificationStatus(false);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new PropertyNotFoundException("User Role not found!"));
        roles.add(userRole);
        user.setRoles(roles);
        User returnUserSaved = userRepository.save(user);
        UserDto returnDto = modelMapper.map(returnUserSaved , UserDto.class);

        return returnDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

	@Override
	public UserDto getUserByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found!!"));
		UserDto userDto = new ModelMapper().map(user, UserDto.class);
		/*
		String albumsUrl = String.format(env.getProperty("albums.url"), userId);
		ResponseEntity<List<AlbumResponse>> albumListResponse = 
				restTemplate.exchange( albumsUrl , HttpMethod.GET , null , new ParameterizedTypeReference<List<AlbumResponse>>() {
		});
		List<AlbumResponse> albumList = albumListResponse.getBody();
		*/
		LOG.info("Before calling albums Microservice");
		try {
			List<AlbumResponse> albumList = albumServiceClient.getAlbums(String.valueOf(userId));
			userDto.setAlbums(albumList);
		} catch (FeignException e) {
			e.printStackTrace();
		}
		LOG.info("After calling albums Microservice");
		return userDto;
	}
}
