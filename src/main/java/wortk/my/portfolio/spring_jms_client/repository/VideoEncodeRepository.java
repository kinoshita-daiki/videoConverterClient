package wortk.my.portfolio.spring_jms_client.repository;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;

/**
 * 動画変換repository
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
public interface VideoEncodeRepository {

	/**
	 * メールを送信する
	 * 
	 * @param fileName   動画ファイル名
	 * @param videoModel ビデオモデル
	 */
	void sendMessage(String fileName, VideoModel videoModel);

	/**
	 * ダウンロード画面用メタデータを取得する
	 * 
	 * @param fileName ファイル名
	 * @return ダウンロード画面用メタデータ
	 */
	DownloadViewMetaData getDownloadMetaData(String fileName);

	/**
	 * 動画を取得する
	 * 
	 * @param fileName ファイル名
	 * @return 動画
	 */
	ResponseEntity<Resource> downloadVideo(String fileName);
}
