/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PDFReader;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PDFViewer extends JFrame {
    /*
    public PDFViewer(InputStream pdfInputStream) {
        setTitle("PDF Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load PDF document
        try (PDDocument document = PDDocument.class.getRe) {
            // Create a PDFRenderer to render the PDF pages
            PDFRenderer renderer = new PDFRenderer(document);
            // Get the number of pages in the PDF
            int pageCount = document.getNumberOfPages();

            // Create a panel to hold the PDF pages
            JPanel pdfPanel = new JPanel();
            pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));

            for (int i = 0; i < pageCount; i++) {
                // Render the page to an image
                BufferedImage pageImage = renderer.renderImageWithDPI(i, 100); // 100 DPI for rendering

                // Convert the image to an ImageIcon and add it to a JLabel
                ImageIcon imageIcon = new ImageIcon(pageImage);
                JLabel imageLabel = new JLabel(imageIcon);
                pdfPanel.add(imageLabel);
            }

            // Add the panel to a JScrollPane to allow scrolling
            JScrollPane scrollPane = new JScrollPane(pdfPanel);
            add(scrollPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}
