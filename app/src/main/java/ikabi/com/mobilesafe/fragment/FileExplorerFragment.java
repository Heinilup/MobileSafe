package ikabi.com.mobilesafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.business.model.Catalog;
import ikabi.com.mobilesafe.utils.FileUtils;
import ikabi.com.mobilesafe.view.ExplorerControl;

;

/**
 * 文件浏览
 */
public class FileExplorerFragment extends Fragment {

    ExplorerControl explorerControl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fileexplorer, null);
        init(view);
        return view;
    }

    void init(View view) {
        List<Catalog> list = new ArrayList<>();
        Catalog rootCatalog = new Catalog();
        rootCatalog.setImage(R.mipmap.data_folder_memory_default);
        rootCatalog.setName(this.getString(R.string.root_memory_storage));
        rootCatalog.setPath(File.separator);
        list.add(rootCatalog);
        if (FileUtils.ExistSDCard()) {
            Catalog inSdCard = new Catalog();
            inSdCard.setImage(R.mipmap.data_folder_sdcard_default);
            inSdCard.setName(this.getString(R.string.root_sdcard_storage));
            inSdCard.setPath(FileUtils.getStorageCardPath());
            list.add(inSdCard);
        }
        List<String> outSdCardPaths = FileUtils.getSdCardPaths();
        for (String string : outSdCardPaths) {
            Catalog outSdCard = new Catalog();
            outSdCard.setImage(R.mipmap.data_folder_sdcard_default);
            outSdCard.setName(this.getString(R.string.root_sdcard_storage_exten));
            outSdCard.setPath(string);
            list.add(outSdCard);
        }
        explorerControl = (ExplorerControl) view.findViewById(R.id.explorer);
        explorerControl.setActivity(getActivity());
        explorerControl.setCatalog(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        explorerControl.onDestroy();
    }
}