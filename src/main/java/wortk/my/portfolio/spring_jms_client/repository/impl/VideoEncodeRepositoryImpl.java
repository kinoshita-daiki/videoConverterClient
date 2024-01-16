package wortk.my.portfolio.spring_jms_client.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;
import wortk.my.portfolio.spring_jms_client.repository.VideoEncodeRepository;

@Repository
class VideoEncodeRepositoryImpl implements VideoEncodeRepository {

	private final String destination;

	private final JmsTemplate jmsTemplate;

	private final String resultView;

	private final String resultDownload;

	@Autowired
	VideoEncodeRepositoryImpl(JmsTemplate jmsTemplate, @Value("${jms.destination}") String destination,
			ResultUri resultUri) {
		this.jmsTemplate = jmsTemplate;
		this.destination = destination;
		this.resultView = resultUri.getView();
		this.resultDownload = resultUri.getDownload();
	}

	@Override
	public void sendMessage(String fileName, VideoModel videoModel) {
		VideoMessage message = VideoMessage.of(fileName, videoModel);
		try {
			jmsTemplate.convertAndSend(destination, message);
		} catch (JmsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DownloadViewMetaData getDownloadMetaData(String fileName) {
		return new RestTemplate().getForObject(resultView,
				DownloadViewMetaData.class, fileName);
	}

	@Override
	public ResponseEntity<Resource> downloadVideo(String fileName) {
		return new RestTemplate().getForEntity(resultDownload, Resource.class, fileName);
	}
}