/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bamnn;

/**
 *
 * @author gers
 */
import javax.swing.*;  
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel
{  
    int g_h=10,g_w=10;
    int width=50,height=50;
    int cornerradius;
    Image castle;
    Dimension size;
    protected int x1,y1;

    Color c1=new Color(255, 0, 0); 
    Rectangle rec;
    boolean b=false;
    boolean imboo=false;
    boolean roundb= false;

    Graphics g= this.getGraphics();
    protected int strokeSize = 1;
    protected Color shadowColor = Color.BLACK;
    boolean shadowed = false;

    public ImagePanel()  
    {     
        //super();
        setOpaque(false);
        setLayout(null);
        System.out.println("it executed");
    }

    public ImagePanel(int x, int y)
    {
        setSize(x, y);
    }

    public void setSize(int x,int y){
        width=x;
        height=y;
    }

    public int getheight(){
        return height;
    }

    public int getwidth(){
        return width;
    }

    public void setImagePanelBounds(
            int x, int y, int width, int height){
        x1=x;
        y1=y;
        this.width= width;
        this.height= height;
        System.out.println("6it executed");
    }

    public void setroundcorners(boolean b, int i){
        roundb=b;
        cornerradius=i;
        System.out.println("5it executed");
    }

    public void setImage(String s){
        imboo=true;
        size = new Dimension();
        castle = new ImageIcon(s).getImage();
        size.width = castle.getWidth(null);
        size.height = castle.getHeight(null);
        setPreferredSize(size);
        System.out.println("4it executed");
    }

    public void paint(Graphics gh){ 
        System.out.println("it executed p");
        {int x=this.getWidth();
        int j=20,a=20;
        Graphics2D g2= (Graphics2D)gh.create();
        { 
            g2.setColor(Color.WHITE);
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.SrcAtop);     
            rec= new Rectangle(x1, y1, width, height);
            //Start of If-else

            if(roundb){
                g2.setClip(new RoundRectangle2D.Float(
                        (int)rec.getX(),(int)rec.getY(), 
                            (int)rec.getWidth(),(int)rec.getHeight(),
                                            cornerradius, cornerradius));
                System.out.println("it executed");
            }
            // End of If-Else
            // Image condition  Starts
            if (imboo){
                g2.drawImage(castle, (int)rec.getX(),
                    (int)rec.getY(), (int)rec.getWidth(),
                                (int)rec.getHeight(), null);
                //g.drawImage(castle, (int)rec.getX(),(int)rec.getY(),  null);
            }
            // Image condition  Ends
            g2.setColor(Color.BLUE);
        }
        }
    }

    public static void main(String[]args)  
    {  
        ImagePanel t1=new ImagePanel();  
        JPanel jp1= new JPanel();
        jp1.add(t1);
        jp1.setLayout(null);
        jp1.setBounds(0, 0, 600, 600);
        JFrame jf1= new JFrame("Testing");
        t1.setImage("icons/1.png");
        //t1.setImage("1.jpg");
        t1.setLayout(null);
        t1.setroundcorners(true, 10);
        //t1.setShadow(true);
        t1.add(new JLabel("niak"));
        //t1.setShadowDimensions(18, 18, 305, 305, 12);
        t1.setImagePanelBounds(20, 20, 100, 100);
        // jf1.add(t1);
        jf1.setSize(600, 600);
        jf1.setDefaultCloseOperation(jf1.EXIT_ON_CLOSE);
        jf1.setVisible(true);
        //jf1.revalidate();
        jf1.setLayout(null);
    }  
}