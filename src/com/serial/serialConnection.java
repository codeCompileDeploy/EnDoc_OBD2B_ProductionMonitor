package com.serial;

import com.fazecast.jSerialComm.SerialPort;
import javax.swing.JOptionPane;

/**
 * @author : IamDeadHacker
 */
public class serialConnection {

    public static SerialPort portVar;

    //Method to connect and open serial port in Windows OS
    public void serialCon(int index, String COM) {
        SerialPort[] portlist = SerialPort.getCommPorts();
        portVar = portlist[index];

        //Now set the COM Port Parameters
        //portVar.setBaudRate(38400); //Ideal for Connecting UART with 32pin LPI controller
        //Now set the COM Port Parameters
        portVar.setBaudRate(38400);
//        if (COM.equals("Tec-UART")) {
//            portVar.setBaudRate(38400);
//        } else {
//            portVar.setBaudRate(115200); //Ideal for Connecting UART with 32pin LPI controller
//        }
        //portVar.setBaudRate(38400); 
        portVar.setNumDataBits(8);
        portVar.setNumStopBits(1);
        portVar.setParity(0);

        //Open COM Port
        portVar.openPort();

        //Check if connection was established
        if (portVar.isOpen()) {
            JOptionPane.showMessageDialog(null, "--Communication link Established Successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "<html>COM Port Not Connected -- <br />Communication Link broken</html>");
        }
    }

    public SerialPort getSerial() {
        if (portVar.isOpen()) {
            return portVar;
        }
        return null;
    }

}
