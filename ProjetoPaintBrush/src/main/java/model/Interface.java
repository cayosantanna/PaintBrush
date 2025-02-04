/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package model;

import java.awt.event.MouseEvent;
import java.util.Stack;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.AlphaComposite;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.io.File;

public class Interface extends javax.swing.JFrame {
    private enum TipoFigura {tfPonto, tfReta, tfCirculo, tfRetangulo, tfSpray, tfBorracha, tfPiramide, tfCilindro, tfPoligono}
    private TipoFigura tipoFigura = TipoFigura.tfPonto; // variável controladora do tipo figura
    private Reta reta = new Reta();
    private Retangulo retangulo = new Retangulo();
    private Circulo circulo = new Circulo();
    private Poligono poligono = new Poligono();
    private Borracha borracha = new Borracha(35);
    private Spray spray = new Spray();
    private Cilindro cilindro = new Cilindro();
    private Piramide piramide = new Piramide();
    private Stack<BufferedImage> undoStack = new Stack<>();
    private Stack<BufferedImage> redoStack = new Stack<>();
    private float zoomFactor = 1.0f;
    private BufferedImage previewLayer;
    private JSlider spraySlider;
    private JLabel sprayLabel;
    
    private void salvarEstado() {
        BufferedImage bi = new BufferedImage(pnlPaint.getWidth(), pnlPaint.getHeight(), BufferedImage.TYPE_INT_ARGB);
        pnlPaint.paint(bi.getGraphics());
        undoStack.push(bi);
        redoStack.clear();
    }
    
    public void desfazer() {
        if(!undoStack.isEmpty()) {
            BufferedImage bi = undoStack.pop();
            redoStack.push(bi);
            pnlPaint.getGraphics().drawImage(bi, 0, 0, null);
        }
    }
    
