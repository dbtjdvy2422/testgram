package com.cos.blog.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.CMRespDto;
import com.cos.blog.model.Image;
import com.cos.blog.service.ImageService;
import com.cos.blog.service.LikesService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {
	
	private final ImageService imageService;
	private final LikesService likesService;
	
	@GetMapping("/api/imageSearch")
	public ResponseEntity<?> imageSearch(
	@RequestParam(value="keyword") String keyword,
	@PageableDefault(size=3) Pageable pageable){
		
		Page<Image> search =  imageService.page(pageable);
		if(keyword.equals("")|| keyword.equals(null)) {
			search =  imageService.page(pageable);
		}
		else {
			search = imageService.search(keyword, pageable);
		}
		return new ResponseEntity<>(new CMRespDto<>(1, "성공", search), HttpStatus.OK);
		
	}
	@GetMapping("/api/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetail principalDetail, 
			@PageableDefault(size=3) Pageable pageable){
		Page<Image> images =  imageService.이미지스토리(principalDetail.getUser().getId(), pageable);
		return new ResponseEntity<>(new CMRespDto<>(1, "성공", images), HttpStatus.OK);
	}
	
	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetail principalDetail){
		likesService.좋아요(imageId, principalDetail.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요성공", null), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetail principalDetail){
		likesService.좋아요취소(imageId, principalDetail.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요취소성공", null), HttpStatus.OK);
	}
}
