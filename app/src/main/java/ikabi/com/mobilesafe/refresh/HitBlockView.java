package ikabi.com.mobilesafe.refresh;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import ikabi.com.mobilesafe.R;

/**
 * Created by Hitomis on 2016/2/29.
 */
public class HitBlockView extends View {

    public static final int STATUS_GAME_PREPAR = 0;

    public static final int STATUS_GAME_PLAY = 1;

    public static final int STATUS_GAME_FINISHED = 2;

    public static final int STATUS_GAME_OVER = 3;

    /**
     * 默认矩形块竖向排列的数目
     */
    private static final int BLOCK_VERTICAL_NUM = 5;

    /**
     * 默认矩形块横向排列的数目
     */
    private static final int BLOCK_HORIZONTAL_NUM = 3;

    /**
     * 挡板所在位置占屏幕宽度的比率
     */
    private static final float RACKET_POSITION_RATIO = .8f;

    /**
     * 矩形块所在位置占屏幕宽度的比率
     */
    private static final float BLOCK_POSITION_RATIO = .08f;

    /**
     * 小球默认其实弹射角度
     */
    private static final int DEFAULT_ANGLE = 30;

    /**
     * 分割线默认宽度大小
     */
    static final float DIVIDING_LINE_SIZE = 1.f;

    /**
     * 小球移动速度
     */
    private static final int SPEED = 6;

    /**
     * 矩形块的高度
     */
    private static final float BLOCK_HEIGHT = 40.f;

    /**
     * 矩形块的宽度
     */
    private static final float BLOCK_WIDTH = 13.f;

    /**
     * 小球半径
     */
    private static final float BALL_RADIUS = 8.f;

    private static final String TEXT_GAME_OVER = "Game Over";

    private static final String TEXT_LOADING = "Loading...";

    private static final String TEXT_LOADING_FINISHED = "Loading Finished";

    private Paint mPaint, blockPaint;

    private TextPaint textPaint;

    private float blockLeft;

    private float racketLeft, racketTop, racketHeight;

    private float cx, cy;

    private int screenWidth;

    private List<Point> pointList;

    private boolean isleft;

    private int status = STATUS_GAME_PREPAR;

    private int angle;

    private int blockHorizontalNum;

    private int speed;

    private int blockColor, ballColor, racketColor;

    public HitBlockView(Context context) {
        this(context, null);
    }

    public HitBlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HitBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        screenWidth = getScreenWidth(context);

