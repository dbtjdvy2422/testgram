package com.cos.blog.dto;

import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.model.Image;
import com.cos.blog.model.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	
	private MultipartFile file;
	private String caption;
	private String ffmpegPath;
	
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user)
				.build();
	}
	
	public Image toffmpegEntity(String postImageUrl,String ffmpegPath, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.ffmpegPath(ffmpegPath)
				.user(user)
				.build();
	}
	
}
