package wortk.my.portfolio.spring_jms_client.repository.impl;

import wortk.my.portfolio.spring_jms_client.model.VideoModel;

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
