package wortk.my.portfolio.spring_jms_client.repository.impl;

import wortk.my.portfolio.spring_jms_client.model.VideoModel;

/**
 * メッセージング用データモデル
 * 
 * @author kinoshita daiki
 * @since 2024/03/02
 */
record VideoMessage(
		String originalFilename,
		String fileName,
		Integer startMinutes,
		Integer startSeconds,
		Integer endMinutes,
		Integer endSeconds,
		String email) {

	static VideoMessage of(String fileName, VideoModel model) {
		return new VideoMessage(model.getVideo().getOriginalFilename(), fileName, //
				model.getStartMinutes(), model.getStartSeconds(), model.getEndMinutes(), model.getEndSeconds(),
				model.getEmail());
	}
}
