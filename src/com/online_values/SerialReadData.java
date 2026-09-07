package com.online_values;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.monitor.EnDocMqttPublisher;

import com.serial.serialConnection;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manish Pandey
 */
public class SerialReadData {

    SerialPort read_Port;
    serialConnection con = new serialConnection();
    int log_Data[] = new int[84];

    //Variable
    float Battery;
    int TPS, EOT, ERPM, Pressure, DOI, VS, injections, EGR_Set_Point, Actual_EGR, Injector_Pr, DeltaPressure, error_code, error_code2, error_code3, EGT_1, EGT_2, waterLevel_val;
    int EGR_ADC, EOT_ADC, Pressure_ADC, TPS_ADC, Cons_Timer, EGT_1_ADC, EGT_2_ADC, water_at_zero;
    float EGR_ADCf, EOT_ADCf, Pressure_ADCf, TPS_ADCf, EGT_1_ADCf, EGT_2_ADCf, DeltaPressure_ADCf;
    int milStatus_OBD, pumpSt;
    int pending_DTC, stored_DTC, mil_ON_time, Demand_Consumption;
    int rpm_freeze, eot_freeze, tps_freeze, pressure_freeze, egr_freeze, freeze_frame_DTC, Actual_Consumption, levelDelta, DeltaPressure_ADC, message_index, numOfCrank;
    int deltaPressureFreeze;
    int inducement_Runtime, inducement_Runtime2, inducement_Runtime3;
    long inducement_SUM;
    int inducement1, inducement2, inducement3;
    String str_milStatus, consumptionMessage;
    int count_Timer = 0, timer;
    int rowIndexToHighlight, columnIndexToHighlight;
    String DTC_code, DTC_code2, DTC_code3;
    int waterEmpty, WIM, waterInducement;
    float FW_v = 0f;
    int actual_TPS, actual_RPM;

    int sessionCount = 1;
    
    boolean isDataSentToCloud = false;

    // Class variable (declare outside your method/timer loop)
    

    //Method to Scroll down the table
    public void ScrollTOBottom(String btnText, JScrollPane jScrollPane5) {
        AdjustmentListener[] listeners = jScrollPane5.getVerticalScrollBar().getAdjustmentListeners();
        for (AdjustmentListener listener : listeners) {
            jScrollPane5.getVerticalScrollBar().removeAdjustmentListener(listener);
        }

    }

