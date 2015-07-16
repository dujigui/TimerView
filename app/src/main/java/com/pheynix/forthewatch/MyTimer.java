package com.pheynix.forthewatch;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by pheynix on 7/12/15.
 * <p/>
 * Sorry for my English...
 */
public class MyTimer extends View {

    //Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
    //Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
    //Disable hardware accelerate if you need the glow effect,See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
    //如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/
    //如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/
    //如果需要辉光效果，请务必关闭硬件加速，参考： http://blog.chenming.info/blog/2012/09/18/android-hardware-accel/

    //use this in Log,saving lots of time;
    //在打Log的时候使用，节约时间
    private static final String tag = "pheynix";
    private static final String msg = ">>>>>>>>>>>>>>>>>>>>>> LOOK AT ME <<<<<<<<<<<<<<<<<<<<<<<<";

    //flags
    private boolean isInitialized = false;
    private boolean isStarted = false;// =true if countdown begin //倒计时开始的时候为true
    private boolean isInDragButton;
    private int whichDragButton;//1==hour 2==minute 3==second

    //store time,calculate result when countdown pause/stop
    //保存时间，倒计时结束时计算时间差
    private Calendar timeStart;
    private Calendar timeRemain;

    //dimension and coordinate
    //尺寸和坐标
    private float viewWidth;
    private float viewHeight;
    private float circleRadiusHour;
    private float circleRadiusMinute;
    private float circleRadiusSecond;
    private float circleRadiusDragButton;
    private float currentDegreeHour;
    private float currentDegreeMinute;
    private float currentDegreeSecond;
    private float centerXHour;
    private float centerYHour;
    private float centerXMinute;
    private float centerYMinute;
    private float centerXSecond;
    private float centerYSecond;
    private float strokeWidth;
    private String displayNumberHour;
    private String displayNumberMinute;
    private String displayNumberSecond;

    private float[] dragButtonHourPosition;
    private float[] dragButtonMinutePosition;
    private float[] dragButtonSecondPosition;

    private float[] defaultDragButtonHourPosition;
    private float[] defaultDragButtonMinutePosition;
    private float[] defaultDragButtonSecondPosition;

    //paint
    private Paint paintCircleBackground;
    private Paint paintDragButton;
    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;
    private Paint paintNumber;
    private Paint paintGlowEffect;

    //color
    private static final int colorDefault = 0xFFD6D6D6;
    private static final int colorHour = 0xFF9AD13C;
    private static final int colorMinute = 0xFFA55F7C;
    private static final int colorSecond = 0xFF00BCD4;

    private OnTimeChangeListener timeChangeListener;
    private OnMinChangListener minChangListener;
    private OnHourChangListener hourChangListener;
    private OnSecondChangListener secondChangListener;
    private OnInitialFinishListener initialFinishListener;
    //add model by Swifty default timer
    private Model model = Model.Timer;
    private boolean maxTime;

