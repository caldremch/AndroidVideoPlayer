package com.caldremch.common.widget.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.caldremch.common.R;

/**
 * @author Caldremch
 * @date 2019-07-07 00:09
 * @email caldremch@163.com
 * @describe
 **/
public class RoundWidgetDelegate {

    private static final String TAG = RoundWidgetDelegate.class.getSimpleName();
    /**
     * 自定义属性
     */
    private float rl_radius = 0f;
    private float rl_radius_left_top = 0f;
    private float rl_radius_right_top = 0f;
    private float rl_radius_right_bottom = 0f;
    private float rl_radius_left_bottom = 0f;

    private int rl_backgroundColor;
    private int rl_startColor;
    private int rl_endColor;
    private int rl_centerColor;

    private int rl_stroke_color;
    private float rl_stroke_width;
    private int rl_gradient_orientation;
    private int rl_gradient_type;

    public RoundWidgetDelegate(View instance, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);

        if (array != null) {
            rl_stroke_width = array.getDimension(R.styleable.RoundLayout_rl_stroke_width, 0);
            rl_radius = array.getDimension(R.styleable.RoundLayout_rl_radius, 0);
            rl_radius_left_top = array.getDimension(R.styleable.RoundLayout_rl_radius_left_top, 0);
            rl_radius_right_top = array.getDimension(R.styleable.RoundLayout_rl_radius_right_top, 0);
            rl_radius_right_bottom = array.getDimension(R.styleable.RoundLayout_rl_radius_right_bottom, 0);
            rl_radius_left_bottom = array.getDimension(R.styleable.RoundLayout_rl_radius_left_bottom, 0);
            rl_backgroundColor = array.getColor(R.styleable.RoundLayout_rl_backgroundColor, -1);
            rl_startColor = array.getColor(R.styleable.RoundLayout_rl_startColor, -1);
            rl_endColor = array.getColor(R.styleable.RoundLayout_rl_endColor, -1);
            rl_centerColor = array.getColor(R.styleable.RoundLayout_rl_centerColor, -1);
            rl_stroke_color = array.getColor(R.styleable.RoundLayout_rl_stroke_color, -1);
            rl_gradient_orientation = array.getInt(R.styleable.RoundLayout_rl_gradient_orientation, GradientDrawable.Orientation.TOP_BOTTOM.ordinal());
            rl_gradient_type = array.getInt(R.styleable.RoundLayout_rl_gradient_type, GradientDrawable.LINEAR_GRADIENT);
            array.recycle();
        }

        try {


            GradientDrawable shapeDrawable = new GradientDrawable();


            if (rl_startColor != -1 || rl_endColor != -1) {
                if (rl_startColor != -1 && rl_endColor == -1) {
                    rl_endColor = rl_startColor;
                } else if (rl_startColor == -1) {
                    rl_startColor = rl_endColor;
                }
                if (rl_centerColor != -1){
                    shapeDrawable.setColors(new int[]{rl_startColor,rl_centerColor,  rl_endColor});
                }else{
                    shapeDrawable.setColors(new int[]{rl_startColor, rl_endColor});
                }
            } else {
                shapeDrawable = new GradientDrawable();
                if (rl_backgroundColor != -1) {
                    shapeDrawable.setColor(rl_backgroundColor);
                }
            }
            shapeDrawable.setGradientType(rl_gradient_type);
            shapeDrawable.setOrientation(GradientDrawable.Orientation.values()[rl_gradient_orientation]);

            /**
             * 1.设置了rl_backgroundColor, 那么就会覆盖掉原生background
             * 2.同时设置了rl_radius 和 其他 rl_radius_xxxx , 那么rl_radius 的效果不生效(全部圆角都是一个值)
             * 3.如果同时设置了rl_startColor和rl_endColor(如果只设置了一个, 就等同于设置了那么设置了rl_backgroundColor或原生background将会被覆盖), 那么设置了rl_backgroundColor或原生background将会被覆盖
             */
            if (rl_radius_left_top != 0 ||
                    rl_radius_right_bottom != 0 ||
                    rl_radius_right_top != 0 ||
                    rl_radius_left_bottom != 0
            ) {
                //左上，右上，右下，左下
                shapeDrawable.setCornerRadii(new float[]{
                        rl_radius_left_top,
                        rl_radius_left_top,
                        rl_radius_right_top,
                        rl_radius_right_top,
                        rl_radius_right_bottom,
                        rl_radius_right_bottom,
                        rl_radius_left_bottom,
                        rl_radius_left_bottom
                });
            } else if (rl_radius != 0) {
                shapeDrawable.setCornerRadius(rl_radius);
            }

            // TODO: 2019/7/7  目前默认RECTANGLE, 后期加入其他类型
            shapeDrawable.setShape(GradientDrawable.RECTANGLE);
            if (rl_stroke_width != 0){
                shapeDrawable.setStroke((int) rl_stroke_width, rl_stroke_color);
            }

            instance.setBackground(shapeDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
