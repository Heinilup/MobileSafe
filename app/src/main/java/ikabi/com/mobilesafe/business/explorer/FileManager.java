package ikabi.com.mobilesafe.business.explorer;

import android.content.Context;

import com.huangjiang.business.model.FileInfo;
import com.huangjiang.business.model.FileInfo.FileType;
import com.huangjiang.filetransfer.R;
import com.huangjiang.utils.XFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	private Context mContext;

	public FileManager(Context context) {
		mContext = context;
	}

	public List<FileInfo> fillFiles(File[] list) {

		if (mContext == null) {
			throw new NullPointerException("Context Null");
		}
		if (list == null) {
			return new ArrayList<FileInfo>();
		}
		List<FileInfo> listVO = new ArrayList<FileInfo>();
		for (File file : list) {

			FileInfo vo = new FileInfo();
			vo.setName(file.getName());
			vo.setPath(file.getPath());
			vo.setDirectory(file.isDirectory());
			vo.setSelectable(false);

			String fileName = file.getName();
			
			if (file.isDirectory()) {
				vo.setFileType(FileType.Folder);
			} else {
				if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingImage))) {
					vo.setFileType(FileType.Image);
				} else if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingAudio))) {
					vo.setFileType(FileType.Audio);
				} else if (XFileUtils.checkEndsWithInStringArray(fileName, mContext.getResources().getStringArray(R.array.fileEndingVideo))) {
					vo.setFileType(FileType.Video);
				} else {
					vo.setFileType(FileType.Normal);
				}
			}
			listVO.add(vo);

		}
		return listVO;
	}
}
