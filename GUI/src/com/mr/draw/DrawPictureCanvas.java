package com.mr.draw;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 简笔画展示窗体
 */
public class DrawPictureCanvas extends Canvas {
    private Image image=null;
    /**
     * 设置画板中的图片
     * @Param image - 画板子展示图片对象
     */
    public void setImage(Image image){
        this.image=image;

    }//setImage(Image image)结束

    /**
     * 重写paint()方法,在画布上绘制图像
     * @param g
     */
    public void  paint(Graphics g){
            g.drawImage(image,0,0,null);//画布上绘制图像

    }//paint(Graphics g)结束
    /**
     * 重写update()方法,这样可以解决屏幕闪烁问题
     */
    public void update(Graphics g){
        paint(g);

    }//update(Graphics g)结束




}//DrawPictureCanvas结束
