package com.VIN;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.jdi.connect.spi.Connection;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author manis
 */
public class VIN_flashing {

    int vin_array[] = new int[18];

    public void main(String vin, JTable table_OD, JTextField txtVIN, SerialPort portVar, JLabel processStatus) {
        try {

            String VIN_f = vin.toUpperCase();

            //Converting VIN into integer values
            for (int i = 1; i < vin.length() + 1; i++) {
                vin_array[i] = (int) VIN_f.charAt(i - 1);
            }

            int rowCountBefore = table_OD.getRowCount();
            Thread.sleep(1000);
            int rowCountAfter = table_OD.getRowCount();

            if (rowCountAfter == rowCountBefore) {
                JOptionPane.showMessageDialog(null, "ECU not Connected!");
                txtVIN.setText("");
            } else {
                try {

                    OutputStream Serial = portVar.getOutputStream();

                    //Sending Communication Bits
                    Serial.write(0xAB);
                    Serial.write(0x60);
                    Serial.write(0x02);

                    for (int i = 1; i < 18; i++) {
                        Serial.write(vin_array[i]);
                        System.out.println(vin_array[i]);
                    }
                    processStatus.setText("VIN Flashed");
                    JOptionPane.showMessageDialog(null, "VIN Flashed!");
                    txtVIN.setText("");
                    processStatus.setText("IDLE");
                } catch (IOException ex) {
                    //Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex);
                }
            }

        } catch (InterruptedException ex) {
            //Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
