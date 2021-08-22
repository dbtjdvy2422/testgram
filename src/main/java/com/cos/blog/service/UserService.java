package com.cos.blog.service;



import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//스프링이 컴포넌트 스캔을 통해서 bean 에 들록을 해줌 .IOC를 해준다.
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.dto.UserProfileDto;
import com.cos.blog.handler.CustomApiException;
import com.cos.blog.handler.CustomException;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.SubscribeRepository;
import com.cos.blog.repository.UserRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final SubscribeRepository subscribeRepository;
	
	
	
	@Value("${file.path}")
	private String uploadFolder;
	
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
			return user;
		
	}
	
	@Transactional
	public User 회원가입 (User user) throws RuntimeException {
		String rawPassword = user.getPassword(); // 1234원문
		String encPassword = encoder.encode(rawPassword); //해쉬가 됨
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);// 관리자 ROLE_ADMIN
		User userEntity = null;
		userEntity = userRepository.save(user);
		return userEntity;
	}
	
	@Transactional
	public void 회원수정(User user) {
		//수정시에는 영속성 컨텍스트 USER오브젝트를 영속화 시키고 영속화되 user 오브젝트를 수정
		//select를 해서 USER 오브젝트를 DB로 부터 자겨오는 이유는 영속화를 하기 위해
		//영속화된 오브젝트를 변경하면 자동으로 DB에 UPDATE문을 날려줌
		User persistance = userRepository.findById(user.getId()).orElseThrow(()->{
			return new IllegalArgumentException("회원 찾기 실패");
		});
		
		//Validate 체크 -> oauth값이 없으면 수정 가능
		if(persistance.getOauth()==null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);
			persistance.setEmail(user.getEmail());
		}	
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto 회원프로필(int pageUserId, int principalId) {
		UserProfileDto dto = new UserProfileDto(); 
		
		// SELECT * FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).<CustomException>orElseThrow(()-> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState =  subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		// 좋아요 카운트 추가하기
		userEntity.getImages().forEach((image)->{
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); // uuid
		String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름 : "+imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		// 통신, I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).<CustomException>orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	} // 더티체킹으로 업데이트 됨.
	
	//세션 등록
	/*
	 * @Transactional(readOnly =true) //select 할 때 트랜잭션 시작 , 서비스 종료시에 트랜잭션 종료(정합성)
	 * public User 로그인(User user) { return
	 * userRepository.findByUsernameAndPassword(user.getUsername(),
	 * user.getPassword()); }
	 */
}
