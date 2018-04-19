package com.chinatelecom.pimtest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.utils.BitmapUtils;
import com.chinatelecom.pimtest.utils.DeviceUtils;
import com.chinatelecom.pimtest.utils.StringUtils;

/**
 * Created by Shuo on 2018/4/19.
 */

public class HeaderView extends RelativeLayout {

    private RelativeLayout delegateView;

    private View delegate;
    private ImageView leftView;
    private TextView leftTextView;

    private ImageView rightView;
    private TextView rightTextView;
    private ImageView rightNextView;
    private TextView rightNextTextView;
    private ImageView rightThreeView;
    private TextView rightThreeTextView;

    private ViewGroup middleViewGroup;

    private RelativeLayout layout;

    private Context context;

    private ImageView middleImage;
    private ImageView rightImage;
    private LinearLayout rightImageBg;

    private TextView middleTextView;

    private View shadow;
    private float density;
    private ImageView sharedImage;

    private TextView promptText;
    private ImageView delIcon;


    private RelativeLayout title_setting_relative;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        density = DeviceUtils.getDisplayDensity(context);
        setupView(context, attrs);
    }

    public View getShadow() {
        return shadow;
    }

    public void setShadow(View shadow) {
        this.shadow = shadow;
    }

    private void setupView(final Context context, AttributeSet attrs) {
        View delegate = LayoutInflater.from(context).inflate(R.layout.header_view, null, false);
        delegateView = (RelativeLayout) delegate.findViewById(R.id.layout_header);
        layout = (RelativeLayout) delegate.findViewById(R.id.layout_header_view);
        leftView = (ImageView)delegate.findViewById(R.id.left_view);
        leftTextView = (TextView) delegate.findViewById(R.id.left_text_view);

        middleViewGroup = (ViewGroup) delegate.findViewById(R.id.layout_middle);

        rightView = (ImageView) delegate.findViewById(R.id.right_view);
        rightTextView = (TextView) delegate.findViewById(R.id.right_text_view);

        rightNextView = (ImageView) delegate.findViewById(R.id.right_next_view);
        rightNextTextView = (TextView) delegate.findViewById(R.id.right_next_text_view);

        rightThreeView = (ImageView) delegate.findViewById(R.id.right_three_view);
        rightThreeTextView = (TextView) delegate.findViewById(R.id.right_three_text_view);

        rightImage = (ImageView) delegate.findViewById(R.id.right_image_view);
        rightImageBg = (LinearLayout) delegate.findViewById(R.id.right_image_bg_view);

        shadow = delegate.findViewById(R.id.shadow);
        title_setting_relative = (RelativeLayout)delegate.findViewById(R.id.title_setting_relative);
        delIcon = (ImageView) delegate.findViewById(R.id.title_setting_img);
        promptText = (TextView)delegate.findViewById(R.id.title_setting_text);


        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.com_chinatelecom_pim_ui_view_HeaderView);

        boolean enableShadow = typedArray.getBoolean(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_shadow_enable, true);
        if (!enableShadow) {
            shadow.setVisibility(View.GONE);
        }
        boolean is_encryption_message_bg =typedArray.getBoolean(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_is_encryption_message_bg,false);
        if (is_encryption_message_bg){
            layout.setBackgroundColor(Color.parseColor("#313638"));
        }
        Drawable leftDrawable = typedArray.getDrawable(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_left_view);
        if (leftDrawable != null) {
            leftView.setVisibility(View.VISIBLE);
            setBackgroundDrawable(leftView, leftDrawable);
        } else {
            leftView.setVisibility(View.GONE);
        }

        String middleText = String.valueOf(typedArray.getText(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_middle_view));
        if (StringUtils.isNotEmpty(middleText)) {
            addMiddleView(middleText);
        }

        Drawable rightDrawable = typedArray.getDrawable(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_right_view);
        if (rightDrawable != null) {
            rightView.setVisibility(View.VISIBLE);
            setBackgroundDrawable(rightView, rightDrawable);
        } else {
            rightView.setVisibility(View.GONE);
        }

        Drawable rightNextDrawable = typedArray.getDrawable(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_right_next_view);
        if (rightNextDrawable != null) {
            rightNextView.setVisibility(View.VISIBLE);
            setBackgroundDrawable(rightNextView, rightNextDrawable);
        } else {
//            rightNextView.setBackgroundResource(R.drawable.transparent);
            rightNextView.setVisibility(View.GONE);
        }

        Drawable rightThreeDrawable = typedArray.getDrawable(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_right_three_view);
        if (rightThreeDrawable != null) {
            rightThreeView.setVisibility(View.VISIBLE);
            setBackgroundDrawable(rightThreeView, rightThreeDrawable);
        } else {
            rightThreeView.setVisibility(View.GONE);
        }

        boolean showSmsPrompt = typedArray.getBoolean(R.styleable.com_chinatelecom_pim_ui_view_HeaderView_show_sms_prompt, false);
        if(showSmsPrompt) {
            setupPromptLayout();
        }

        typedArray.recycle();
        //setBackgroundResource(R.drawable.bg_pim_top);
        addView(delegate, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setBackgroundDrawable(ImageView target, Drawable drawable) {
        if (drawable != null && BitmapUtils.drawable2Bitmap(drawable) != null) {
            target.setImageBitmap(BitmapUtils.drawable2Bitmap(drawable));
        }
    }

    public void setHeaderViewBackgroundResource(int resourceId) {
        layout.setBackgroundResource(resourceId);
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setMiddleView(String text) {
        middleViewGroup.removeAllViewsInLayout();
        middleViewGroup.removeAllViews();
        addMiddleView(text);
    }

    public void addMiddleView(String text) {
        addTextView(text, 16);
    }

    public void addMiddleView(String text, int textSize) {
        addTextView(text, textSize);
    }

    /**
     * 头部导航条中间的内容
     *
     * @param text       文本
     * @param resourceId 文本旁边的小图标
     */
    public void setMiddleView(String text, int resourceId) {
        middleViewGroup.removeAllViewsInLayout();
        middleViewGroup.removeAllViews();
        addTextView(text, 16);
        if (resourceId > 0) {
            addImageView(resourceId);
        }
    }

    public void setMiddleViewPadding(int left, int top, int right, int bottom) {
        middleViewGroup.setPadding(left, top, right, bottom);
    }

    public ImageView getRightImage() {
        return rightImage;
    }

    public LinearLayout getRightImageBg() {
        return rightImageBg;
    }

    private void addTextView(String text, int textSize) {
        middleTextView = new TextView(getContext());
        middleTextView.setTextColor(0xffffffff);
        //middleTextView.setTypeface(null, Typeface.BOLD);
        middleTextView.setText(text==null?"等我加载":text);
        middleTextView.setSingleLine(true);
        middleTextView.setGravity(Gravity.CENTER_VERTICAL);

        middleTextView.setEllipsize(TextUtils.TruncateAt.END);
        middleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//        layoutParams.gravity = Gravity.RIGHT;
//        textView.setLayoutParams(layoutParams);
        middleViewGroup.addView(middleTextView);
    }

    private void addImageView(int resourceId) {
        middleImage = new ImageView(getContext());
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = 5;
        params.leftMargin = getPx(6);
        middleImage.setLayoutParams(params);
        middleImage.setImageResource(resourceId);

        middleViewGroup.addView(middleImage);
    }

    public ImageView getMiddleImage() {
        return middleImage;
    }

    public ViewGroup getMiddleView() {
        return middleViewGroup;
    }

    public TextView getMiddleTextView() {
        return middleTextView;
    }

    public ImageView getLeftView() {
        return leftView;
    }

    public ImageView getSharedView() {
        return sharedImage;
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public void setLeftView(Bitmap bitmap){
        if(bitmap != null){
            leftView.setVisibility(View.VISIBLE);
            leftView.setImageBitmap(bitmap);
        } else {
            leftView.setVisibility(View.GONE);
        }
    }

    public void setLeftView(int imageId){
        if(imageId > 0){
            leftView.setVisibility(View.VISIBLE);
            leftView.setImageResource(imageId);
        } else {
            leftView.setVisibility(View.GONE);
        }
    }

    public void setLeftView(String leftText){
        if(StringUtils.isNotBlank(leftText)){
            leftTextView.setVisibility(View.VISIBLE);
            leftTextView.setText(leftText);
        } else{
            leftTextView.setVisibility(View.GONE);
        }
    }


    public ImageView getRightView() {
        return rightView;
    }
    public TextView getRightTextViewView() {
        return rightTextView;
    }
    public TextView getRightTextView(){
        return rightTextView;
    }

    public void setRightView(Bitmap bitmap){
        if(bitmap != null){
            rightView.setVisibility(View.VISIBLE);
            rightView.setImageBitmap(bitmap);
        } else {
            rightView.setVisibility(View.GONE);
        }
    }

    public void setRightView(int imageId){
        if(imageId > 0){
            rightView.setVisibility(View.VISIBLE);
            rightView.setImageResource(imageId);
        } else {
            rightView.setVisibility(View.GONE);
        }
    }

    public void setRightView(String text) {
        if(StringUtils.isNotBlank(text)){
            rightTextView.setVisibility(View.VISIBLE);
            rightTextView.setText(text);
        } else{
            rightTextView.setVisibility(View.GONE);
        }
    }

    public View getRightNextView() {
        return rightNextView;
    }

    public TextView getRightNextTextView(){
        return rightNextTextView;
    }

    public void setRightNextView(Bitmap bitmap){
        if(bitmap != null){
            rightNextView.setVisibility(View.VISIBLE);
            rightNextView.setImageBitmap(bitmap);
        } else {
            rightNextView.setVisibility(View.GONE);
        }
    }

    public void setRightNextView(int imageId){
        if(imageId > 0){
            rightNextView.setVisibility(View.VISIBLE);
            rightNextView.setImageResource(imageId);
        } else {
            rightNextView.setVisibility(View.GONE);
        }
    }

    public void setRightNextView(String text) {
        if(StringUtils.isNotBlank(text)){
            rightNextTextView.setVisibility(View.VISIBLE);
            rightNextTextView.setText(text);
        } else{
            rightNextTextView.setVisibility(View.GONE);
        }
    }

    public View getRightThreeView() {
        return rightThreeView;
    }

    public TextView getRightThreeTextView(){
        return rightThreeTextView;
    }

    public void setRightThreeView(Bitmap bitmap){
        if(bitmap != null){
            rightThreeView.setVisibility(View.VISIBLE);
            rightThreeView.setImageBitmap(bitmap);
        } else {
            rightThreeView.setVisibility(View.GONE);
        }
    }

    public void setRightThreeView(int imageId){
        if(imageId > 0){
            rightThreeView.setVisibility(View.VISIBLE);
            rightThreeView.setImageResource(imageId);
        } else {
            rightThreeView.setVisibility(View.GONE);
        }
    }

    public void setRightThreeView(String text) {
        if(StringUtils.isNotBlank(text)){
            rightThreeTextView.setVisibility(View.VISIBLE);
            rightThreeTextView.setText(text);
        } else{
            rightThreeTextView.setVisibility(View.GONE);
        }
    }

    public RelativeLayout getTitle_setting_relative() {
        return title_setting_relative;
    }

    public void setTitle_setting_relative(RelativeLayout title_setting_relative) {
        this.title_setting_relative = title_setting_relative;
    }

    public void setPromptMsg(String msg){
        promptText.setText(msg);
    }

    public void checkDefaultSmsApp(){
        if (!DeviceUtils.isDefaultMessageApp(context)) {
            title_setting_relative.setVisibility(View.VISIBLE);
        } else {
            title_setting_relative.setVisibility(View.GONE);
        }
    }

    private void setupPromptLayout(){
        if (!DeviceUtils.isDefaultMessageApp(context)) {
            title_setting_relative.setVisibility(View.VISIBLE);
        } else {
            title_setting_relative.setVisibility(View.GONE);
        }
        delIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_setting_relative.setVisibility(View.GONE);
            }
        });
        String setting_text = "使用短信功能，需设为默认应用。点此设置";
        SpannableStringBuilder style = new SpannableStringBuilder(setting_text);
        style.clearSpans();
//        SettingClickSpan span = new SettingClickSpan(context, SettingClickSpan.SettingType.MSG_TYPE);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#47b520"));
        style.setSpan(span, setting_text.length() - 4, setting_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        promptText.setText(style);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
        promptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_setting_relative.setVisibility(View.GONE);
                DeviceUtils.setDefaultMessageApp(context);
            }
        });
    }

    public int getPx(int dp) {
        float deviceDs = 0;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        deviceDs = dm.density;
        return (int) (dp * deviceDs);
    }
}