    //initialize every thing
    //初始化
    private void initialize(Canvas canvas) {
        Log.e("aa", "init");
        timeRemain = Calendar.getInstance();
        timeStart = Calendar.getInstance();
        timeStart.clear();
        timeRemain.clear();

        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        //use different dimension in high resolution device
        //保证高分辨率屏幕有比较好的显示效果
        if (viewWidth > 720) {
            strokeWidth = 30;
            circleRadiusDragButton = 50;
        } else {
            strokeWidth = 15;
            circleRadiusDragButton = 25;
        }

        circleRadiusHour = viewWidth / 5;
        circleRadiusMinute = viewWidth / 4;
        circleRadiusSecond = viewWidth / 5;
        currentDegreeHour = 0;
        currentDegreeMinute = 0;
        currentDegreeSecond = 0;
        displayNumberHour = "0";
        displayNumberMinute = "0";
        displayNumberSecond = "0";
        centerXHour = viewWidth / 4;
        centerYHour = 3 * viewWidth / 4;
        centerXMinute = viewWidth / 2;
        centerYMinute = viewWidth / 4 + strokeWidth / 2 + viewWidth / 32;
        centerXSecond = 3 * viewWidth / 4;
        centerYSecond = 3 * viewWidth / 4;

        defaultDragButtonHourPosition = new float[]{centerXHour, centerYHour - circleRadiusHour};
        defaultDragButtonMinutePosition = new float[]{centerXMinute, centerYMinute - circleRadiusMinute};
        defaultDragButtonSecondPosition = new float[]{centerXSecond, centerYSecond - circleRadiusSecond};
        dragButtonHourPosition = defaultDragButtonHourPosition;
        dragButtonMinutePosition = defaultDragButtonMinutePosition;
        dragButtonSecondPosition = defaultDragButtonSecondPosition;

        paintCircleBackground = new Paint();
        paintDragButton = new Paint();
        paintHour = new Paint();
        paintMinute = new Paint();
        paintSecond = new Paint();
        paintNumber = new Paint();
        paintGlowEffect = new Paint();

        paintCircleBackground.setColor(colorDefault);
        paintCircleBackground.setStrokeWidth(strokeWidth);
        paintCircleBackground.setStyle(Paint.Style.STROKE);
        paintCircleBackground.setAntiAlias(true);
        paintDragButton.setStrokeWidth(5);
        paintDragButton.setStyle(Paint.Style.FILL);
        paintDragButton.setAntiAlias(true);
        paintHour.setColor(colorHour);
        paintHour.setStrokeWidth(strokeWidth);
        paintHour.setStyle(Paint.Style.STROKE);
        paintHour.setAntiAlias(true);
        paintMinute.setColor(colorMinute);
        paintMinute.setStrokeWidth(strokeWidth);
        paintMinute.setStyle(Paint.Style.STROKE);
        paintMinute.setAntiAlias(true);
        paintSecond.setColor(colorSecond);
        paintSecond.setStrokeWidth(strokeWidth);
        paintSecond.setStyle(Paint.Style.STROKE);
        paintSecond.setAntiAlias(true);
        paintNumber.setStrokeWidth(2);
        paintNumber.setStyle(Paint.Style.FILL);
        paintNumber.setAntiAlias(true);

        //draw glow effect on the end of arc,glow-effect == dragButton
        //用于绘制圆弧尽头的辉光效果,辉光区域就是dragButton的区域
        paintGlowEffect.setMaskFilter(new BlurMaskFilter(2 * strokeWidth / 3, BlurMaskFilter.Blur.NORMAL));
        paintGlowEffect.setStrokeWidth(strokeWidth);
        paintGlowEffect.setAntiAlias(true);
        paintGlowEffect.setStyle(Paint.Style.FILL);

        //完成初始化回调
        if (initialFinishListener != null) {
            initialFinishListener.onInitialFinishListener();
        }
    }

    public MyTimer(Context context) {
        super(context);
    }

    //use this view in .xml file will invoke this constructor
    //在.xml中使用此控件时调用此构造函数
    public MyTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //initialize dimension and coordinate just for once
        //初始化尺寸，只会调用一次
        if (!isInitialized) {
            initialize(canvas);
            isInitialized = true;
        }

        //arc and number depending on degree,update before drawing
        //角度决定圆弧长度和数字，每次重绘前先更新角度
        if (isStarted) {
            updateDegree();
        }


        //draw background circle
        //画背景的圆圈
        canvas.drawCircle(centerXHour, centerYHour, circleRadiusHour, paintCircleBackground);
        canvas.drawCircle(centerXMinute, centerYMinute, circleRadiusMinute, paintCircleBackground);
        canvas.drawCircle(centerXSecond, centerYSecond, circleRadiusSecond, paintCircleBackground);

        //draw arc
        //画弧形
        RectF rectFHour = new RectF(centerXHour - circleRadiusHour, centerYHour - circleRadiusHour
                , centerXHour + circleRadiusHour, centerYHour + circleRadiusHour);
        RectF rectFMinute = new RectF(centerXMinute - circleRadiusMinute, centerYMinute - circleRadiusMinute
                , centerXMinute + circleRadiusMinute, centerYMinute + circleRadiusMinute);
        RectF rectFSecond = new RectF(centerXSecond - circleRadiusSecond, centerYSecond - circleRadiusSecond
                , centerXSecond + circleRadiusSecond, centerYSecond + circleRadiusSecond);

        canvas.drawArc(rectFHour, -90, currentDegreeHour, false, paintHour);
        canvas.drawArc(rectFMinute, -90, currentDegreeMinute, false, paintMinute);
        canvas.drawArc(rectFSecond, -90, currentDegreeSecond, false, paintSecond);


