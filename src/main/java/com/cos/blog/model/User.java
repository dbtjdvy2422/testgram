package com.cos.blog.model;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA - Java Persistence API (자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity // 디비에 테이블을 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다.
	private int id;
	
	@Column(nullable = false,length =100)
	private String username; //아이디
	
	@Column(nullable = false, length =100) //123456 =>해쉬( 비밀번호 암호화)
	private String password;
	
	private String website; // 웹 사이트
	
	private String bio; // 자기 소개
	
	@Column(nullable = false, length =50)
	private String email;
	
	private String phone;
	
	private String gender;
	
	private String profileImageUrl; // 사진
	//@ColunmDefalut("'user'")
		//DB는 RoleType이라는게 업다. 그래서 @Enumerated(EnumType.STRING)으로 스트링값이라는걸 알려준다.
		@Enumerated(EnumType.STRING)
		private RoleType role; //enum을 쓰는게 좋다.//admin, user( enum을 쓰면 설정헤준  두 가지만 넣을 수 있다) 
		
	private String oauth; //kakao 로그인 하는 사람 
	 // 나는 연관관계의 주인이 아니다. 그러므로 테이블에 칼럼을 만들지마.
	// User를 Select할 때 해당 User id로 등록된 image들을 다 가져와
	// Lazy = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages() 함수의 image들이 호출될 때 가져와!!
	// Eager = User를 Select할 때 해당 User id로 등록된 image들을 전부 Join해서 가져와!!
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"user"})
	private List<Image> images; // 양방향 매핑
	

	private LocalDateTime createDate;
	
	
	@PrePersist // 디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role +", createDate="
				+ createDate + "]";
	}
	
	}


