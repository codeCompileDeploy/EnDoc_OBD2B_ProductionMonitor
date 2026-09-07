package com.Reset;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KHATRI
 */
public class NewClass {

    static class Row {

        String S_no;
        String Pcode;
        String DESCRIPTION;
        String Min_Adc;
        String Max_Adc;

        public Row(String S_no, String Pcode, String DESCRIPTION, String Min_Adc, String Max_Adc) {
            this.S_no = S_no;
            this.Pcode = Pcode;
            this.DESCRIPTION = DESCRIPTION;
            this.Min_Adc = Min_Adc;
            this.Max_Adc = Max_Adc;
        }
    }

    static List<Row> table = new ArrayList<>();

    // Function to add data to the table
    static void add(String S_no, String Pcode, String DESCRIPTION, String Min_Adc, String Max_Adc) {
        table.add(new Row(S_no, Pcode, DESCRIPTION, Min_Adc, Max_Adc));
    }

    public void ADDdata() {
        String[] S_no = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        String[] Pcode = {"P0689", "P0690", "P0120", "P0123", "P0335", "P0197", "P0195", "P0196", "PO199", "P0406", "P0405", "P0400", "P0404", "PO401", "P0402", "P204D", "P204A", "P204B", "P20E8", "P2048", "P2047", "P203F", "P20F5", "P20F6"};
        String[] DESCRIPTION = {"Battery Voltage Low", "Battery Voltage High", "TPS Open Circuit", "TPS Sensor for circuit High", "Crank sensor Open Circuit", "ET circuit Low", "ET Open Circuit", "EOT Sensor out of range", "EOT Sensor Performance Error", "EGR Circuit High", "EGR Circuit Low", "EGR Open Circuit", "EGR Out of range", "EGR Stuck flow insufficient", "EGR Stuck flow Excessive", "Reductant Pr Sensor Circuit High", "Reductant Pr Sensor open Circuit", "Reductant Pr Sensor out of range circuit range/performance", "Reductant Pr Sensor too Low", "Reductant Injector Circuit Low"};
        String[] Min_Adc = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        String[] Max_Adc = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

        // Add data to the table
        for (int i = 0; i < Pcode.length; i++) {
            add(S_no[i], Pcode[i], DESCRIPTION[i], Min_Adc[i], Max_Adc[i]);
        }
    }

    public void cal_path() {
        // Call ADDdata() function to retrieve data
        ADDdata();

        // Add data from ADDdata() function to the table
        for (int i = 0; i < table.size(); i++) {
            Row row = table.get(i);
            System.out.println("S_no: " + row.S_no + ", Pcode: " + row.Pcode + ", DESCRIPTION: " + row.DESCRIPTION + ", Min_Adc: " + row.Min_Adc + ", Max_Adc: " + row.Max_Adc);
        }
    }
}
