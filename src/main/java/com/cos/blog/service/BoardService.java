package com.cos.blog.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//스프링이 컴포넌트 스캔을 통해서 bean 에 들록을 해줌 .IOC를 해준다.
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.Board;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.UserRepository;
@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;
	
	
	@Transactional
	public void 글쓰기(Board board, User user) { //title,content
		board.setCount(0);
		board.setUser(user);
	boardRepository.save(board);

	}
	
	/*
	 * @Transactional(readOnly =true) //select 할 때 트랜잭션 시작 , 서비스 종료시에 트랜잭션 종료(정합성)
	 * public User 로그인(User user) { return
	 * userRepository.findByUsernameAndPassword(user.getUsername(),
	 * user.getPassword()); }
	 */
	@Transactional(readOnly =true)
	public Page<Board> 글목록(Pageable pageable){
		
		return boardRepository.findAll(pageable);
	}
	
	
	@Transactional(readOnly =true)
	public Board 상세보기(int id) {
		return boardRepository.findById(id)
		.orElseThrow(()->{
			return new IllegalArgumentException("글 상세보기 실패");
		});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
		
	}
	
	
	public void 글수정하기(int id, Board requestboard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패");
				});//영속화 완료
		board.setTitle(requestboard.getTitle());
		board.setContent(requestboard.getContent());
		//해당 함수로 종료시(Service가 종료될 때 ) 트랜잭션이 종료됩니다. 이때 더티체킹 -자동 업데이트가 됨 db flush
	}
	
	
	
}
