package com.cos.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.handler.CustomApiException;
import com.cos.blog.handler.CustomException;
import com.cos.blog.model.Comment;
import com.cos.blog.model.Image;
import com.cos.blog.model.User;
import com.cos.blog.repository.CommentRepository;
import com.cos.blog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		
		// Tip (객체를 만들 때 id값만 담아서 insert 할 수 있다)
		// 대신 return 시에 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).<CustomException>orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}