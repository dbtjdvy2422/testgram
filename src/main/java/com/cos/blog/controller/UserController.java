package com.cos.blog.controller;



import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.CMRespDto;
import com.cos.blog.dto.SignupDto;
import com.cos.blog.dto.SubscribeDto;
import com.cos.blog.dto.UserProfileDto;
import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;

import com.cos.blog.model.User;

import com.cos.blog.service.BoardService;
import com.cos.blog.service.SubscribeService;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

//인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
//그냥
@RequiredArgsConstructor
@Controller
public class UserController {
 
	@Value("${cos.key}")
	private String cosKey;
	
	private final BoardService boardService;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final SubscribeService subscribeService;
	
	@GetMapping({"/", "/image/imageSearch"})
	public String Allsearch() {
		return "/image/imageSearch";
	}
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetail principalDetail) {
		UserProfileDto dto = userService.회원프로필(pageUserId, principalDetail.getUser().getId());
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("board/{id}")
	public String findById(Model model,  @PathVariable int id) {
		
		model.addAttribute("board",boardService.상세보기(id));
		return "board/detail";
		
	}
	
	
	@GetMapping("/auth/joinForm")
	public String join() {
	return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String login() {
	return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
	return "user/updateForm";
	}
	
	
	@PostMapping("/auth/joinProc")
	public String save(@Valid SignupDto signupDto, BindingResult bindingResult) {
		System.out.println("UserApiController :save 호줄됨");
		User user = signupDto.toEntity();
		userService.회원가입(user);
		return "user/loginForm";
	}
	  
	@GetMapping("/user/{id}/update")
	public String updateForm(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principalDetail) {
	
		return "user/update";
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe")
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetail principalDetail){
		
		List<SubscribeDto> subscribeDto = subscribeService.구독리스트(principalDetail.getUser().getId(), pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}
	
	@GetMapping("auth/kakao/callback")
	public String kakaoCallback(String code) { //data를 리턴해주는 컨트롤러 함수
		//Post방식으로 key=value 데이터를 요청(카카오쪽으로)
		//Retrofit2
		//OkHttp
		//RestTemplate
		
		RestTemplate rt = new RestTemplate();
		
		//HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
	
		//httpBody 오브젝트 생성
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("grant_type","authorization_code");
		params.add("client_id","57ea8b244103264c206819e7f856ddcc");
		params.add("redirect_uri","https://blogram.site/auth/kakao/callback");
		params.add("code",code);
		
		//httpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = 
				new HttpEntity<>(params,headers);
		
		//Http 요청하기 = post방식으로 - 그리고 reponse변수의 응답받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);
		
		//Gson, Json, Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println(" 카카오 엑세스 토큰: " + oauthToken.getAccess_token());
		
		
	RestTemplate rt2 = new RestTemplate();
		
		//HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
	
		
		//httpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest2 = 
				new HttpEntity<>(headers2);
		
		//Http 요청하기 - post방식으로 - 그리고 reponse변수의 응답받음
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
				);
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 아이디(번호): "+kakaoProfile.getId());
		System.out.println("카카오 이메일: "+kakaoProfile.getKakao_account().getEmail());
		
		System.out.println("블로그 유저네임: "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
		System.out.println("블로그 서버네임: "+kakaoProfile.getKakao_account().getEmail());
		UUID garbagePassword = UUID.randomUUID();
		System.out.println("블로그 패스워드: " +cosKey);
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
				.password(cosKey)
				.email(kakaoProfile.getKakao_account().getEmail())
				.oauth("kakao")
				.build();
		
		//가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername());
		
		if(originUser.getUsername()==null) {
			System.out.println("기존 회원이 아니기에 회원가입을 진행합니다.");
			userService.회원가입(kakaoUser);
		}
		
		//로그인처리

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(),cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/"; 
		
		
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board",boardService.상세보기(id));
		return "board/updateForm";
		
	} 
	

	
	
}