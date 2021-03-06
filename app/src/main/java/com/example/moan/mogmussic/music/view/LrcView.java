package com.example.moan.mogmussic.music.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.moan.mogmussic.music.LrcRow;

import java.util.List;

import androidx.annotation.Nullable;

public class LrcView extends View implements ILrcView {
    List<LrcRow> mLrcRows;
    public final static int DISPLAY_MODE_NORMAL = 0;
    public final static int DISPLAY_MODE_SEEK = 1;
    public final static int DISPLAY_MODE_SCALE = 2;
    private int mDisplayMode = DISPLAY_MODE_NORMAL;
    private int mMinSeekFiredOffset = 10;
    private int mHighlightRow = 0;
    private int mHighlightRowColor = Color.parseColor("#FFCC80");
    private int mNormalRowColor = Color.WHITE;
    private int mSeekLineColor = Color.parseColor("#D3D3D3");
    private int mSeekLineTextColor = Color.parseColor("#FFCC80");
    private int mSeekLineTextSize = 25;
    private int mMinSeekLineTextSize = 20;
    private int mMaxSeekLineTextSize = 30;
    private int mLrcFontSize = 45;    // font size of lrc
    private int mMinLrcFontSize = 35;
    private int mMaxLrcFontSize = 55;
    private int mPaddingY = 50;
    private int mSeekLinePaddingX = 0;
    private ILrcViewListener mLrcViewListener;
    private String mLoadingLrcTip = "暂无歌词";
    private Paint mPaint;


