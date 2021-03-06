/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hopfield;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author II SNH31D3R II
 */
public class Interfaz extends javax.swing.JFrame {

    File myDir = new File(".");
    String myDirStr = "";

    String Ruta;
    String RutaPrueba;
    String RutaImaPatrones;
    String RutaMemoria;
    Color c;
    BufferedImage bi, ImageEnt;
    File img, mem;
    ImageIcon foto;
    Graphics2D g2, g3;

    int VA[][];

    int VEnt[];

    int VS[];

    int matW[][];

    int cont;

    String nombreI[];

    ///////////BackPropagation
    double x[][], wH[][], wO[][], netH[][], netO[][], yH[][], yO[][], y[][], A, dH[][];
    double dO[][], eT;
    int numEnt, pat = 0;

    ///variables pintar
    int X = -1, Y = -1, x1 = -1, y1 = -1;
    boolean nnw = false;
    double rred;
    int boton;
    int WidthImagen, HeightImagen;

    /*
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        try {
            myDirStr = myDir.getCanonicalPath(); ///Guarda la ruta del equipo
            System.out.printf(myDirStr);
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        ///Inicializa ruta
        Ruta = myDirStr + "/imagenes_entrenamiento/";
        RutaPrueba = myDirStr + "/prueba_50x50.jpeg";
        RutaImaPatrones = myDirStr + "/Letras.png";
        RutaMemoria = myDirStr + "/memoria/";

        foto = new ImageIcon(RutaImaPatrones);
        L_ImagenPatrones.setIcon(new ImageIcon(foto.getImage().getScaledInstance(L_ImagenPatrones.getWidth(),
                L_ImagenPatrones.getHeight(), Image.SCALE_DEFAULT)));

        crearBuferImagen();
        canvas1.setBackground(Color.white);

        canvas1.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 3) {
                    boton = 3;
                    X = e.getX();
                    Y = e.getY();

                    paintCanvas(canvas1.getGraphics());
                }
                if (e.getButton() == 1) {
                    boton = 1;
                    X = e.getX();
                    Y = e.getY();

                    paintCanvas(canvas1.getGraphics());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                X = -1;
                x1 = -1;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        canvas1.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (boton == 3) {
                    x1 = e.getX();
                    y1 = e.getY();
                    paintCanvas(canvas1.getGraphics());
                    X = x1;
                    Y = y1;
                }
                if (boton == 1) {

                    x1 = e.getX();
                    y1 = e.getY();
                    paintCanvas(canvas1.getGraphics());
                    X = x1;
                    Y = y1;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    public void crearBuferImagen() {

        WidthImagen = canvas1.getWidth() / 4;
        HeightImagen = canvas1.getHeight() / 4;

        // Creamos una imagen con ese tama??o y con su correspondiente formato de
        // color
        ImageEnt = new BufferedImage(WidthImagen, HeightImagen, BufferedImage.TYPE_INT_RGB);
        g3 = (Graphics2D) ImageEnt.getGraphics();

        // rellenamos el fondo
        g3.setColor(Color.white);
        g3.fillRect(0, 0, WidthImagen, WidthImagen);

        g3.setStroke(new BasicStroke(13));
        g3.setColor(Color.BLACK);

    }

    public void paintCanvas(Graphics g) {
        //B_Analizar.setEnabled(true);
        g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(52));
        if (boton == 1) {
            // g1.setColor(Color.black);
            g2.setColor(Color.black);
        } else if (boton == 3) {
            g2.setColor(Color.white);
        }
        if (x1 != -1 && X != -1) {
            g2.drawLine(X, Y, x1, y1);
            //   g1.drawLine(X, Y, x1,y1);
        }

        if (nnw) {
            g2.setBackground(Color.white);
            g2.dispose();
            //g1.dispose();
            nnw = false;
        }

        if (x1 != -1 && X != -1) {
            g3.drawLine(X / 4, Y / 4, x1 / 4, y1 / 4);
            // g3.drawRect(X/4, Y/4, x1/4,y1/4);
        }

    }

    public void AnalizarPatrones(String nombreImagen) {

        //Crea una copia del mismo tama??o que la imagen
        //BufferedImage biDestino = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
        //	   .createCompatibleImage(bi.getWidth(), bi.getHeight(), Transparency.OPAQUE);
        //Recorre las imagenes y obtiene el color de la imagen original y la almacena en el destino
        //////////////////////////////////////////////////////////////////////////////////
        img = new File(Ruta + nombreImagen);

        try {
            bi = ImageIO.read(img);
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        //////Vector de entrada
        cont = 0;
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x1 = 0; x1 < bi.getWidth(); x1++) {
                //Obtiene el color
                c = new Color(bi.getRGB(x1, y));

                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    //System.out.print(" ");
                    VA[pat][cont] = -1;
                    //System.out.print(mat[y][x]);
                } else {
                    //System.out.print("1");
                    VA[pat][cont] = 1;

                    //System.out.print(mat[y][x]);
                }
                cont++;
            }
            //System.out.println();
        }

    }

    public void funcionamiento() {

        ////////////Guardar imagen en el disco
        // Ejecutamos el metodo Dispose para finalizar
        g3.dispose();
        // Se crea un flujo de datos, en este caso ser?? FileOutPutStream, aunque
        // pod??s utilizar cualquier otro.

        FileOutputStream out;
        try {
            out = new FileOutputStream(new File(RutaPrueba));

            // Se decodifica la imagen y se env??a al flujo.
            ImageIO.write(ImageEnt, "jpeg", out);

            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        crearBuferImagen();
        B_BorrarActionPerformed(null);

        img = new File(RutaPrueba);

        try {
            bi = ImageIO.read(img);
            //bi = ImageIO.read(img);
        } catch (IOException ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }

        ///imagen al vector 1 -1
        VEnt = new int[bi.getWidth() * bi.getHeight()];

        cont = 0;
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x1 = 0; x1 < bi.getWidth(); x1++) {
                //Obtiene el color
                c = new Color(bi.getRGB(x1, y));

                if (c.getRed() == 255 && c.getGreen() == 255 && c.getBlue() == 255) {
                    //System.out.print(" ");
                    VEnt[cont] = -1;
                    //System.out.print(mat[y][x]);
                } else {
                    //System.out.print(" 1");
                    VEnt[cont] = 1;
                    //System.out.print(mat[y][x]);
                }
                cont++;
            }
            //  System.out.println();
        }

        ///Llenar entrenamiento
        try {
            matW = new int[50 * 50][50 * 50];

            //Creamos un archivo FileReader que obtiene lo que tenga el archivo
            FileReader fr = new FileReader(RutaMemoria + "aprendizaje.txt");

            //Con el siguiente ciclo extraemos todo el contenido del objeto "contenido" y lo mostramos
            int aux = 0, caract = fr.read();

            pat = Integer.parseInt(String.valueOf((char) caract));
            caract = fr.read();
            //Se recorre el fichero hasta encontrar el car??cter -1
            //   que marca el final del fichero
            int x = 0, y = 0;
            while (caract != -1) {

                //Mostrar en pantalla el car??cter le??do convertido a char
                if ((char) caract == '-') {
                    aux = 1;
                } else {

                    if (aux == 0) {
                        matW[y][x] = Integer.parseInt(String.valueOf((char) caract));
                    } else {
                        matW[y][x] = Integer.parseInt(String.valueOf((char) caract)) * -1;
                    }
                    aux = 0;
                    x++;
                    if (x > (50 * 50) - 1) {
                        y++;
                        x = 0;
                    }

                }
                //Leer el siguiente car??cter
                caract = fr.read();
            }
        } //Si se causa un error al leer cae aqui
        catch (Exception e) {
            System.out.println("Error al leer");
        }

        ////////////////////multiplica entrada con el entrenamiento
        cont = 0;
        VS = new int[bi.getWidth() * bi.getHeight()];

        for (int y = 0; y < bi.getWidth() * bi.getHeight(); y++) {

            for (int x = 0; x < bi.getWidth() * bi.getHeight(); x++) {

                cont = cont + VEnt[x] * matW[y][x];
            }

            VS[y] = cont;
            cont = 0;
            // System.out.print(VS[y]);
        }

        /////////////matriz
        cont = 0;

        for (int y = 0; y < bi.getWidth(); y++) {

            for (int x = 0; x < bi.getHeight(); x++) {

                if (VS[cont] < 0) {
                    VS[cont] = (VS[cont] / VS[cont]) * -1;
                    //       System.out.print(" ");
                } else {
                    if (VS[cont] > 0) {
                        VS[cont] = (VS[cont] / VS[cont]);
                        //           System.out.print(" "+VS[cont]);
                    }
                }
                cont++;

            }
            // System.out.println();
        }

        //////Valores de patrones = de entrada y aprendidos
        int cont[] = new int[VA.length];

        for (int nPat = 0; nPat < VA.length; nPat++) {
            cont[nPat] = 0;
            for (int x = 0; x < bi.getHeight() * bi.getWidth(); x++) {

                if (VA[nPat][x] == VS[x]) {
                    cont[nPat]++;

                }

            }
            System.out.println(cont[nPat] + "  " + nPat);
        }

        //////coje el mayor mas cercano al aprendido y al de entrada
        this.cont = 0;
        int k = 0;
        for (int i = 0; i < VA.length; i++) {
            for (int j = i + 1; j < VA.length; j++) {

                if (cont[i] > cont[j]) {
                    this.cont = i;
                    k = 1;
                } else {
                    if (cont[i] <= cont[j]) {
                        this.cont = j;
                        i = j - 1;
                        k = 0;
                        break;
                    }
                }
            }
            if (k == 1) {
                break;
            }
        }

        ///////imprimir imagen en Label
        foto = new ImageIcon(Ruta + nombreI[this.cont]);
        L_ImagenResultado.setIcon(new ImageIcon(foto.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        L_Mensaje3 = new javax.swing.JLabel();
        B_Borrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        L_ImagenResultado = new javax.swing.JLabel();
        L_Mensaje4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        canvas1 = new java.awt.Canvas();
        B_Analizar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        L_ImagenPatrones = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        B_AgregarPatrones = new javax.swing.JButton();
        L_Mensaje1 = new javax.swing.JLabel();
        L_Mensaje2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RED NEURONAL HOPFIELD");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        L_Mensaje3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        L_Mensaje3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-t??rminos-y-condiciones-24.png"))); // NOI18N
        L_Mensaje3.setText("PATRON A EVALUAR");

        B_Borrar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        B_Borrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-bordes-de-limpieza-24.png"))); // NOI18N
        B_Borrar.setText("Borrar Panel Dibujo");
        B_Borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_BorrarActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        L_ImagenResultado.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        L_ImagenResultado.setText("IMAGEN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(L_ImagenResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(L_ImagenResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        L_Mensaje4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        L_Mensaje4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-boleta-de-calificaciones-24.png"))); // NOI18N
        L_Mensaje4.setText("PATRON RESULTADO");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        B_Analizar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        B_Analizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-m??ltiples-entradas-24.png"))); // NOI18N
        B_Analizar.setText("Analizar Patron");
        B_Analizar.setFocusPainted(false);
        B_Analizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_AnalizarActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(L_ImagenPatrones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(L_ImagenPatrones, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.setBackground(java.awt.SystemColor.controlHighlight);

        B_AgregarPatrones.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        B_AgregarPatrones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-agregar-propiedad-24.png"))); // NOI18N
        B_AgregarPatrones.setText("Agregar Patrones");
        B_AgregarPatrones.setFocusPainted(false);
        B_AgregarPatrones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_AgregarPatronesActionPerformed(evt);
            }
        });

        L_Mensaje1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        L_Mensaje1.setForeground(new java.awt.Color(51, 51, 51));
        L_Mensaje1.setText("PATRONES APRENDIDOS");

        L_Mensaje2.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        L_Mensaje2.setText("RED NEURONAL HOPFIELD");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(L_Mensaje2)
                    .addComponent(L_Mensaje1)
                    .addComponent(B_AgregarPatrones))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addComponent(L_Mensaje2)
                .addGap(55, 55, 55)
                .addComponent(L_Mensaje1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(B_AgregarPatrones, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(L_Mensaje3)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(B_Borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(B_Analizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(L_Mensaje4)
                        .addGap(164, 164, 164))))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(L_Mensaje3)
                            .addComponent(L_Mensaje4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(B_Borrar)
                                .addGap(61, 61, 61))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(B_Analizar)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void B_AnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_AnalizarActionPerformed
        // TODO add your handling code here
        cont = 0;
        pat = 0;

        File directorio = new File(Ruta);

        // Si es un directorio
        if (directorio.isDirectory()) {

            // obtenemos su contenido
            File[] ficheros = directorio.listFiles();

            // y lo sacamos por pantalla
            for (File fichero : ficheros) {
                cont++;
            }

            VA = new int[cont][50 * 50];
            nombreI = new String[cont];

            for (File fichero : ficheros) {
                AnalizarPatrones(fichero.getName());
                nombreI[pat] = fichero.getName();
                pat++;
            }

        }

        funcionamiento();


    }//GEN-LAST:event_B_AnalizarActionPerformed

    private void B_BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_BorrarActionPerformed
        // TODO add your handling code here:
        nnw = true;
        //B_Analizar.setEnabled(false);
        canvas1.repaint();

        crearBuferImagen();

    }//GEN-LAST:event_B_BorrarActionPerformed

    private void B_AgregarPatronesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_AgregarPatronesActionPerformed
        // TODO add your handling code here:

        IngresarPatrones ver = new IngresarPatrones();
        ver.setLocation(300, 0);
        ver.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_B_AgregarPatronesActionPerformed

    void backPropagation() {
        pat = 1;
        numEnt = 2;

        x = new double[pat + 1][numEnt + 1];
        y = new double[pat + 1][numEnt + 1];
        wH = new double[numEnt + 1][numEnt + 1];
        wO = new double[numEnt + 1][numEnt + 1];
        netH = new double[pat + 1][numEnt + 1];
        netO = new double[pat + 1][numEnt + 1];
        yH = new double[pat + 1][numEnt + 1];
        yO = new double[pat + 1][numEnt + 1];
        dH = new double[pat + 1][numEnt + 1];
        dO = new double[pat + 1][numEnt + 1];
        A = 0.2;

        x[pat][1] = 0.4;
        x[pat][2] = 0.3; //valor de entrada
        y[pat][1] = 0.2018;
        y[pat][2] = 0.3011; //Valor deseado

        ////valor de las magnitudes
        for (int i = 1; i < numEnt + 1; i++) {
            for (int j = 1; j < numEnt + 1; j++) {

                wH[i][j] = 0.1;
                wO[i][j] = 0.1;

            }
        }
        int iterac = 1;

        do {
            ///valor de las netas ocultas
            for (int j = 1; j < numEnt + 1; j++) {
                for (int i = 1; i < numEnt + 1; i++) {

                    netH[pat][j] += x[pat][i] * wH[j][i];

                }
                ///Determinar las salidas
                yH[pat][j] = (1 / (1 + Math.exp(-1 * netH[pat][j])));

            }

            ///Calcular la entrada neta de las neuronas de salida
            for (int j = 1; j < numEnt + 1; j++) {
                for (int i = 1; i < numEnt + 1; i++) {

                    netO[pat][j] += yH[pat][i] * wH[j][i];

                }
                ///Determinar las salidas
                yO[pat][j] = (1 / (1 + Math.exp(-1 * netO[pat][j])));

            }
            ///////Backward

            for (int k = 1; k < numEnt + 1; k++) {
                ///calcular el error de las neuronas capa de salida
                dO[pat][k] = (y[pat][k] - yO[pat][k]) * yO[pat][k] * (1 - yO[pat][k]);
            }

            ///calcular el error de las neuronas capa oculta
            for (int j = 1; j < numEnt + 1; j++) {
                for (int k = 1; k < numEnt + 1; k++) {

                    dH[pat][j] += dO[pat][k] * wO[k][j];
                }
                dH[pat][j] = x[pat][j] * (1 - x[pat][j]) * dH[pat][j];

            }

            ///Actualizacion de pesos de salida
            for (int k = 1; k < numEnt + 1; k++) {
                for (int j = 1; j < numEnt + 1; j++) {

                    wO[k][j] = wO[k][j] + A * dO[pat][k] * yH[pat][j];
                }
            }

            ///Actualiacion de pesos entrada
            for (int j = 1; j < numEnt + 1; j++) {
                for (int i = 1; i < numEnt + 1; i++) {

                    wH[j][i] = wH[j][i] + A * dH[pat][j] * x[pat][i];

                }
            }

            eT = (0.5 * (dO[pat][1] * dO[pat][1] + dO[pat][2] * dO[pat][2]));

            iterac++;
        } while (eT >= y[pat][1] || eT >= y[pat][2] || eT >= 0.000001);

        System.out.println(iterac);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_AgregarPatrones;
    private javax.swing.JButton B_Analizar;
    private javax.swing.JButton B_Borrar;
    private javax.swing.JLabel L_ImagenPatrones;
    private javax.swing.JLabel L_ImagenResultado;
    private javax.swing.JLabel L_Mensaje1;
    private javax.swing.JLabel L_Mensaje2;
    private javax.swing.JLabel L_Mensaje3;
    private javax.swing.JLabel L_Mensaje4;
    private java.awt.Canvas canvas1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
