package wortk.my.portfolio.spring_jms_client.repository;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;

public interface VideoEncodeRepository {

	void sendMessage(String fileName, VideoModel videoModel);

	DownloadViewMetaData getDownloadMetaData(String fileName);

	ResponseEntity<Resource> downloadVideo(String fileName);
}
