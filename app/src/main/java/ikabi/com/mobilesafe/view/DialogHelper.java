package ikabi.com.mobilesafe.view;

import android.app.Activity;
import android.content.Context;

import ikabi.com.mobilesafe.business.model.TFileInfo;


/**
 * 对话框帮助
 */
public class DialogHelper {

    public static void showProperty(Context context, TFileInfo tFileInfo) {
        CustomDialog.PropertyBuilder builder = new CustomDialog.PropertyBuilder(context);
        builder.setTFileInfo(tFileInfo);
        builder.create().show();
    }

    public static void showMore(Activity activity, TFileInfo tFileInfo, CustomDialog.DialogCallback onListener) {
        CustomDialog.MoreBuilder builder = new CustomDialog.MoreBuilder(activity);
        builder.setTFileInfo(tFileInfo);
        builder.setOnClickListener(onListener);
        builder.create().show();
    }

    public static void showRename(Activity activity, TFileInfo tFileInfo, CustomDialog.DialogCallback onListener) {
        CustomDialog.RenameBuilder builder = new CustomDialog.RenameBuilder(activity);
        builder.setTFileInfo(tFileInfo);
        builder.setOnListener(onListener);
        builder.create().show();
    }

    public static void showDel(Activity activity, TFileInfo tFileInfo, CustomDialog.DialogCallback onListener) {
        CustomDialog.DelBuilder builder = new CustomDialog.DelBuilder(activity);
        builder.setTFileInfo(tFileInfo);
        builder.setOnListener(onListener);
        builder.create().show();
    }

}
