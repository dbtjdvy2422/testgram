package com.cos.blog.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ImageUploadDto;
import com.cos.blog.model.Image;
import com.cos.blog.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Transactional(readOnly = true)
    public Page<Image> page(Pageable pageable) {
		return imageRepository.findAll(pageable);
	 
	}
	
	@Transactional(readOnly = true)
    public Page<Image> search(String keyword, Pageable pageable) {
		return imageRepository.findByCaptionContaining(keyword,pageable);
	}
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(){
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true) // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(반영) X
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		// 2(cos) 로그인 
		// images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) -> {
				if(like.getUser().getId() == principalId) { // 해당 이미지에 좋아요한 사람들을 찾아서 현재 로긴한 사람이 좋아요 한것인지 비교
					image.setLikeState(true);
				}
			});
			
		});
		
		return images;
	}
	
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Value("${ffmpeg.path}")
	private String ffmpegFolder;
	
	@Value("${ffprobe.path}")
	private String ffprobeFolder;
	
	
	
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetail principalDetail) throws IOException {
		UUID uuid = UUID.randomUUID(); // uuid
		
		FFmpeg ffmpeg = new FFmpeg(ffmpegFolder);		// ffmpeg.exe 파일 경로
		FFprobe ffprobe = new FFprobe(ffprobeFolder);	// ffprobe.exe 파일 경로
		
		
		String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름 : "+imageFileName);
		
		
		
		// 통신, I/O -> 예외가 발생할 수 있다.
	
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		// 3. FFmpegBuilder를 통해 FFmpeg 명령어를 만들 수 있음
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		if(!imageFileName.contains("mp4")) {
		Image image = imageUploadDto.toEntity(imageFileName, principalDetail.getUser()); // 5cf6237d-c404-43e5-836b-e55413ed0e49_bag.jpeg
		imageRepository.save(image);
		
	}
		else {
			String ffmpegFileName =  uuid+"_"+imageUploadDto.getFile().getOriginalFilename();
			System.out.println("썸네일 파일이름 : "+ffmpegFileName);
			Path ffmpegFilePath = Paths.get(uploadFolder+ffmpegFileName);
			
			
			FFmpegBuilder builder = new FFmpegBuilder()	
					.overrideOutputFiles(true)					// output 파일을 덮어쓸 것인지 여부(false일 경우, output path에 해당 파일이 존재할 경우 예외 발생 - File 'C:/Users/Desktop/test.png' already exists. Exiting.)
	                .setInput(uploadFolder+imageFileName)     					// 썸네일 이미지 추출에 사용할 영상 파일의 절대 경로
	                .addExtraArgs("-ss", "00:00:01") 			// 영상에서 추출하고자 하는 시간 - 00:00:01은 1초를 의미 
	                .addOutput(uploadFolder+ffmpegFileName+".jpeg") 		// 저장 절대 경로(확장자 미 지정 시 예외 발생 - [NULL @ 000002cc1f9fa500] Unable to find a suitable output format for 'C:/Users/Desktop/test')
	                .setFrames(1)								
	                .done();    											
			try {
				Files.write(ffmpegFilePath, imageUploadDto.getFile().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
			executor.createJob(builder).run();									// one-pass encode
//	 		executor.createTwoPassJob(builder).run();	
			
			
			
			Image image = imageUploadDto.toffmpegEntity(imageFileName,ffmpegFileName+".jpeg", principalDetail.getUser()); //썸네일 경로 저장 
			imageRepository.save(image);
		}
}
}
