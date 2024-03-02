package wortk.my.portfolio.spring_jms_client.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import wortk.my.portfolio.spring_jms_client.model.DownloadViewMetaData;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;

/**
 * 動画変換Service
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
public interface VideoEncodeService {

	/**
	 * 動画を変換する
	 * 
	 * @param model モデル
	 * @throws IOException 変換失敗時
	 */
	void convertVideo(VideoModel model) throws IOException;

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

	/**
	 * 動画を削除する
	 * 
	 * @param videoName 動画名
	 * @throws IOException 削除失敗時
	 */
	void deleteVideo(String videoName) throws IOException;
}
