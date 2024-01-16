package wortk.my.portfolio.spring_jms_client.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;
import wortk.my.portfolio.spring_jms_client.service.ProperVideoModel;
import wortk.my.portfolio.spring_jms_client.service.VideoEncodeService;

@Controller
public class VideoEncodeClientController {

	private final VideoEncodeService service;

	private final String videoDirectory;

	VideoEncodeClientController(@Autowired VideoEncodeService service,
			@Value("${upload.file.directory}") String videoDirectory) {
		this.service = service;
		this.videoDirectory = videoDirectory;
	}

	@PostMapping("/videoEncoderOutput")
	public String displayOutputView(@Validated @ProperVideoModel VideoModel videoModel, Model model)
			throws IOException {
		service.convertVideo(videoModel);
		model.addAttribute(videoModel);
		return "videoEncoderOutput";
	}

	@GetMapping("/videoEncoderOutput")
	public String displayOutputViewForGet() {
		return "redirect:/videoEncoderInput";
	}

	@GetMapping("/videoEncoderInput")
	public String displayInputView(Model model) {
		model.addAttribute(new VideoModel());
		return "videoEncoderInput";
	}

	@ResponseBody
	@GetMapping("/uploadFile/{videoName}")
	public ResponseEntity<byte[]> getUploadFile(@PathVariable String videoName) {
		File uploadedFile = new File(videoDirectory + videoName);
		if (!uploadedFile.exists()) {
			return ResponseEntity.notFound().build();
		}
		try {
			return ResponseEntity.ok()//
					.header("Content-Type", "video/mp4")//
					.body(Files.readAllBytes(uploadedFile.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}

	@ResponseBody
	@DeleteMapping("/uploadFile/{videoName}")
	public void delete(@PathVariable String videoName) throws IOException {
		if (!StringUtils.hasText(videoName)) {
			return;
		}
		service.deleteVideo(videoName);
	}

	@GetMapping("/videoDownloadView")
	public String getVideoDownloadView(@RequestParam String fileName, Model model) {
		// PathVariableではリソースの参照がずれ、css等がうまく読み込めなくなる。リクエストパラメータを使う
		DownloadViewMetaData downloadViewMeataModel = service.getDownloadMetaData(fileName);
		String outputFileName = downloadViewMeataModel.fileName();
		if (downloadViewMeataModel.isExpired()) {
			return "expiredView";
		}
		model.addAttribute("fileName", outputFileName);
		model.addAttribute("expiredDateTime", downloadViewMeataModel.expiredDateTime());
		return downloadViewMeataModel.viewName();
	}

	@ResponseBody
	@GetMapping("/videoDownload")
	public ResponseEntity<Resource> downloadVideo(@RequestParam String fileName) {
		return service.downloadVideo(fileName);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public String displayValidationErrorPage(Model model) {
		model.addAttribute(new VideoModel());
		model.addAttribute("errorMessage", "適用できない変換条件が入力されています。選択したファイル、または切り抜き時間を確認してください。");
		return "videoEncoderInput";
	}

	@ExceptionHandler(Exception.class)
	public String displayCommonErrorPage() {
		return "commonErrorPage";
	}
}
