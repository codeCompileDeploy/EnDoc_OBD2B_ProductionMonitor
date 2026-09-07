package com.PDFReader;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PDFRead {

    //Hoe to Start PDF
    public void HowToStartPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToStart.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("Start", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Connect PDF
    public void HowToConnectPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToConnect.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("Connect", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Disconnect PDF
    public void HowToDisconnectPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToDisconnect.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("Disconnect", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //LUT Module starts here
    //How to Load LUT PDF
    public void HowToLoadLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToLoadLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("LoadLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to ReadLUT PDF
    public void HowToReadLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToReadLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("ReadLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Edit/Update PDF
    public void HowToUpdateLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToEditLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("updateLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Save PDF
    public void HowToSaveLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToSaveLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("SaveLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Open External PDF
    public void HowToOpenLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToOpenExternalLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("OpenLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to flash PDF
    public void HowToFlashLUTPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToFlashLUT.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("FlashLUT", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to flash VIN PDF
    public void HowToFlashVINPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToFlashVIN.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("FlashVIN", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Generate Error PDF
    public void HowToGenerateErrorPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/HowToGenrateError.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("GenErr", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }

    //How to Generate Error PDF
    public void HowToAboutPDF() {
        // Ensure the Desktop class is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Load the PDF file from the source package
            try (InputStream is = PDFRead.class.getResourceAsStream("/com/PDF/AboutEnDoc.pdf")) {
                if (is == null) {
                    System.err.println("PDF file not found");
                    return;
                }
                // Create a temporary file
                Path tempFile = Files.createTempFile("About", ".pdf");
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                // Open the PDF file
                desktop.open(tempFile.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop class is not supported on this platform");
        }
    }
}