        //draw glow effect
        //画辉光效果
        paintDragButton.setColor(colorHour);
        canvas.drawCircle(dragButtonHourPosition[0], dragButtonHourPosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorHour);
        canvas.drawCircle(dragButtonHourPosition[0], dragButtonHourPosition[1], strokeWidth, paintGlowEffect);

        paintDragButton.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth, paintGlowEffect);

        paintDragButton.setColor(colorSecond);
        canvas.drawCircle(dragButtonSecondPosition[0], dragButtonSecondPosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorSecond);
        canvas.drawCircle(dragButtonSecondPosition[0], dragButtonSecondPosition[1], strokeWidth, paintGlowEffect);


        //draw letter "H""M""S",point(0,0) of text area is on the bottom-left of this area！
        //画"H""M""S"这三个字母，文字区域的(0,0)在左下角！
        getDisplayNumber();
        Rect rect = new Rect();

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorHour);
        paintNumber.getTextBounds(displayNumberHour, 0, displayNumberHour.length(), rect);
        canvas.drawText(displayNumberHour, centerXHour - rect.width() / 2, centerYHour + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("H", centerXHour + 30, centerYHour + 25, paintNumber);

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorMinute);
        paintNumber.getTextBounds(displayNumberMinute, 0, displayNumberMinute.length(), rect);
        canvas.drawText(displayNumberMinute, centerXMinute - rect.width() / 2, centerYMinute + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("M", centerXMinute + 50, centerYMinute + 25, paintNumber);

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorSecond);
        paintNumber.getTextBounds(displayNumberSecond, 0, displayNumberSecond.length(), rect);
        canvas.drawText(displayNumberSecond, centerXSecond - rect.width() / 2, centerYSecond + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("S", centerXSecond + 50, centerYSecond + 25, paintNumber);
    }


    //handle touch event
    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            //whether touch in the drag button or not
            //判断点击是否在dragButton内
            case MotionEvent.ACTION_DOWN:
                isInDragButton(event.getX(), event.getY());
                break;

            //update coordination of dragButton
            //更新dragButton的位置
            case MotionEvent.ACTION_MOVE:
                if (!isStarted) {
                    if (isInDragButton) {
                        switch (whichDragButton) {
                            case 1:
                                currentDegreeHour = getDegree(event.getX(), event.getY(), centerXHour, centerYHour);
                                updateTime(1);
                                updateDragButtonPosition(1);
                                invalidate();
                                break;
                            case 2:
                                currentDegreeMinute = getDegree(event.getX(), event.getY(), centerXMinute, centerYMinute);
                                updateTime(2);
                                updateDragButtonPosition(2);
                                invalidate();
                                break;
                            case 3:
                                currentDegreeSecond = getDegree(event.getX(), event.getY(), centerXSecond, centerYSecond);
                                updateTime(3);
                                updateDragButtonPosition(3);
                                invalidate();
                                break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                isInDragButton = false;
                break;
        }

        return true;
    }

    //update degree,depend on the path user dragging on the screen
    //根据用户在屏幕划过的轨迹更新角度
    private float getDegree(float eventX, float eventY, float centerX, float centerY) {

        //    http://stackoverflow.com/questions/7926816/calculate-angle-of-touched-point-and-rotate-it-in-android
        //    Math has defeated me once again.So sad...
        //    卧槽...
        double tx = eventX - centerX;
        double ty = eventY - centerY;
        double t_length = Math.sqrt(tx * tx + ty * ty);
        double a = Math.acos(ty / t_length);
        float degree = 180 - (float) Math.toDegrees(a);

        if (centerX > eventX) {
            degree = 180 + (float) Math.toDegrees(a);
        }

        return degree;
    }

    private void getDisplayNumber() {
        if (Integer.valueOf(displayNumberHour) != timeRemain.get(Calendar.HOUR_OF_DAY)) {
            displayNumberHour = timeRemain.get(Calendar.HOUR_OF_DAY) + "";
            if (hourChangListener != null) {
                hourChangListener.onHourChange(timeRemain.get(Calendar.HOUR_OF_DAY));
            }
        }
        if (Integer.valueOf(displayNumberMinute) != timeRemain.get(Calendar.MINUTE)) {
            displayNumberMinute = timeRemain.get(Calendar.MINUTE) + "";
            if (minChangListener != null) {
                minChangListener.onMinChange(timeRemain.get(Calendar.MINUTE));
            }
        }
        if (Integer.valueOf(displayNumberSecond) != timeRemain.get(Calendar.SECOND)) {
            displayNumberSecond = timeRemain.get(Calendar.SECOND) + "";
            if (secondChangListener != null) {
                secondChangListener.onSecondChange(timeRemain.get(Calendar.SECOND));
            }
        }

    }


    private void isInDragButton(float eventX, float eventY) {

        if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonHourPosition[0], 2)
                + Math.pow(eventY - dragButtonHourPosition[1], 2))) {
            //在dragButtonHour中
            isInDragButton = true;
            whichDragButton = 1;
        } else if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonMinutePosition[0], 2)
                + Math.pow(eventY - dragButtonMinutePosition[1], 2))) {
            //在dragButtonMinute中
            isInDragButton = true;
            whichDragButton = 2;
        } else if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonSecondPosition[0], 2)
                + Math.pow(eventY - dragButtonSecondPosition[1], 2))) {
            //在dragButtonSecond中
            isInDragButton = true;
            whichDragButton = 3;
        } else {
            //不在
            isInDragButton = false;
            whichDragButton = 0;
        }
    }


    //degree depending on timeRemain
    //角度由剩余时间决定
    private void updateDegree() {

        currentDegreeHour = (float) ((timeRemain.get(Calendar.HOUR_OF_DAY) * 60 + timeRemain.get(Calendar.MINUTE)) / (6.0 * 60)) * 360;
        currentDegreeMinute = (float) ((timeRemain.get(Calendar.MINUTE) * 60 + timeRemain.get(Calendar.SECOND)) / (60.0 * 60)) * 360;
        currentDegreeSecond = (float) ((timeRemain.get(Calendar.SECOND) * 1000 + timeRemain.get(Calendar.MILLISECOND)) / (60.0 * 1000)) * 360;

        updateDragButtonPosition(0);

    }

    //update drag button position(glow effect area)
    //更新拖动按钮中心点（辉光效果区域）
    private void updateDragButtonPosition(int flag) {

        switch (flag) {
            case 0:
                dragButtonHourPosition[0] = (float) (centerXHour + circleRadiusHour * (Math.sin(Math.toRadians(currentDegreeHour))));
                dragButtonHourPosition[1] = (float) (centerYHour - circleRadiusHour * (Math.cos(Math.toRadians(currentDegreeHour))));

                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));

                dragButtonSecondPosition[0] = (float) (centerXSecond + circleRadiusSecond * Math.sin(Math.toRadians(currentDegreeSecond)));
                dragButtonSecondPosition[1] = (float) (centerYSecond - circleRadiusSecond * Math.cos(Math.toRadians(currentDegreeSecond)));
                break;
            case 1:
                dragButtonHourPosition[0] = (float) (centerXHour + circleRadiusHour * (Math.sin(Math.toRadians(currentDegreeHour))));
                dragButtonHourPosition[1] = (float) (centerYHour - circleRadiusHour * (Math.cos(Math.toRadians(currentDegreeHour))));
                break;
            case 2:
                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));
                break;
            case 3:
                dragButtonSecondPosition[0] = (float) (centerXSecond + circleRadiusSecond * Math.sin(Math.toRadians(currentDegreeSecond)));
                dragButtonSecondPosition[1] = (float) (centerYSecond - circleRadiusSecond * Math.cos(Math.toRadians(currentDegreeSecond)));
                break;
        }

    }


    //get the time from currentDegree and store it in timeStart and timeRemain
    //从当前的角度获取时间，保存到timeStart和timeRemain
    private void updateTime(int flag) {

        switch (flag) {
            case 0:
                timeStart.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                timeRemain.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));

                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));

                timeStart.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                timeRemain.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                break;
            case 1:
                timeStart.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                timeRemain.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                break;
            case 2:
                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                break;
            case 3:
                timeStart.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                timeRemain.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                break;
        }

    }


    //common Timer-TimerTask-Handler countdown solution
    //常见的Timer-TimerTask-Handler倒计时模式
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                //countdown running
                //可以倒计时
                case 1:

                    timeRemain.add(Calendar.MILLISECOND, -100);

                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }

                    invalidate();

                    break;
                //countdown stop
                //时间为空，停止倒计时，提示用户
                case 2:
                    isStarted = false;
                    timerTask.cancel();
                    break;

                //StopWatch running
                case 11:
                    timeRemain.add(Calendar.MILLISECOND, 100);
                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }
                    invalidate();
                    break;
                //StopWatch stop
                //到达MAX TIME
                case 12:
                    isStarted = false;
                    timerTask.cancel();
                    break;
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask timerTask;


    public boolean start() {
        if (model == Model.Timer) {
            if (!isTimeEmpty() && !isStarted) {

                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isTimeEmpty()) {
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }

                    }
                };

                timer.schedule(timerTask, 1000, 100);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        } else if (model == Model.StopWatch) {
            if (!isMaxTime() && !isStarted) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isMaxTime()) {
                            Message message = new Message();
                            message.what = 11;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 12;
                            mHandler.sendMessage(message);
                        }
                    }
                };

                timer.schedule(timerTask, 1000, 100);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        }
        return isStarted;
    }

    private boolean isTimeEmpty() {
        if (timeRemain.get(Calendar.HOUR_OF_DAY) != 0
                || timeRemain.get(Calendar.MINUTE) != 0
                || timeRemain.get(Calendar.SECOND) != 0
                || timeRemain.get(Calendar.MILLISECOND) != 0) {
            return false;
        } else {
            return true;
        }
    }


    public long stop() {
        timerTask.cancel();
        isStarted = false;

        if (timeChangeListener != null) {
            timeChangeListener.onTimeStop(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
        }

        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }


    public Calendar getTimeStart() {
        return timeStart;
    }


    public Calendar getTimeRemaid() {
        return timeRemain;
    }


    public long getTimePass() {
        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }


    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        if (listener != null) {
            timeChangeListener = listener;
        }
    }

    public void setMinChangListener(OnMinChangListener minChangListener) {
        this.minChangListener = minChangListener;
    }

    public void setSecondChangListener(OnSecondChangListener secondChangListener) {
        this.secondChangListener = secondChangListener;
    }

    public void setHourChangListener(OnHourChangListener hourChangListener) {
        this.hourChangListener = hourChangListener;
    }

    public void reset() {
        //先停止计时
        stop();
        //初始化calendar
        isInitialized = false;
        invalidate();
    }

    public boolean isMaxTime() {
        if (timeRemain.get(Calendar.HOUR_OF_DAY) == 5
                && timeRemain.get(Calendar.MINUTE) == 59
                && timeRemain.get(Calendar.SECOND) == 59) {
            return true;
        } else {
            return false;
        }
    }


    //listener
    public interface OnTimeChangeListener {
        public void onTimerStart(long timeStart);

        public void onTimeChange(long timeStart, long timeRemain);

        public void onTimeStop(long timeStart, long timeRemain);
    }

    public interface OnMinChangListener {
        public void onMinChange(int minute);
    }

    public interface OnHourChangListener {
        public void onHourChange(int hour);
    }

    public interface OnInitialFinishListener {
        public void onInitialFinishListener();
    }

    public interface OnSecondChangListener {
        public void onSecondChange(int second);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * set default time
     *
     * @param h max 5
     * @param m max 59
     * @param s max 59
     */
    public void setStartTime(final int h, final int m, final int s) throws NumberFormatException {
        initialFinishListener = new OnInitialFinishListener() {
            @Override
            public void onInitialFinishListener() {
                if (h > 5 || m > 59 || s > 69 || h < 0 || m < 0 | s < 0) {
                    throw new NumberFormatException("hour must in [0-5], minute and second must in [0-59]");
                }
                timeRemain.set(Calendar.HOUR_OF_DAY, h);
                timeRemain.set(Calendar.MINUTE, m);
                timeRemain.set(Calendar.SECOND, s);
                timeStart.set(Calendar.HOUR_OF_DAY, h);
                timeStart.set(Calendar.MINUTE, m);
                timeStart.set(Calendar.SECOND, s);
                updateDegree();
                invalidate();
            }
        };
    }

}
