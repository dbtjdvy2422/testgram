package com.cos.blog.controller;



import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ImageUploadDto;
import com.cos.blog.handler.CustomValidationException;
import com.cos.blog.model.Image;
import com.cos.blog.service.ImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {
	
	private final ImageService imageService;
	
	@GetMapping("/image/story")
	public String story() {
		return "image/story";
	}
	
	// API 구현한다면 - 이유 - (브라우저에서 요청하는게 아니라, 안드로이드,iOS 요청)
	@GetMapping("/image/popular")
	public String popular(Model model) {
		// api는 데이터를 리턴하는 서버!!
		List<Image> images = imageService.인기사진();
		model.addAttribute("images", images);
		
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetail principalDetail) throws Exception {

		// 깍둑이
		if(imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		
		imageService.사진업로드(imageUploadDto, principalDetail);
		return "redirect:/user/"+principalDetail.getUser().getId();
	}
}




