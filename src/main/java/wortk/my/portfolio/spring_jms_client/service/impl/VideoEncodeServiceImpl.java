package wortk.my.portfolio.spring_jms_client.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;
import wortk.my.portfolio.spring_jms_client.repository.VideoEncodeRepository;
import wortk.my.portfolio.spring_jms_client.service.VideoEncodeService;

@Service
class VideoEncodeServiceImpl implements VideoEncodeService {

	private final VideoEncodeRepository repository;

	private final String videoDirectory;

	VideoEncodeServiceImpl(@Autowired VideoEncodeRepository repository,
			@Value("${upload.file.directory}") String videoDirectory) {
		this.repository = repository;
		this.videoDirectory = videoDirectory;
	}

	public void convertVideo(VideoModel model) throws IOException {
		String fileName = UUID.randomUUID().toString() + ".mp4";
		String filePath = videoDirectory + fileName;
		uploadVideo(filePath, model.getVideo().getInputStream());
		repository.sendMessage(fileName, model);
	}

	private void uploadVideo(String filePath, InputStream inputStream) throws IOException {
		File outputPath = new File(filePath);
		FileUtils.copyInputStreamToFile(inputStream, outputPath);
	}

	@Override
	public DownloadViewMetaData getDownloadMetaData(String fileName) {
		return repository.getDownloadMetaData(fileName);
	}

	@Override
	public ResponseEntity<Resource> downloadVideo(String fileName) {
		return repository.downloadVideo(fileName);
	}

	@Override
	public void deleteVideo(String videoName) throws IOException {
		Path filePath = Path.of(videoDirectory + videoName);
		Files.deleteIfExists(filePath);
	}

}
