package com.cos.blog.dto;


import com.cos.blog.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	private boolean pageOwnerState;
	private int imageCount;
	private boolean subscribeState;
	private int subscribeCount;
	private User user;
}
