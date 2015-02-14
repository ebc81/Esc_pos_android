package com.wordpress.ebc81.esc_pos_lib;

import java.nio.charset.Charset;

/**
 * EPSON ESC/POS Commands
 * Created by ebc on 05.01.2015.
 * http://content.epson.de/fileadmin/content/files/RSD/downloads/escpos.pdf
 */
public class ESC_POS_EPSON_ANDROID {
    private USBPort mPort;


    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte CR = 13;
    public static final byte FF = 12;
    public static final byte CAN = 24;
    /**
     * CodePage table
     */
    public static class CodePage {
        public static final byte PC437       = 0;
        public static final byte KATAKANA    = 1;
        public static final byte PC850       = 2;
        public static final byte PC860       = 3;
        public static final byte PC863       = 4;
        public static final byte PC865       = 5;
        public static final byte WPC1252     = 16;
        public static final byte PC866       = 17;
        public static final byte PC852       = 18;
        public static final byte PC858       = 19;
    }

    /**
     * BarCode table
     */
    public static class BarCode {
        public static final byte UPC_A       = 0;
        public static final byte UPC_E       = 1;
        public static final byte EAN13       = 2;
        public static final byte EAN8        = 3;
        public static final byte CODE39      = 4;
        public static final byte ITF         = 5;
        public static final byte NW7         = 6;
        //public static final byte CODE93      = 72;
        // public static final byte CODE128     = 73;
    }

    public ESC_POS_EPSON_ANDROID(USBPort _port )
    {
        mPort = _port;
    }


    /**
     * Print and line feed
     * LF
     * @return bytes for this command
     */
    public  byte[] print_linefeed()
    {
        byte[] result = new byte[1];
        result[0] = LF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode on, set at 1-dot width
     * ESC - n
     * @return bytes for this command
     */
    public  byte[] underline_1dot_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode on, set at 2-dot width
     * ESC - n
     * @return bytes for this command
     */
    public  byte[] underline_2dot_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn underline mode off
     * ESC - n
     * @return bytes for this command
     */
    public  byte[] underline_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }


