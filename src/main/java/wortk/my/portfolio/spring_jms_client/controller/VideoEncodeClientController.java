package wortk.my.portfolio.spring_jms_client.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import lombok.extern.log4j.Log4j2;
import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;
import wortk.my.portfolio.spring_jms_client.service.ProperVideoModel;
import wortk.my.portfolio.spring_jms_client.service.VideoEncodeService;

/**
 * 動画変換コントローラ(クライアント側)
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
@Log4j2
@Controller
public class VideoEncodeClientController {

	private final VideoEncodeService service;

	private final String videoDirectory;

	private final Optional<String> path;

	VideoEncodeClientController(@Autowired VideoEncodeService service,
			@Value("${upload.file.directory}") String videoDirectory,
			@Value("${my.client.path:#{null}}") Optional<String> path) {
		this.service = service;
		this.videoDirectory = videoDirectory;
		this.path = path;
	}

	/**
	 * リクエスト受付後の画面を表示する
	 * 
	 * @param videoModel ビデオモデル
	 * @param model      モデル
	 * @return 画面
	 * @throws IOException 変換処理失敗時
	 */
	@PostMapping("/videoEncoderOutput")
	public String displayOutputView(@Validated @ProperVideoModel VideoModel videoModel, Model model)
			throws IOException {
		service.convertVideo(videoModel);
		model.addAttribute(videoModel);
		return "videoEncoderOutput";
	}

	/**
	 * 入力画面をリダイレクトで表示する
	 * 
	 * @return 入力画面
	 */
	@GetMapping("/videoEncoderOutput")
	public String displayOutputViewForGet() {
		String redirect = path.map(p -> "redirect:/" + p)//
				.orElseGet(() -> "redirect:/");
		return redirect + "videoEncoderInput";
	}

	/**
	 * 入力画面を表示する
	 * 
	 * @return 入力画面
	 */
	@GetMapping("/videoEncoderInput")
	public String displayInputView(Model model) {
		model.addAttribute(new VideoModel());
		return "videoEncoderInput";
	}

	/**
	 * アップロードされたファイルを取得する
	 * 
	 * @param videoName 動画名
	 * @return アップロードされたファイルを含むレスポンス
	 */
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
			log.error("read uploadfile error", e);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * アップロードされた動画を削除する
	 * 
	 * @param videoName 動画名
	 * @throws IOException 削除失敗時
	 */
	@ResponseBody
	@DeleteMapping("/uploadFile/{videoName}")
	public void delete(@PathVariable String videoName) throws IOException {
		if (!StringUtils.hasText(videoName)) {
			return;
		}
		service.deleteVideo(videoName);
	}

	/**
	 * 動画ダウンロード画面を表示する
	 * 
	 * @param fileName ファイル名
	 * @param model    モデル
	 * @return 動画ダウンロード画面
	 */
	@GetMapping("/videoDownloadView")
	public String getVideoDownloadView(@RequestParam String fileName, Model model) {
		DownloadViewMetaData downloadViewMeataModel = service.getDownloadMetaData(fileName);
		String outputFileName = downloadViewMeataModel.fileName();
		if (downloadViewMeataModel.isExpired()) {
			return "expiredView";
		}
		model.addAttribute("fileName", outputFileName);
		model.addAttribute("expiredDateTime", downloadViewMeataModel.expiredDateTime());
		return downloadViewMeataModel.viewName();
	}

	/**
	 * 動画をダウンロードする
	 * 
	 * @param fileName ファイル名
	 * @return 動画
	 */
	@ResponseBody
	@GetMapping("/videoDownload")
	public ResponseEntity<Resource> downloadVideo(@RequestParam String fileName) {
		return service.downloadVideo(fileName);
	}

	@ResponseBody
	@GetMapping("/videoEncoderInput/test")
	public ResponseEntity<Resource> downloadTestVideo() {
		File testVideoFile = new File("src/main/resources/video/test.mp4");
		if (!testVideoFile.exists()) {
			return ResponseEntity.notFound().build();
		}
		Path testFilePath = testVideoFile.toPath();
		Resource resource = new PathResource(testFilePath);
		log.info("resources near");
		return ResponseEntity.ok()
				.contentType(getContentType(testFilePath))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
				.body(resource);
	}

	private MediaType getContentType(Path path) {
		try {
			return MediaType.parseMediaType(Files.probeContentType(path));
		} catch (IOException e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	/**
	 * アップロード時のバリデーション用のエラーページを表示する
	 * 
	 * @param model モデル
	 * @return エラーページ
	 */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public String displayValidationErrorPage(Model model) {
		model.addAttribute(new VideoModel());
		model.addAttribute("errorMessage", "適用できない変換条件が入力されています。選択したファイル、または切り抜き時間を確認してください。");
		return "videoEncoderInput";
	}

	/**
	 * 
	 * @return 汎用エラーページ
	 */
	@ExceptionHandler(Exception.class)
	public String displayCommonErrorPage() {
		return "commonErrorPage";
	}
}
