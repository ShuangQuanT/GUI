package com.mr.draw; //类所在的包名

import com.mr.util.DrawImageUtil;
import com.mr.util.FrameGetShape;
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/*画图主窗体*/
public class DrawPictureFrame extends JFrame implements FrameGetShape{//继承窗体类

    private JToolBar toolBar;//工具栏
    private JButton eraserButton;//橡皮按钮
    private JToggleButton strokeButton1;//细线按钮
    private JToggleButton strokeButton2;//粗线按钮
    private JToggleButton strokeButton3;//较粗按钮
    private JButton backgroundButton;//背景按钮
    private JButton foregroundButton;//前景按钮
    private JButton clearButton;//清楚按钮
    private JButton saveButton;//保存按钮
    private JButton shapeButton;//图像按钮

    //菜单
    private JMenuItem strokeMenuItem1;// 细线菜单
    private JMenuItem strokeMenuItem2;// 粗线菜单
    private JMenuItem strokeMenuItem3;// 较粗菜单
    private JMenuItem clearMenuItem;// 清除菜单
    private JMenuItem foregroundMenuItem;// 前景色菜单
    private JMenuItem backgroundMenuItem;// 背景色菜单
    private JMenuItem eraserMenuItem;// 橡皮菜单
    private JMenuItem exitMenuItem;// 退出菜单
    private JMenuItem saveMenuItem;// 保存菜单

    //水印
    private JMenuItem shuiyinMenuItem;// 水印菜单
    private String shuiyin = "";// 水印字符内容

    //简笔画
    private PictureWindow picWindow;//简笔画窗体
    private JButton showPicButton;//展示简笔画按钮

    boolean drawShape = false;//画图形标识变量
    Shapes shape;//绘图的形状

    int x = -1;//上一次鼠标绘制点的横坐标
    int y = -1;//上一次鼠标绘制点的纵坐标
    boolean rubber = false;



    BufferedImage image= new BufferedImage(570,390,BufferedImage.TYPE_INT_BGR);
    Graphics gs=image.getGraphics();//获得图像的绘图对象
    Graphics2D g=(Graphics2D) gs;//将绘图对象转化为Graohics2D类型
    DrawPictureCanvas canvas=new DrawPictureCanvas();//创建画布对象
    Color foreColor = Color.BLACK;//定义前景色
    Color backgroundColor = Color.WHITE;//定义背景色


    /**
     * 构造方法。
     */
    public DrawPictureFrame() {
        setResizable(false);//窗体不能改变大小setTitle("画图程序");//设置标题
        setTitle("画图");//标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗体关闭则停止程序setBounds(500,100,574, 460);//设置窗口位置和宽高
        setBounds(500, 100, 574, 460);
        init();//组件初始化
    }// DrawPictureFrame()结束

    /**
     * FrameGetShape接口实现类,用于获取空间返回的被选中的图形
     */
    @Override
    public void getShape(Shapes shape){
        this.shape = shape;//将返回的图形对象赋给类变量

        drawShape = true;//画图形标识变量为true,说明现在鼠标画的是图形,而不是线
    }//getShape()结束.

    /**
     * 恢复展开简笔画按钮的文本内容,此方法供简笔画隐藏按钮调用
     *
     */
    public void initShowPicbutton(){
        showPicButton.setText("展开简笔画");//修改简笔画窗口的隐藏按钮;
    }

