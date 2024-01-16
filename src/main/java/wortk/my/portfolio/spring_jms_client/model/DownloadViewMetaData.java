package wortk.my.portfolio.spring_jms_client.model;

import java.time.LocalDateTime;

import org.thymeleaf.util.StringUtils;

public record DownloadViewMetaData(String viewName, String fileName, LocalDateTime expiredDateTime) {

	public boolean isExpired() {
		return StringUtils.isEmpty(fileName()) || expiredDateTime() == null;
	}
}
