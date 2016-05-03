package ikabi.com.mobilesafe.business.model;

import android.graphics.drawable.Drawable;

public class FileInfo implements Comparable<FileInfo> {

    /**
     * 文件名
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 图标
     */
    private Drawable icon;
    /**
     * 文件或文件夹
     */
    private boolean directory;
    /**
     * 文件类型
     */
    private FileType type;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 描述
     */
    private String description;

    /**
     * 作者
     */
    private String creator;

    /**
     * 大小3M
     */
    private String space;

    /**
     * 总时长:秒数
     */
    private int totalTime;
    /**
     * 总时长:字符
     */
    private String totalTimeStr;

    /*
     * 文件MD5值
     */
    private String md5;

    /*
     * 文件定位
     */
    private long postion;

    public long getFileLength() {
        return size;
    }

    public void setFileLength(long fileLength) {
        this.size = fileLength;
    }

    public String getFileLengthDesc() {
        return description;
    }

    public void setFileLengthDesc(String fileLengthDesc) {
        this.description = fileLengthDesc;
    }

    public FileType getFileType() {
        return type;
    }

    public void setFileType(FileType fileType) {
        this.type = fileType;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean isDirectory) {
        this.directory = isDirectory;
    }

    // 是否选中
    private boolean selectable;

    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable fileIcon) {
        this.icon = fileIcon;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String filePath) {
        this.path = filePath;
    }

    @Override
    public int compareTo(FileInfo vo) {
        if (this.name != null)
            return this.name.compareTo(vo.getName());
        else
            throw new IllegalArgumentException();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTimeStr() {
        return totalTimeStr;
    }

    public void setTotalTimeStr(String totalTimeStr) {
        this.totalTimeStr = totalTimeStr;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getPostion() {
        return postion;
    }

    public void setPostion(long postion) {
        this.postion = postion;
    }


    public enum FileType {
        Folder, Normal, Audio, Video, Image, Apk
    }

}
