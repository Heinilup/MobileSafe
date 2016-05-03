package ikabi.com.mobilesafe.business.model;

/**
 * @author jiang.huang
 * 
 */
public class VideoInfo implements Comparable<VideoInfo> {

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 创建时间
	 */

	private String createTime;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件大小
	 */
	private String fileSize;

	/**
	 * 播放时长
	 */
	private String totalTime;

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public int compareTo(VideoInfo vo) {
		if (this.createTime != null && vo != null)
			return vo.getCreateTime().compareTo(this.createTime);
		else
			throw new IllegalArgumentException();
	}

}