        initAttrs(context, attrs);
        initTools();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HitBlock);

        blockHorizontalNum = typedArray.getInt(R.styleable.HitBlock_block_horizontal_num, BLOCK_HORIZONTAL_NUM);
        speed = typedArray.getInt(R.styleable.HitBlock_ball_speed, SPEED);

        blockColor = typedArray.getColor(R.styleable.HitBlock_block_color, Color.rgb(255, 255, 255));
        ballColor = typedArray.getColor(R.styleable.HitBlock_ball_color, Color.BLACK);
        racketColor = typedArray.getColor(R.styleable.HitBlock_racket_color, Color.parseColor("#A5A5A5"));

        typedArray.recycle();
    }

    private void initTools() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(DIVIDING_LINE_SIZE);

        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#C1C2C2"));
    }

    private void initConfigParams() {
        cx = racketLeft - 2 * BALL_RADIUS;
        cy = (int) (getHeight() * .5f);

        racketTop = 0;

        angle = DEFAULT_ANGLE;

        isleft = true;

        if (pointList == null) {
            pointList = new ArrayList<>();
        } else {
            pointList.clear();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int) Math.ceil(BLOCK_VERTICAL_NUM * BLOCK_HEIGHT + (BLOCK_VERTICAL_NUM - 1) * DIVIDING_LINE_SIZE + DIVIDING_LINE_SIZE * 2);
        setMeasuredDimension(screenWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        blockLeft = screenWidth * BLOCK_POSITION_RATIO;

        racketLeft = screenWidth * RACKET_POSITION_RATIO;

        racketHeight = (int) (BLOCK_HEIGHT * 1.6f);

        if (status == STATUS_GAME_PREPAR) {
            initConfigParams();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoundary(canvas);

        drawColorBlock(canvas);

        drawRacket(canvas);

        if (status >=STATUS_GAME_PLAY && status<= STATUS_GAME_FINISHED)
        makeBallPath(canvas);

        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        switch (status) {
            case STATUS_GAME_PREPAR:
            case STATUS_GAME_PLAY:
                textPaint.setTextSize(60);
                promptText(canvas, TEXT_LOADING);
                break;
            case STATUS_GAME_FINISHED:
                textPaint.setTextSize(50);
                promptText(canvas, TEXT_LOADING_FINISHED);
                break;
            case STATUS_GAME_OVER:
                textPaint.setTextSize(60);
                promptText(canvas, TEXT_GAME_OVER);
                break;
        }
    }



    /**
     * 绘制分割线
     * @param canvas 默认画布
     */
    private void drawBoundary(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#606060"));
        canvas.drawLine(0, 0, screenWidth, 0, mPaint);
        canvas.drawLine(0, getHeight(), screenWidth, getHeight(), mPaint);
    }

    /**
     * 绘制挡板
     * @param canvas 默认画布
     */
    private void drawRacket(Canvas canvas) {
        mPaint.setColor(racketColor);
        canvas.drawRect(racketLeft, racketTop, racketLeft + BLOCK_WIDTH, racketTop + racketHeight, mPaint);
    }

    /**
     * 绘制并处理小球运动的轨迹
     * @param canvas 默认画布
     */
    private void makeBallPath(Canvas canvas) {
        mPaint.setColor(ballColor);

        if (cx <= blockLeft +  blockHorizontalNum * BLOCK_WIDTH + (blockHorizontalNum - 1) * DIVIDING_LINE_SIZE + BALL_RADIUS) { // 小球进入到色块区域
            if (checkTouchBlock(cx, cy)) { // 反弹回来
                isleft = false;
            }
        }
        if (cx <= blockLeft + BALL_RADIUS ) { // 小球穿过色块区域
            isleft = false;
        }

        if (cx + BALL_RADIUS >= racketLeft && cx - BALL_RADIUS < racketLeft + BLOCK_WIDTH) { //小球当前坐标X值在挡板X值区域范围内
            if (checkTouchRacket(cy)) { // 小球与挡板接触
                if (pointList.size() == blockHorizontalNum * BLOCK_VERTICAL_NUM) { // 矩形块全部被消灭，游戏结束
                    status = STATUS_GAME_OVER;
                    return;
                }
                isleft = true;
            }
        } else if (cx > canvas.getWidth()) { // 小球超出挡板区域
            status = STATUS_GAME_OVER;
        }

        if (cy <= BALL_RADIUS + DIVIDING_LINE_SIZE) { // 小球撞到上边界
            angle = 180 - DEFAULT_ANGLE;
        } else if (cy >= getHeight() - BALL_RADIUS - DIVIDING_LINE_SIZE) { // 小球撞到下边界
            angle = 180 + DEFAULT_ANGLE;
        }


        if (isleft) {
            cx -= speed;
        } else {
            cx += speed;
        }
        cy -= (float) Math.tan(Math.toRadians(angle)) * speed;

        canvas.drawCircle(cx, cy, BALL_RADIUS, mPaint);

        invalidate();

    }

    private void promptText(Canvas canvas, String text) {
        float textX = (canvas.getWidth() - textPaint.measureText(text)) * .5f;
        float textY = canvas.getHeight()  * .5f - (textPaint.ascent() + textPaint.descent()) * .5f;
        canvas.drawText(text, textX, textY, textPaint);
    }

    /**
     * 检查小球是否撞击到挡板
     * @param y 小球当前坐标Y值
     * @return 小球位于挡板Y值区域范围内：true，反之：false
     */
    private boolean checkTouchRacket(float y) {
        boolean flag = false;
        float diffVal = y - racketTop;
        if (diffVal >= 0 && diffVal <= racketHeight) { // 小球位于挡板Y值区域范围内
            flag = true;
        }
        return flag;
    }

    /**
     * 检查小球是否撞击到矩形块
     * @param x 小球坐标X值
     * @param y 小球坐标Y值
     * @return 撞击到：true，反之：false
     */
    private boolean checkTouchBlock(float x, float y) {
        int columnX = (int) ((x - blockLeft - BALL_RADIUS - speed ) / BLOCK_WIDTH);
        columnX = columnX == blockHorizontalNum ? columnX - 1 : columnX;
        int rowY = (int) (y / BLOCK_HEIGHT);
        rowY = rowY == BLOCK_VERTICAL_NUM ? rowY - 1 : rowY;
        Point p = new Point();
        p.set(columnX, rowY);

        boolean flag = false;
        for (Point point : pointList) {
            if (point.equals(p.x, p.y)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            pointList.add(p);
        }
        return !flag;
    }

    /**
     * 绘制矩形色块
     * @param canvas 默认画布
     */
    private void drawColorBlock(Canvas canvas) {
        float left, top;
        int column, row, redCode, greenCode, blueCode;
        for (int i = 0; i < blockHorizontalNum * BLOCK_VERTICAL_NUM; i++) {
            row = i / blockHorizontalNum;
            column = i % blockHorizontalNum;

            boolean flag = false;
            for (Point point : pointList) {
                if (point.equals(column, row)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }

            redCode = (Color.red(blockColor) / blockHorizontalNum) * column;
            greenCode = (Color.green(blockColor) / blockHorizontalNum) * column;
            blueCode = (Color.blue(blockColor) / blockHorizontalNum) * column;
            blockPaint.setColor(Color.rgb(redCode, greenCode, blueCode));

            left = blockLeft + column * (BLOCK_WIDTH + DIVIDING_LINE_SIZE);
            top = DIVIDING_LINE_SIZE + row * (BLOCK_HEIGHT + DIVIDING_LINE_SIZE);
            canvas.drawRect(left, top, left + BLOCK_WIDTH, top + BLOCK_HEIGHT, blockPaint);
        }
    }


    /**
     * 控制挡板上下移动
     * @param distance 挡板移动的距离，正数：下移，负数：上移
     */
    public void moveRacket(float distance) {
        float maxDistance = (getHeight() -  2 * DIVIDING_LINE_SIZE - racketHeight);

        if (distance > maxDistance) {
            distance = maxDistance;
        }

        racketTop = distance;
        postInvalidate();
    }

    public void postStatus(int status) {
        this.status = status;
        postInvalidate();
        requestLayout();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context context
     * @return 手机屏幕宽度
     */
    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
