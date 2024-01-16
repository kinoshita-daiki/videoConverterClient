package wortk.my.portfolio.spring_jms_client.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;

public interface VideoEncodeService {

	void convertVideo(VideoModel model) throws IOException;

	DownloadViewMetaData getDownloadMetaData(String fileName);

	ResponseEntity<Resource> downloadVideo(String fileName);

	void deleteVideo(String videoName) throws IOException;
}
