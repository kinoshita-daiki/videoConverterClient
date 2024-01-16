package wortk.my.portfolio.spring_jms_client.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import wortk.my.portfolio.spring_jms_client.model.VideoModel;

public class VideoModelValidator implements ConstraintValidator<ProperVideoModel, VideoModel> {

	@Value("${upload.file.directory}")
	private String videoDirectory;

	@Value("${path.to.ff.probe}")
	private String ffprobePath;

	@Value("${accept.file.size.mega}")
	private String acceptFileSize;

	@Override
	public boolean isValid(VideoModel model, ConstraintValidatorContext context) {
		if (model == null) {
			return true;
		}
		if (model.getVideo() == null || model.getVideo().isEmpty() || model.getVideo().getSize() == 0L) {
			return false;
		}
		long dataSize = DataSize.of(model.getVideo().getSize(), DataUnit.BYTES).toMegabytes();
		if (Long.parseLong(acceptFileSize) < dataSize) {
			return false;
		}
		boolean valid = false;
		try {
			Path tempPath = Files.createTempFile(Path.of(videoDirectory), "video_validation_", null);
			long duration = getDuration(model, tempPath);
			valid = isProperClipping(model, duration);
			Files.deleteIfExists(tempPath);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return valid;
	}

	private boolean isProperClipping(VideoModel model, long videoDuration) {
		long start = toSeconds(model.getStartMinutes(), model.getStartSeconds());
		long end = toSeconds(model.getEndMinutes(), model.getEndSeconds());
		Range<Long> videoRange = Range.of(0L, videoDuration);
		return start < end && videoRange.contains(start) && videoRange.contains(end);
	}

	private long toSeconds(int minutes, int seconds) {
		return minutes * 60L + seconds;
	}

	private long getDuration(VideoModel model, Path tempPath) throws IOException {
		FileUtils.copyInputStreamToFile(model.getVideo().getInputStream(), tempPath.toFile());
		FFprobe ffprobe = new FFprobe(ffprobePath);
		FFmpegProbeResult probeResult = ffprobe.probe(tempPath.toString());
		return (long) probeResult.getFormat().duration;
	}
}
