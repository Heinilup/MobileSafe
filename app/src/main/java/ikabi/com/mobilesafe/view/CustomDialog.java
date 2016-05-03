package ikabi.com.mobilesafe.view;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2016/5/3
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ikabi.com.mobilesafe.R;
import ikabi.com.mobilesafe.business.model.TFileInfo;
import ikabi.com.mobilesafe.utils.FileUtils;

/**
 * 文件属性
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 删除对话框
     */
    public static class DelBuilder {

        private Context context;
        private TFileInfo tFileInfo;
        private TextView txt_del_name;
        private Button btn_cancel, btn_ok;
        private DialogCallback onListener;

        public DelBuilder(Context context) {
            this.context = context;
        }

        public void setTFileInfo(TFileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        public void setOnListener(DialogCallback onListener) {
            this.onListener = onListener;
        }

        public CustomDialog create() {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_file_delete, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            txt_del_name = (TextView) layout.findViewById(R.id.txt_del_name);
            btn_cancel = (Button) layout.findViewById(R.id.dialog_del_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_ok = (Button) layout.findViewById(R.id.dialog_del_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (onListener != null) {
                        onListener.onDialogClick(v.getId(), tFileInfo);
                    }
                }
            });
            if (tFileInfo != null) {
                txt_del_name.setText(tFileInfo.getName());
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    /**
     * 重命名对话框
     */
    public static class RenameBuilder {

        private Context context;
        private TFileInfo tFileInfo;
        private EditText edt_file_name;
        private Button btn_cancel, btn_ok;
        private DialogCallback onListener;

        public RenameBuilder(Context context) {
            this.context = context;
        }

        public void setTFileInfo(TFileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        public void setOnListener(DialogCallback onListener) {
            this.onListener = onListener;
        }

        public CustomDialog create() {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_file_rename, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            edt_file_name = (EditText) layout.findViewById(R.id.edt_new_name);
            btn_cancel = (Button) layout.findViewById(R.id.dialog_rename_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_ok = (Button) layout.findViewById(R.id.dialog_rename_ok);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    String newName = edt_file_name.getText().toString();
                    if (onListener != null && !newName.equals(tFileInfo.getFullName())) {
                        onListener.onDialogClick(v.getId(), tFileInfo, newName);
                    }
                }
            });
            if (tFileInfo != null) {
                edt_file_name.setText(tFileInfo.getName());
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    /**
     * 属性对话框
     */
    public static class PropertyBuilder {

        private Context context;
        private TFileInfo tFileInfo;
        private TextView txt_name, txt_type, txt_path, txt_size, txt_edit_time;
        private String mime_audio, mime_video, mime_app, mime_normal, mime_image;

        public PropertyBuilder(Context context) {
            this.context = context;
            mime_audio = context.getString(R.string.mime_audio);
            mime_video = context.getString(R.string.mime_video);
            mime_image = context.getString(R.string.mime_image);
            mime_app = context.getString(R.string.mime_app);
            mime_normal = context.getString(R.string.mime_normal);
        }

        public void setTFileInfo(TFileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_file_property, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            txt_name = (TextView) layout.findViewById(R.id.txt_property_name);
            txt_type = (TextView) layout.findViewById(R.id.txt_property_type);
            txt_path = (TextView) layout.findViewById(R.id.txt_property_path);
            txt_size = (TextView) layout.findViewById(R.id.txt_property_size);
            txt_edit_time = (TextView) layout.findViewById(R.id.txt_property_edit_time);
            if (tFileInfo != null) {
                txt_name.setText(String.format(context.getString(R.string.property_name), tFileInfo.getName()));
                String mime_type = "";
                switch (tFileInfo.getFileType()) {
                    case Audio:
                        mime_type = mime_audio;
                        break;
                    case Video:
                        mime_type = mime_video;
                        break;
                    case Image:
                        mime_type = mime_image;
                        break;
                    case Apk:
                        mime_type = mime_app;
                        break;
                    case Normal:
                        mime_type = mime_normal;
                        break;
                }
                txt_type.setText(String.format(context.getString(R.string.property_type), mime_type));
                txt_path.setText(String.format(context.getString(R.string.property_path), tFileInfo.getPath()));
                txt_size.setText(String.format(context.getString(R.string.property_size), FileUtils.parseSize(tFileInfo.getLength())));
                txt_edit_time.setText(String.format(context.getString(R.string.property_edit_time), tFileInfo.getCreateTime()));


            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    /**
     * 更多对话框
     */
    public static class MoreBuilder {

        private Button btnDelete, btnRename, btnUninstall, btnBackup, btnProperty;
        private View lineDelete, lineRename, lineUninstall, lineBackup;
        private Activity activity;
        private TFileInfo tFileInfo;
        private DialogCallback onClickListener;

        public MoreBuilder(Activity activity) {
            this.activity = activity;
        }

        public void setTFileInfo(TFileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        public void setOnClickListener(DialogCallback onClickListener) {
            this.onClickListener = onClickListener;
        }

        public CustomDialog create() {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_file_more, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btnDelete = (Button) layout.findViewById(R.id.more_del);
            btnRename = (Button) layout.findViewById(R.id.more_rename);
            btnUninstall = (Button) layout.findViewById(R.id.more_uninstall);
            btnBackup = (Button) layout.findViewById(R.id.more_back);
            btnProperty = (Button) layout.findViewById(R.id.more_property);

            lineDelete = layout.findViewById(R.id.more_del_line);
            lineRename = layout.findViewById(R.id.more_rename_line);
            lineUninstall = layout.findViewById(R.id.more_uninstall_line);
            lineBackup = layout.findViewById(R.id.more_back_line);


            if (tFileInfo != null) {
                switch (tFileInfo.getFileType()) {
                    case Folder:
                        btnDelete.setVisibility(View.GONE);
                        lineDelete.setVisibility(View.GONE);
                        btnRename.setVisibility(View.GONE);
                        lineRename.setVisibility(View.GONE);
                        btnUninstall.setVisibility(View.GONE);
                        lineUninstall.setVisibility(View.GONE);
                        btnBackup.setVisibility(View.GONE);
                        lineBackup.setVisibility(View.GONE);
                    case Normal:
                    case Audio:
                    case Video:
                    case Image:
                        btnDelete.setVisibility(View.VISIBLE);
                        lineDelete.setVisibility(View.VISIBLE);
                        btnRename.setVisibility(View.VISIBLE);
                        lineRename.setVisibility(View.VISIBLE);
                        btnUninstall.setVisibility(View.GONE);
                        lineUninstall.setVisibility(View.GONE);
                        btnBackup.setVisibility(View.GONE);
                        lineBackup.setVisibility(View.GONE);
                        break;
                    case Apk:
                        btnDelete.setVisibility(View.GONE);
                        lineDelete.setVisibility(View.GONE);
                        btnRename.setVisibility(View.GONE);
                        lineRename.setVisibility(View.GONE);
                        btnUninstall.setVisibility(View.VISIBLE);
                        lineUninstall.setVisibility(View.VISIBLE);
                        btnBackup.setVisibility(View.VISIBLE);
                        lineBackup.setVisibility(View.VISIBLE);
                        break;
                }

            } else {
                btnDelete.setVisibility(View.GONE);
                lineDelete.setVisibility(View.GONE);
                btnRename.setVisibility(View.GONE);
                lineRename.setVisibility(View.GONE);
                btnUninstall.setVisibility(View.GONE);
                lineUninstall.setVisibility(View.GONE);
                btnBackup.setVisibility(View.GONE);
                lineBackup.setVisibility(View.GONE);
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        dialog.dismiss();
                        onClickListener.onDialogClick(v.getId(), tFileInfo);
                    }
                }
            };

            btnDelete.setOnClickListener(listener);
            btnRename.setOnClickListener(listener);
            btnUninstall.setOnClickListener(listener);
            btnBackup.setOnClickListener(listener);
            btnProperty.setOnClickListener(listener);

            dialog.setContentView(layout);
            return dialog;
        }

    }

    public interface DialogCallback {
        void onDialogClick(int id, TFileInfo tFileInfo, Object... params);
    }

}
