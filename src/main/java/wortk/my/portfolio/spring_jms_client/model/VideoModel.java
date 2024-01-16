package wortk.my.portfolio.spring_jms_client.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class VideoModel {

	@Max(59)
	private Integer startMinutes;

	@Max(59)
	private Integer startSeconds;

	@Max(59)
	private Integer endMinutes;

	@Max(59)
	private Integer endSeconds;

	private MultipartFile video;

	@Email
	private String email;
}