    public void refazer() {
        if(!redoStack.isEmpty()) {
            BufferedImage bi = redoStack.pop();
            undoStack.push(bi);
            pnlPaint.getGraphics().drawImage(bi, 0, 0, null);
        }
    }
    /**
     * Creates new form Interface
     */
    public Interface() {
        initComponents();
        previewLayer = new BufferedImage(1600, 900, BufferedImage.TYPE_INT_ARGB);
        
        // Adiciona controle de zoom
        pnlPaint.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    zoomFactor *= 1.1f;
                } else {
                    zoomFactor /= 1.1f;
                }
                atualizarZoom();
            }
        });
        
        // Consolida o listener para preview e feedback da borracha
        pnlPaint.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                if (tipoFigura == TipoFigura.tfBorracha) {
                    mostrarTamanhoBorracha();
                } else {
                    atualizarPreview(evt);
                }
            }
        });
        
        // Adicionar key listener ao painel
        pnlPaint.setFocusable(true);
        pnlPaint.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                        if (tipoFigura == TipoFigura.tfBorracha) {
                            borracha = new Borracha(borracha.largura + 5);
                            mostrarTamanhoBorracha();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                        if (tipoFigura == TipoFigura.tfBorracha && borracha.largura > 10) {
                            borracha = new Borracha(borracha.largura - 5);
                            mostrarTamanhoBorracha();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                        desfazer();
                    } else if (e.getKeyCode() == KeyEvent.VK_Y) {
                        refazer();
                    }
                }
            }
        });

        // Adiciona mouse motion listener para preview
        pnlPaint.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                if (tipoFigura == TipoFigura.tfBorracha) {
                    mostrarTamanhoBorracha();
                }
            }
        });
        
        // Adiciona controle de tamanho do Spray
        spraySlider = new JSlider(JSlider.HORIZONTAL, 5, 30, 8);
        spraySlider.setBounds(10, pnlPaint.getHeight() + 50, 200, 50);
        spraySlider.setVisible(false);
        sprayLabel = new JLabel("Tamanho do Spray:");
        sprayLabel.setBounds(10, pnlPaint.getHeight() + 30, 200, 20);
        sprayLabel.setVisible(false);
        
        add(spraySlider);
        add(sprayLabel);
        
        spraySlider.addChangeListener(e -> {
            if (tipoFigura == TipoFigura.tfSpray) {
                spray.raio = spraySlider.getValue();
            }
        });
        
        // Adiciona menu para salvar/carregar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Arquivo");
        JMenuItem salvar = new JMenuItem("Salvar");
        JMenuItem carregar = new JMenuItem("Carregar");
        
        salvar.addActionListener(e -> salvarDesenho());
        carregar.addActionListener(e -> carregarDesenho());
        
        menu.add(salvar);
        menu.add(carregar);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void mostrarTamanhoBorracha() {
        // Cria e atualiza camada temporária para feedback visual
        BufferedImage temp = new BufferedImage(pnlPaint.getWidth(), pnlPaint.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = temp.createGraphics();
        g2.drawImage(undoStack.isEmpty() ? null : undoStack.peek(), 0, 0, null);
        g2.setColor(new Color(128, 128, 128, 128));
        g2.drawRect(borracha.x - borracha.largura/2, borracha.y - borracha.largura/2, borracha.largura, borracha.largura);
        g2.dispose();
        // Solicita repaint, fazendo o painel desenhar a imagem atualizada
        pnlPaint.repaint();
    }
    
    private void atualizarZoom() {
        BufferedImage temp = new BufferedImage(
            pnlPaint.getWidth(), 
            pnlPaint.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = temp.createGraphics();
        g2.scale(zoomFactor, zoomFactor);
        g2.drawImage(undoStack.isEmpty() ? null : undoStack.peek(), 0, 0, null);
        pnlPaint.repaint();
        g2.dispose();
    }
    
    private void atualizarPreview(MouseEvent evt) {
        Graphics2D g2 = previewLayer.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g2.fillRect(0, 0, previewLayer.getWidth(), previewLayer.getHeight());
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        switch(tipoFigura) {
            case tfCirculo:
                g2.setColor(new Color(128, 128, 128, 128));
                g2.drawOval(evt.getX() - 20, evt.getY() - 20, 40, 40);
                break;
            case tfRetangulo:
                g2.setColor(new Color(128, 128, 128, 128));
                g2.drawRect(evt.getX() - 20, evt.getY() - 20, 40, 40);
                break;
            // Adicione casos para outras ferramentas
        }
        g2.dispose();
        pnlPaint.repaint();
    }

    private void salvarDesenho() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage bi = new BufferedImage(
                    pnlPaint.getWidth(),
                    pnlPaint.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                pnlPaint.paint(bi.getGraphics());
                ImageIO.write(bi, "png", fc.getSelectedFile());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void carregarDesenho() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage bi = ImageIO.read(fc.getSelectedFile());
                Graphics2D g2 = (Graphics2D) pnlPaint.getGraphics();
                g2.clearRect(0, 0, pnlPaint.getWidth(), pnlPaint.getHeight());
                g2.drawImage(bi, 0, 0, null);
                salvarEstado();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void BtnSprayMouseClicked(java.awt.event.MouseEvent evt) {
        alternarFerramentas(TipoFigura.tfSpray);
    }

    private void alternarFerramentas(TipoFigura tipo) {
        tipoFigura = tipo;
        
        // Esconde todos os controles especiais
        spraySlider.setVisible(false);
        sprayLabel.setVisible(false);
        
        // Mostra apenas os controles relevantes para a ferramenta selecionada
        if (tipo == TipoFigura.tfSpray) {
            spraySlider.setVisible(true);
            sprayLabel.setVisible(true);
        }
        
        // Atualiza o estado dos checkboxes
        ChkArea.setEnabled(tipo == TipoFigura.tfCirculo || 
                          tipo == TipoFigura.tfRetangulo || 
                          tipo == TipoFigura.tfPoligono);
                          
        ChkComprimento.setEnabled(tipo == TipoFigura.tfReta);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        BtnPonto = new javax.swing.JButton();
        BtnReta = new javax.swing.JButton();
        BtnSpray = new javax.swing.JButton();
        BtnPiramide = new javax.swing.JButton();
        BtnCilindro = new javax.swing.JButton();
        BtnRetangulo = new javax.swing.JButton();
        BtnCirculo = new javax.swing.JButton();
        BtnPoligono = new javax.swing.JButton();
        BtnBorracha = new javax.swing.JButton();
        pnlPaint = new javax.swing.JPanel();
        Cores = new javax.swing.JColorChooser();
        ChkComprimento = new javax.swing.JCheckBox();
        ChkArea = new javax.swing.JCheckBox();
        pnlCorInterna = new javax.swing.JButton();
        pnlCorExterna = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jLabel1.setText("Propriedades:");

        BtnPonto.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnPonto.setText("Ponto");
        BtnPonto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnPontoMouseClicked(evt);
            }
        });

        BtnReta.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnReta.setText("Reta");
        BtnReta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnRetaMouseClicked(evt);
            }
        });

        BtnSpray.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnSpray.setText("Spray");
        BtnSpray.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnSprayMouseClicked(evt);
            }
        });

        BtnPiramide.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnPiramide.setText("Pirâmide");
        BtnPiramide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnPiramideMouseClicked(evt);
            }
        });
        BtnPiramide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPiramideActionPerformed(evt);
            }
        });

        BtnCilindro.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnCilindro.setText("Cilindro");
        BtnCilindro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnCilindroMouseClicked(evt);
            }
        });

        BtnRetangulo.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnRetangulo.setText("Retângulo");
        BtnRetangulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnRetanguloMouseClicked(evt);
            }
        });

        BtnCirculo.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnCirculo.setText("Círculo");
        BtnCirculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnCirculoMouseClicked(evt);
            }
        });
        BtnCirculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCirculoActionPerformed(evt);
            }
        });

        BtnPoligono.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnPoligono.setText("Polígano");
        BtnPoligono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnPoligonoMouseClicked(evt);
            }
        });

        BtnBorracha.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        BtnBorracha.setText("Borracha");
        BtnBorracha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnBorrachaMouseClicked(evt);
            }
        });

        pnlPaint.setBackground(new java.awt.Color(255, 255, 255));
        pnlPaint.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlPaintMouseDragged(evt);
            }
        });
        pnlPaint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlPaintMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlPaintMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlPaintLayout = new javax.swing.GroupLayout(pnlPaint);
        pnlPaint.setLayout(pnlPaintLayout);
        pnlPaintLayout.setHorizontalGroup(
            pnlPaintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1417, Short.MAX_VALUE)
        );
        pnlPaintLayout.setVerticalGroup(
            pnlPaintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );

        ChkComprimento.setText("Exbir Comprimento");
        ChkComprimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkComprimentoActionPerformed(evt);
            }
        });

        ChkArea.setText("Exibir Area");
        ChkArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAreaActionPerformed(evt);
            }
        });

        pnlCorInterna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlCorInternaMouseClicked(evt);
            }
        });

        pnlCorExterna.setBackground(new java.awt.Color(0, 0, 0));
        pnlCorExterna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlCorExternaMouseClicked(evt);
            }
        });
        pnlCorExterna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pnlCorExternaActionPerformed(evt);
            }
        });

        jLabel2.setText("Cor Interna:");

        jLabel3.setText("Cor Externa:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlPaint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(pnlCorInterna, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlCorExterna, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ChkComprimento)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnPonto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnReta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(85, 85, 85)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnCilindro, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnPiramide))
                                .addGap(43, 67, Short.MAX_VALUE)))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnRetangulo)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnPoligono, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnBorracha)
                                    .addComponent(BtnSpray, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(ChkArea))
                        .addGap(74, 74, 74)))
                .addComponent(Cores, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(261, 261, 261))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlPaint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BtnPonto, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnPoligono, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnSpray, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtnPiramide, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(BtnReta, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(BtnBorracha, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(BtnCirculo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(BtnCilindro, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(BtnRetangulo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ChkArea)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(ChkComprimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pnlCorInterna, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pnlCorExterna, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(54, 54, 54))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cores, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCirculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCirculoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCirculoActionPerformed

    private void pnlPaintMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPaintMouseDragged
    if(tipoFigura == TipoFigura.tfBorracha){
            borracha.corInterna = pnlCorInterna.getBackground();
            borracha.x = evt.getX();
            borracha.y = evt.getY();
            borracha.desenhar(pnlPaint.getGraphics());
        }
    else if(tipoFigura == TipoFigura.tfSpray) {
            spray.cor = pnlCorExterna.getBackground();
            spray.x = evt.getX();
            spray.y = evt.getY();
            spray.desenhar(pnlPaint.getGraphics());
        }
    }//GEN-LAST:event_pnlPaintMouseDragged

    private void pnlPaintMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPaintMousePressed
    salvarEstado();
    if (tipoFigura == TipoFigura.tfPonto){
        Ponto p = new Ponto();
        p.cor = pnlCorExterna.getBackground();
        p.x = evt.getX();
        p.y = evt.getY();
        p.desenhar(pnlPaint.getGraphics());  
    }
    else if(tipoFigura == TipoFigura.tfReta) {
            reta.cor = pnlCorExterna.getBackground();
            reta.exibirCompr = ChkComprimento.isSelected();
            reta.x = evt.getX();
            reta.y = evt.getY();
        }
    else if (tipoFigura == TipoFigura.tfRetangulo){
            retangulo.cor = pnlCorExterna.getBackground();
            retangulo.corInterna = pnlCorInterna.getBackground();
            retangulo.exibirArea = ChkArea.isSelected();
            retangulo.x = evt.getX();
            retangulo.y = evt.getY();
        }
    else if(tipoFigura == TipoFigura.tfCirculo) {
            circulo.exibirArea = ChkArea.isSelected();
            circulo.x = evt.getX();
            circulo.y = evt.getY();
        }
    else if(tipoFigura == TipoFigura.tfPoligono) {
            if(poligono.listaPontos.size() >= 2 && evt.getButton() == MouseEvent.BUTTON3) poligono.desenhar(pnlPaint.getGraphics());
            else {
                poligono.cor = pnlCorExterna.getBackground();
                poligono.corInterna = pnlCorInterna.getBackground();
                poligono.adicionarPonto(evt.getX(), evt.getY());
            }
        }
    else if(tipoFigura == TipoFigura.tfCilindro) {
            cilindro.cor = pnlCorExterna.getBackground();
            cilindro.x = evt.getX();
            cilindro.y = evt.getY();
        }
    else if(tipoFigura == TipoFigura.tfPiramide) {
            piramide.cor = pnlCorExterna.getBackground();
            piramide.x = evt.getX();
            piramide.y = evt.getY();
        }
    }//GEN-LAST:event_pnlPaintMousePressed

    private void pnlPaintMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPaintMouseReleased
        if(tipoFigura == TipoFigura.tfReta) {
            reta.x1 = evt.getX();
            reta.y1 = evt.getY();
            reta.desenhar(pnlPaint.getGraphics());
        }
        else if (tipoFigura == TipoFigura.tfRetangulo) {
            retangulo.base = Math.abs(evt.getX() - retangulo.x);
            retangulo.largura = Math.abs(evt.getY() - retangulo.y);
            if(evt.getX() < retangulo.x) retangulo.x = evt.getX();
            if(evt.getY() < retangulo.y) retangulo.y = evt.getY();
            retangulo.desenhar(pnlPaint.getGraphics());
        }
        else if(tipoFigura == TipoFigura.tfCirculo) {
            circulo.raio = (int) Math.sqrt(Math.pow(evt.getX() - circulo.x, 2) + Math.pow(evt.getY() - circulo.y, 2));
            circulo.corInterna = pnlCorInterna.getBackground();
            circulo.cor = pnlCorExterna.getBackground();
            circulo.desenhar(pnlPaint.getGraphics());
        }
        else if(tipoFigura == TipoFigura.tfCilindro) {
            cilindro.raio = Math.abs(evt.getX() - cilindro.x)/2;
            cilindro.altura = Math.abs(evt.getY() - cilindro.y);
            if(evt.getX() < cilindro.x) cilindro.x = evt.getX();
            if(evt.getY() < cilindro.y) cilindro.y = evt.getY();
            cilindro.desenhar(pnlPaint.getGraphics());
        }
        else if(tipoFigura == TipoFigura.tfPiramide) {
            piramide.base = Math.abs(evt.getX() - piramide.x);
            piramide.altura = Math.abs(evt.getY() - piramide.y);
            piramide.largura = piramide.altura/3;
            if(evt.getX() < piramide.x) piramide.x = evt.getX();
            if(evt.getY() < piramide.y) piramide.y = evt.getY();
            piramide.desenhar(pnlPaint.getGraphics());
        }
    }//GEN-LAST:event_pnlPaintMouseReleased

    private void ChkComprimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkComprimentoActionPerformed
        if(tipoFigura == TipoFigura.tfReta){
            reta.exibirCompr = ChkComprimento.isSelected();
            pnlPaint.repaint();
        }
    }//GEN-LAST:event_ChkComprimentoActionPerformed

    private void ChkAreaActionPerformed(java.awt.event.ActionEvent evt) {
        // Atualiza as propriedades das figuras 2D para que exibam a área
        circulo.exibirArea = ChkArea.isSelected();
        retangulo.exibirArea = ChkArea.isSelected();
        poligono.exibirArea = ChkArea.isSelected();
        pnlPaint.repaint();
    }
    
    private void BtnPiramideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPiramideActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPiramideActionPerformed

    private void pnlCorInternaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlCorInternaMouseClicked
        pnlCorInterna.setBackground(Cores.getColor());
    }//GEN-LAST:event_pnlCorInternaMouseClicked

    private void pnlCorExternaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlCorExternaMouseClicked
        pnlCorExterna.setBackground(Cores.getColor());
    }//GEN-LAST:event_pnlCorExternaMouseClicked

    private void pnlCorExternaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pnlCorExternaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlCorExternaActionPerformed

    private void BtnPontoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnPontoMouseClicked
        alternarFerramentas(TipoFigura.tfPonto);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPontoMouseClicked

    private void BtnPoligonoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnPoligonoMouseClicked
        alternarFerramentas(TipoFigura.tfPoligono);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPoligonoMouseClicked

    private void BtnPiramideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnPiramideMouseClicked
        alternarFerramentas(TipoFigura.tfPiramide);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPiramideMouseClicked

    private void BtnRetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRetaMouseClicked
        alternarFerramentas(TipoFigura.tfReta);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnRetaMouseClicked

    private void BtnCirculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnCirculoMouseClicked
       alternarFerramentas(TipoFigura.tfCirculo); // TODO add your handling code here:
    }//GEN-LAST:event_BtnCirculoMouseClicked

    private void BtnRetanguloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnRetanguloMouseClicked
        alternarFerramentas(TipoFigura.tfRetangulo);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnRetanguloMouseClicked

    private void BtnBorrachaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnBorrachaMouseClicked
        alternarFerramentas(TipoFigura.tfBorracha);        
        pnlPaint.requestFocusInWindow(); // Dar foco ao painel quando selecionar a borracha
    }//GEN-LAST:event_BtnBorrachaMouseClicked

    private void BtnCilindroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnCilindroMouseClicked
        alternarFerramentas(TipoFigura.tfCilindro);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCilindroMouseClicked

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnBorracha;
    private javax.swing.JButton BtnCilindro;
    private javax.swing.JButton BtnCirculo;
    private javax.swing.JButton BtnPiramide;
    private javax.swing.JButton BtnPoligono;
    private javax.swing.JButton BtnPonto;
    private javax.swing.JButton BtnReta;
    private javax.swing.JButton BtnRetangulo;
    private javax.swing.JButton BtnSpray;
    private javax.swing.JCheckBox ChkArea;
    private javax.swing.JCheckBox ChkComprimento;
    private javax.swing.JColorChooser Cores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton pnlCorExterna;
    private javax.swing.JButton pnlCorInterna;
    private javax.swing.JPanel pnlPaint;
    // End of variables declaration//GEN-END:variables
}
