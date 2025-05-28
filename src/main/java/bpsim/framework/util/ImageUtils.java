/*=================================================================================
 *@Copyright 2016 Kicompany all rights reserved.
 *@File			:	ImageUtils.java
 *@FileName		:	ImageUtils
 *@CreateDate	:	2016. 6. 16.
 *@author		:	황진성
 =================================================================================*/

package bpsim.framework.util;

import java.io.File;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 이미지 유틸
 * 지정된 크기로 이미지 사이즈를 조절합니다.
 * <p>FileName	:	ImageUtils.java </p>
 *
 * @author 황진성
 */
public class ImageUtils {
    public static final int RATIO = 0;
    public static final int SAME = -1;

    /**
     * 파일 크기를 변경합니다.<br>
     * 넓이와 높이중 최대값을 기준으로 비율에 맞게 파일 크기를 변경합니다.<br>
     *
     * @param src     소스파일
     * @param dest    타겟파일
     * @param boxsize 최대값
     * @throws IOException
     */
    public static void resizeImage(File src, File dest, int boxsize) throws IOException {
        int width = 0;
        int height = 0;
        Image srcImg = setImage(src);
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);

        if (srcWidth > srcHeight) {
            width = boxsize;
            height = (int) ((double) boxsize / (double) srcWidth);
        } else if (srcWidth < srcHeight) {
            width = (int) ((double) boxsize / (double) srcHeight);
            height = boxsize;
        } else {
            width = boxsize;
            height = boxsize;
        }

        if (srcWidth <= boxsize && srcHeight <= boxsize)
            resizeImage(src, dest, -1, -1);
        else
            resizeImage(src, dest, width, height);
    }

    /**
     * 파일 크기를 변경합니다.<br>
     * 지정된 가로x세로 크기로 파일 크기를 변경합니다.
     *
     * @param src    소스파일
     * @param dest   타겟파일
     * @param width  넓이값
     * @param height 높이값
     * @throws IOException
     */
    public static void resizeImageFix(File src, File dest, int width, int height) throws IOException {
        Image srcImg = setImage(src);
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        int outWidth = width;
        int outHeight = height;

        if (outWidth < 1 && outHeight < 1) {
            outWidth = srcWidth;
            outHeight = srcHeight;
        } else if (outWidth < 1) {
            outWidth = (int) ((double) srcWidth * ((double) outHeight / (double) srcHeight));
            outHeight = srcHeight;
        } else if (outHeight < 1) {
            outWidth = srcWidth;
            outHeight = (int) ((double) srcHeight * ((double) outWidth / (double) srcWidth));
        }

        if (srcWidth <= outWidth || srcHeight <= outHeight) {
            width = -1;
            height = -1;
        }

        resizeImage(src, dest, width, height);
    }
    
    /**
     * 파일 크기를 변경합니다.<br>
     * 지정된 가로x세로 크기로 파일 크기를 변경합니다.
     *
     * @param src    소스파일
     * @param dest   타겟파일
     * @param width  넓이값
     * @param height 높이값
     * @throws IOException
     */
    public static void resizeImageFixCommty(File src, File dest, int width, int height) throws IOException {
        Image srcImg = setImage(src);
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        int outWidth = width;
        int outHeight = height;

        if (outWidth < 1 && outHeight < 1) {
            outWidth = srcWidth;
            outHeight = srcHeight;
        } else if (outWidth < 1) {
            outWidth = (int) ((double) srcWidth * ((double) outHeight / (double) srcHeight));
            outHeight = srcHeight;
        } else if (outHeight < 1) {
            outWidth = srcWidth;
            outHeight = (int) ((double) srcHeight * ((double) outWidth / (double) srcWidth));
        }

        resizeImage(src, dest, width, height);
    }

    /**
     * 파일 크기를 변경합니다.<br>
     * 넓이값을 기준으로 비율에 맞게 파일 크기를 변경합니다.
     *
     * @param src     소스파일
     * @param dest    타겟파일
     * @param boxsize 넓이값
     * @throws IOException
     */
    public static void resizeImageFixWidth(File src, File dest, int boxsize) throws IOException {
        Image srcImg = setImage(src);
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        int width = boxsize;
        int height = (int) ((double) srcHeight * ((double) boxsize / (double) srcWidth));

        if (srcWidth <= boxsize) {
            width = -1;
            height = -1;
        }

        resizeImage(src, dest, width, height);
    }

    /**
     * 파일 크기를 변경합니다.<br>
     * 높이값을 기준으로 비율에 맞게 파일 크기를 변경합니다.
     *
     * @param src     소스파일
     * @param dest    타겟파일
     * @param boxsize 높이값
     * @throws IOException
     */
    public static void resizeImageFixHeight(File src, File dest, int boxsize) throws IOException {
        Image srcImg = setImage(src);
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        int width = (int) ((double) srcWidth * ((double) boxsize / (double) srcHeight));
        int height = boxsize;

        if (srcHeight <= boxsize) {
            width = -1;
            height = -1;
        }

        resizeImage(src, dest, width, height);
    }

    /**
     * 파일 크기를 변경합니다.<br>
     * 높이와 넓이값에 맞도록 파일 크기를 변경합니다.
     *
     * @param src    소스파일
     * @param dest   타겟파일
     * @param width  넓이값
     * @param height 높이값
     * @throws IOException
     */
    public static void resizeImage(File src, File dest, int width, int height) throws IOException {
        Image srcImg = setImage(src);

        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);
        int destWidth = -1;
        int destHeight = -1;

        if (width == SAME) destWidth = srcWidth;
        else if (width > 0) destWidth = width;

        if (height == SAME) destHeight = srcHeight;
        else if (height > 0) destHeight = height;

        if (width == RATIO && height == RATIO) {
            destWidth = srcWidth;
            destHeight = srcHeight;
        } else if (width == RATIO) {
            double ratio = ((double) destHeight) / ((double) srcHeight);
            destWidth = (int) ((double) srcWidth * ratio) - 1;
        } else if (height == RATIO) {
            double ratio = ((double) destWidth) / ((double) srcWidth);
            destHeight = (int) ((double) srcHeight * ratio) - 1;
        }

        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }

        BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
        ImageIO.write(destImg, "jpg", dest);
    }

    private static Image setImage(File src) throws IOException {
        Image srcImg = null;
        String suffix = src.getName().substring(src.getName().lastIndexOf('.') + 1).toLowerCase();
        if (suffix.equals("bmp")) srcImg = ImageIO.read(src);
        else srcImg = new ImageIcon(src.toURI().toURL()).getImage();
        return srcImg;
    }
}