    public LrcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mLrcFontSize);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int width = getWidth();
        if (mLrcRows == null || mLrcRows.size() == 0) {
            if (mLoadingLrcTip != null) {
                mPaint.setColor(mHighlightRowColor);
                mPaint.setTextSize(mLrcFontSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(mLoadingLrcTip, width / 2, height / 2 - mLrcFontSize, mPaint);
            }
            return;
        }
        int rowY;
        final int rowX = width / 2;
        int rowNum;
        // playing now
        String highLightText = mLrcRows.get(mHighlightRow).content;
        int highlightRowY = height / 2 - mLrcFontSize;
        mPaint.setColor(mHighlightRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(highLightText, rowX, highlightRowY, mPaint);
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            mPaint.setColor(mSeekLineColor);
            canvas.drawLine(mSeekLinePaddingX, highlightRowY + mPaddingY,
                    width - mSeekLinePaddingX, highlightRowY + mPaddingY, mPaint);
            mPaint.setColor(mSeekLineTextColor);
            mPaint.setTextSize(mSeekLineTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(mLrcRows.get(mHighlightRow).strTime, 0, highlightRowY, mPaint);
        }
        // above
        mPaint.setColor(mNormalRowColor);
        mPaint.setTextSize(mLrcFontSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        rowNum = mHighlightRow - 1;
        rowY = highlightRowY - mPaddingY - mLrcFontSize;
        while (rowY > -mLrcFontSize && rowNum >= 0) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY -= (mPaddingY + mLrcFontSize);
            rowNum--;
        }
        // below
        rowNum = mHighlightRow + 1;
        rowY = highlightRowY + mPaddingY + mLrcFontSize;
        while (rowY < height && rowNum < mLrcRows.size()) {
            String text = mLrcRows.get(rowNum).content;
            canvas.drawText(text, rowX, rowY, mPaint);
            rowY += (mPaddingY + mLrcFontSize);
            rowNum++;
        }
    }

    private float mLastMotionY;
    private PointF mPointerOneLastMotion = new PointF();
    private PointF mPointerTwoLastMotion = new PointF();
    private boolean isFirstMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = event.getY();
                isFirstMove = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    doScale(event);
                    return true;
                }
                if (mDisplayMode == DISPLAY_MODE_SCALE) {
                    //if scaling but pointer become not two ,do nothing.
                    return true;
                }
                doSeek(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mDisplayMode == DISPLAY_MODE_SEEK) {
                    seekLrc(mHighlightRow, true);
                }
                mDisplayMode = DISPLAY_MODE_NORMAL;
                invalidate();
                break;
        }
        return true;
    }

    private void doSeek(MotionEvent event) {
        float y = event.getY();
        float offsetY = y - mLastMotionY;
        if (Math.abs(offsetY) < mMinSeekFiredOffset) {
            return;
        }
        mDisplayMode = DISPLAY_MODE_SEEK;
        int rowOffset = Math.abs((int) offsetY / mLrcFontSize);


        if (offsetY < 0) {
            mHighlightRow += rowOffset;
        } else if (offsetY > 0) {
            mHighlightRow -= rowOffset;
        }
        mHighlightRow = Math.max(0, mHighlightRow);
        mHighlightRow = Math.min(mHighlightRow, mLrcRows.size() - 1);
        if (rowOffset > 0) {
            mLastMotionY = y;
            invalidate();
        }
    }

    private void doScale(MotionEvent event) {
        if (mDisplayMode == DISPLAY_MODE_SEEK) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            return;
        }
        // two pointer mode , scale font
        if (isFirstMove) {
            mDisplayMode = DISPLAY_MODE_SCALE;
            invalidate();
            isFirstMove = false;
            setTwoPointerLocation(event);
        }
        int scaleSize = getScale(event);
        if (scaleSize != 0) {
            setNewFontSize(scaleSize);
            invalidate();
        }
        setTwoPointerLocation(event);

    }

    private int getScale(MotionEvent event) {
        float x0 = event.getX(0);
        float y0 = event.getY(0);
        float x1 = event.getX(1);
        float y1 = event.getY(1);

        float maxOffset;

        boolean zoomin;
        float oldXOffset = Math.abs(mPointerOneLastMotion.x - mPointerTwoLastMotion.x);
        float newXOffSet = Math.abs(x1 - x0);

        float oldYOffset = Math.abs(mPointerOneLastMotion.y - mPointerTwoLastMotion.y);
        float newYOffSet = Math.abs(y1 - y0);

        maxOffset = Math.max(Math.abs(newXOffSet - oldXOffset), Math.abs(newYOffSet - oldYOffset));
        if (maxOffset == Math.abs(newXOffSet - oldXOffset)) {
            zoomin = newXOffSet > oldXOffset;
        } else {
            zoomin = newYOffSet > oldYOffset;
        }
        if (zoomin) {
            return (int) (maxOffset / 10);
        } else {
            return -(int) (maxOffset / 10);
        }

    }

    private void setNewFontSize(int scaleSize) {
        mLrcFontSize += scaleSize;
        mLrcFontSize = Math.max(mLrcFontSize, mMinLrcFontSize);
        mLrcFontSize = Math.min(mLrcFontSize, mMaxLrcFontSize);

        //设置歌词的最新字体大小
        mSeekLineTextSize += scaleSize;
        mSeekLineTextSize = Math.max(mSeekLineTextSize, mMinSeekLineTextSize);
        mSeekLineTextSize = Math.min(mSeekLineTextSize, mMaxSeekLineTextSize);

    }

    private void setTwoPointerLocation(MotionEvent event) {
        mPointerOneLastMotion.x = event.getX(0);
        mPointerOneLastMotion.y = event.getY(0);
        mPointerTwoLastMotion.x = event.getX(1);
        mPointerTwoLastMotion.y = event.getY(1);
    }

    @Override
    public void setLrc(List<LrcRow> lrcRows) {
        mLrcRows = lrcRows;
        invalidate();
    }

    @Override
    public void seekLrcToTime(long time) {
        if (mLrcRows == null || mLrcRows.size() == 0) {
            return;
        }
        if (mDisplayMode != DISPLAY_MODE_NORMAL) {
            return;
        }

        for (int i = 0; i < mLrcRows.size(); i++) {
            LrcRow current = mLrcRows.get(i);
            LrcRow next = i + 1 == mLrcRows.size() ? null : mLrcRows.get(i + 1);
            /**
             *  正在播放的时间大于current行的歌词的时间而小于next行歌词的时间， 设置要高亮的行为current行
             *  正在播放的时间大于current行的歌词，而current行为最后一句歌词时，设置要高亮的行为current行
             */
            if ((time >= current.time && next != null && time < next.time)
                    || (time > current.time && next == null)) {
                seekLrc(i, false);
                return;
            }
        }
    }


    @Override
    public void setListener(ILrcViewListener iLrcViewListener) {
        mLrcViewListener = iLrcViewListener;
    }

    public void seekLrc(int position, boolean isToBeHighlightLrcRow) {
        if (mLrcRows == null || position < 0 || position > mLrcRows.size()) {
            return;
        }
        LrcRow lrcRow = mLrcRows.get(position);
        mHighlightRow = position;
        invalidate();
        if (mLrcViewListener != null && isToBeHighlightLrcRow) {
            mLrcViewListener.onLrcSeeking(position, lrcRow);
        }
    }

    public void setLoadingLrcTipText(String text) {
        mLoadingLrcTip = text;
    }
}
