package wortk.my.portfolio.spring_jms_client.model;

import java.time.LocalDateTime;

import org.thymeleaf.util.StringUtils;

/**
 * ダウンロード画面用メタモデル
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
public record DownloadViewMetaData(String viewName, String fileName, LocalDateTime expiredDateTime) {

	public boolean isExpired() {
		return StringUtils.isEmpty(fileName()) || expiredDateTime() == null;
	}
}
