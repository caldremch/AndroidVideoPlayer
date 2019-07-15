package com.caldremch.democommom.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.caldremch.common.utils.DensityUtil;
import com.caldremch.democommom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author fxYan
 */
public final class LiveThumbView extends FrameLayout {

    private int[] images = {
            R.mipmap.live_ic_thumb_01,
            R.mipmap.live_ic_thumb_02,
            R.mipmap.live_ic_thumb_03,
            R.mipmap.live_ic_thumb_04,
            R.mipmap.live_ic_thumb_05,
            R.mipmap.live_ic_thumb_06,
            R.mipmap.live_ic_thumb_07,
            R.mipmap.live_ic_thumb_08,
            R.mipmap.live_ic_thumb_09,
            R.mipmap.live_ic_thumb_10,
            R.mipmap.live_ic_thumb_11,
            R.mipmap.live_ic_thumb_12,
            R.mipmap.live_ic_thumb_13,
            R.mipmap.live_ic_thumb_14,
            R.mipmap.live_ic_thumb_15,
            R.mipmap.live_ic_thumb_16,
            R.mipmap.live_ic_thumb_17,
    };

    private Random random;
    private PointF startPoint;

    private List<Animator> animators = new ArrayList<>();
    private SparseArray<ImageView> imageViews = new SparseArray<>();

    public LiveThumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        imageViews.put("");

        random = new Random();
        startPoint = new PointF();
    }

    public void addThumbImage() {
        ImageView imageView = getThumbView(getRandomResource());
        addView(imageView);
        Animator animator = getBezierAnimator(imageView);
        animator.addListener(new EndAnimatorListener(imageView));
        animator.start();
        animators.add(animator);
    }

    public void cancel() {
        for (Animator animator : animators) {
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animators.clear();
    }

    public void release(){
        removeAllViews();

    }

    // 定义ImageView的运动轨迹
    private ValueAnimator getBezierAnimator(final View target) {
        // 贝塞尔曲线中间的两个点
        PointF p1 = randomPointF(3.0f);
        PointF p2 = randomPointF(1.5f);
        startPoint.x = getWidth() - DensityUtil.dp2px( 45);
        startPoint.y = getHeight() - DensityUtil.dp2px(30);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ThumbTypeEvaluator(p1, p2), startPoint, new PointF(random.nextInt(getWidth()), 0));

        valueAnimator.setInterpolator(new AccelerateInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                //更新target的坐标
                target.setX(pointF.x);
                target.setY(pointF.y);
                //透明度 从不透明到完全透明
                target.setAlpha(1.0f - animation.getAnimatedFraction() * animation.getAnimatedFraction());
            }
        });
        valueAnimator.setDuration(1000);

        return valueAnimator;
    }

    private PointF randomPointF(float scale) {
        PointF pointF = new PointF();
        pointF.x = random.nextInt(getWidth());
        pointF.y = random.nextInt(getHeight()) / scale;

        return pointF;
    }

    private int getRandomResource() {
        int index = random.nextInt(images.length);
        return images[index];
    }

    private ImageView getThumbView(int resId) {
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);
        imageView.setImageResource(resId);
        return imageView;
    }

    private class EndAnimatorListener extends AnimatorListenerAdapter {

        private View target;

        public EndAnimatorListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 当动画结束的时候移除target
            removeView(target);
            if (target != null){
                target.setBackgroundResource(0);
                target.setBackgroundDrawable(null);
            }

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            // 当动画取消的时候移除target
            removeView(target);
            target = null;
        }
    }

    private static class ThumbTypeEvaluator implements TypeEvaluator<PointF> {

        private PointF p1;
        private PointF p2;

        public ThumbTypeEvaluator(PointF p1, PointF p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            // 三阶方贝塞尔曲线
            float timeLeft = 1.0f - fraction;
            PointF pointF = new PointF();
            pointF.x = (float) (startValue.x * Math.pow(timeLeft, 3)
                    + 3 * p1.x * fraction * Math.pow(timeLeft, 2)
                    + 3 * p2.x * Math.pow(fraction, 2) * timeLeft
                    + endValue.x * Math.pow(fraction, 3));

            pointF.y = (float) (startValue.y * Math.pow(timeLeft, 3)
                    + 3 * p1.y * fraction * Math.pow(timeLeft, 2)
                    + 3 * p2.y * Math.pow(fraction, 2) * timeLeft
                    + endValue.y * Math.pow(fraction, 3));
            return pointF;
        }
    }

}
