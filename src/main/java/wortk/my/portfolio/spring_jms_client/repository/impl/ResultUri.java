package wortk.my.portfolio.spring_jms_client.repository.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "convert.result.uri")
@Component
class ResultUri {

	ResultUri() {
	}

	private String view;

	private String download;

}
