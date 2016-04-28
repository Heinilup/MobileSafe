package ikabi.com.mobilesafe.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.view.PictureBrowserControl;

/**
 * 图片资源
 */
public class PictureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.picture_fragment, null);
        PictureBrowserControl sgvPicture = (PictureBrowserControl) view.findViewById(R.id.sgv);
        sgvPicture.loadPicture();
        return view;
    }
}
