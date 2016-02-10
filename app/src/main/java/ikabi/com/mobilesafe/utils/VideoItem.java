package ikabi.com.mobilesafe.utils;

import java.io.Serializable;

public class VideoItem implements Serializable{
	
	//标题
	private String title;
	//时长
	private String duration;
	//文件大小
	private long size;
	//播发地址
	private String data;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "VideoItem [title=" + title + ", duration=" + duration
				+ ", size=" + size + ", data=" + data + "]";
	} 

}
