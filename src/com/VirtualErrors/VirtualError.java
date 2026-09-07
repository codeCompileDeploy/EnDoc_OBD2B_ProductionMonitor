package com.VirtualErrors;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 *
 * @author manis
 */
public class VirtualError {

    public void generateError(JCheckBox Battery, JCheckBox TPS, JCheckBox EGR, JCheckBox PressureSensor, JCheckBox DeltaPressure, JCheckBox EOT, JCheckBox EGT1, JCheckBox EGT2, JSpinner BatteryADC, JSpinner TPS_ADC, JSpinner EGR_ADC, JSpinner PressureSensor_ADC, JSpinner DeltaPressure_ADC, JSpinner EOT_ADC, JSpinner EGT1_ADC, JSpinner EGT2_ADC, SerialPort port, JCheckBox EOT_Intermit, JCheckBox EGT1_Intermit, JCheckBox EGT2_Intermit) {
        int BatteryCheck = 0;
        int TPSCheck = 0;
        int EGRCheck = 0;
        int PressureSensorCheck = 0;
        int DeltaPressureCheck = 0;
        int EOTCheck = 0;
        int EGT1Check = 0;
        int EGT2Check = 0;
        int EOT_Intermittent = 0;
        int EGT1_Intermittent = 0;
        int EGT2_Intermittent = 0;

        //Logic to update the values
        if (Battery.isSelected()) {
            BatteryCheck = 1;
        }

        if (TPS.isSelected()) {
            TPSCheck = 1;
        }

        if (EGR.isSelected()) {
            EGRCheck = 1;
        }

        if (PressureSensor.isSelected()) {
            PressureSensorCheck = 1;
        }

        if (DeltaPressure.isSelected()) {
            DeltaPressureCheck = 1;
        }

        if (EOT.isSelected()) {
            EOTCheck = 1;

        }
        if (EOT_Intermit.isSelected()) {
            EOT_Intermittent = 1;
        }

        if (EGT1.isSelected()) {
            EGT1Check = 1;
        }
        if (EGT1_Intermit.isSelected()) {
            EGT1_Intermittent = 1;
        }

        if (EGT2.isSelected()) {
            EGT2Check = 1;

        }
        if (EGT2_Intermit.isSelected()) {
            EGT2_Intermittent = 1;
        }

        //Generating : Flashing Module
        if (port.isOpen()) {
            try {
                OutputStream Serial = port.getOutputStream();
                Serial.flush();

                //Sending Data
                Serial.write(0xAA);
                Serial.write(0x56);
                Serial.write(0x69);

                //Battery
                Serial.write(BatteryCheck >> 8);
                Serial.write(BatteryCheck);
                Serial.write(((int) BatteryADC.getValue()) >> 8);
                Serial.write((int) BatteryADC.getValue());

                //TPS
                Serial.write(TPSCheck >> 8);
                Serial.write(TPSCheck);
                Serial.write(((int) TPS_ADC.getValue()) >> 8);
                Serial.write((int) TPS_ADC.getValue());

                //EGR
                Serial.write(EGRCheck >> 8);
                Serial.write(EGRCheck);
                Serial.write(((int) EGR_ADC.getValue()) >> 8);
                Serial.write((int) EGR_ADC.getValue());

                //PressureSensor
                Serial.write(PressureSensorCheck >> 8);
                Serial.write(PressureSensorCheck);
                Serial.write(((int) PressureSensor_ADC.getValue()) >> 8);
                Serial.write((int) PressureSensor_ADC.getValue());

                //Delta Pressure
                Serial.write(DeltaPressureCheck >> 8);
                Serial.write(DeltaPressureCheck);
                Serial.write(((int) DeltaPressure_ADC.getValue()) >> 8);
                Serial.write((int) DeltaPressure_ADC.getValue());

                //EOT
                Serial.write(EOTCheck >> 8);
                Serial.write(EOTCheck);
                Serial.write(((int) EOT_ADC.getValue()) >> 8);
                Serial.write((int) EOT_ADC.getValue());
                Serial.write(EOT_Intermittent >> 8);
                Serial.write(EOT_Intermittent);

                //EGT1
                Serial.write(EGT1Check >> 8);
                Serial.write(EGT1Check);
                Serial.write(((int) EGT1_ADC.getValue()) >> 8);
                Serial.write((int) EGT1_ADC.getValue());
                Serial.write(EGT1_Intermittent >> 8);
                Serial.write(EGT1_Intermittent);

                //EGT2
                Serial.write(EGT2Check >> 8);
                Serial.write(EGT2Check);
                Serial.write(((int) EGT2_ADC.getValue()) >> 8);
                Serial.write((int) EGT2_ADC.getValue());
                Serial.write(EGT2_Intermittent >> 8);
                Serial.write(EGT2_Intermittent);

                JOptionPane.showMessageDialog(null, "Error Generated!");
                Serial.close();

            } catch (IOException ex) {
                Logger.getLogger(VirtualError.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void resetError(JCheckBox Battery, JCheckBox TPS, JCheckBox EGR, JCheckBox PressureSensor, JCheckBox DeltaPressure, JCheckBox EOT, JCheckBox EGT1, JCheckBox EGT2, JSpinner BatteryADC, JSpinner TPS_ADC, JSpinner EGR_ADC, JSpinner PressureSensor_ADC, JSpinner DeltaPressure_ADC, JSpinner EOT_ADC, JSpinner EGT1_ADC, JSpinner EGT2_ADC, SerialPort port, JCheckBox EOT_Intermit, JCheckBox EGT1_Intermit, JCheckBox EGT2_Intermit) {
        int BatteryCheck = 0;
        int TPSCheck = 0;
        int EGRCheck = 0;
        int PressureSensorCheck = 0;
        int DeltaPressureCheck = 0;
        int EOTCheck = 0;
        int EGT1Check = 0;
        int EGT2Check = 0;
        int EOT_Intermittent = 0;
        int EGT1_Intermittent = 0;
        int EGT2_Intermittent = 0;

        Battery.setSelected(false);
        TPS.setSelected(false);
        EGR.setSelected(false);
        PressureSensor.setSelected(false);
        DeltaPressure.setSelected(false);
        EOT.setSelected(false);
        EGT1.setSelected(false);
        EGT2.setSelected(false);
        EOT_Intermit.setSelected(false);
        EGT1_Intermit.setSelected(false);
        EGT2_Intermit.setSelected(false);

        BatteryADC.setValue(0);
        TPS_ADC.setValue(0);
        EGR_ADC.setValue(0);
        PressureSensor_ADC.setValue(0);
        DeltaPressure_ADC.setValue(0);
        EOT_ADC.setValue(0);
        EGT1_ADC.setValue(0);
        EGT2_ADC.setValue(0);

        //Logic to update the values
        if (Battery.isSelected()) {
            BatteryCheck = 1;
        }

        if (TPS.isSelected()) {
            TPSCheck = 1;
        }

        if (EGR.isSelected()) {
            EGRCheck = 1;
        }

        if (PressureSensor.isSelected()) {
            PressureSensorCheck = 1;
        }

        if (DeltaPressure.isSelected()) {
            DeltaPressureCheck = 1;
        }

        if (EOT.isSelected()) {
            EOTCheck = 1;

        }
        if (EOT_Intermit.isSelected()) {
            EOT_Intermittent = 1;
        }

        if (EGT1.isSelected()) {
            EGT1Check = 1;
        }
        if (EGT1_Intermit.isSelected()) {
            EGT1_Intermittent = 1;
        }

        if (EGT2.isSelected()) {
            EGT2Check = 1;

        }
        if (EGT2_Intermit.isSelected()) {
            EGT2_Intermittent = 1;
        }

        //Generating : Flashing Module
        if (port.isOpen()) {
            try {
                OutputStream Serial = port.getOutputStream();
                Serial.flush();

                //Sending Data
                Serial.write(0xAA);
                Serial.write(0x56);
                Serial.write(0x69);

                //Battery
                Serial.write(BatteryCheck >> 8);
                Serial.write(BatteryCheck);
                Serial.write(((int) BatteryADC.getValue()) >> 8);
                Serial.write((int) BatteryADC.getValue());

                //TPS
                Serial.write(TPSCheck >> 8);
                Serial.write(TPSCheck);
                Serial.write(((int) TPS_ADC.getValue()) >> 8);
                Serial.write((int) TPS_ADC.getValue());

                //EGR
                Serial.write(EGRCheck >> 8);
                Serial.write(EGRCheck);
                Serial.write(((int) EGR_ADC.getValue()) >> 8);
                Serial.write((int) EGR_ADC.getValue());

                //PressureSensor
                Serial.write(PressureSensorCheck >> 8);
                Serial.write(PressureSensorCheck);
                Serial.write(((int) PressureSensor_ADC.getValue()) >> 8);
                Serial.write((int) PressureSensor_ADC.getValue());

                //Delta Pressure
                Serial.write(DeltaPressureCheck >> 8);
                Serial.write(DeltaPressureCheck);
                Serial.write(((int) DeltaPressure_ADC.getValue()) >> 8);
                Serial.write((int) DeltaPressure_ADC.getValue());

                //EOT
                Serial.write(EOTCheck >> 8);
                Serial.write(EOTCheck);
                Serial.write(((int) EOT_ADC.getValue()) >> 8);
                Serial.write((int) EOT_ADC.getValue());
                Serial.write(EOT_Intermittent >> 8);
                Serial.write(EOT_Intermittent);

                //EGT1
                Serial.write(EGT1Check >> 8);
                Serial.write(EGT1Check);
                Serial.write(((int) EGT1_ADC.getValue()) >> 8);
                Serial.write((int) EGT1_ADC.getValue());
                Serial.write(EGT1_Intermittent >> 8);
                Serial.write(EGT1_Intermittent);

                //EGT2
                Serial.write(EGT2Check >> 8);
                Serial.write(EGT2Check);
                Serial.write(((int) EGT2_ADC.getValue()) >> 8);
                Serial.write((int) EGT2_ADC.getValue());
                Serial.write(EGT2_Intermittent >> 8);
                Serial.write(EGT2_Intermittent);

                JOptionPane.showMessageDialog(null, "Reset Successful!");
                Serial.close();

            } catch (IOException ex) {
                Logger.getLogger(VirtualError.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
