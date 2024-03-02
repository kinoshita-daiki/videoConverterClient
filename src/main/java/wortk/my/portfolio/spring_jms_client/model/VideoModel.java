package wortk.my.portfolio.spring_jms_client.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.Data;

/**
 * ビデオモデル
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
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
