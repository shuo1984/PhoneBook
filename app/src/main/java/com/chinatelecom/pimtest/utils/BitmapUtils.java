package com.chinatelecom.pimtest.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author lichao
 * @since 18/12/2014
 * desc:
 */
public class BitmapUtils {
    //绘制图片的位置
    public enum DrawPosition {
        LEFTTOP,
        LEFTBOTTOM,
        RIGHTTOP,
        RIGHTBOTTOM,
        CENTER,
        CENTER_SMS,
        RIGHTBOTTOM_PHOTO
    }

    //在指定的位图上添加图标
    public static Bitmap addMarkImage(Bitmap source, Bitmap markImg, DrawPosition drawPosition) {
        //创建一个和原图同样大小的位图
        //Bitmap newBitmap = Bitmap.createBitmap(imgMapWidth,imgMapHeight, Bitmap.Config.RGB_565);
        Bitmap newBitmap = source;
        if (!newBitmap.isMutable()) {
            newBitmap = source.copy(Bitmap.Config.ARGB_8888, true);
        }
        int left = 0;
        int top = 0;
        switch (drawPosition) {
            case LEFTTOP:
                left = -2;
                top = -2;
                break;
            case LEFTBOTTOM:
            case RIGHTTOP:
            case CENTER:
                left = Math.round(source.getWidth() / 2) - Math.round(markImg.getWidth() / 2);
                top = Math.round(source.getHeight() / 2) - Math.round(markImg.getHeight() / 2);
                break;
            case CENTER_SMS:
                left = Math.round(source.getWidth() / 2) - Math.round(markImg.getWidth() / 2);
                top = Math.round(source.getHeight() / 2) - Math.round(markImg.getHeight() / 2);
                break;
            case RIGHTBOTTOM:
                left = source.getWidth() - markImg.getWidth() - 2;
                top = source.getHeight() - markImg.getHeight() - 2;
                break;
            case RIGHTBOTTOM_PHOTO:
                left = source.getWidth() - markImg.getWidth() - 2;
                top = source.getHeight() - markImg.getHeight() - 2;
                break;

        }

        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        paint.setAlpha(0x80);
        canvas.drawBitmap(markImg, left, top, new Paint());//插入图标
        canvas.save(Canvas.ALL_SAVE_FLAG);
        //存储新合成的图片
        canvas.restore();
        return newBitmap;
    }

    /**
     * Drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565
                    );
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * bitmap 转 Drawable
     */
    public static Drawable bitmap2Drawble(Context mcontext, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

    /**
     * 生成缩略图
     *
     * @param original 原始图
     * @param width    宽度
     * @return 缩略图
     */
    public static Bitmap thumbnail(Bitmap original, double width) {
        int originalWidth = original.getWidth();
        double ratio = (width / originalWidth);
        double height = ratio * original.getHeight();
        return Bitmap.createScaledBitmap(original, (int) width, (int) height, false);
    }

    /**
     * 生成缩略图
     *
     * @param original 原始图
     * @param height   宽度
     * @return 缩略图
     */
    public static Bitmap thumbnail2(Bitmap original, double height) {
        int originalHeight = original.getHeight();
        double ratio = (height / originalHeight);
        double width = ratio * original.getWidth();
        return Bitmap.createScaledBitmap(original, (int) width, (int) height, false);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static Bitmap createShortPhoto(Bitmap bitmap) {
        Bitmap firstBitmap = zoomBitmap(bitmap, 84, 84);
        return decorateRoundCorner(42, firstBitmap);
    }


    /**
     * 生成缩略图
     *
     * @param original 原始图
     * @param width    宽度
     * @return 缩略图
     */
    public static Bitmap thumbnail(InputStream original, double width) {
        Bitmap bitmap = BitmapFactory.decodeStream(original);
        return thumbnail(bitmap, width);
    }


    /**
     * 获得bitmap对应得字节数组
     *
     * @param bitmap bitmap
     * @return array of byte
     */
    public static byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * bitmap转换成字节数组   无压缩
     * @param b
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static byte[] toByteArray2(Bitmap b) {
        int bytes = b.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
        byte[] array = buffer.array(); //Get the underlying array containing the data.

        return array;
    }

    /**
     * 圆角bitmap
     *
     * @param bitmap bitmap
     * @return round corner bitmap
     */
    public static Bitmap decorateRoundCorner(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 8;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 圆角bitmap
     *
     * @param bitmap bitmap
     * @return round corner bitmap
     */
    public static Bitmap decorateRoundCorner(int roundPx, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (output == null) {
            return null;
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获取椭圆形图片
     *
     * @param context
     * @param input    输入图片
     * @param pixels   椭圆度
     * @param squareTL 左上
     * @param squareTR 右上
     * @param squareBL 左下
     * @param squareBR 右下
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels, boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR) {
        int width = input.getWidth();
        int height = input.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);


        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels * densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        //draw rectangles over the corners we want to be square
        if (!squareTL) {
            canvas.drawRect(0, 0, width / 2, height / 2, paint);
        }
        if (!squareTR) {
            canvas.drawRect(width / 2, 0, width, height / 2, paint);
        }
        if (!squareBL) {
            canvas.drawRect(0, height / 2, width / 2, height, paint);
        }
        if (!squareBR) {
            canvas.drawRect(width / 2, height / 2, width, height, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//         paint.setXfermode(new AvoidXfermode(Color.WHITE, 255, AvoidXfermode.Mode.TARGET));
        canvas.drawBitmap(input, 0, 0, paint);

        return output;
    }

    public static Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