    /**
     * 组件初始化
     */
    private void init(){
        g.setColor(backgroundColor);//将背景色设置绘图对象颜色
        g.fillRect(0,0,570,390);
        g.setColor(foreColor);//将前景色设置绘图对象的颜色
        canvas.setImage(image);//设置画布的图像
        getContentPane().add(canvas);//将画布添加到窗体容器默认布局的中部位置


        toolBar = new JToolBar();//初始化工具栏
        getContentPane().add(toolBar,BorderLayout.NORTH);//工具栏添加到窗口最北位置

        saveButton = new JButton();//初始化按钮对象,并添加文本内容
        saveButton.setToolTipText("保存");
        saveButton.setIcon(new ImageIcon("src/icon/保存.png"));
        toolBar.add(saveButton);//工具栏添加按钮
        toolBar.addSeparator();//添加分割线
        //初始化有选择状态的按钮对象,并添加文本内容
        strokeButton1 = new JToggleButton();
        strokeButton1.setToolTipText("细线");
        strokeButton1.setIcon(new ImageIcon("src/icon/1像素线条.png"));
        strokeButton1.setSelected(true);//细线按钮处于选择状态
        toolBar.add(strokeButton1);//添加strokeButton1按钮到工具栏

        //初始化有选择状态的按钮对象,并添加文本内容
        strokeButton2 = new JToggleButton();
        strokeButton2.setToolTipText("粗线");
        strokeButton2.setIcon(new ImageIcon("src/icon/2像素线条.png"));
        strokeButton2.setSelected(true);//细线按钮处于选择状态
        toolBar.add(strokeButton2);//添加strokeButton2按钮到工具栏

        //初始化有选择状态的按钮对象,并添加文本内容
        strokeButton3 = new JToggleButton();
        strokeButton3.setToolTipText("较粗");
        strokeButton3.setIcon(new ImageIcon("src/icon/4像素线条.png"));
        strokeButton3.setSelected(true);//细线按钮处于选择状态
        toolBar.add(strokeButton3);//添加strokeButton3按钮到工具栏

        toolBar.addSeparator();//添加分割
        //画笔粗细按钮组,保证同时只有一个按钮被选中
        ButtonGroup strokeGroup = new ButtonGroup();
        strokeGroup.add(strokeButton1);//按钮组添加按钮
        strokeGroup.add(strokeButton2);//按钮组添加按钮
        strokeGroup.add(strokeButton3);//按钮组添加按钮

        backgroundButton = new JButton();//初始化按钮对象,并添加文本内容
        backgroundButton.setToolTipText("背景颜色");
        backgroundButton.setIcon(new ImageIcon("src/icon/背景色.png"));
        toolBar.add(backgroundButton);//工具栏添加按钮

        foregroundButton = new JButton();//初始化按钮对象,并添加文本内容
        foregroundButton.setToolTipText("前景颜色");
        foregroundButton.setIcon(new ImageIcon("src/icon/前景色.png"));
        toolBar.add(foregroundButton);//工具栏添加按钮
        toolBar.addSeparator();//添加分割条

        clearButton = new JButton();//初始化按钮对象,并添加文本内容
        clearButton.setToolTipText("清除");
        clearButton.setIcon(new ImageIcon("src/icon/清除.png"));
        toolBar.add(clearButton);//工具栏添加按钮

        eraserButton = new JButton();//初始化按钮对象,并添加文本内容
        eraserButton.setToolTipText("橡皮");
        eraserButton.setIcon(new ImageIcon("src/icon/橡皮.png"));
        toolBar.add(eraserButton);//工具栏添加按钮

        shapeButton = new JButton();//初始按钮对象,添加文本内容
        shapeButton.setToolTipText("形状");
        shapeButton.setIcon(new ImageIcon("src/icon/形状.png"));
        toolBar.add(shapeButton);//添加工具栏

        //简笔画按钮
        showPicButton = new JButton();
        showPicButton.setToolTipText("展开简笔画");
        showPicButton.setIcon(new ImageIcon("src/icon/展开.png"));
        toolBar.add(showPicButton);

        //创建简笔画展示面板,将本类作为父亲
        picWindow = new PictureWindow(this);

        //菜单
        menu();

        addListener();//添加组件监听
    }//init()结束