    //Method to read online data jRa
    public void readOnlineData(JCheckBox toRead, JTable Online_Data, JTable ADC_Data, JTable DTC_Error_Data, JTable Freeze_frame_Data, JTable DTC_Description, JProgressBar milStatus, JProgressBar pumpStatus, JProgressBar engineHalt, JTable ConsumptionData, JLabel dtcCountLabel, JProgressBar waterLevel, JLabel waterLevelPercent, JLabel display_Message, JTable newFreezeFrame, JLabel display_msg_1, JRadioButtonMenuItem btn_100, JRadioButtonMenuItem btn_500, JRadioButtonMenuItem btn_1000, JTable table_DOI, JLabel comStatus, JButton btnConnect, JButton btnDisconnect, JScrollPane jScrollPane5, JLabel lbldataFrequency, JCheckBoxMenuItem convertADCtoVoltage, JProgressBar statusWIM, JProgressBar statusWaterEmpty, JProgressBar statusInducement, JLabel lbl_FW_version, JLabel comProtocol, JLabel lblFunctionalityTest) {
        read_Port = con.getSerial();
        DefaultTableModel OD_table_model = (DefaultTableModel) Online_Data.getModel();
        DefaultTableModel ADC_table_model = (DefaultTableModel) ADC_Data.getModel();
        DefaultTableModel DTC_Error_table_model = (DefaultTableModel) DTC_Error_Data.getModel();
        DefaultTableModel Freeze_frame_table_model = (DefaultTableModel) Freeze_frame_Data.getModel();
        DefaultTableModel DTC_Description_table_model = (DefaultTableModel) DTC_Description.getModel();
        DefaultTableModel consumption_table_model = (DefaultTableModel) ConsumptionData.getModel();
        DefaultTableModel newFreeze_model = (DefaultTableModel) newFreezeFrame.getModel();
        DefaultTableModel DOI_model = (DefaultTableModel) table_DOI.getModel();

        if (!toRead.isSelected()) {
            read_Port.addDataListener(new SerialPortDataListener() {

                @Override
                public void serialEvent(SerialPortEvent spe) {

                    //Handling Disconnection
                    if (spe.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) {
                        System.out.println("MAYDAY - MAYDAY - MAYDAY");
                        System.out.println("UART was Accidently removed!!!!");
                        comStatus.setForeground(Color.yellow);
                        comStatus.setText("Disconnected");
                        read_Port.closePort();
                        btnDisconnect.setEnabled(false);
                        btnConnect.setEnabled(true);
                        ScrollTOBottom("Scroll", jScrollPane5);

                    }

                    try {

                        InputStream read_Stream;
                        read_Stream = read_Port.getInputStream();

                        int triggerVal = 255;
                        int counter;
                        int alterCounter = 0;
                        boolean shouldStore = false;
                        //checking Everytime
                        while ((counter = read_Stream.read()) != -1) {
                            //System.out.println(" counter:  " + counter);
                            //System.out.println("alter: " + alterCounter);
                            if ((counter == triggerVal)) {
                                alterCounter++;
                               // System.out.println("alter: " + alterCounter);
                                if (alterCounter == 3) {
                                    shouldStore = true;
                                    count_Timer += 100;
                                    //System.out.println("ReadCounter : " + count_Timer);

                                    alterCounter = 0;
                                }
                            } else {
                                alterCounter = 0;
                            }

                            if (btn_100.isSelected()) {
                                timer = 100;
                                lbldataFrequency.setText("100 ms");

                            }
                            if (btn_500.isSelected()) {
                                timer = 500;
                                lbldataFrequency.setText("500 ms");

                            }
                            if (btn_1000.isSelected()) {
                                timer = 1000;
                                lbldataFrequency.setText("1000 ms");

                            }
                            if (count_Timer > timer) {
                                count_Timer = 0;
                            }

                            //Now it counter equals trigger it will read data
                            if (shouldStore && timer == count_Timer) {

                                for (int i = 1; i < 84; i++) {
                                    log_Data[i] = read_Stream.read();
                                   // System.out.print(log_Data[i] + " ");
                                }

                                //Online Data
                                Battery = (log_Data[1] << 8) + log_Data[2];
                                Battery = Battery / 100;                              //Battery  
                                TPS = log_Data[3];                                    //TPS
                                ERPM = (log_Data[4] << 8) + log_Data[5];              //ERPM                  
                                EOT = (log_Data[6] << 8) + log_Data[7];               //EOT  
                                if (EOT > 60000) {
                                    EOT = EOT - 65535;
                                }
                                DOI = (log_Data[8] << 8) + log_Data[9];             //DOI                   
                                injections = (log_Data[10] << 8) + log_Data[11];      //injections                          
                                VS = (log_Data[12] << 8) + log_Data[13];              //VS
                                Pressure = (log_Data[14] << 8) + log_Data[15];          //Solvent Pressure
                                EGR_Set_Point = log_Data[16];                         //EGR Set Point
                                Actual_EGR = log_Data[17];
                                if (Actual_EGR > 200) {
                                    Actual_EGR = 0;
                                }                                                     //Actual EGR                                
                                DeltaPressure = (log_Data[18] << 8) + log_Data[19];   //Delta Pressure
                                if (DeltaPressure > 60000) {
                                    DeltaPressure = DeltaPressure - 65535;
                                }
                                EGT_1 = (log_Data[20] << 8) + log_Data[21];           //EGT 1
                                if (EGT_1 > 60000) {
                                    EGT_1 = EGT_1 - 65535;
                                }
                                EGT_2 = (log_Data[22] << 8) + log_Data[23];           //EGT 2
                                if (EGT_2 > 60000) {
                                    EGT_2 = EGT_2 - 65535;
                                }

                                //ADC
                                EOT_ADC = (log_Data[24] << 8) + log_Data[25];         //EOT ADC
                                EGR_ADC = (log_Data[26] << 8) + log_Data[27];         //EGR ADC
                                Pressure_ADC = (log_Data[28] << 8) + log_Data[29];    //Pressure ADC
                                DeltaPressure_ADC = (log_Data[30] << 8) + log_Data[31]; // Delta Pressure ADC
                                EGT_1_ADC = (log_Data[32] << 8) + log_Data[33];        //EGT 1 ADC

                                EGT_2_ADC = (log_Data[34] << 8) + log_Data[35];        //EGT 2 ADC
                                TPS_ADC = (log_Data[36] << 8) + log_Data[37];         //TPS ADC
                                error_code = (log_Data[38] << 8) + log_Data[39];      //DTC Code
                                error_code2 = (log_Data[77] << 8) + log_Data[78];
                                error_code3 = (log_Data[82] << 8) + log_Data[83];

//                                System.out.println("error code: " + error_code);
//                                System.out.println("error code 2: " + error_code2);
//                                System.out.println("error code 3: " + error_code3);

                                EGT_1_ADC = (log_Data[32] << 8) + log_Data[33];           //EGT 2
                                if (EGT_1_ADC > 60000) {
                                    EGT_1_ADC = EGT_1_ADC - 65535;
                                }

                                //Indicators
                                milStatus_OBD = log_Data[40];                        //MIL Status
                                pumpSt = log_Data[41];                               //Pump Status
                                waterEmpty = log_Data[73];
                                waterInducement = log_Data[74];
                                WIM = log_Data[75];

                                //Consumptions
                                Cons_Timer = (log_Data[42] << 8) + log_Data[43];          //Cons_Timer
                                Actual_Consumption = (log_Data[44] << 8) + log_Data[45];  //Actual Consumption                                
                                Demand_Consumption = (log_Data[46] << 8) + log_Data[47];  //Demand Consumption
                                Injector_Pr = (log_Data[48] << 8) + log_Data[49];         //Injector Pressure
                                levelDelta = (log_Data[50] << 8) + log_Data[51];          //Level Delta
                                message_index = (log_Data[68]);
                                numOfCrank = (log_Data[69]);
                                water_at_zero = (log_Data[70]);
                                inducement_Runtime = log_Data[79];
                                inducement_Runtime2 = log_Data[80];
                                inducement_Runtime3 = log_Data[81];
                                //Freeze Frame Data                         
                                //milStatus_OBD
                                pending_DTC = log_Data[52];                               //Pending DTC
                                stored_DTC = log_Data[53];                                //stored DTC
                                mil_ON_time = log_Data[54];                               //mil_ONTime
                                freeze_frame_DTC = (log_Data[55] << 8) + log_Data[56];    //Freeze frame dtc
                                String dataHex = Integer.toHexString(freeze_frame_DTC).toUpperCase();
                                if (dataHex.length() == 3) {
                                    dataHex = "P0" + dataHex;
                                } else {
                                    dataHex = "P" + dataHex;
                                }

                                if (dataHex.equals("P0")) {
                                    dataHex = "0";
                                }

                                FW_v = log_Data[76];
                                rpm_freeze = (log_Data[57] << 8) + log_Data[58];          //RPM freeze
                                eot_freeze = (log_Data[59] << 8) + log_Data[60];          //EOT Freeze
                                tps_freeze = (log_Data[61] << 8) + log_Data[62];          //TPS Freeze
                                pressure_freeze = (log_Data[63] << 8) + log_Data[64];     //Pressure Freeze
                                egr_freeze = (log_Data[65] << 8) + log_Data[66];          //EGR Freeze
                                waterLevel_val = log_Data[67];
                                deltaPressureFreeze = (log_Data[71] << 8) + log_Data[72];
                                if (deltaPressureFreeze > 60000) {
                                    deltaPressureFreeze = deltaPressureFreeze - 65535;
                                }
                                //EGT1_freeze = (log_Data[73] << 8) + log_Data[74];
                                //EGT2_freeze = (log_Data[75] << 8) + log_Data[76];

                              //  System.out.println();
//                                System.out.println("Log Data 74: " + log_Data[73]);
//                                System.out.println("Log Data 75: " + log_Data[75]);
//                                System.out.println("FWv: " + FW_v);

                                int Hour = 0, Minutes = 0;
                                inducement1 = inducement_Runtime;

                                inducement2 = (inducement_Runtime2 * 256);

                                inducement3 = ((((inducement_Runtime3 * 256 * 256) + inducement2 + inducement1)) - 40);

                                Hour = inducement3 / 3600;
                                Minutes = (inducement3 % 3600) / 60;

                               // System.out.println("InduceMent SUM: " + inducement_SUM);

                                //DTC module goes here 
                                int binaryArray[] = new int[17];
                                int binaryArray2[] = new int[17];
                                int binaryArray3[] = new int[17];

                                int indexOfBinary = 0;
                                while (error_code > 0) {
                                    // System.out.println("Error Code: " + error_code);
                                    binaryArray[indexOfBinary++] = error_code % 2;
                                    error_code = error_code / 2;
                                }

                                //          
                                indexOfBinary = 0;
                                while (error_code2 > 0) {
                                    // System.out.println("Error Code: " + error_code2);
                                    binaryArray2[indexOfBinary++] = error_code2 % 2;
                                    error_code2 = error_code2 / 2;
                                }

                                indexOfBinary = 0;
                                while (error_code3 > 0) {
                                    // System.out.println("Error Code: " + error_code3);
                                    binaryArray3[indexOfBinary++] = error_code3 % 2;
                                    error_code3 = error_code3 / 2;
                                }

                                DTC_Description_table_model.setRowCount(0);
                                if (binaryArray[0] == 1) {
                                    Object[] dtcaddTest = {"P0335", "RPM Sensor open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[1] == 1) {
                                    Object[] dtcaddTest = {"P0120", "TPS open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[2] == 1) {
                                    Object[] dtcaddTest = {"P0123", "TPS circuit high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[3] == 1) {
                                    Object[] dtcaddTest = {"P2047", "Injector open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[4] == 1) {
                                    Object[] dtcaddTest = {"P0195", "EOT open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[5] == 1) {
                                    Object[] dtcaddTest = {"P0197", "EOT circuit low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[6] == 1) {
                                    Object[] dtcaddTest = {"P0196", "EOT out of range"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[7] == 1) {
                                    Object[] dtcaddTest = {"P0690", "Battery voltage very high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[8] == 1) {
                                    Object[] dtcaddTest = {"P0689", "Battery voltage low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[9] == 1) {
                                    Object[] dtcaddTest = {"P0403", "EGR open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[10] == 1) {
                                    Object[] dtcaddTest = {"P0405", "EGR circuit low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[11] == 1) {
                                    Object[] dtcaddTest = {"P0406", "EGR circuit high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[12] == 1) {
                                    Object[] dtcaddTest = {"P0401", "EGR stuck flow insufficient"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[13] == 1) {
                                    Object[] dtcaddTest = {"P0402", "EGR stuck flow excessive"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[14] == 1) {
                                    Object[] dtcaddTest = {"P0404", "EGR out of range"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray[15] == 1) {
                                    Object[] dtcaddTest = {"P204A", "Reductant pressure open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }

                                //Binary code 2
                                if (binaryArray2[0] == 1) {
                                    Object[] dtcaddTest = {"P204D", "Reductant pressure circuit high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[1] == 1) {
                                    Object[] dtcaddTest = {"P0199", "EOT Performance"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[2] == 1) {
                                    Object[] dtcaddTest = {"P20F4", "Reductant consumption too low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[3] == 1) {
                                    Object[] dtcaddTest = {"P20F5", "Reductant consumption too high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[4] == 1) {
                                    Object[] dtcaddTest = {"P204B", "Reductant pressure out of range"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[5] == 1) {
                                    Object[] dtcaddTest = {"P20E8", "Reductant pressure too low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[6] == 1) {
                                    Object[] dtcaddTest = {"P203F", "Reductant level empty"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[7] == 1) {
                                    Object[] dtcaddTest = {"P2455", "Delta pressure circuit high"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[8] == 1) {
                                    Object[] dtcaddTest = {"P2452", "Delta pressure open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[9] == 1) {
                                    Object[] dtcaddTest = {"P2453", "Delta pressure out of range"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[10] == 1) {
                                    Object[] dtcaddTest = {"P242F", "Delta Pressure signal stuck"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[11] == 1) {
                                    Object[] dtcaddTest = {"P025A", "cut-off solenoid open circuit"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[12] == 1) {
                                    Object[] dtcaddTest = {"P0400", "EGR Circulation Flow Error"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[13] == 1) {
                                    Object[] dtcaddTest = {"P0545", "EGT1 Circuit low"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[14] == 1) {
                                    Object[] dtcaddTest = {"P0544", "EGT1 circuit open"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray2[15] == 1) {
                                    Object[] dtcaddTest = {"P2080", "EGT1 circuit out of range"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }

                                //Binary code 3
                                if (binaryArray3[0] == 1) {
                                    Object[] dtcaddTest = {"P244B", "PF Diffrential Pr. too High"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray3[1] == 1) {
                                    Object[] dtcaddTest = {"P244A", "PF Diffrential Pr. too LOW"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray3[2] == 1) {
                                    Object[] dtcaddTest = {"P226D", "PF Missing substrate bank 1"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray3[3] == 1) {
                                    Object[] dtcaddTest = {"P204E", "Reductant Pressure Signal stuck"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray3[4] == 1) {
                                    Object[] dtcaddTest = {"P0198", "EOT Signal High/stuck"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }
                                if (binaryArray3[5] == 1) {
                                    Object[] dtcaddTest = {"P2454", "Diffrential Pressure Performance Error"};
                                    DTC_Description_table_model.addRow(dtcaddTest);
                                }

                                try {
                                    DTC_code = (String) DTC_Description_table_model.getValueAt(0, 0);
                                } catch (Exception e) {
                                    DTC_code = "OK";
                                }

                                try {
                                    DTC_code2 = (String) DTC_Description_table_model.getValueAt(1, 0);
                                } catch (Exception e) {
                                    DTC_code2 = "OK";
                                }

                                try {
                                    DTC_code3 = (String) DTC_Description_table_model.getValueAt(2, 0);
                                } catch (Exception e) {
                                    DTC_code3 = "OK";
                                }

                                //Consumption Display messages
                                if (message_index == 1) {
                                    consumptionMessage = "Interruption of Water";
                                } else if (message_index == 2) {
                                    consumptionMessage = "Inducement ON water flow error";
                                } else if (message_index == 3) {
                                    consumptionMessage = "Water injection malfunction";
                                } else if (message_index == 4) {
                                    consumptionMessage = "Level Sensor open circuit";
                                } else if (message_index == 5) {
                                    consumptionMessage = "Flow of water monitoring";
                                } else if (message_index == 6) {
                                    consumptionMessage = "No. of remaining restart: " + String.valueOf(numOfCrank);
                                } else if (message_index == 8) {
                                    consumptionMessage = "Inducement System OFF";
                                } else if (message_index == 9) {
                                    consumptionMessage = "NO Cranking allowed";
                                } else if (message_index == 10) {
                                    consumptionMessage = "Inducement ON water Level LOW";
                                } else if (message_index == 255) {
                                    consumptionMessage = "::--::--::--::--::--::--::--::";
                                }

                                if (waterLevel_val == 0) {
                                    display_msg_1.setText("Water Level Empty!");
                                    display_msg_1.setForeground(Color.red);
                                } else if (waterLevel_val > 0 && waterLevel_val < 21) {
                                    display_msg_1.setText("Fill up water!");
                                    display_msg_1.setForeground(Color.YELLOW);
                                } else if (waterLevel_val > 20 && waterLevel_val < 41) {
                                    display_msg_1.setText("Water Level Low!");
                                    display_msg_1.setForeground(Color.white);
                                } else {
                                    display_msg_1.setText("::--::--::--::--::--::--::--::");
                                    display_msg_1.setForeground(Color.white);
                                }

                                actual_TPS = TPS;

                                //System.out.println("Timer: " + timer);
                                //System.out.println(consumptionMessage);
                                display_Message.setText(consumptionMessage);

                                rowIndexToHighlight = 1;
                                columnIndexToHighlight = 1;

                                int[] load_array = {0, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90};
                                int[] RPM_array = {0, 800, 1300, 1500, 1800, 2000, 2200, 2500, 2600, 2700, 2800, 2900, 3000, 3200, 3500, 4000, 4500};

                                //Calibration Task for LUT Tracking
                                try {
                                    for (int key = 1; key < load_array.length; key++) {
                                        if (key < 16) {
                                            if (TPS > 90) {
                                                TPS = 90;
                                            }
                                            if (TPS >= load_array[key] && TPS <= load_array[key + 1]) {
                                                rowIndexToHighlight = key;

                                            }
                                        }

                                        if (key < 16) {
                                            if (ERPM >= RPM_array[key] && ERPM <= RPM_array[key + 1]) {
                                                columnIndexToHighlight = key;
                                                //break;
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //System.out.println("Battery: " + (int) Battery);
                                if (Battery < 100) {
                                    count_Timer = 0;
                                    //Converting all ADC to Voltage if required
                                    if (convertADCtoVoltage.isSelected()) {
                                        EGR_ADCf = (float) ((((EGR_ADC * 1.23) / 10)) / 100);
                                        EOT_ADCf = (float) ((EOT_ADC * 1.23) / 10) / 100;
                                        Pressure_ADCf = (float) ((Pressure_ADC * 1.23) / 10) / 100;
                                        TPS_ADCf = (float) ((TPS_ADC * 1.23) / 10) / 100;
                                        DeltaPressure_ADCf = (float) ((DeltaPressure_ADC * 1.23) / 10) / 100;
                                        EGT_1_ADCf = (float) ((EGT_1_ADC * 1.23) / 10) / 100;
                                        EGT_2_ADCf = (float) ((EGT_2_ADC * 1.23) / 10) / 100;

                                        Object[] row = {Battery, actual_TPS, ERPM, EOT, DOI, injections, VS, Pressure, EGR_Set_Point, Actual_EGR, DeltaPressure, EGT_1, EGT_2, Cons_Timer, DTC_code, DTC_code2, DTC_code3, String.format("%.02f", EOT_ADCf), String.format("%.02f", EGR_ADCf), String.format("%.02f", Pressure_ADCf), String.format("%.02f", DeltaPressure_ADCf), String.format("%.02f", EGT_1_ADCf), String.format("%.02f", EGT_2_ADCf), String.format("%.02f", TPS_ADCf)};

                                        Object[] row_ADC = {String.format("%.02f", EGR_ADCf), String.format("%.02f", EOT_ADCf), String.format("%.02f", Pressure_ADCf), String.format("%.02f", TPS_ADCf), String.format("%.02f", DeltaPressure_ADCf), String.format("%.02f", EGT_1_ADCf), String.format("%.02f", EGT_2_ADCf)};
                                        OD_table_model.addRow(row);
                                        ADC_table_model.setRowCount(0);
                                        ADC_table_model.addRow(row_ADC);
                                    } else {
                                        Object[] row = {Battery, actual_TPS, ERPM, EOT, DOI, injections, VS, Pressure, EGR_Set_Point, Actual_EGR, DeltaPressure, EGT_1, EGT_2, Cons_Timer, DTC_code, DTC_code2, DTC_code3, EOT_ADC, EGR_ADC, Pressure_ADC, DeltaPressure_ADC, EGT_1_ADC, EGT_2_ADC, TPS_ADC};

                                        Object[] row_ADC = {EGR_ADC, EOT_ADC, Pressure_ADC, TPS_ADC, DeltaPressure_ADC, EGT_1_ADC, EGT_2_ADC};
                                        OD_table_model.addRow(row);
                                        ADC_table_model.setRowCount(0);
                                        ADC_table_model.addRow(row_ADC);
                                    }

                                    if (waterLevel_val <= 20) {
                                        waterLevel.setForeground(Color.red);
                                    } else if (waterLevel_val > 20 && waterLevel_val < 51) {
                                        waterLevel.setForeground(Color.ORANGE);
                                    } else if (waterLevel_val > 50 && waterLevel_val < 63) {
                                        waterLevel.setForeground(Color.YELLOW);
                                    } else {
                                        waterLevel.setForeground(Color.green);
                                    }

                                    waterLevelPercent.setText(String.valueOf(waterLevel_val) + "%");
                                    waterLevel.setValue(waterLevel_val);

                                    dtcCountLabel.setText(String.valueOf(pending_DTC));

                                    DTC_Error_table_model.setRowCount(0);
                                    Object[] row_error_DTC = {str_milStatus, stored_DTC, pending_DTC, mil_ON_time};
                                    DTC_Error_table_model.addRow(row_error_DTC);

                                    Freeze_frame_table_model.setRowCount(0);
                                    Object[] row_freeze_frame = {dataHex, rpm_freeze, eot_freeze, tps_freeze, pressure_freeze, egr_freeze};
                                    Freeze_frame_table_model.addRow(row_freeze_frame);

                                    newFreeze_model.setRowCount(0);
                                    Object[] row_newFreeze = {deltaPressureFreeze};
                                    newFreeze_model.addRow(row_newFreeze);

                                    consumption_table_model.setRowCount(0);
                                    Object[] row_Consumption_frame = {Actual_Consumption, Demand_Consumption, Injector_Pr, levelDelta, Cons_Timer, waterLevel_val, water_at_zero, String.valueOf(Hour) + " Hr " + String.valueOf(Minutes) + " min"};
                                    consumption_table_model.addRow(row_Consumption_frame);

                                    if (milStatus_OBD != 0) {

                                        milStatus.setValue(100);
                                        milStatus.setString("1");
                                        milStatus.setForeground(Color.red);
                                        str_milStatus = "ON";
                                    } else if (milStatus_OBD == 0) {
                                        milStatus.setValue(100);
                                        milStatus.setString("0");
                                        milStatus.setForeground(Color.GREEN);
                                        str_milStatus = "OFF";
                                    }

                                    if (pumpSt == 1) {
                                        pumpStatus.setValue(1);
                                        pumpStatus.setString("1");
                                        //pumpStatus.setForeground(Color.RED);                                        
                                        pumpStatus.setForeground(Color.GREEN);
                                    } else if (pumpSt == 0) {
                                        pumpStatus.setValue(1);
                                        pumpStatus.setString("0");
                                        //pumpStatus.setForeground(Color.GREEN);
                                        pumpStatus.setForeground(Color.RED);
                                    }

                                    if (ERPM < 100) {

                                        engineHalt.setValue(100);
                                        engineHalt.setString("1");
                                        engineHalt.setForeground(Color.red);
                                    } else {
                                        engineHalt.setValue(100);
                                        engineHalt.setString("0");
                                        engineHalt.setForeground(Color.GREEN);
                                    }

                                    if (waterEmpty == 1) {
                                        statusWaterEmpty.setValue(1);
                                        statusWaterEmpty.setString("1");
                                        statusWaterEmpty.setForeground(Color.red);
                                    } else {
                                        statusWaterEmpty.setValue(1);
                                        statusWaterEmpty.setString("0");
                                        statusWaterEmpty.setForeground(Color.GREEN);
                                    }

                                    if (WIM == 1) {
                                        statusWIM.setValue(1);
                                        statusWIM.setString("1");
                                        statusWIM.setForeground(Color.RED);
                                    } else {
                                        statusWIM.setValue(1);
                                        statusWIM.setString("0");
                                        statusWIM.setForeground(Color.GREEN);
                                    }

                                    if (waterInducement == 1) {
                                        statusInducement.setValue(1);
                                        statusInducement.setString("1");
                                        statusInducement.setForeground(Color.RED);
                                    } else {
                                        statusInducement.setValue(1);
                                        statusInducement.setString("0");
                                        statusInducement.setForeground(Color.GREEN);
                                    }

                                    int ideal_Injection = ERPM / 60;

// Functionality Testing
                                    if (!(DOI > 60000 && Pressure > 60000)) {
                                        if (pending_DTC == 0) {
                                            lblFunctionalityTest.setForeground(Color.ORANGE);
                                            lblFunctionalityTest.setText("Passed Indication Checks");

                                            if (milStatus.getString().equals("0") && pumpStatus.getString().equals("1")
                                                    && engineHalt.getString().equals("0") && statusWaterEmpty.getString().equals("0")
                                                    && statusWIM.getString().equals("0") && statusInducement.getString().equals("0")) {

                                                lblFunctionalityTest.setForeground(Color.ORANGE);
                                                lblFunctionalityTest.setText("Passed DTC Checks");

                                                if ((Hour == 0 && Minutes < 3) && mil_ON_time == 0
                                                        && Actual_Consumption == 480 && Demand_Consumption == 450) {

                                                    lblFunctionalityTest.setForeground(Color.ORANGE);
                                                    lblFunctionalityTest.setText("Passed Inducement Test");

                                                    if (injections <= (ideal_Injection + 3) && injections >= (ideal_Injection - 3)) {

                                                        lblFunctionalityTest.setForeground(Color.GREEN);
                                                        lblFunctionalityTest.setText("Functionality OKAY");
                                                        
                                                         
                                                        if(!isDataSentToCloud){
                                                            System.out.println("Calling MQTT Service");
                                                            java.util.Map<String, Object> payloadMap = new java.util.HashMap<>();
                                                            payloadMap.put("sessionCount", sessionCount);
                                                            payloadMap.put("status", "Functionality Okay");
                                                            payloadMap.put("timestamp", System.currentTimeMillis());

                                                            EnDocMqttPublisher.sendMqttData(payloadMap);

                                                            // Lock flag so 100ms timer cycle won't trigger another MQTT push
                                                            isDataSentToCloud = true;
                                                        }

                                                      

                                                    } else {
                                                        lblFunctionalityTest.setForeground(Color.RED);
                                                        lblFunctionalityTest.setText("Injector not operating");
                                                    }
                                                } else {
                                                    lblFunctionalityTest.setForeground(Color.RED);
                                                    lblFunctionalityTest.setText("Inducement ERROR");
                                                }
                                            } else {
                                                lblFunctionalityTest.setForeground(Color.RED);
                                                lblFunctionalityTest.setText("Indication ERROR");
                                            }
                                        } else {
                                            lblFunctionalityTest.setForeground(Color.RED);
                                            lblFunctionalityTest.setText("DTC Code Found");
                                        }
                                    } else {
                                        lblFunctionalityTest.setForeground(Color.RED);
                                        lblFunctionalityTest.setText("Flash The ECU");

                                        // Reset lock when "Flash The ECU" condition is reached
                                        isDataSentToCloud = false;
                                    }

                                    lbl_FW_version.setText("V_" + String.valueOf(FW_v / 10));

                                    table_DOI.setDefaultRenderer(DOI_model.getColumnClass(columnIndexToHighlight), new HighlightCellRenderer(rowIndexToHighlight, columnIndexToHighlight));

                                }

                                shouldStore = false;

                            }
                        }

                    } catch (IOException ex) {

//                       
                        Logger.getLogger(SerialReadData.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                @Override

                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
                }

            }
            );
        } else {
            read_Port.removeDataListener();

            try {

                if (comProtocol.getText().equals("CAN")) {
                    OutputStream Serial = read_Port.getOutputStream();
                    InputStream inRead = read_Port.getInputStream();
                    Serial.flush();
                    //inRead.reset();
                    read_Port.removeDataListener();

//                Serial.write(0xAA);
//                Serial.write(0x56);
//                Serial.write(0x23);
                    Serial.flush();
                    Serial.flush();
                    Serial.flush();
                } else {

                    //Call method to read data from the ECU
                    OutputStream Serial = read_Port.getOutputStream();
                    InputStream inRead = read_Port.getInputStream();
                    Serial.flush();
                    //inRead.reset();
                    read_Port.removeDataListener();

                    Serial.write(0xAA);
                    Serial.write(0x56);
                    Serial.write(0x23);
                    Serial.flush();
                    Serial.flush();
                    Serial.flush();

                    Serial.close();

                }

                //Call method to read data from the ECU
            } catch (IOException ex) {
                Logger.getLogger(SerialReadData.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    static class HighlightCellRenderer extends DefaultTableCellRenderer {

        private final int targetRow;
        private final int targetColumn;

        HighlightCellRenderer(int targetRow, int targetColumn) {
            this.targetRow = targetRow;
            this.targetColumn = targetColumn;

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row == targetRow && column == targetColumn) {
                cellComponent.setBackground(Color.YELLOW); // Change the background color to highlight
                table.repaint();

            } else {
                cellComponent.setBackground(table.getBackground());
                table.repaint();

            }

            return cellComponent;
        }
    }

    //Method to fetch first Digits 
    public static int getFirstDigit(int number) {
        // Convert the number to a string
        String numberStr = Integer.toString(Math.abs(number)); // Use Math.abs to handle negative numbers
        // Get the first character and convert it back to an integer
        char firstChar = numberStr.charAt(0);
        char secondChar = numberStr.charAt(1);
        int num = (Character.getNumericValue(firstChar) * 10) + Character.getNumericValue(secondChar);
        return num;
    }

}
