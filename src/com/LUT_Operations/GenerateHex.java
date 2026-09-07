package com.LUT_Operations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manish Pandey
 */
public class GenerateHex {

    int data[] = new int[1248];
    String filePath;

    int DOI[] = new int[513];
    int EGR[] = new int[257];
    int ERPM[] = new int[33];
    int TPS[] = new int[17];
    int TempArray[] = new int[33];
    int TempADC[] = new int[33];
    int TempCF[] = new int[17];
    int PressureADC[] = new int[17];
    int PressureVoltage[] = new int[9];
    int cal_array_num[] = new int[17];
    int EGTADC[] = new int[33];
    int EGT1[] = new int[33];
    int EGT2[] = new int[33];
    int DP_ADC[] = new int[25];
    int DP_Voltage[] = new int[25];
    int[] D_Bounce_array = new int[200];
    int[] D_Bounce_Min_ADC = new int[200];
    int[] D_Bounce_Max_ADC = new int[200];
    int DBounce_count = 0;

    public void genHex(JTable table_DOI, JTable table_EGR, JLabel EGR_SUM, JTable table_Temperature, JTable table_Pressure, String cal_ID, JTable table_EGT, JTable table_DeltaPressure, int[] EngineParams, JTable table_DBounce) {

        // Get the path to the user's "Documents" folder
        String userHome = System.getProperty("user.home");
        File documentsFolder = new File(userHome, "Documents");

        // Create the "EnDoc" folder if it does not exist
        File enDocFolder = new File(documentsFolder, "EnDoc/EnDoc_MAP");
        if (!enDocFolder.exists()) {
            enDocFolder.mkdirs();
        }

        // Opening JFileChooser
        JFileChooser selectFile = new JFileChooser(enDocFolder);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Hex Files", "hex");
        selectFile.setFileFilter(filter);
        selectFile.setDialogTitle("Save Lookup Table");

        int response = selectFile.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = selectFile.getSelectedFile();
            filePath = file.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".hex")) {
                filePath += ".hex";
            }
        } else if (response == JFileChooser.CANCEL_OPTION) {
            filePath = "NoFile";
        }

        if (filePath.equals("NoFile")) {
            JOptionPane.showMessageDialog(null, "Operation was cancelled!");
        } else {

            //Call methods to fetch and save data 
            fetch_DOI(table_DOI);
            fetch_ERPM(table_DOI);
            fetch_TPS(table_DOI);
            fetch_EGR(table_EGR);
            fetch_Temperature(table_Temperature);
            fetch_Pressure(table_Pressure);
            fetch_CALID(cal_ID);
            fetch_EGT(table_EGT);
            fetch_DeltaPressure(table_DeltaPressure);
            fetch_Engine_Params(EngineParams);
            fetch_DBounce_array(table_DBounce);

            appendDataToHexArray(EGR_SUM);
            generateHexFileFromArray(data, filePath);
        }
    }

    //This method generates Hex file for map
    private void generateHexFileFromArray(int[] data, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {

            for (int value : data) {
                fos.write(value & 0xFF);
            }

            JOptionPane.showMessageDialog(null, "Hex File was generated Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////  METHODS TO FETCH DATA  /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Fetch DOI Data
    private void fetch_DOI(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // 16*16 = 256 Data Point; 1DP = 2 => 2*256 = 512 Array Elements
        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            for (int col = 1; col < model.getColumnCount(); col++) {
                DOI[counter++] = ((int) model.getValueAt(row, col)) >> 8;
                DOI[counter++] = ((int) model.getValueAt(row, col));
            }
        }
    }

    //Fetch ERPM Data
    private void fetch_ERPM(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        //16*1 = 16 Data Point; 1DP = 2 => 2*16 = 32 Array Elemets
        int counter = 1;
        for (int col = 1; col < model.getRowCount(); col++) {
            ERPM[counter++] = ((int) model.getValueAt(0, col)) >> 8;
            ERPM[counter++] = ((int) model.getValueAt(0, col));
        }
    }

    //Fetch TPS Data
    private void fetch_TPS(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        //16*1 = 16 Data Point; 1DP = 1 => 1*16 = 16 Array Elemets
        int counter = 1;
        for (int i = 1; i < model.getRowCount(); i++) {
            TPS[counter++] = ((int) model.getValueAt(i, 0));
        }
    }

    //Fetch EGR Data
    private void fetch_EGR(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        //16*16 = 256 Data Point; 1DP = 1 => 1*256 = 256 Array Elements
        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            for (int col = 1; col < model.getColumnCount(); col++) {
                EGR[counter++] = ((int) model.getValueAt(col, row));
            }
        }
    }

    //Fetch Temperature Data
    private void fetch_Temperature(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            TempArray[counter++] = ((int) model.getValueAt(row, 0)) >> 8;
            TempArray[counter++] = ((int) model.getValueAt(row, 0));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            TempADC[counter++] = ((int) model.getValueAt(row, 2)) >> 8;
            TempADC[counter++] = ((int) model.getValueAt(row, 2));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            TempCF[counter++] = ((int) model.getValueAt(row, 1));
        }
    }

    //Fetch Pressure Data
    private void fetch_Pressure(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            PressureADC[counter++] = ((int) model.getValueAt(row, 0)) >> 8;
            PressureADC[counter++] = ((int) model.getValueAt(row, 0));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            PressureVoltage[counter++] = ((int) model.getValueAt(row, 1));
        }
    }

    private void fetch_CALID(String cal_ID) {
        int[] cal_array = new int[17];

        //Converting calId into integer values
        for (int i = 1; i < 17; i++) {
            cal_array[i] = (int) cal_ID.charAt(i - 1);
        }

        //Storing numerical data
        cal_array_num[1] = cal_array[1];
        cal_array_num[2] = cal_array[2];
        cal_array_num[3] = cal_array[3];
        cal_array_num[4] = cal_array[4];
        cal_array_num[5] = cal_array[5];
        cal_array_num[6] = cal_array[6];
        cal_array_num[7] = cal_array[7];

        //Storing alphabetical data
        cal_array_num[8] = cal_array[8];
        cal_array_num[9] = cal_array[9];
        cal_array_num[10] = cal_array[10];
        cal_array_num[11] = cal_array[11];
        cal_array_num[12] = cal_array[12];
        cal_array_num[13] = cal_array[13];
        cal_array_num[14] = cal_array[14];
        cal_array_num[15] = cal_array[15];
        cal_array_num[16] = cal_array[16];
    }

    //Fetch EGT Data
    private void fetch_EGT(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            EGTADC[counter++] = ((int) model.getValueAt(row, 1)) >> 8;
            EGTADC[counter++] = ((int) model.getValueAt(row, 1));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            EGT1[counter++] = ((int) model.getValueAt(row, 0)) >> 8;
            EGT1[counter++] = ((int) model.getValueAt(row, 0));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            EGT2[counter++] = ((int) model.getValueAt(row, 2)) >> 8;
            EGT2[counter++] = ((int) model.getValueAt(row, 2));
        }

    }

    //Fetch Delta Pressure Data
    private void fetch_DeltaPressure(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        int counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            DP_ADC[counter++] = ((int) model.getValueAt(row, 1)) >> 8;
            DP_ADC[counter++] = ((int) model.getValueAt(row, 1));
        }

        counter = 1;
        for (int row = 1; row < model.getRowCount(); row++) {
            DP_Voltage[counter++] = ((int) model.getValueAt(row, 1)) >> 8;
            DP_Voltage[counter++] = ((int) model.getValueAt(row, 1));
        }

    }

    int Engine_Speed, Fuel_cut_OFF, DOI_microsec, Delay_for_injection, EOT_C, EOT_CF, pumpON, glowPlugON, VSS_CF, max_Acc, EGR_POT_ADC_Max;
    int EGR_POT_ADC_Min, Consumption_Error_Max, Consumption_Error_Min, WIReset, MinConsumption, Inducement, Para3, Para4, Para5, Para6, Para7;

    //Fetch Engine Param
    private void fetch_Engine_Params(int[] Params) {
        Engine_Speed = Params[1];
        Fuel_cut_OFF = Params[2];
        DOI_microsec = Params[3];
        Delay_for_injection = Params[4];
        EOT_C = Params[5];
        EOT_CF = Params[6];
        pumpON = Params[7];
        glowPlugON = Params[8];
        VSS_CF = Params[9];
        max_Acc = Params[10];
        EGR_POT_ADC_Max = Params[11];
        EGR_POT_ADC_Min = Params[12];
        Consumption_Error_Max = Params[13];
        Consumption_Error_Min = Params[14];
        WIReset = Params[15];
        MinConsumption = Params[16];
        Inducement = Params[17];
        Para3 = Params[18];
        Para4 = Params[19];
        Para5 = Params[20];
        Para6 = Params[21];
        Para7 = Params[22];
    }

    //Fetch D_Bounce Time
    private void fetch_DBounce_array(JTable Dbounce) {
        DefaultTableModel Dbounce_model = (DefaultTableModel) Dbounce.getModel();
        DBounce_count = Dbounce_model.getRowCount();
        int min_ADC_Counter = 0;
        int max_ADC_Counter = 0;
        for (int i = 0; i < Dbounce_model.getRowCount(); i++) {
            D_Bounce_array[i] = Integer.parseInt((String) Dbounce_model.getValueAt(i, 3));

            D_Bounce_Min_ADC[min_ADC_Counter++] = Integer.parseInt((String) Dbounce_model.getValueAt(i, 4)) >> 8;
            D_Bounce_Min_ADC[min_ADC_Counter++] = Integer.parseInt((String) Dbounce_model.getValueAt(i, 4));

            D_Bounce_Max_ADC[max_ADC_Counter++] = Integer.parseInt((String) Dbounce_model.getValueAt(i, 5)) >> 8;
            D_Bounce_Max_ADC[max_ADC_Counter++] = Integer.parseInt((String) Dbounce_model.getValueAt(i, 5));

        }
    }

    //Method to append data into Hex Array
    private void appendDataToHexArray(JLabel EGR_SUM) {
        int counter = 0;

        //Command to start flashing sequence
        data[counter++] = 0xAA;
        data[counter++] = 0x55;
        data[counter++] = 0x01;

        //DOI Data (512)
        for (int i = 1; i < DOI.length; i++) {
            data[counter++] = DOI[i];
        }

        //Checksum Digits
        data[counter++] = 0x05;
        data[counter++] = 0x06;
        data[counter++] = 0x07;
        data[counter++] = 0x11;

        //ERPM Data (33)
        for (int i = 1; i < ERPM.length; i++) {
            data[counter++] = ERPM[i];
        }

        //TPS Data (33)
        for (int i = 1; i < TPS.length; i++) {
            data[counter++] = TPS[i];
        }

        //EGR Data (256)
        for (int i = 1; i < EGR.length; i++) {
            data[counter++] = EGR[i];
        }

        //EGR SUM data
        data[counter++] = Integer.parseInt(EGR_SUM.getText()) >> 8;
        data[counter++] = Integer.parseInt(EGR_SUM.getText());

        //Temperature Array Data
        for (int i = 1; i < TempArray.length; i++) {
            data[counter++] = TempArray[i];
        }

        //Temperature ADC Data
        for (int i = 1; i < TempADC.length; i++) {
            data[counter++] = TempADC[i];
        }

        //Temperature CF Data
        for (int i = 1; i < TempCF.length; i++) {
            data[counter++] = TempCF[i];
        }

        //Pressure ADC
        for (int i = 1; i < PressureADC.length; i++) {
            data[counter++] = PressureADC[i];
        }

        //Pressure Voltage
        for (int i = 1; i < PressureVoltage.length; i++) {
            data[counter++] = PressureVoltage[i];
        }

        //Calibration ID
        for (int i = 1; i < cal_array_num.length; i++) {
            data[counter++] = cal_array_num[i];
        }

        //EGT ADC
        for (int i = 1; i < EGTADC.length; i++) {
            data[counter++] = EGTADC[i];
        }

        //EGT 1
        for (int i = 1; i < EGT1.length; i++) {
            data[counter++] = EGT1[i];
        }

        //EGT 2
        for (int i = 1; i < EGT2.length; i++) {
            data[counter++] = EGT2[i];
        }

        //Delta Pressure ADC
        for (int i = 1; i < DP_ADC.length; i++) {
            data[counter++] = DP_ADC[i];
        }

        //DP Voltage 
        for (int i = 1; i < DP_Voltage.length; i++) {
            data[counter++] = DP_Voltage[i];
        }

        //Engine Parameter
        data[counter++] = Engine_Speed >> 8;
        data[counter++] = Engine_Speed;

        data[counter++] = pumpON >> 8;
        data[counter++] = pumpON;

        data[counter++] = glowPlugON >> 8;
        data[counter++] = glowPlugON;

        data[counter++] = VSS_CF >> 8;
        data[counter++] = VSS_CF;

        data[counter++] = EOT_C >> 8;
        data[counter++] = EOT_C;

        data[counter++] = EOT_CF >> 8;
        data[counter++] = EOT_CF;

        data[counter++] = Delay_for_injection >> 8;
        data[counter++] = Delay_for_injection;

        data[counter++] = DOI_microsec >> 8;
        data[counter++] = DOI_microsec;

        data[counter++] = max_Acc >> 8;
        data[counter++] = max_Acc;

        data[counter++] = EGR_POT_ADC_Max >> 8;
        data[counter++] = EGR_POT_ADC_Max;

        data[counter++] = EGR_POT_ADC_Min >> 8;
        data[counter++] = EGR_POT_ADC_Min;

        data[counter++] = Consumption_Error_Max >> 8;
        data[counter++] = Consumption_Error_Max;

        data[counter++] = Consumption_Error_Min >> 8;
        data[counter++] = Consumption_Error_Min;

        data[counter++] = WIReset >> 8;
        data[counter++] = WIReset;

        data[counter++] = MinConsumption >> 8;
        data[counter++] = MinConsumption;

        data[counter++] = Inducement >> 8;
        data[counter++] = Inducement;

        data[counter++] = Fuel_cut_OFF >> 8;
        data[counter++] = Fuel_cut_OFF;

        data[counter++] = Para3 >> 8;
        data[counter++] = Para3;

        data[counter++] = Para4 >> 8;
        data[counter++] = Para4;

        data[counter++] = Para5 >> 8;
        data[counter++] = Para5;

        data[counter++] = Para6 >> 8;
        data[counter++] = Para6;

        data[counter++] = Para7 >> 8;
        data[counter++] = Para7;

        //DBounce Data
        for (int i = 0; i < DBounce_count - 1; i++) {                     //1123 + 32 = 1155
            data[counter++] = (D_Bounce_array[i] / 500);
        }

        System.out.println("DBounce count: " + DBounce_count);

        int minADC_Count = 0;
        for (int i = 0; i < DBounce_count - 1; i++) {                     //1123 + 32 = 1155
            data[counter++] = (D_Bounce_Min_ADC[minADC_Count++]);
            data[counter++] = (D_Bounce_Min_ADC[minADC_Count++]);
        }

        data[counter++] = (D_Bounce_Max_ADC[14]);
        data[counter++] = (D_Bounce_Max_ADC[15]);

        data[counter++] = (D_Bounce_Max_ADC[24]);
        data[counter++] = (D_Bounce_Max_ADC[25]);

        data[counter++] = (D_Bounce_Max_ADC[26]);
        data[counter++] = (D_Bounce_Max_ADC[27]);

        data[counter++] = (D_Bounce_Max_ADC[28]);
        data[counter++] = (D_Bounce_Max_ADC[29]);

        data[counter++] = (D_Bounce_Max_ADC[34]);
        data[counter++] = (D_Bounce_Max_ADC[35]);

        data[counter++] = (D_Bounce_Max_ADC[52]);
        data[counter++] = (D_Bounce_Max_ADC[53]);

        data[counter++] = (D_Bounce_Max_ADC[52]);
        data[counter++] = (D_Bounce_Max_ADC[53]);

        data[counter++] = (D_Bounce_Max_ADC[62]);
        data[counter++] = (D_Bounce_Max_ADC[63]);
        
        System.out.println("Counter: "+ counter);

    }

}
