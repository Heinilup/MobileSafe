package ikabi.com.mobilesafe.business.model;


import ikabi.com.mobilesafe.utils.FileEvent;

/**
 * 传输文件实体
 */
public class TFileInfo implements Comparable<TFileInfo> {

    /*
    required string name = 1;
    required string md5 = 2;
    required bytes data = 3;
    required int64 position = 4;
    required int64 length = 5;
    required string path = 6;
    required string extension = 7;
    required string full_name = 8;
    required string task_id = 9;
    */


    /**
     * 文件名称
     */
    private String name;
    /**
     * 传输位置
     */
    private long position;
    /**
     * 文件长度
     */
    private long length;
    /**
     * 路径
     */
    private String path;
    /**
     * 后缀名
     */
    private String extension;
    /**
     * 文件全名
     */
    private String fullName;
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 是否发送方,true发送方,false接收方
     */
    private boolean isSend;
    /**
     * 发送方
     */
    private String from;
    /**
     * 传输百分比
     */
    private long percent;
    /**
     * 传输状态
     */
    private FileEvent fileEvent = FileEvent.NONE;
    /*
    * 文件类型
     */
    private FileType fileType = FileType.Normal;
    /*
     *创建时间
     */
    private String createTime;

    /**
     * 播放时长
     */
    private int playTime;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 是否目录
     */
    private boolean directory = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getPercent() {
        return percent;
    }

    public void setPercent(long percent) {
        this.percent = percent;
    }

    public FileEvent getFileEvent() {
        return fileEvent;
    }

    public void setFileEvent(FileEvent fileEvent) {
        this.fileEvent = fileEvent;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int compareTo(TFileInfo fileInfo) {
        if (this.name != null)
            return this.name.compareTo(fileInfo.getName());
        else
            throw new IllegalArgumentException();
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
}