    /**
     * Initialize printer
     * Clears the data in the print buffer and resets the printer modes to the modes that were
     * in effect when the power was turned on.
     * ESC @
     * @return bytes for this command
     */
    public  byte[] init_printer()
    {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn emphasized mode on
     * ESC E n
     * @return bytes for this command
     */
    public  byte[] emphasized_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Turn emphasized mode off
     * ESC E n
     * @return bytes for this command
     */
    public  byte[] emphasized_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double_strike_on
     * ESC G n
     * @return bytes for this command
     */
    public  byte[] double_strike_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double_strike_off
     * ESC G n
     * @return bytes for this command
     */
    public  byte[] double_strike_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 71;
        result[2] = 0xF;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font A
     * ESC M n
     * @return bytes for this command
     */
    public  byte[] select_fontA()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font B
     * ESC M n
     * @return bytes for this command
     */
    public  byte[] select_fontB()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select Font C ( some printers don't have font C )
     * ESC M n
     * @return bytes for this command
     */
    public  byte[] select_fontC()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 77;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double height width mode on Font A
     * ESC ! n
     * @return bytes for this command
     */
    public  byte[] double_height_width_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 56;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * double height width mode off Font A
     * ESC ! n
     * @return bytes for this command
     */
    public  byte[] double_height_width_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select double height mode Font A
     * ESC ! n
     * @return bytes for this command
     */
    public  byte[] double_height_on()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 16;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * disable double height mode, select Font A
     * ESC ! n
     * @return bytes for this command
     */
    public  byte[] double_height_off()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justification_left
     * ESC a n
     * @return bytes for this command
     */
    public  byte[] justification_left()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justification_center
     * ESC a n
     * @return bytes for this command
     */
    public  byte[] justification_center()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * justification_right
     * ESC a n
     * @return bytes for this command
     */
    public  byte[] justification_right()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Print and feed n lines
     * Prints the data in the print buffer and feeds n lines
     * ESC d n
     * @param n lines
     * @return bytes for this command
     */
    public byte[] print_and_feed_lines(byte n)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 100;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Print and reverse feed n lines
     * Prints the data in the print buffer and feeds n lines in the reserve direction
     * ESC e n
     * @param n lines
     * @return bytes for this command
     */
    public byte[] print_and_reverse_feed_lines(byte n)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 101;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Drawer Kick
     * Drawer kick-out connector pin 2
     * ESC p m t1 t2
     * @return bytes for this command
     */
    public byte[] drawer_kick()
    {
        byte[] result = new byte[5];
        result[0] = ESC;
        result[1] = 112;
        result[2] = 0;
        result[3] = 60;
        result[4] = 120;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select printing color1
     * ESC r n
     * @return bytes for this command
     */
    public byte[] select_color1()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select printing color2
     * ESC r n
     * @return bytes for this command
     */
    public byte[] select_color2()
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 114;
        result[2] = 1;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * Select character code table
     * ESC t n
     * @param cp example:CodePage.WPC1252
     * @return bytes for this command
     */
    public  byte[] select_code_tab(byte cp)
    {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 116;
        result[2] = cp;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * white printing mode on
     * Turn white/black reverse printing mode on
     * GS B n
     * @return bytes for this command
     */
    public  byte[] white_printing_on()
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = (byte)128;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * white printing mode off
     * Turn white/black reverse printing mode off
     * GS B n
     * @return bytes for this command
     */
    public  byte[] white_printing_off()
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 66;
        result[2] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * feed paper and cut
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a full cut ( cuts the paper completely )
     * @return bytes for this command
     */
    public  byte[] feedpapercut()
    {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * feed paper and cut partial
     * Feeds paper to ( cutting position + n x vertical motion unit )
     * and executes a partial cut ( one point left uncut )
     * @return bytes for this command
     */
    public  byte[] feedpapercut_partial()
    {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select bar code height
     * Select the height of the bar code as n dots
     * default dots = 162
     * @param dots ( heigth of the bar code )
     * @return bytes for this command
     */
    public  byte[] barcode_height(byte dots)
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 104;
        result[2] = dots;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select font hri
     * Selects a font for the Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     * @param n
     *           Font
     *           0, 48 Font A
     *           1, 49 Font B
     * @return bytes for this command
     */
    public  byte[] select_font_hri( byte n )
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 102;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * select position_hri
     * Selects the print position of Human Readable Interpretation (HRI) characters when printing a barcode, using n as follows:
     * @param n
     *           Print position
     *           0, 48 Not printed
     *           1, 49 Above the barcode
     *           2, 50 Below the barcode
     *           3, 51 Both above and below the barcode
     * @return bytes for this command
     */
    public  byte[] select_position_hri( byte n )
    {
        byte[] result = new byte[3];
        result[0] = GS;
        result[1] = 72;
        result[2] = n;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * print bar code
     * @param barcode_typ( Barcode.CODE39, Barcode.EAN8 ,...)
     * @param barcode2print
     * @return bytes for this command
     */
    public  byte[] print_bar_code(byte barcode_typ, String barcode2print )
    {
        byte[] barcodebytes = barcode2print.getBytes();
        byte[] result = new byte[3+barcodebytes.length+1];
        result[0] = GS;
        result[1] = 107;
        result[2] = barcode_typ;
        int idx = 3;

        for ( int i = 0; i < barcodebytes.length; i++ )
        {
            result[idx] = barcodebytes[i];
            idx++;
        }
        result[idx] = 0;
        mPort.AddData2Printer(result);

        return result;
    }


    /**
     * Set horizontal tab positions

     * @param col ( coulumn )
     * @return bytes for this command
     */
    public  byte[] set_HT_position( byte col )
    {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
        mPort.AddData2Printer(result);
        return result;
    }

    /**
     * print_line
     * adds a LF command to the text
     * @param line (text to print)
     */
    public void print_line( String line)
    {
        mPort.AddData2Printer(line.getBytes(Charset.forName("ISO-8859-1")));
        print_linefeed();
    }
    /**
     * print_text
     * without LF , means text is not printed immediately
     * @param line (text to print)
     */
    public void print_text(String line)
    {
        //mPort.AddData2Printer(line.getBytes());
        mPort.AddData2Printer(line.getBytes(Charset.forName("ISO-8859-1")));
    }


    public void print_sample1()
    {
        String test = null;
        init_printer();
        select_fontA();
        select_code_tab(ESC_POS_EPSON_ANDROID.CodePage.WPC1252);
        underline_1dot_on();
        justification_center();
        test = "Sample Receipt 1";
        print_line(test);
        test = "Umlaute";
        print_line(test);
        double_height_width_on();
        test = "ÄÖÜß";
        print_line(test);
        double_height_width_off();

        feedpapercut();
    }


    public void print_sample()
    {
        String test = null;

        init_printer();
        select_fontA();
        underline_1dot_on();
        justification_center();
        test = "Sample Receipt";
        print_line(test);
        underline_off();
        print_linefeed();
        justification_left();
        test = "Left justification";
        print_text(test);
        print_linefeed();
        justification_right();
        test = "right justification";
        print_line(test);
        print_linefeed();
        justification_left();
        test = "Testzeile\tTab1\tTab2";
        print_line(test);
        set_HT_position((byte)35); //Set horizontal tab positions: 35th column
        test = "Testzeile\tTab1";
        print_line(test);
        emphasized_on();
        test = "emphasized_on";
        print_line(test);
        emphasized_off();
        underline_2dot_on();
        justification_right();
        test = "underline 2dot";
        print_line(test);
        underline_off();
        double_strike_on();
        test = "double strike";
        print_line(test);
        double_strike_off();
        select_fontB();
        test = "Font B";
        print_line(test);
        white_printing_on();
        test = "white printing on";
        print_line(test);
        white_printing_off();
        print_and_feed_lines((byte)3);
        select_position_hri((byte)2);
        print_bar_code(BarCode.CODE39,"123456789");
        print_linefeed();

        print_and_feed_lines((byte)2);
        barcode_height((byte)80);
        justification_center();
        select_position_hri((byte)1);
        print_bar_code(BarCode.EAN13,"9783125171541");
        print_linefeed();
        feedpapercut();

    }
}
