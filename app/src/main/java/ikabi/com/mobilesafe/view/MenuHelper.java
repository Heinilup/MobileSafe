package ikabi.com.mobilesafe.view;

import android.content.Context;
import android.view.View;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.FileEvent;

/**
 * 菜单帮助类
 */
public class MenuHelper {


    public static void showMenu(Context context, View anchorView, int itemPosition, TFileInfo tFileInfo, PopMenu.MenuCallback callback) {
        showMenu(context, anchorView, itemPosition, -1, tFileInfo, callback);
    }

    public static void showMenu(Context context, View anchorView, int itemPosition, int groupPosition, TFileInfo tFileInfo, PopMenu.MenuCallback callback) {

        PopMenu menu = new PopMenu(context);
        menu.setItemPosition(itemPosition);
        menu.setGroupPosition(groupPosition);
        menu.setTFileInfo(tFileInfo);
        menu.setOnItemSelectedListener(callback);
        if (tFileInfo.getFileEvent() == FileEvent.NONE) {
            // 本地普通文件
            menu.add(R.id.menu_transfer, R.string.transfer).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_send));
            menu.add(R.id.menu_open, R.string.open).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_open));
            menu.add(R.id.menu_more, R.string.more).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_more));
        } else {
            // 传输文件
            switch (tFileInfo.getFileEvent()) {
                case SET_FILE_SUCCESS:
                    // 文件传输成功:打开,删除,属性
                    menu.add(R.id.menu_open, R.string.open).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_open));
                    menu.add(R.id.menu_delete, R.string.delete).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_delete));
                    menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    break;
                case SET_FILE:
                    if (tFileInfo.isSend()) {
                        // 正在发送文件:取消,打开,属性
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_open, R.string.open).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_open));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    } else {
                        // 正在接收文件:暂停,取消,属性
                        menu.add(R.id.menu_stop, R.string.stop).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_pause));
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    }
                    break;
                case CHECK_TASK_SUCCESS:
                case CREATE_FILE_SUCCESS:
                case WAITING:
                    if (tFileInfo.isSend()) {
                        // 等待发送文件:取消,打开,属性
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_open, R.string.open).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_open));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    } else {
                        // 等待接收文件:取消,属性
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    }
                    break;
                case SET_FILE_STOP:
                    if (tFileInfo.isSend()) {
                        // 暂停发送文件:取消,打开,属性
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_open, R.string.open).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_open));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    } else {
                        // 暂停接收文件:继续,取消,属性
                        menu.add(R.id.menu_resume, R.string.resume).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_continue));
                        menu.add(R.id.menu_cancel, R.string.cancel).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_cancel));
                        menu.add(R.id.menu_property, R.string.property).setIcon(context.getResources().getDrawable(R.mipmap.data_downmenu_detail));
                    }
                    break;


            }

        }

        menu.show(anchorView);
    }

}