    /**
     * 为组件添加动作监听器
     */
    private void addListener(){
        //简笔画
        showPicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isVisible = picWindow.isVisible();//获取可见状态
                if(isVisible){
                    showPicButton.setText("展示简笔画");//修改按钮文本
                    picWindow.setVisible(false);//隐藏简笔画
                }else {//如果是隐藏的
                    showPicButton.setText("隐藏简笔画");
                    //重新指定简笔画显示的位置
                    //横坐标=主窗体-简笔画宽-5
                    //纵=主窗体纵
                    picWindow.setLocation(getX()-5-picWindow.getWidth(),getY());
                    picWindow.setVisible(true);

                }
            }
        });
        //画板添加移动事件监听
        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e)  {
                if(x > 0 && y > 0) {//如果x和y存在鼠标记录
                    if (rubber) {//橡皮标识为true,表示使用橡皮
                        g.setColor(backgroundColor);//绘图工具使用背景色
                        g.fillRect(x, y, 10, 10);//在鼠标划过的位置填充正方形
                    } else {//如果表示为false,表示用画笔画图
                        g.drawLine(x, y, e.getX(), e.getY());//在鼠标划过的位置画直线
                    }//else结束
                }//if结束
                    x = e.getX();//上一次鼠标绘制点的横坐标
                    y = e.getY();//上一次鼠标绘制点的纵坐标
                    canvas.repaint();//更新画布
                }//mouseDragged()结束
            //鼠标
            public void mouseMoved(final MouseEvent arg0) {// 当鼠标移动时
                if (rubber) {// 如果使用橡皮
                    // 设置鼠标指针的形状为图片
                    Toolkit kit = Toolkit.getDefaultToolkit();// 获得系统默认的组件工具包
                    Image img = kit.createImage("src/icon/鼠标橡皮.png");// 利用工具包获取图片

                    // 利用工具包创建一个自定义的光标对象
                    // 参数为图片，光标热点(写成0,0就行)和光标描述字符串
                    Cursor c = kit.createCustomCursor(img, new Point(0, 0), "clear");
                    setCursor(c);// 使用自定义的光标
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));// 设置鼠标指针的形状为十字光标
                } // else结束
            }// mouseMoved()结束
        });//canvas.addMouseMotionListener(new MouseAdapter()结束

        strokeButton1.addActionListener(new ActionListener() {//细线,按钮添加动作监听
            @Override
            public void actionPerformed(final ActionEvent args0) {//单击时
                //声明画笔属性,粗线为1像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
            }//actionPerformed(final ActionEvent args0)结束
        });//strokeButton1.addActionListener(new ActionListener()结束

        strokeButton2.addActionListener(new ActionListener() {//细线,按钮添加动作监听
            @Override
            public void actionPerformed(final ActionEvent args0) {//单击时
                //声明画笔属性,粗线为2像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
            }//actionPerformed(final ActionEvent args0)结束
        });//strokeButton1.addActionListener(new ActionListener()结束

        strokeButton3.addActionListener(new ActionListener() {//细线,按钮添加动作监听
            @Override
            public void actionPerformed(final ActionEvent args0) {//单击时
                //声明画笔属性,粗线为4像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
            }//actionPerformed(final ActionEvent args0)结束
        });//strokeButton1.addActionListener(new ActionListener()结束

        backgroundButton.addActionListener(new ActionListener() {//背景颜色按钮添加动作监听
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (bgColor != null) {//如果选中的颜色不是空的
                    backgroundColor = bgColor;//将选择的颜色赋给背景变量
                }
                //背景色按钮也更换为这种颜色
                backgroundButton.setBackground(backgroundColor);
                g.setColor(backgroundColor);//绘图工具使用背景色
                g.fillRect(0,0,570,390);//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);//绘图工具使用前景色
                canvas.repaint();//更新画布
            }//    actionPerformed()结束
        });//backgroundButton.addActionListener(new ActionListener()结束
        foregroundButton.addActionListener(new ActionListener() {//前景色
            @Override
            public void actionPerformed(ActionEvent e) {//单击
                //打开选择颜色对话框,参数依次为父窗体,标题,默认选择颜色(青色)
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
                if(fColor != null){
                    foreColor = fColor;//将选中的颜色赋给前景变量
                }
                //前景色按钮的文字也要更换为这种颜色
                foregroundButton.setForeground(foreColor);
                g.setColor(foreColor);//绘图工具使用前景色
            }//actionPerformed()
        });//foregroundButton.addActionListener()
        clearButton.addActionListener(new ActionListener() {//清除按钮添加动作监听
            @Override
            public void actionPerformed(final ActionEvent e) {//单击时
                g.setColor(backgroundColor);//绘制工具使用背景色
                g.fillRect(0,0,570,390);//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);//绘图工具使用前景色
                canvas.repaint();//更新画布

            }//addActionListener()
        });//clearButton.addActionListener

        eraserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent arg0){//单击时
                //单击工具栏上的橡皮按钮,使用橡皮
                if(rubber){//如果菜单是橡皮
                    eraserButton.setToolTipText("橡皮");
                    eraserButton.setIcon(new ImageIcon("src/icon/橡皮.png"));
                    eraserMenuItem.setText("橡皮");
                    g.setColor(foreColor);
                    rubber = false;

                }else {
                    eraserMenuItem.setText("画图");
                    eraserButton.setToolTipText("画图");
                    //按钮图标
                    eraserButton.setIcon(new ImageIcon("src/icon/画笔.png"));
                    g.setColor(backgroundColor);//设置绘图对象的前景色
                    rubber = true;//橡皮识别为true

                }

            }//actionPerformed()结束
        });//eraserButton.addActionListener()结束


        shapeButton.addActionListener(new ActionListener() {//图像按钮添加动作监听
            @Override
            public void actionPerformed(ActionEvent e) {//单击时
                //创建图形组件
                ShapeWindow shapeWindow= new ShapeWindow((FrameGetShape) DrawPictureFrame.this);
                int shapeButtonWidth = shapeButton.getWidth();//获取图像按钮宽度
                int shapeWindowWidth = shapeWindow.getWidth();//获取图像按钮高度
                int shapeButtonX = shapeButton.getX();//获取图像按钮横坐标
                int shapeButtonY = shapeButton.getY();//获取图像按钮Y坐标
                //计算图像组件横坐标,让组件与图形按钮居中对齐
                int shaoeWindowX = getX() + shapeButtonX - (shapeWindowWidth-shapeButtonWidth)/2;//getX(),getY()是当前窗体相对屏幕的位置
                ////计算图像组件纵坐标,让组件显示图形按钮下方
                int shapeWindowY = getY()+shapeButtonY+80;
                //设置图形组件坐标位置
                shapeWindow.setLocation(shaoeWindowX,shapeWindowY);
                shapeWindow.setVisible(true);//图形组件可见
            }//actionPerformed()
        });//shapeButton.addActionListener()

        canvas.addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent arg0){
                x = -1;//将记录上次鼠标绘制点的横坐标恢复为-1
                y = -1;//将记录上次鼠标绘制点的y坐标恢复为-1
            }
            @Override
            public void mousePressed(MouseEvent e) {//鼠标按下时
                if(drawShape){//如果此时鼠标画的是图形

                    switch(shape.getType()){//判断图形的种类
                        case Shapes.YUAN://如果是圆形
                            //计算坐标,让鼠标处于图形的中心位置
                            int yuanX = e.getX()-shape.getWidth()/2;
                            int yuanY = e.getY()-shape.getHeigth()/2;
                            //创建圆形图形,并指定坐标和宽高
                            Ellipse2D yuan = new Ellipse2D.Double(yuanX,yuanY,shape.getWidth(),shape.getHeigth());
                            g.draw(yuan);//画图工具画此圆形
                            break;//结束switch
                        case Shapes.FANG://如果是方形
                            //计算坐标,让鼠标处于图形的中心位置
                            int fangX = e.getX()-shape.getWidth()/2;
                            int fangY = e.getY()-shape.getHeigth()/2;
                            //创建方形图形,指定坐标和宽高
                            Rectangle2D fang = new Rectangle2D.Double(fangX,fangY,shape.getWidth(),shape.getHeigth());
                            g.draw(fang);//画图工具画此方形
                            break;
                    }//switch
                    canvas.repaint();//更新画布
                    drawShape = false;
                }//if
            }//mousePressed()
        });//canvas.addMouseMotionListener()

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {//点击时
                addWatermark();
                DrawImageUtil.saveImage(DrawPictureFrame.this,image);//打印图片
            }
        });

        //菜单栏
        eraserMenuItem.addActionListener(new ActionListener() {// 橡皮按钮添加动作监听
            public void actionPerformed(final ActionEvent e) {	// 单击时
                //单击工具栏上的橡皮按钮,使用橡皮
                if(rubber){//如果菜单是橡皮
                    System.out.println(rubber);
                    eraserButton.setToolTipText("橡皮");
                    eraserButton.setIcon(new ImageIcon("src/icon/橡皮.png"));
                    eraserMenuItem.setText("橡皮");
                    g.setColor(foreColor);
                    rubber = false;

                }else {
                    System.out.println(rubber);

                    eraserMenuItem.setText("画图");
                    eraserButton.setToolTipText("画图");
                    //按钮图标
                    eraserButton.setIcon(new ImageIcon("src/icon/画笔.png"));
                    g.setColor(backgroundColor);//设置绘图对象的前景色
                    rubber = true;//橡皮识别为true

                }
            }// actionPerformed()结束
        });// eraserButton.addActionListener()结束

        //菜单
        exitMenuItem.addActionListener(new ActionListener() {	// 退出菜单栏添加动作监听
            public void actionPerformed(final ActionEvent e) {	// 单击时
                System.exit(0);								// 程序关闭
            }/// actionPerformed()结束
        });// exitMenuItem.addActionListener()结束



        //保存
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWatermark();
                DrawImageUtil.saveImage(DrawPictureFrame.this,image);//打印图片
            }
        });
        strokeMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //声明画笔属性,粗线为1像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
                strokeButton1.setSelected(true);
            }
        });
        strokeMenuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //声明画笔属性,粗线为1像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
                strokeButton2.setSelected(true);
            }
        });
        strokeMenuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //声明画笔属性,粗线为1像素,线条末端无修饰,折线处呈现尖角
                BasicStroke bs = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
                g.setStroke(bs);//画图工具使用此画笔
                strokeButton3.setSelected(true);

            }
        });
        //前景
        foregroundMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开选择颜色对话框,参数依次为父窗体,标题,默认选择颜色(青色)
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this,"选择颜色对话框",Color.CYAN);
                if(fColor != null){
                    foreColor = fColor;//将选中的颜色赋给前景变量
                }
                //前景色按钮的文字也要更换为这种颜色
                foregroundButton.setForeground(foreColor);
                g.setColor(foreColor);//绘图工具使用前景色
            }
        });
        backgroundMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (bgColor != null) {//如果选中的颜色不是空的
                    backgroundColor = bgColor;//将选择的颜色赋给背景变量
                }
                //背景色按钮也更换为这种颜色
                backgroundButton.setBackground(backgroundColor);
                g.setColor(backgroundColor);//绘图工具使用背景色
                g.fillRect(0,0,570,390);//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);//绘图工具使用前景色
                canvas.repaint();//更新画布
            }
        });
        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.setColor(backgroundColor);//绘制工具使用背景色
                g.fillRect(0,0,570,390);//画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);//绘图工具使用前景色
                canvas.repaint();//更新画布
            }
        });

        //水印
        shuiyinMenuItem.addActionListener(new ActionListener() {// 水印菜单项添加动作监听
            public void actionPerformed(ActionEvent e) {//单击时
                // 弹出输入对话框
                shuiyin = JOptionPane.showInputDialog(DrawPictureFrame.this,"你想添加什么水印？");
                if (null == shuiyin) {// 如果输入对话框返回的是null
                    shuiyin = "";// 字符串设为空内容
                } else {// 如果不是null
                    setTitle(shuiyin);   // 修改窗体标题
                } // else结束
            }// actionPerformed()结束
        });// shuiyinMenuItem.addActionListener()结束

        toolBar.addMouseMotionListener(new MouseMotionAdapter() {// 工具栏添加鼠标移动监听
            public void mouseMoved(final MouseEvent arg0) {// 当鼠标移动时
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// 设置鼠标指针的形状为默认光标
            }// mouseMoved()
        });// toolBar.addMouseMotionListener()结束

    }//addListener()

    /**
     * 添加水印。
     */
    private void addWatermark() {
        if (!"".equals(shuiyin.trim())) {// 如果水印字段不是空字符串
            g.rotate(Math.toRadians(-30));// 将图片旋转-30弧度
            Font font = new Font("楷体", Font.BOLD, 72);// 设置字体
            g.setFont(font);// 载入字体
            g.setColor(Color.gray);// 使用灰色
            AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.4f);// 设置透明效果0.4
            g.setComposite(alpha);// 使用透明效果
            g.drawString(shuiyin,150,500);// 绘制水印文字
            canvas.repaint();// 面板重绘
            g.rotate(Math.toRadians((30)));// 将旋转的图片再转回来
            alpha = AlphaComposite.SrcOver.derive(1f);// 不透明效果
            g.setComposite(alpha);// 使用不透明效果
            g.setColor(foreColor);// 画笔恢复之前颜色
        } // if结束
    }// addWatermark() 结束


    //菜单
    public  void menu(){
        //菜单
        JMenuBar menuBar = new JMenuBar();// 创建菜单栏
        setJMenuBar(menuBar);// 窗体载入菜单栏

        JMenu systemMenu = new JMenu("系统");// 初始化菜单对象，并添加文本内容
        menuBar.add(systemMenu);// 菜单栏添加菜单对象
        saveMenuItem = new JMenuItem("保存");// 初始化菜单项对象，并添加文本内容
        systemMenu.add(saveMenuItem);// 菜单添加菜单项
        systemMenu.addSeparator();// 添加分割条
        exitMenuItem = new JMenuItem("退出");// 初始化菜单项对象，并添加文本内容
        systemMenu.add(exitMenuItem);// 菜单添加菜单项

        JMenu systemMenu1 = new JMenu("线型");// 初始化菜单对象，并添加文本内容
        menuBar.add(systemMenu1);// 菜单栏添加菜单对象
        strokeMenuItem1 = new JMenuItem("细线");// 初始化菜单项对象，并添加文本内容
        systemMenu1.add(strokeMenuItem1);// 菜单添加菜单项
        systemMenu1.addSeparator();// 添加分割条
        strokeMenuItem2 = new JMenuItem("粗线");// 初始化菜单项对象，并添加文本内容
        systemMenu1.add(strokeMenuItem2);// 菜单添加菜单项
        systemMenu1.addSeparator();// 添加分割条
        strokeMenuItem3 = new JMenuItem("较粗");// 初始化菜单项对象，并添加文本内容
        systemMenu1.add(strokeMenuItem3);// 菜单添加菜单项

        JMenu systemMenu2 = new JMenu("颜色");// 初始化菜单对象，并添加文本内容
        menuBar.add(systemMenu2);// 菜单栏添加菜单对象
        foregroundMenuItem = new JMenuItem("前景颜色");// 初始化菜单项对象，并添加文本内容
        systemMenu2.add(foregroundMenuItem);// 菜单添加菜单项
        systemMenu2.addSeparator();// 添加分割条
        backgroundMenuItem = new JMenuItem("后景颜色");// 初始化菜单项对象，并添加文本内容
        systemMenu2.add(backgroundMenuItem);// 菜单添加菜单项

        JMenu systemMenu3 = new JMenu("编辑");// 初始化菜单对象，并添加文本内容
        menuBar.add(systemMenu3);// 菜单栏添加菜单对象
        clearMenuItem = new JMenuItem("清除");// 初始化菜单项对象，并添加文本内容
        systemMenu3.add(clearMenuItem);// 菜单添加菜单项
        systemMenu3.addSeparator();// 添加分割条
        eraserMenuItem = new JMenuItem("橡皮 ");// 初始化菜单项对象，并添加文本内容
        systemMenu3.add(eraserMenuItem);// 菜单添加菜单项

        shuiyinMenuItem = new JMenuItem("水印");// 初始化菜单项对象，并添加文本内容
        systemMenu.add(shuiyinMenuItem);// 菜单添加菜单项

    }

}
//DrawPictureFrame类结束