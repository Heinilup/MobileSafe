package ikabi.com.mobilesafe.view;

import android.graphics.drawable.Drawable;

public class MenuItem {

    private int itemId;
    private String title;
    private Drawable icon;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }
}
