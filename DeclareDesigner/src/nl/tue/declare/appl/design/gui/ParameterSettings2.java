/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package nl.tue.declare.appl.design.gui;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import minerful.concept.ProcessModel;
//import minerful.io.encdec.declare.DeclareEncoderDecoder;
import minerful.io.encdec.declaremap.DeclareMapEncoderDecoder;
import minerful.logmaker.MinerFulLogMaker;
import minerful.logmaker.params.LogMakerCmdParameters;
import minerful.logmaker.params.LogMakerCmdParameters.Encoding;
import log.generation.*;

public class ParameterSettings2 extends javax.swing.JFrame {

	
	public static final Integer MIN_EVENTS_PER_TRACE = 5;
	public static final Integer MAX_EVENTS_PER_TRACE = 45;
	public static final Long TRACES_IN_LOG = (long)100;
	public static final Encoding OUTPUT_ENCODING = Encoding.xes;
	public static final File OUTPUT_LOG = new File("");
	
    /**
     * Creates new form ContactEditor
     */
    public ParameterSettings2() {
        initComponents();
        move(500, 368);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTextField7 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        jTextField7.setText("2000");
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jButton2.setText("Select");

        jLabel10.setText("Max Size");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Generation Wizard"));
        jPanel2.setToolTipText("");

        jLabel6.setText("Select Output");

        jTextField5.setText("testdata.xes");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton1.setText("Select");
        
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            	 // For Directory
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                FileFilter filter = new FileNameExtensionFilter("XES files", "xes");
                fileChooser.addChoosableFileFilter(filter);
                filter = new FileNameExtensionFilter("MXML files", "mxml");
                fileChooser.addChoosableFileFilter(filter);
                // For File
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         
                //fileChooser.setAcceptAllFileFilterUsed(false);
         
                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                	jTextField5.setText(fileChooser.getSelectedFile().toString());
                }//jTextField5.setText(JFileChooser.fileName);
            }
        });
        jLabel11.setText("Progress");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(60, 60, 60)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1)
                .add(24, 24, 24))
            .add(jPanel2Layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 221, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 448, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .add(31, 31, 31)
                .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        jButton5.setText("Cancel");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dispose();
            }
        });

        jButton6.setText("Generate");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jButton6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton5))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton5)
                    .add(jButton6))
                .addContainerGap())
        );

        jPanel2.getAccessibleContext().setAccessibleName("Model and Parameters");
        setTitle("Log Generation Settings"); //Tartu
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        new Thread(new Runnable() {

            public void run() {
            	
            	Instant start = Instant.now();
            	String path = System.getProperty("java.library.path");
            	System.out.println(path);
                
            	if (jTextField5.getText().isEmpty())
            	{
            		JOptionPane.showMessageDialog(null, "Please select output file!");
            		jTextField5.requestFocus();
            		return;
            	}
            	          	
            	
            	try {
            		
            		jProgressBar1.setMaximum(10);
            		jProgressBar1.setValue(1);
            		
            	int minVal  = Integer.parseInt(ParameterSettings.jTextField6.getText());
            	int maxVal  = Integer.parseInt(ParameterSettings.jTextField8.getText());
            	long traceVal  =  Long.valueOf(ParameterSettings.jTextField9.getText());
            	Encoding OUTPUT_ENCODING = Encoding.xes;
            	           	
            	File OUTPUT_LOG = new File(jTextField5.getText());         		
            
            	if (ParameterSettings.jRadioButton2.isSelected()){
            		
            		OUTPUT_ENCODING = Encoding.string;
            	} else if (ParameterSettings.jRadioButton3.isSelected())
            	{
            		OUTPUT_ENCODING = Encoding.mxml;	
            	} else
            	{
            		OUTPUT_ENCODING = Encoding.xes;	
            	}
            	
            	
            	jProgressBar1.setValue(2);
            	
            	LogGeneration lg = new LogGeneration();
            	ProcessModel proMod = null;            	
            	boolean isConditional =false;
            	boolean isOk = false;
            	if(lg.checkforCondition(ParameterSettings.jTextField5.getText()))
            	{
            		isConditional =true;            		
            		if (DeclareLogGenerator.GenerateLog(minVal, maxVal,traceVal, ParameterSettings.jTextField5.getText(),jTextField5.getText())){
            			isOk=true;
            		}
            		System.out.println("Modle with Conditions");            		
            	}
            	else
            	{            		
            		System.out.println("Model with No Conditions");
            		proMod = DeclareMapEncoderDecoder.fromDeclareMapToMinerfulProcessModel(ParameterSettings.jTextField5.getText());
            		 
            		LogMakerCmdParameters logMakParameters = new LogMakerCmdParameters(minVal, maxVal, traceVal);
            		//Tartu MIN_EVENTS_PER_TRACE, MAX_EVENTS_PER_TRACE, TRACES_IN_LOG);
            		jProgressBar1.setValue(7);
            		MinerFulLogMaker logMak = new MinerFulLogMaker(logMakParameters);
            		logMak.createLog(proMod);        		
            		jProgressBar1.setValue(9);
            		logMakParameters.outputEncoding = OUTPUT_ENCODING;        		
    			//	System.out.println(logMak.printEncodedLog());			
            		logMakParameters.outputLogFile = OUTPUT_LOG;
            		logMak.storeLog();                    
                   // System.exit(0); Tartu

            	}
            	jProgressBar1.setValue(10);
            	
            	 Instant end = Instant.now();
                 Duration timeElapsed = Duration.between(start, end);
                 System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");
            	if (isConditional){            		
            		if (isOk) JOptionPane.showMessageDialog(null, "Log Generation (with data) completed!");
            	} else {
            		JOptionPane.showMessageDialog(null, "Simple Log Generation completed!");
            	}
               
        		
        		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
            	            }
        
        }).start();
    }//GEN-LAST:event_jButton6ActionPerformed
    
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
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ParameterSettings2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ParameterSettings2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ParameterSettings2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParameterSettings2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParameterSettings2().setVisible(true);                
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
    
}
