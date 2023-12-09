
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.awt.Color;
import java.sql.Connection;
//import java.sql.Date;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author HP
 */
public class HoltelMagReal extends javax.swing.JFrame {
    public int tongTienDV = 0;
    float tongThuePhong = 0;
    private static String IDNhanVien = "NV001";
    private static int languageTab=0;

    /**
     * Creates new form HoltelMagReal
     */
    public HoltelMagReal() {
        initComponents();
        dsHD();
    }
    
    public void dsHD() {
        // danh sach hoa don
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT * FROM hoadon";
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable10.getModel(); // get the table

            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                String IDHD = rs.getString(1);
                String ngayLap = rs.getString(2);
                String IDthue = rs.getString(3);
                String IDNV = rs.getString(4);
                String IDPhong = rs.getString(5);
                double tongTien = rs.getDouble(6);

                String[] row = {IDHD, ngayLap, IDthue, IDNV, IDPhong, Double.toString(tongTien)};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int TongTienDV(String IDDV, int soLuong) {
        int tong = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String select = "SELECT giaDV FROM dichvu WHERE IDdichVu = ";
            String ID = "'" + IDDV + "'"; // lay ID 
            String sql = select.concat(ID);
            System.out.println(sql);
            
            ResultSet rs = st.executeQuery(sql);
            
            rs.next();
            int gia = Integer.parseInt(rs.getString(1));
            tong = (int) gia*soLuong;
            System.out.println(tong);
             
            return gia*soLuong;
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return tong;
    }
    
    public String DVsudung() {
        ArrayList<String> tmp = new ArrayList<String>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String ID = "'" + jTextField38.getText() + "'"; // lay ID 
            String query = "SELECT tenDichVu, giaDV FROM sudungdichvu JOIN thuephong ON thuephong.IDthue = sudungdichvu.IDthue " +
                           "JOIN dichvu ON dichvu.IDdichVu = sudungdichvu.IDdichVu " +
                           "WHERE ngaySuDung >= ngayThue AND ngaySuDung <= ngayTra AND thuephong.IDKH = ";
            String full = query.concat(ID); // Ket noi ID vao sql
            System.out.println(full);
            ResultSet rs = st.executeQuery(full);
            
            while (rs.next()) {
                tmp.add(rs.getString(1));
                tongTienDV += rs.getInt(2);
            }
            String first = tmp.get(0);
            String fullDV = new String();
            
            for (int i=0; i<tmp.size(); i++) {
                if (tmp.get(i).equals(first)) {
                    fullDV = fullDV.concat(first);
                } else fullDV = fullDV.concat(", " + tmp.get(i));
            }
            System.out.println("Cac dich vu: " + fullDV);
            
            st.close();
            con.close();
            
            return fullDV;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    
    public String autoHD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String query = "SELECT IDHoaDon FROM hoadon ORDER BY IDHoaDon";
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            String lastID = rs.getString(1);
            // tim ID cuoi cung trong csdl
            
            String newIDnum, HD = "HD00", HD2 = "HD0", HD3 = "HD", newID="";
            
            int last = Integer.parseInt(lastID.substring(2, 5));
            last++;
            newIDnum = Integer.toString(last);
            System.out.println(newIDnum);
            
            switch (newIDnum.length()) {
                case 1:
                    newID = HD.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 2:
                    newID = HD2.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 3:
                    newID = HD3.concat(newIDnum);
                    System.out.println(newID);
                    break;
            }
                  
            st.close();
            con.close();
            
            return newID;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
    
    public void themTT(String IDHoaDon) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String test = "INSERT INTO thanhtoan(IDGiaoDich, ngayGD, IDHoaDon)" + "VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);
            
            Date currentDate = new Date();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

            ps.setString(1, autoHD());
            ps.setString(2, sd.format(currentDate));
            ps.setString(3, IDHoaDon); 
            ps.execute();
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            System.out.println(e);   
            
        }
    }
    
    public void themVisa(String IDGiaoDich) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String test = "INSERT INTO thevisa(IDGiaoDich, SoThe, SoCVV, HoTen, MaBuuDien)" + "VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);

            ps.setString(1, IDGiaoDich);
            ps.setString(2, jTextField49.getText());
            ps.setString(3, jTextField57.getText());
            ps.setString(4, jTextField50.getText().concat(" " + jTextField51.getText()));
            ps.setString(5, jTextField55.getText());
            ps.execute();
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            System.out.println(e);   
            
        }
    }
    
    public boolean isDate(JTextField txt) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        try {
            Date date = sdf.parse(txt.getText());
            return date != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int[] dsKM() {
        int[] t = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            ArrayList<Integer> tmp = new ArrayList<>(); // List dung de chua cac ty le 
            
            String sql = "SELECT tyLe FROM khuyenmai WHERE ngayKT >= CURRENT_DATE AND ngayBD <= CURRENT_DATE";
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                int TL = rs.getInt(1);
                tmp.add(TL);
            }
            int[] dsTL = tmp.stream().mapToInt(Integer::intValue).toArray();;
            
            return dsTL;
        } catch (Exception e) {
            System.out.println(e);
        }
        return t;
    }
    
    public int giaPhong(String IDPhong) {
        int gia = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String ID = "'" + IDPhong + "'"; // lay ID cua DV
            String query = "SELECT giaPhong FROM phong WHERE IDPhong = ";
            String SQL = query.concat(ID); // Ket noi ID vao sql
            System.out.println(SQL);
            
            ResultSet rs = st.executeQuery(SQL);
            rs.next();
            gia = rs.getInt(1);
            
            return gia;
        } catch (Exception e) {
            System.out.println(e);
        }
        return gia;
    }
    
    public double[] dsTL_decimal() {
        int[] listTL = dsKM();
        double[] dsTL = new double[listTL.length];
        
        for (int i=0; i<listTL.length; i++) {
            double tmp = listTL[i] / 100.0;
            dsTL[i] = (float) 1 - tmp;
            System.out.print(dsTL[i]);
        }
        
        return dsTL;
    }
    
    public String autoThuePhong() {
        String id = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String query = "SELECT IDthue FROM thuephong ORDER BY IDthue";
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            String lastID = rs.getString(1);
            // tim ID cuoi cung trong csdl
            
            String newIDnum, TP = "TP00", TP2 = "NV0", TP3 = "TP", newID="";
            
            int last = Integer.parseInt(lastID.substring(2, 5));
            last++;
            newIDnum = Integer.toString(last);
            System.out.println(newIDnum);

            switch (newIDnum.length()) {
                case 1:
                    newID = TP.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 2:
                    newID = TP2.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 3:
                    newID = TP3.concat(newIDnum);
                    System.out.println(newID);
                    break;
            }
             
            st.close();
            con.close();
            
            return newID;
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }
    
    public void thongTinPhong() {
        // thong tin phong trong trang chu
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String IDthue = "'" + jTextField1.getText() + "';";
            String query = "SELECT * FROM thuephong JOIN Phong p ON p.IDPhong = thuephong.IDPhong WHERE IDthue = ";
            String full = query.concat(IDthue);
            System.out.println(full);
            
            ResultSet rs = st.executeQuery(full);
            
            rs.next();
            jTextField7.setText(rs.getString(3)); // ma phong
            jTextField8.setText(rs.getString(8)); // trang thai phong
            jTextField9.setText(rs.getString(4)); // ngay dat
            jTextField10.setText(rs.getString(5)); // ngay tra
            jTextField11.setText(rs.getString(12)); // loai phong
            jTextField12.setText(rs.getString(9)); // gia phong
            jTextField13.setText(rs.getString(10)); // tang
            jTextField14.setText(rs.getString(11)); // khu vuc
            
            
            st.close();
            con.close();
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    public void thongTinKH() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String IDthue = "'" + jTextField1.getText() + "'";
            String query = "SELECT * FROM thuephong JOIN khachhang ON khachhang.IDKH = thuephong.IDKH WHERE IDthue = ";
            String full = query.concat(IDthue);
            System.out.println(full);
            
            ResultSet rs = st.executeQuery(full);
            
            rs.next();
            
            if (rs.getString(11).equals("1")) {
                jTextField21.setText("Nam");
            } else jTextField21.setText("Nữ");
            jTextField15.setText(rs.getString(2)); // ma khach
            jTextField16.setText(rs.getString(7)); // ten khach
            jTextField17.setText(rs.getString(9)); // email
            jTextField19.setText(rs.getString(10)); // so dien thoai
            jTextField20.setText(rs.getString(8)); // CMND
            
            st.close();
            con.close();
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        MainTabbedPane = new javax.swing.JTabbedPane();
        Main = new javax.swing.JPanel();
        MainTabblePane = new javax.swing.JTabbedPane();
        TrangChu = new javax.swing.JPanel();
        ThongTinPhong_TK = new javax.swing.JPanel();
        ThongTinPhongTK_Info = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        kGradientPanel18 = new keeptoo.KGradientPanel();
        jLabel8 = new javax.swing.JLabel();
        ThongTinKhach_TK = new javax.swing.JPanel();
        ThongTInKhach_Info = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        kGradientPanel19 = new keeptoo.KGradientPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        kGradientPanel20 = new keeptoo.KGradientPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        kGradientPanel25 = new keeptoo.KGradientPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel45 = new javax.swing.JLabel();
        jButton40 = new javax.swing.JButton();
        kGradientPanel21 = new keeptoo.KGradientPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jTextField26 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        khachdat = new javax.swing.JTextField();
        kGradientPanel22 = new keeptoo.KGradientPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jTextField33 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jButton35 = new javax.swing.JButton();
        jSpinner2 = new javax.swing.JSpinner();
        jButton36 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        kGradientPanel23 = new keeptoo.KGradientPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        ThongTinPhong_TK1 = new javax.swing.JPanel();
        ThongTinPhongTK_Info1 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jTextField47 = new javax.swing.JTextField();
        jButton46 = new javax.swing.JButton();
        kGradientPanel26 = new keeptoo.KGradientPanel();
        jLabel74 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton43 = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jTextField50 = new javax.swing.JTextField();
        jTextField51 = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jTextField52 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jTextField53 = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField54 = new javax.swing.JTextField();
        jLabel109 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel110 = new javax.swing.JLabel();
        jTextField55 = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jTextField57 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        kGradientPanel34 = new keeptoo.KGradientPanel();
        jLabel91 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        kGradientPanel30 = new keeptoo.KGradientPanel();
        jLabel81 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        kGradientPanel28 = new keeptoo.KGradientPanel();
        jLabel80 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        kGradientPanel29 = new keeptoo.KGradientPanel();
        jLabel83 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        StaffManagement = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jPanel78 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        kGradientPanel13 = new keeptoo.KGradientPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel79 = new javax.swing.JPanel();
        jPanel80 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        kGradientPanel14 = new keeptoo.KGradientPanel();
        jLabel17 = new javax.swing.JLabel();
        jPanel81 = new javax.swing.JPanel();
        jPanel82 = new javax.swing.JPanel();
        jPanel83 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        kGradientPanel15 = new keeptoo.KGradientPanel();
        jLabel18 = new javax.swing.JLabel();
        kGradientPanel16 = new keeptoo.KGradientPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jPanel71 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton22 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        kGradientPanel9 = new keeptoo.KGradientPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel72 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        kGradientPanel10 = new keeptoo.KGradientPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel74 = new javax.swing.JPanel();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        kGradientPanel11 = new keeptoo.KGradientPanel();
        jLabel12 = new javax.swing.JLabel();
        kGradientPanel12 = new keeptoo.KGradientPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        jPanel60 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        kGradientPanel6 = new keeptoo.KGradientPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        kGradientPanel7 = new keeptoo.KGradientPanel();
        jLabel14 = new javax.swing.JLabel();
        kGradientPanel8 = new keeptoo.KGradientPanel();
        jButton10 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel59 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel62 = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel16 = new javax.swing.JLabel();
        kGradientPanel4 = new keeptoo.KGradientPanel();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        kGradientPanel27 = new keeptoo.KGradientPanel();
        jLabel97 = new javax.swing.JLabel();
        jTextField58 = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jLabel99 = new javax.swing.JLabel();
        jTextField59 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jPanel84 = new javax.swing.JPanel();
        jPanel85 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        kGradientPanel31 = new keeptoo.KGradientPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        kGradientPanel24 = new keeptoo.KGradientPanel();
        jLabel58 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        kGradientPanel17 = new keeptoo.KGradientPanel();
        jLabel53 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 0));

        MainTabbedPane.setBackground(new java.awt.Color(255, 255, 255));
        MainTabbedPane.setPreferredSize(new java.awt.Dimension(1200, 635));
        MainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                MainTabbedPaneStateChanged(evt);
            }
        });

        Main.setBackground(new java.awt.Color(255, 255, 255));
        Main.setForeground(new java.awt.Color(0, 153, 0));

        MainTabblePane.setBackground(new java.awt.Color(255, 255, 255));
        MainTabblePane.setPreferredSize(new java.awt.Dimension(1200, 600));

        ThongTinPhong_TK.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ThongTinPhong_TK.setPreferredSize(new java.awt.Dimension(525, 283));

        ThongTinPhongTK_Info.setBackground(new java.awt.Color(255, 255, 255));
        ThongTinPhongTK_Info.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setText("Mã Phòng:");

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Tình Trạng");

        jLabel28.setBackground(new java.awt.Color(102, 102, 102));
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText("Ngày Đặt:");

        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setText("Ngày Trả");

        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        jLabel30.setForeground(new java.awt.Color(102, 102, 102));
        jLabel30.setText("Loại Phòng");

        jLabel31.setForeground(new java.awt.Color(102, 102, 102));
        jLabel31.setText("Giá Tiền");

        jLabel32.setForeground(new java.awt.Color(102, 102, 102));
        jLabel32.setText("Tầng");

        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setText("Khu Vực");

        javax.swing.GroupLayout ThongTinPhongTK_InfoLayout = new javax.swing.GroupLayout(ThongTinPhongTK_Info);
        ThongTinPhongTK_Info.setLayout(ThongTinPhongTK_InfoLayout);
        ThongTinPhongTK_InfoLayout.setHorizontalGroup(
            ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhongTK_InfoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jTextField12)
                    .addComponent(jLabel32)
                    .addComponent(jTextField13)
                    .addComponent(jLabel33)
                    .addComponent(jTextField14))
                .addGap(30, 30, 30))
        );
        ThongTinPhongTK_InfoLayout.setVerticalGroup(
            ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhongTK_InfoLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel30))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7)
                    .addComponent(jTextField11))
                .addGap(4, 4, 4)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel31))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel32))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel33))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49))
        );

        kGradientPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel18.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel18.setkGradientFocus(0);
        kGradientPanel18.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Thông Tin Phòng");

        javax.swing.GroupLayout kGradientPanel18Layout = new javax.swing.GroupLayout(kGradientPanel18);
        kGradientPanel18.setLayout(kGradientPanel18Layout);
        kGradientPanel18Layout.setHorizontalGroup(
            kGradientPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel18Layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel18Layout.setVerticalGroup(
            kGradientPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel18Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout ThongTinPhong_TKLayout = new javax.swing.GroupLayout(ThongTinPhong_TK);
        ThongTinPhong_TK.setLayout(ThongTinPhong_TKLayout);
        ThongTinPhong_TKLayout.setHorizontalGroup(
            ThongTinPhong_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ThongTinPhongTK_Info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ThongTinPhong_TKLayout.setVerticalGroup(
            ThongTinPhong_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhong_TKLayout.createSequentialGroup()
                .addComponent(kGradientPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ThongTinPhongTK_Info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        ThongTinKhach_TK.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ThongTinKhach_TK.setPreferredSize(new java.awt.Dimension(525, 283));

        ThongTInKhach_Info.setBackground(new java.awt.Color(255, 255, 255));
        ThongTInKhach_Info.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setForeground(new java.awt.Color(102, 102, 102));
        jLabel34.setText("Mã Khách:");

        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jLabel35.setBackground(new java.awt.Color(255, 255, 255));
        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setText("Tên Khách");

        jLabel36.setBackground(new java.awt.Color(102, 102, 102));
        jLabel36.setForeground(new java.awt.Color(102, 102, 102));
        jLabel36.setText("Email Khách:");

        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setText("Số điện thoại Khách");

        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Căn Cước Công Dân");

        jLabel40.setForeground(new java.awt.Color(102, 102, 102));
        jLabel40.setText("Giới Tinh");

        javax.swing.GroupLayout ThongTInKhach_InfoLayout = new javax.swing.GroupLayout(ThongTInKhach_Info);
        ThongTInKhach_Info.setLayout(ThongTInKhach_InfoLayout);
        ThongTInKhach_InfoLayout.setHorizontalGroup(
            ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTInKhach_InfoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(jTextField20)
                    .addComponent(jLabel40)
                    .addComponent(jTextField21))
                .addGap(30, 30, 30))
        );
        ThongTInKhach_InfoLayout.setVerticalGroup(
            ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTInKhach_InfoLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel38))
                .addGap(2, 2, 2)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField15)
                    .addComponent(jTextField19))
                .addGap(4, 4, 4)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel39))
                .addGap(2, 2, 2)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel40))
                .addGap(2, 2, 2)
                .addGroup(ThongTInKhach_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(102, 102, 102))
        );

        kGradientPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel19.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel19.setkGradientFocus(0);
        kGradientPanel19.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Thông Tin khách Thuê");

        javax.swing.GroupLayout kGradientPanel19Layout = new javax.swing.GroupLayout(kGradientPanel19);
        kGradientPanel19.setLayout(kGradientPanel19Layout);
        kGradientPanel19Layout.setHorizontalGroup(
            kGradientPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel19Layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel19Layout.setVerticalGroup(
            kGradientPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel19Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ThongTinKhach_TKLayout = new javax.swing.GroupLayout(ThongTinKhach_TK);
        ThongTinKhach_TK.setLayout(ThongTinKhach_TKLayout);
        ThongTinKhach_TKLayout.setHorizontalGroup(
            ThongTinKhach_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ThongTInKhach_Info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ThongTinKhach_TKLayout.setVerticalGroup(
            ThongTinKhach_TKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ThongTinKhach_TKLayout.createSequentialGroup()
                .addComponent(kGradientPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ThongTInKhach_Info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane14.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane14.setForeground(new java.awt.Color(255, 255, 255));

        jTable11.setForeground(new java.awt.Color(0, 0, 0));
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Dịch Vụ", "Tên Dịch Vụ", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane14.setViewportView(jTable11);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
        );

        kGradientPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel20.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel20.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel20.setkGradientFocus(0);
        kGradientPanel20.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Thông Tin Các Dịch Vụ Phòng Sử Dụng");

        javax.swing.GroupLayout kGradientPanel20Layout = new javax.swing.GroupLayout(kGradientPanel20);
        kGradientPanel20.setLayout(kGradientPanel20Layout);
        kGradientPanel20Layout.setHorizontalGroup(
            kGradientPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel20Layout.createSequentialGroup()
                .addGap(400, 400, 400)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel20Layout.setVerticalGroup(
            kGradientPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel20Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(kGradientPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kGradientPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel25.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel25.setkGradientFocus(0);
        kGradientPanel25.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quản Lý Khách Sạn");

        javax.swing.GroupLayout kGradientPanel25Layout = new javax.swing.GroupLayout(kGradientPanel25);
        kGradientPanel25.setLayout(kGradientPanel25Layout);
        kGradientPanel25Layout.setHorizontalGroup(
            kGradientPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel25Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        kGradientPanel25Layout.setVerticalGroup(
            kGradientPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel25Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setBackground(new java.awt.Color(245, 245, 245));
        jTextField1.setText("Mã Thuê Phòng:");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Tìm Kiếm Thuê Phòng:");

        jButton19.setText("Tìm Kiếm");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jTextField4.setBackground(new java.awt.Color(245, 245, 245));
        jTextField4.setText("Ko xài");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField4))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton19)
                .addContainerGap(370, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(kGradientPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout TrangChuLayout = new javax.swing.GroupLayout(TrangChu);
        TrangChu.setLayout(TrangChuLayout);
        TrangChuLayout.setHorizontalGroup(
            TrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrangChuLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(TrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TrangChuLayout.createSequentialGroup()
                        .addComponent(ThongTinPhong_TK, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(ThongTinKhach_TK, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        TrangChuLayout.setVerticalGroup(
            TrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TrangChuLayout.createSequentialGroup()
                .addGroup(TrangChuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ThongTinPhong_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(ThongTinKhach_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 290, Short.MAX_VALUE))
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 565, Short.MAX_VALUE)
        );

        MainTabblePane.addTab("Thông Tin Phòng", TrangChu);

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setPreferredSize(new java.awt.Dimension(400, 565));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Mã Khách *");

        jLabel41.setForeground(new java.awt.Color(102, 102, 102));
        jLabel41.setText("Họ Tên Khách *");

        jLabel42.setForeground(new java.awt.Color(102, 102, 102));
        jLabel42.setText("Căn Cước Công Dân");

        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jLabel43.setForeground(new java.awt.Color(102, 102, 102));
        jLabel43.setText("Email");

        jLabel44.setForeground(new java.awt.Color(102, 102, 102));
        jLabel44.setText("Số Điện Thoại Khách *");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Nam");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Nữ");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(61, 192, 96));
        jButton28.setForeground(new java.awt.Color(255, 255, 255));
        jButton28.setText("Clear");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(61, 192, 96));
        jButton29.setForeground(new java.awt.Color(255, 255, 255));
        jButton29.setText("Thêm");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel49.setForeground(new java.awt.Color(102, 102, 102));
        jLabel49.setText("Số Khách Dẫn Theo*");

        jLabel45.setForeground(new java.awt.Color(102, 102, 102));
        jLabel45.setText("Giới Tính Khách Hàng");

        jButton40.setBackground(new java.awt.Color(61, 192, 96));
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setText("Auto");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField25, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel29Layout.createSequentialGroup()
                            .addComponent(jButton28)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton29))
                        .addComponent(jSpinner1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                        .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField24, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel29Layout.createSequentialGroup()
                            .addComponent(jRadioButton1)
                            .addGap(34, 34, 34)
                            .addComponent(jRadioButton2))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel29Layout.createSequentialGroup()
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))))
                .addGap(79, 79, 79))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel37)
                .addGap(2, 2, 2)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton40))
                .addGap(8, 8, 8)
                .addComponent(jLabel41)
                .addGap(2, 2, 2)
                .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel42)
                .addGap(2, 2, 2)
                .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel43)
                .addGap(2, 2, 2)
                .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel44)
                .addGap(2, 2, 2)
                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel49)
                .addGap(2, 2, 2)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel45)
                .addGap(2, 2, 2)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton28)
                    .addComponent(jButton29))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSpinner1.setValue(1);

        kGradientPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel21.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel21.setkGradientFocus(0);
        kGradientPanel21.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Thêm Khách");

        javax.swing.GroupLayout kGradientPanel21Layout = new javax.swing.GroupLayout(kGradientPanel21);
        kGradientPanel21.setLayout(kGradientPanel21Layout);
        kGradientPanel21Layout.setHorizontalGroup(
            kGradientPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        kGradientPanel21Layout.setVerticalGroup(
            kGradientPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel21Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel19)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(kGradientPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setPreferredSize(new java.awt.Dimension(400, 565));

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField26ActionPerformed(evt);
            }
        });

        jLabel46.setForeground(new java.awt.Color(102, 102, 102));
        jLabel46.setText("Mã Phòng *");

        jLabel47.setForeground(new java.awt.Color(102, 102, 102));
        jLabel47.setText("Ngày Đặt *");

        jTextField27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField27ActionPerformed(evt);
            }
        });

        jLabel48.setForeground(new java.awt.Color(102, 102, 102));
        jLabel48.setText("Ngày Trả *");

        jTextField28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField28ActionPerformed(evt);
            }
        });

        jLabel50.setForeground(new java.awt.Color(102, 102, 102));
        jLabel50.setText("Khách đặt *");

        jLabel51.setForeground(new java.awt.Color(102, 102, 102));
        jLabel51.setText("Thành Tiền");

        jTextField29.setEditable(false);
        jTextField29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField29ActionPerformed(evt);
            }
        });

        jButton30.setBackground(new java.awt.Color(61, 192, 96));
        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.setText("Thuê");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton37.setBackground(new java.awt.Color(61, 192, 96));
        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.setText("Clear");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton38.setBackground(new java.awt.Color(61, 192, 96));
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.setText("Chọn Phòng");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(jLabel48))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField28)
                            .addComponent(jTextField27)
                            .addComponent(jTextField29)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton38, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                                .addComponent(jButton37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton30))
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(khachdat))
                        .addGap(79, 79, 79))))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel46)
                .addGap(2, 2, 2)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton38))
                .addGap(8, 8, 8)
                .addComponent(jLabel47)
                .addGap(2, 2, 2)
                .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel48)
                .addGap(2, 2, 2)
                .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(khachdat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel51)
                .addGap(2, 2, 2)
                .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton30)
                    .addComponent(jButton37))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        kGradientPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel22.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel22.setkGradientFocus(0);
        kGradientPanel22.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Thuê Phòng");

        javax.swing.GroupLayout kGradientPanel22Layout = new javax.swing.GroupLayout(kGradientPanel22);
        kGradientPanel22.setLayout(kGradientPanel22Layout);
        kGradientPanel22Layout.setHorizontalGroup(
            kGradientPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        kGradientPanel22Layout.setVerticalGroup(
            kGradientPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel22Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel20)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(kGradientPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setPreferredSize(new java.awt.Dimension(400, 565));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField33ActionPerformed(evt);
            }
        });

        jLabel61.setForeground(new java.awt.Color(102, 102, 102));
        jLabel61.setText("ID Thuê Phòng");

        jLabel62.setForeground(new java.awt.Color(102, 102, 102));
        jLabel62.setText("Mã Dịch Vụ*");

        jLabel63.setForeground(new java.awt.Color(102, 102, 102));
        jLabel63.setText("Ngày Sử Dụng *");

        jTextField35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField35ActionPerformed(evt);
            }
        });

        jLabel64.setForeground(new java.awt.Color(102, 102, 102));
        jLabel64.setText("Số Lượng*");

        jLabel65.setForeground(new java.awt.Color(102, 102, 102));
        jLabel65.setText("Tổng Tiền");

        jTextField36.setEditable(false);

        jButton35.setBackground(new java.awt.Color(61, 192, 96));
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.setText("Xác Nhận");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(61, 192, 96));
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.setText("Clear");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton41.setText("Chọn Khách");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton42.setText("Chọn Dịch Vụ");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61)
                            .addComponent(jLabel62)
                            .addComponent(jLabel63))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSpinner2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField35, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField34, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField33, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(jTextField36, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel31Layout.createSequentialGroup()
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel65)
                                    .addComponent(jLabel64))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGap(0, 129, Short.MAX_VALUE)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton41)
                                    .addComponent(jButton42)))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jButton36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton35)))
                        .addGap(79, 79, 79))))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel61)
                .addGap(2, 2, 2)
                .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jButton41)
                .addGap(0, 0, 0)
                .addComponent(jLabel62)
                .addGap(2, 2, 2)
                .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jButton42)
                .addGap(0, 0, 0)
                .addComponent(jLabel63)
                .addGap(2, 2, 2)
                .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel64)
                .addGap(2, 2, 2)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65)
                .addGap(2, 2, 2)
                .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton35)
                    .addComponent(jButton36))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jSpinner2.setValue(1);

        kGradientPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel23.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel23.setkGradientFocus(0);
        kGradientPanel23.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Sử Dụng Dịch Vụ");

        javax.swing.GroupLayout kGradientPanel23Layout = new javax.swing.GroupLayout(kGradientPanel23);
        kGradientPanel23.setLayout(kGradientPanel23Layout);
        kGradientPanel23Layout.setHorizontalGroup(
            kGradientPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        kGradientPanel23Layout.setVerticalGroup(
            kGradientPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel23Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel21)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(kGradientPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        MainTabblePane.addTab("Thuê Phòng", jPanel1);

        ThongTinPhong_TK1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ThongTinPhong_TK1.setPreferredSize(new java.awt.Dimension(525, 283));

        ThongTinPhongTK_Info1.setBackground(new java.awt.Color(255, 255, 255));
        ThongTinPhongTK_Info1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel66.setForeground(new java.awt.Color(102, 102, 102));
        jLabel66.setText("ID Thuê Phòng*");

        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });

        jLabel67.setBackground(new java.awt.Color(255, 255, 255));
        jLabel67.setForeground(new java.awt.Color(102, 102, 102));
        jLabel67.setText("ID Khách Hàng");

        jLabel68.setBackground(new java.awt.Color(102, 102, 102));
        jLabel68.setForeground(new java.awt.Color(102, 102, 102));
        jLabel68.setText("Họ Tên Khách Hàng");

        jLabel69.setForeground(new java.awt.Color(102, 102, 102));
        jLabel69.setText("Số Điện Thoại");

        jTextField40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField40ActionPerformed(evt);
            }
        });

        jLabel70.setForeground(new java.awt.Color(102, 102, 102));
        jLabel70.setText("Email Khách Hàng");

        jLabel71.setForeground(new java.awt.Color(102, 102, 102));
        jLabel71.setText("Mã Phòng");

        jLabel72.setForeground(new java.awt.Color(102, 102, 102));
        jLabel72.setText("Số Người:");

        jLabel73.setForeground(new java.awt.Color(102, 102, 102));
        jLabel73.setText("Ngày Đặt");

        jLabel75.setForeground(new java.awt.Color(102, 102, 102));
        jLabel75.setText("Số Ngày Thuê:");

        jLabel76.setForeground(new java.awt.Color(102, 102, 102));
        jLabel76.setText("Dịch Vụ Sử Dụng:");

        jLabel77.setForeground(new java.awt.Color(102, 102, 102));
        jLabel77.setText("Tổng Tiền ( Đã Kèm Thuế VAT 10%):");

        jButton46.setText("Chọn");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ThongTinPhongTK_Info1Layout = new javax.swing.GroupLayout(ThongTinPhongTK_Info1);
        ThongTinPhongTK_Info1.setLayout(ThongTinPhongTK_Info1Layout);
        ThongTinPhongTK_Info1Layout.setHorizontalGroup(
            ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField47)
                    .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addGap(117, 117, 117))
                    .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                        .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel70)
                                .addComponent(jTextField40, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                .addComponent(jTextField38)
                                .addComponent(jLabel66)
                                .addComponent(jLabel67)
                                .addComponent(jLabel68)
                                .addComponent(jTextField39)
                                .addComponent(jLabel69)
                                .addComponent(jTextField41))
                            .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                                .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(56, 56, 56)
                        .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField42, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                            .addComponent(jTextField43)
                            .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel71)
                                    .addComponent(jLabel72)
                                    .addComponent(jLabel73)
                                    .addComponent(jLabel75)
                                    .addComponent(jLabel76))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jTextField44)
                            .addComponent(jTextField45)
                            .addComponent(jTextField46))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        ThongTinPhongTK_Info1Layout.setVerticalGroup(
            ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhongTK_Info1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jLabel71))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField37)
                    .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(jLabel72))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(jLabel73))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(jLabel75))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(jLabel76))
                .addGap(2, 2, 2)
                .addGroup(ThongTinPhongTK_Info1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel77)
                .addGap(2, 2, 2)
                .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );

        kGradientPanel26.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel26.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel26.setkGradientFocus(0);
        kGradientPanel26.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel74.setBackground(new java.awt.Color(255, 255, 255));
        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Thông Tin Phòng");

        javax.swing.GroupLayout kGradientPanel26Layout = new javax.swing.GroupLayout(kGradientPanel26);
        kGradientPanel26.setLayout(kGradientPanel26Layout);
        kGradientPanel26Layout.setHorizontalGroup(
            kGradientPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel26Layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(jLabel74)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel26Layout.setVerticalGroup(
            kGradientPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel26Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel74)
                .addGap(16, 16, 16))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel79.setText("Mã Giao Dịch");

        jLabel78.setText("Phương Thức Thanh Toán:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn Phương Thức Thanh Toán", "Thẻ Visa", "Tiền Mặt" }));

        jButton43.setText("Auto");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jLabel87.setText("Số Thẻ");

        jLabel88.setText("Tháng Hết Hạn");

        jLabel89.setText("Tên");

        jLabel90.setText("Họ");

        jLabel93.setText("Địa Chỉ Thanh Toán");

        jLabel94.setText("Địa Chỉ Thanh Toán (Dòng 2)");

        jLabel108.setText("Quốc Gia");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--", "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua & Deps", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Central African Rep", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Congo {Democratic Rep}", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland {Republic}", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea North", "Korea South", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar, {Burma}", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russian Federation", "Rwanda", "St Kitts & Nevis", "St Lucia", "Saint Vincent & the Grenadines", "Samoa", "San Marino", "Sao Tome & Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad & Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe" }));

        jLabel109.setText("Thành Phố");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        jComboBox6.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022", "2023" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jLabel110.setText("Mã Bưu Điện");

        jLabel111.setText("Số Điện Thoại");

        jButton44.setText("Thanh Toán");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setText("Clear");

        jLabel92.setText("CVV");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField53, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField52, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jTextField49, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel79)
                                            .addGroup(jPanel15Layout.createSequentialGroup()
                                                .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel87))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel89))
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel90)
                                        .addComponent(jTextField51))))
                            .addComponent(jLabel93)
                            .addComponent(jLabel94))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel78)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel110, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel88, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox5, 0, 1, Short.MAX_VALUE))
                                .addGap(0, 0, 0)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel92)))
                            .addComponent(jTextField55)
                            .addComponent(jLabel111)
                            .addComponent(jTextField56)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel109)
                                    .addComponent(jTextField54)))))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton44)))
                .addGap(34, 34, 34))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel79)
                    .addComponent(jLabel78))
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLabel88)
                    .addComponent(jLabel92))
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(jLabel90)
                    .addComponent(jLabel110))
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(jLabel111))
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(jLabel108)
                    .addComponent(jLabel109))
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton45)
                    .addComponent(jButton44))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ThongTinPhong_TK1Layout = new javax.swing.GroupLayout(ThongTinPhong_TK1);
        ThongTinPhong_TK1.setLayout(ThongTinPhong_TK1Layout);
        ThongTinPhong_TK1Layout.setHorizontalGroup(
            ThongTinPhong_TK1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(ThongTinPhong_TK1Layout.createSequentialGroup()
                .addComponent(ThongTinPhongTK_Info1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ThongTinPhong_TK1Layout.setVerticalGroup(
            ThongTinPhong_TK1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThongTinPhong_TK1Layout.createSequentialGroup()
                .addComponent(kGradientPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(ThongTinPhong_TK1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ThongTinPhongTK_Info1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kGradientPanel34.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel34.setkGradientFocus(0);
        kGradientPanel34.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("SỐ PHÒNG ĐƯỢC THUÊ");

        javax.swing.GroupLayout kGradientPanel34Layout = new javax.swing.GroupLayout(kGradientPanel34);
        kGradientPanel34.setLayout(kGradientPanel34Layout);
        kGradientPanel34Layout.setHorizontalGroup(
            kGradientPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel34Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel91)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        kGradientPanel34Layout.setVerticalGroup(
            kGradientPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel34Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel91)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setForeground(new java.awt.Color(255, 255, 255));

        jLabel124.setBackground(new java.awt.Color(102, 102, 102));
        jLabel124.setForeground(new java.awt.Color(102, 102, 102));
        jLabel124.setText("Trong Hôm Nay");

        jLabel125.setBackground(new java.awt.Color(51, 51, 51));
        jLabel125.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(51, 51, 51));
        jLabel125.setText("99");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jLabel124))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel125)))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel124)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel125)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(kGradientPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kGradientPanel30.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel30.setkGradientFocus(0);
        kGradientPanel30.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("DOANH SỐ");

        javax.swing.GroupLayout kGradientPanel30Layout = new javax.swing.GroupLayout(kGradientPanel30);
        kGradientPanel30.setLayout(kGradientPanel30Layout);
        kGradientPanel30Layout.setHorizontalGroup(
            kGradientPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel30Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel81)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel30Layout.setVerticalGroup(
            kGradientPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel30Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel81)
                .addGap(9, 9, 9))
        );

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jLabel82.setBackground(new java.awt.Color(102, 102, 102));
        jLabel82.setForeground(new java.awt.Color(102, 102, 102));
        jLabel82.setText("Trong Hôm Nay");

        jLabel84.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(51, 51, 51));
        jLabel84.setText("$9999");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addContainerGap(121, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel84)
                    .addComponent(jLabel82))
                .addGap(128, 128, 128))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel82)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel84)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(kGradientPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        kGradientPanel28.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel28.setkGradientFocus(0);
        kGradientPanel28.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("SỐ KHÁCH ĐÃ TIẾP");

        javax.swing.GroupLayout kGradientPanel28Layout = new javax.swing.GroupLayout(kGradientPanel28);
        kGradientPanel28.setLayout(kGradientPanel28Layout);
        kGradientPanel28Layout.setHorizontalGroup(
            kGradientPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel28Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel80)
                .addGap(63, 63, 63))
        );
        kGradientPanel28Layout.setVerticalGroup(
            kGradientPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel28Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel80)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));

        jLabel85.setBackground(new java.awt.Color(102, 102, 102));
        jLabel85.setForeground(new java.awt.Color(102, 102, 102));
        jLabel85.setText("Trong Hôm Nay");

        jLabel86.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(51, 51, 51));
        jLabel86.setText("99");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addGap(114, 114, 114))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                        .addComponent(jLabel86)
                        .addGap(137, 137, 137))))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel86)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(kGradientPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        kGradientPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel29.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel29.setkGradientFocus(0);
        kGradientPanel29.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("Quản Lý Khách Sạn");

        javax.swing.GroupLayout kGradientPanel29Layout = new javax.swing.GroupLayout(kGradientPanel29);
        kGradientPanel29.setLayout(kGradientPanel29Layout);
        kGradientPanel29Layout.setHorizontalGroup(
            kGradientPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel29Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel83)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        kGradientPanel29Layout.setVerticalGroup(
            kGradientPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel29Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel83)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 154, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(kGradientPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ThongTinPhong_TK1, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(ThongTinPhong_TK1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        MainTabblePane.addTab("Thanh Toán", jPanel2);

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addComponent(MainTabblePane, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addComponent(MainTabblePane, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        MainTabbedPane.addTab("Trang Chủ", Main);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1200, 600));

        jPanel77.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel77.setPreferredSize(new java.awt.Dimension(150, 565));

        jPanel78.setBackground(new java.awt.Color(255, 255, 255));
        jPanel78.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel78.setPreferredSize(new java.awt.Dimension(150, 515));

        jLabel6.setText("Tìm Kiếm Nhân Viên:");

        jTextField2.setBackground(new java.awt.Color(245, 245, 245));
        jTextField2.setText("Mã Nhân Viên");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButton20.setText("Tìm Kiếm");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel78Layout = new javax.swing.GroupLayout(jPanel78);
        jPanel78.setLayout(jPanel78Layout);
        jPanel78Layout.setHorizontalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel78Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel78Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel78Layout.setVerticalGroup(
            jPanel78Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel78Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6)
                .addGap(3, 3, 3)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20)
                .addContainerGap(398, Short.MAX_VALUE))
        );

        kGradientPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel13.setForeground(new java.awt.Color(102, 0, 204));
        kGradientPanel13.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel13.setkGradientFocus(0);
        kGradientPanel13.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel13.setPreferredSize(new java.awt.Dimension(0, 50));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Quản Lý Nhân Viên");

        javax.swing.GroupLayout kGradientPanel13Layout = new javax.swing.GroupLayout(kGradientPanel13);
        kGradientPanel13.setLayout(kGradientPanel13Layout);
        kGradientPanel13Layout.setHorizontalGroup(
            kGradientPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(17, 17, 17))
        );
        kGradientPanel13Layout.setVerticalGroup(
            kGradientPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel13Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel5)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel77Layout = new javax.swing.GroupLayout(jPanel77);
        jPanel77.setLayout(jPanel77Layout);
        jPanel77Layout.setHorizontalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel77Layout.createSequentialGroup()
                .addComponent(jPanel78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(kGradientPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel77Layout.setVerticalGroup(
            jPanel77Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel77Layout.createSequentialGroup()
                .addComponent(kGradientPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel78, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel79.setBackground(new java.awt.Color(255, 255, 255));
        jPanel79.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel79.setForeground(new java.awt.Color(0, 0, 0));
        jPanel79.setPreferredSize(new java.awt.Dimension(1050, 283));

        jPanel80.setBackground(new java.awt.Color(255, 255, 255));
        jPanel80.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel80.setForeground(new java.awt.Color(255, 255, 255));

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setForeground(new java.awt.Color(255, 255, 255));

        jTable3.setForeground(new java.awt.Color(0, 0, 0));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Nhân Viên", "Tên Nhân Viên", "Email", "Lương", "Ngày Sinh", "Giới Tính", "Chức Vụ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(3).setHeaderValue("Lương");
            jTable3.getColumnModel().getColumn(4).setResizable(false);
            jTable3.getColumnModel().getColumn(4).setHeaderValue("Ngày Sinh");
            jTable3.getColumnModel().getColumn(5).setResizable(false);
            jTable3.getColumnModel().getColumn(5).setHeaderValue("Giới Tính");
            jTable3.getColumnModel().getColumn(6).setHeaderValue("Chức Vụ");
        }

        javax.swing.GroupLayout jPanel80Layout = new javax.swing.GroupLayout(jPanel80);
        jPanel80.setLayout(jPanel80Layout);
        jPanel80Layout.setHorizontalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4)
        );
        jPanel80Layout.setVerticalGroup(
            jPanel80Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );

        kGradientPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel14.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel14.setkGradientFocus(0);
        kGradientPanel14.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel14.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Danh Sách Nhân Viên");

        javax.swing.GroupLayout kGradientPanel14Layout = new javax.swing.GroupLayout(kGradientPanel14);
        kGradientPanel14.setLayout(kGradientPanel14Layout);
        kGradientPanel14Layout.setHorizontalGroup(
            kGradientPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel14Layout.createSequentialGroup()
                .addGap(455, 455, 455)
                .addComponent(jLabel17)
                .addContainerGap(456, Short.MAX_VALUE))
        );
        kGradientPanel14Layout.setVerticalGroup(
            kGradientPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel14Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel17)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel79Layout = new javax.swing.GroupLayout(jPanel79);
        jPanel79.setLayout(jPanel79Layout);
        jPanel79Layout.setHorizontalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
        );
        jPanel79Layout.setVerticalGroup(
            jPanel79Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel79Layout.createSequentialGroup()
                .addComponent(kGradientPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel81.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel81.setName(""); // NOI18N

        jPanel82.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel82.setPreferredSize(new java.awt.Dimension(1050, 200));

        jPanel83.setBackground(new java.awt.Color(255, 255, 255));
        jPanel83.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Nhân Viên", "Tên Nhân Viên", "Email", "Lương", "Ngày Sinh", "Giới Tính", "Chức Vụ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setPreferredSize(new java.awt.Dimension(75, 48));
        jScrollPane1.setViewportView(jTable2);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel83Layout = new javax.swing.GroupLayout(jPanel83);
        jPanel83.setLayout(jPanel83Layout);
        jPanel83Layout.setHorizontalGroup(
            jPanel83Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane3)
        );
        jPanel83Layout.setVerticalGroup(
            jPanel83Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel83Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
        );

        kGradientPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel15.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel15.setkGradientFocus(0);
        kGradientPanel15.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Chi Tiết Nhân Viên");

        javax.swing.GroupLayout kGradientPanel15Layout = new javax.swing.GroupLayout(kGradientPanel15);
        kGradientPanel15.setLayout(kGradientPanel15Layout);
        kGradientPanel15Layout.setHorizontalGroup(
            kGradientPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel15Layout.createSequentialGroup()
                .addGap(463, 463, 463)
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel15Layout.setVerticalGroup(
            kGradientPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel15Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel18)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel82Layout = new javax.swing.GroupLayout(jPanel82);
        jPanel82.setLayout(jPanel82Layout);
        jPanel82Layout.setHorizontalGroup(
            jPanel82Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel82Layout.setVerticalGroup(
            jPanel82Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel82Layout.createSequentialGroup()
                .addComponent(kGradientPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kGradientPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel16.setkEndColor(new java.awt.Color(96, 186, 136));
        kGradientPanel16.setkGradientFocus(0);
        kGradientPanel16.setkStartColor(new java.awt.Color(68, 168, 110));

        jButton1.setText("Thêm Nhân Viên");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel16Layout = new javax.swing.GroupLayout(kGradientPanel16);
        kGradientPanel16.setLayout(kGradientPanel16Layout);
        kGradientPanel16Layout.setHorizontalGroup(
            kGradientPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel16Layout.setVerticalGroup(
            kGradientPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel16Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(kGradientPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel81Layout = new javax.swing.GroupLayout(jPanel81);
        jPanel81.setLayout(jPanel81Layout);
        jPanel81Layout.setHorizontalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel82, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
            .addComponent(kGradientPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel81Layout.setVerticalGroup(
            jPanel81Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel81Layout.createSequentialGroup()
                .addComponent(kGradientPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel82, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout StaffManagementLayout = new javax.swing.GroupLayout(StaffManagement);
        StaffManagement.setLayout(StaffManagementLayout);
        StaffManagementLayout.setHorizontalGroup(
            StaffManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StaffManagementLayout.createSequentialGroup()
                .addComponent(jPanel77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(StaffManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        StaffManagementLayout.setVerticalGroup(
            StaffManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StaffManagementLayout.createSequentialGroup()
                .addGroup(StaffManagementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(StaffManagementLayout.createSequentialGroup()
                        .addComponent(jPanel79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Nhân Sự", StaffManagement);

        jPanel70.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel70.setPreferredSize(new java.awt.Dimension(150, 565));

        jPanel71.setBackground(new java.awt.Color(255, 255, 255));
        jPanel71.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel71.setPreferredSize(new java.awt.Dimension(150, 515));

        jLabel22.setText("Tìm Kiếm Phòng:");

        jTextField3.setBackground(new java.awt.Color(245, 245, 245));
        jTextField3.setText("Mã Phòng");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton21.setText("Tìm Kiếm");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jLabel23.setText("Tìm Kiếm Phòng:");

        jComboBox1.setBackground(new java.awt.Color(245, 245, 245));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LP001 Giường đon", "LP002 Giường đôi", "LP003 Phòng thường", "LP004 Phòng Superior", "LP005 Phòng Deluxe", "LP006 Phòng Suite", "LP007 Phòng gia đình" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton22.setText("Tìm Kiếm");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton27.setText("Chọn Phòng");

        javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
        jPanel71.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addComponent(jLabel23)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel71Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel71Layout.createSequentialGroup()
                        .addGap(0, 36, Short.MAX_VALUE)
                        .addGroup(jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel71Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel22)
                .addGap(3, 3, 3)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton21)
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton22)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        kGradientPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel9.setForeground(new java.awt.Color(102, 0, 204));
        kGradientPanel9.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel9.setkGradientFocus(0);
        kGradientPanel9.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel9.setPreferredSize(new java.awt.Dimension(0, 50));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Quản Lý Phòng");

        javax.swing.GroupLayout kGradientPanel9Layout = new javax.swing.GroupLayout(kGradientPanel9);
        kGradientPanel9.setLayout(kGradientPanel9Layout);
        kGradientPanel9Layout.setHorizontalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(27, 27, 27))
        );
        kGradientPanel9Layout.setVerticalGroup(
            kGradientPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel70Layout = new javax.swing.GroupLayout(jPanel70);
        jPanel70.setLayout(jPanel70Layout);
        jPanel70Layout.setHorizontalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel70Layout.createSequentialGroup()
                .addComponent(jPanel71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(kGradientPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel70Layout.setVerticalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel70Layout.createSequentialGroup()
                .addComponent(kGradientPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel71, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel72.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel72.setPreferredSize(new java.awt.Dimension(1050, 283));

        jPanel73.setBackground(new java.awt.Color(255, 255, 255));
        jPanel73.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane5.setForeground(new java.awt.Color(255, 255, 255));

        jTable4.setForeground(new java.awt.Color(0, 0, 0));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Phòng", "Tên Phòng", "Trạng Thái Phòng", "Giá Phòng", "ID Tầng", "ID Khu Vực", "ID Loại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel73Layout = new javax.swing.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );

        kGradientPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel10.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel10.setkGradientFocus(0);
        kGradientPanel10.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel10.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Danh Sách Phòng");

        javax.swing.GroupLayout kGradientPanel10Layout = new javax.swing.GroupLayout(kGradientPanel10);
        kGradientPanel10.setLayout(kGradientPanel10Layout);
        kGradientPanel10Layout.setHorizontalGroup(
            kGradientPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel10Layout.createSequentialGroup()
                .addGap(467, 467, 467)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel10Layout.setVerticalGroup(
            kGradientPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel10Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel11)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addComponent(kGradientPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel74.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel74.setName(""); // NOI18N

        jPanel75.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel75.setPreferredSize(new java.awt.Dimension(1050, 200));

        jPanel76.setBackground(new java.awt.Color(255, 255, 255));
        jPanel76.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane2.setForeground(new java.awt.Color(255, 255, 255));

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Phòng", "Tên Phòng", "Trạng Thái Phòng", "Giá Phòng", "ID Tầng", "ID Khu Vực", "ID Loại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.setPreferredSize(new java.awt.Dimension(525, 48));
        jScrollPane2.setViewportView(jTable7);

        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jScrollPane8.setViewportView(jTextArea2);
        jTextArea2.setEditable(false);

        javax.swing.GroupLayout jPanel76Layout = new javax.swing.GroupLayout(jPanel76);
        jPanel76.setLayout(jPanel76Layout);
        jPanel76Layout.setHorizontalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jScrollPane8)
        );
        jPanel76Layout.setVerticalGroup(
            jPanel76Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel76Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        kGradientPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel11.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel11.setkGradientFocus(0);
        kGradientPanel11.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Chi Tiết Phòng");

        javax.swing.GroupLayout kGradientPanel11Layout = new javax.swing.GroupLayout(kGradientPanel11);
        kGradientPanel11.setLayout(kGradientPanel11Layout);
        kGradientPanel11Layout.setHorizontalGroup(
            kGradientPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(475, 475, 475))
        );
        kGradientPanel11Layout.setVerticalGroup(
            kGradientPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel11Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel75Layout = new javax.swing.GroupLayout(jPanel75);
        jPanel75.setLayout(jPanel75Layout);
        jPanel75Layout.setHorizontalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel75Layout.setVerticalGroup(
            jPanel75Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel75Layout.createSequentialGroup()
                .addComponent(kGradientPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel76, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kGradientPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel12.setkEndColor(new java.awt.Color(96, 186, 136));
        kGradientPanel12.setkGradientFocus(0);
        kGradientPanel12.setkStartColor(new java.awt.Color(68, 168, 110));

        jButton4.setText("Thêm Phòng");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Thêm Loại Phòng");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Thêm Tầng");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Thêm Khu Vực");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Clear");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Làm Mới");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setText("Danh Sách Tầng");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Danh Sách Khu Vực");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel12Layout = new javax.swing.GroupLayout(kGradientPanel12);
        kGradientPanel12.setLayout(kGradientPanel12Layout);
        kGradientPanel12Layout.setHorizontalGroup(
            kGradientPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11)
                .addGap(182, 182, 182))
        );
        kGradientPanel12Layout.setVerticalGroup(
            kGradientPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel12Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(kGradientPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8)
                    .addComponent(jButton9)
                    .addComponent(jButton11)
                    .addComponent(jButton12))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel74Layout = new javax.swing.GroupLayout(jPanel74);
        jPanel74.setLayout(jPanel74Layout);
        jPanel74Layout.setHorizontalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel75, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
            .addComponent(kGradientPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel74Layout.setVerticalGroup(
            jPanel74Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel74Layout.createSequentialGroup()
                .addComponent(kGradientPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel75, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jPanel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE)
                    .addComponent(jPanel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jPanel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Phòng", jPanel22);

        jPanel57.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel57.setPreferredSize(new java.awt.Dimension(150, 565));

        jPanel60.setBackground(new java.awt.Color(255, 255, 255));
        jPanel60.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel60.setPreferredSize(new java.awt.Dimension(150, 515));

        jLabel24.setText("Tìm Kiếm Dịch Vụ:");

        jTextField5.setBackground(new java.awt.Color(245, 245, 245));
        jTextField5.setText("Mã Dịch Vụ:");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton23.setText("Chọn Dịch Vụ");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton26.setText("Tìm Kiếm");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel60Layout = new javax.swing.GroupLayout(jPanel60);
        jPanel60.setLayout(jPanel60Layout);
        jPanel60Layout.setHorizontalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel60Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel60Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addGroup(jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel60Layout.setVerticalGroup(
            jPanel60Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel60Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel24)
                .addGap(3, 3, 3)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton26)
                .addContainerGap(370, Short.MAX_VALUE))
        );

        kGradientPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel5.setForeground(new java.awt.Color(102, 0, 204));
        kGradientPanel5.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel5.setkGradientFocus(0);
        kGradientPanel5.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel5.setPreferredSize(new java.awt.Dimension(0, 50));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Quản Lý Dịch Vụ");

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(23, 23, 23))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel57Layout = new javax.swing.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel57Layout.createSequentialGroup()
                .addComponent(jPanel60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(kGradientPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel57Layout.createSequentialGroup()
                .addComponent(kGradientPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel60, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel63.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel63.setPreferredSize(new java.awt.Dimension(1050, 283));

        jPanel65.setBackground(new java.awt.Color(255, 255, 255));
        jPanel65.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane6.setForeground(new java.awt.Color(255, 255, 255));

        jTable5.setForeground(new java.awt.Color(0, 0, 0));
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Dịch Vụ", "Tên Dịch Vụ", "Giá "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel65Layout = new javax.swing.GroupLayout(jPanel65);
        jPanel65.setLayout(jPanel65Layout);
        jPanel65Layout.setHorizontalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
        );
        jPanel65Layout.setVerticalGroup(
            jPanel65Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );

        kGradientPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel6.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel6.setkGradientFocus(0);
        kGradientPanel6.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel6.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Danh Sách Dịch Vụ");

        javax.swing.GroupLayout kGradientPanel6Layout = new javax.swing.GroupLayout(kGradientPanel6);
        kGradientPanel6.setLayout(kGradientPanel6Layout);
        kGradientPanel6Layout.setHorizontalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addGap(463, 463, 463)
                .addComponent(jLabel13)
                .addContainerGap(464, Short.MAX_VALUE))
        );
        kGradientPanel6Layout.setVerticalGroup(
            kGradientPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel6Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel13)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel63Layout = new javax.swing.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel63Layout.createSequentialGroup()
                .addComponent(kGradientPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel67.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel67.setName(""); // NOI18N

        jPanel68.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel68.setPreferredSize(new java.awt.Dimension(1050, 200));

        jPanel69.setBackground(new java.awt.Color(255, 255, 255));
        jPanel69.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane9.setForeground(new java.awt.Color(255, 255, 255));

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID Dịch Vụ", "Tên Dịch Vụ", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.setPreferredSize(new java.awt.Dimension(75, 48));
        jScrollPane9.setViewportView(jTable8);

        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextArea3.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(5);
        jScrollPane10.setViewportView(jTextArea3);

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
            .addComponent(jScrollPane10)
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel69Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
        );

        kGradientPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel7.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel7.setkGradientFocus(0);
        kGradientPanel7.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Chi Tiết Dịch Vụ");

        javax.swing.GroupLayout kGradientPanel7Layout = new javax.swing.GroupLayout(kGradientPanel7);
        kGradientPanel7.setLayout(kGradientPanel7Layout);
        kGradientPanel7Layout.setHorizontalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(471, 471, 471))
        );
        kGradientPanel7Layout.setVerticalGroup(
            kGradientPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel14)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
        jPanel68.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel68Layout.createSequentialGroup()
                .addComponent(kGradientPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kGradientPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel8.setkEndColor(new java.awt.Color(96, 186, 136));
        kGradientPanel8.setkGradientFocus(0);
        kGradientPanel8.setkStartColor(new java.awt.Color(68, 168, 110));

        jButton10.setText("Thêm Dịch Vụ");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton13.setText("Clear");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("Làm Mới");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel8Layout = new javax.swing.GroupLayout(kGradientPanel8);
        kGradientPanel8.setLayout(kGradientPanel8Layout);
        kGradientPanel8Layout.setHorizontalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel8Layout.setVerticalGroup(
            kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(kGradientPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton13)
                    .addComponent(jButton14))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
            .addComponent(kGradientPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel67Layout.createSequentialGroup()
                .addComponent(kGradientPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jPanel63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Dịch Vụ", jPanel23);

        jPanel56.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel56.setPreferredSize(new java.awt.Dimension(150, 565));

        jPanel58.setBackground(new java.awt.Color(255, 255, 255));
        jPanel58.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel58.setPreferredSize(new java.awt.Dimension(150, 515));

        jLabel25.setText("Tìm Kiếm Khuyến Mãi:");

        jTextField6.setBackground(new java.awt.Color(245, 245, 245));
        jTextField6.setText("KM001");
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton24.setText("Tìm Kiếm");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setText("Chọn Sự Kiện");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel58Layout = new javax.swing.GroupLayout(jPanel58);
        jPanel58.setLayout(jPanel58Layout);
        jPanel58Layout.setHorizontalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addGroup(jPanel58Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel58Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel58Layout.setVerticalGroup(
            jPanel58Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel58Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel25)
                .addGap(3, 3, 3)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton24)
                .addContainerGap(366, Short.MAX_VALUE))
        );

        kGradientPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel1.setForeground(new java.awt.Color(102, 0, 204));
        kGradientPanel1.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel1.setkGradientFocus(0);
        kGradientPanel1.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel1.setMinimumSize(new java.awt.Dimension(0, 0));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(0, 50));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Quản Lý Khuyến Mãi");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(12, 12, 12))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel56Layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel59.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel59.setPreferredSize(new java.awt.Dimension(1050, 283));

        jPanel61.setBackground(new java.awt.Color(255, 255, 255));
        jPanel61.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane7.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane7.setForeground(new java.awt.Color(255, 255, 255));

        jTable6.setForeground(new java.awt.Color(0, 0, 0));
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Khuyến Mãi", "Tên Khuyến Mãi", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Tỷ Lệ Giảm"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable6);
        if (jTable6.getColumnModel().getColumnCount() > 0) {
            jTable6.getColumnModel().getColumn(0).setResizable(false);
            jTable6.getColumnModel().getColumn(0).setHeaderValue("ID Khuyến Mãi");
            jTable6.getColumnModel().getColumn(1).setResizable(false);
            jTable6.getColumnModel().getColumn(1).setHeaderValue("Tên Khuyến Mãi");
            jTable6.getColumnModel().getColumn(2).setResizable(false);
            jTable6.getColumnModel().getColumn(2).setHeaderValue("Ngày Bắt Đầu");
            jTable6.getColumnModel().getColumn(3).setResizable(false);
            jTable6.getColumnModel().getColumn(3).setHeaderValue("Ngày Kết Thúc");
            jTable6.getColumnModel().getColumn(4).setHeaderValue("Tỷ Lệ Giảm");
        }

        javax.swing.GroupLayout jPanel61Layout = new javax.swing.GroupLayout(jPanel61);
        jPanel61.setLayout(jPanel61Layout);
        jPanel61Layout.setHorizontalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
        );
        jPanel61Layout.setVerticalGroup(
            jPanel61Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );

        kGradientPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel2.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel2.setkGradientFocus(0);
        kGradientPanel2.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel2.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Danh Sách Chương Trình Khuyến Mãi");

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(408, 408, 408)
                .addComponent(jLabel15)
                .addContainerGap(408, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel15)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel62.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel62.setName(""); // NOI18N

        jPanel64.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel64.setPreferredSize(new java.awt.Dimension(1050, 200));

        jPanel66.setBackground(new java.awt.Color(255, 255, 255));
        jPanel66.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane11.setForeground(new java.awt.Color(255, 255, 255));

        jTable9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã Khuyến Mãi", "Tên Khuyến Mãi", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Tỷ Lệ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.setPreferredSize(new java.awt.Dimension(375, 48));
        jScrollPane11.setViewportView(jTable9);

        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextArea4.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(5);
        jScrollPane12.setViewportView(jTextArea4);
        jTextArea4.setEditable(false);

        javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
        jPanel66.setLayout(jPanel66Layout);
        jPanel66Layout.setHorizontalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11)
            .addComponent(jScrollPane12)
        );
        jPanel66Layout.setVerticalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel66Layout.createSequentialGroup()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
        );

        kGradientPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel3.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel3.setkGradientFocus(0);
        kGradientPanel3.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Chi Tiết Chương Trình Khuyến Mãi");

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(416, 416, 416)
                .addComponent(jLabel16)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel16)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel64Layout = new javax.swing.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel64Layout.createSequentialGroup()
                .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kGradientPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel4.setkEndColor(new java.awt.Color(96, 186, 136));
        kGradientPanel4.setkGradientFocus(0);
        kGradientPanel4.setkStartColor(new java.awt.Color(68, 168, 110));

        jButton15.setText("Thêm Khuyến Mãi");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Clear");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Làm Mới");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("khuyến Mãi Còn Hiệu Lực");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel4Layout = new javax.swing.GroupLayout(kGradientPanel4);
        kGradientPanel4.setLayout(kGradientPanel4Layout);
        kGradientPanel4Layout.setHorizontalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel4Layout.setVerticalGroup(
            kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(kGradientPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton16)
                    .addComponent(jButton17)
                    .addComponent(jButton18))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel62Layout = new javax.swing.GroupLayout(jPanel62);
        jPanel62.setLayout(jPanel62Layout);
        jPanel62Layout.setHorizontalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel64, javax.swing.GroupLayout.DEFAULT_SIZE, 1046, Short.MAX_VALUE)
            .addComponent(kGradientPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel62Layout.setVerticalGroup(
            jPanel62Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel62Layout.createSequentialGroup()
                .addComponent(kGradientPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel64, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jPanel59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quản Lý Khuyến Mãi", jPanel24);

        kGradientPanel27.setkEndColor(new java.awt.Color(204, 248, 216));
        kGradientPanel27.setkGradientFocus(0);
        kGradientPanel27.setkStartColor(new java.awt.Color(61, 192, 96));

        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("Nhập Mật Khẩu:");

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("TẠO TÀI KHOẢN!");

        jButton47.setText("Enter");

        jButton48.setText("Clear");

        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Nhập Tài Khoản:");

        jTextField59.setText("jTextField32");

        javax.swing.GroupLayout kGradientPanel27Layout = new javax.swing.GroupLayout(kGradientPanel27);
        kGradientPanel27.setLayout(kGradientPanel27Layout);
        kGradientPanel27Layout.setHorizontalGroup(
            kGradientPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel27Layout.createSequentialGroup()
                .addContainerGap(511, Short.MAX_VALUE)
                .addGroup(kGradientPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel98)
                        .addGap(485, 485, 485))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel27Layout.createSequentialGroup()
                        .addComponent(jButton48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton47)
                        .addGap(437, 437, 437))))
            .addGroup(kGradientPanel27Layout.createSequentialGroup()
                .addGap(464, 464, 464)
                .addGroup(kGradientPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel97)
                    .addComponent(jTextField58, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(jLabel99)
                    .addComponent(jTextField59))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel27Layout.setVerticalGroup(
            kGradientPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel27Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel98)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel99)
                .addGap(2, 2, 2)
                .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel97)
                .addGap(2, 2, 2)
                .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton48)
                    .addComponent(jButton47))
                .addContainerGap(146, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Tạo Tài Khoản", jPanel25);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        MainTabbedPane.addTab("Quản Trị", jPanel16);

        jPanel84.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel84.setPreferredSize(new java.awt.Dimension(1050, 283));

        jPanel85.setBackground(new java.awt.Color(255, 255, 255));
        jPanel85.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane13.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane13.setForeground(new java.awt.Color(255, 255, 255));

        jTable10.setForeground(new java.awt.Color(0, 0, 0));
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Hóa Đơn", "Ngày Lập", "ID Thuê Phòng", "ID Nhân Viên", "ID Phòng", "Tổng Tiền"
            }
        ));
        jScrollPane13.setViewportView(jTable10);

        javax.swing.GroupLayout jPanel85Layout = new javax.swing.GroupLayout(jPanel85);
        jPanel85.setLayout(jPanel85Layout);
        jPanel85Layout.setHorizontalGroup(
            jPanel85Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 1192, Short.MAX_VALUE)
        );
        jPanel85Layout.setVerticalGroup(
            jPanel85Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );

        kGradientPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        kGradientPanel31.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel31.setkGradientFocus(0);
        kGradientPanel31.setkStartColor(new java.awt.Color(35, 104, 66));
        kGradientPanel31.setPreferredSize(new java.awt.Dimension(40, 40));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("Danh Sách Hóa Đơn");

        javax.swing.GroupLayout kGradientPanel31Layout = new javax.swing.GroupLayout(kGradientPanel31);
        kGradientPanel31.setLayout(kGradientPanel31Layout);
        kGradientPanel31Layout.setHorizontalGroup(
            kGradientPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        kGradientPanel31Layout.setVerticalGroup(
            kGradientPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel31Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel52)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel84Layout = new javax.swing.GroupLayout(jPanel84);
        jPanel84.setLayout(jPanel84Layout);
        jPanel84Layout.setHorizontalGroup(
            jPanel84Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(kGradientPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, 1196, Short.MAX_VALUE)
        );
        jPanel84Layout.setVerticalGroup(
            jPanel84Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel84Layout.createSequentialGroup()
                .addComponent(kGradientPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel84, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel84, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
        );

        MainTabbedPane.addTab("Hóa Đơn", jPanel18);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        MainTabbedPane.addTab("Trợ Giúp", jPanel19);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        MainTabbedPane.addTab("Chuyển Đổi Ngôn Ngữ", jPanel20);

        kGradientPanel24.setkEndColor(new java.awt.Color(204, 248, 216));
        kGradientPanel24.setkGradientFocus(0);
        kGradientPanel24.setkStartColor(new java.awt.Color(61, 192, 96));

        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Nhập Mật Khẩu:");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("ĐỔI TÀI KHOẢN!");

        jButton33.setText("Enter");

        jButton34.setText("Clear");

        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Nhập Tài Khoản:");

        jTextField32.setText("jTextField32");

        javax.swing.GroupLayout kGradientPanel24Layout = new javax.swing.GroupLayout(kGradientPanel24);
        kGradientPanel24.setLayout(kGradientPanel24Layout);
        kGradientPanel24Layout.setHorizontalGroup(
            kGradientPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel24Layout.createSequentialGroup()
                .addContainerGap(512, Short.MAX_VALUE)
                .addGroup(kGradientPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(485, 485, 485))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel24Layout.createSequentialGroup()
                        .addComponent(jButton34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton33)
                        .addGap(437, 437, 437))))
            .addGroup(kGradientPanel24Layout.createSequentialGroup()
                .addGap(464, 464, 464)
                .addGroup(kGradientPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel58)
                    .addComponent(jTextField31, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(jLabel60)
                    .addComponent(jTextField32))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel24Layout.setVerticalGroup(
            kGradientPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel24Layout.createSequentialGroup()
                .addGap(261, 261, 261)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel60)
                .addGap(2, 2, 2)
                .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel58)
                .addGap(2, 2, 2)
                .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton34)
                    .addComponent(jButton33))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MainTabbedPane.addTab("Đổi Tài Khoản", jPanel21);

        kGradientPanel17.setkEndColor(new java.awt.Color(204, 248, 216));
        kGradientPanel17.setkGradientFocus(0);
        kGradientPanel17.setkStartColor(new java.awt.Color(61, 192, 96));

        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Nhập Mật Khẩu:");

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("KHÓA TÀI KHOẢN!");

        jButton31.setText("Enter");

        jButton32.setText("Clear");

        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("User: xxxxxx");

        javax.swing.GroupLayout kGradientPanel17Layout = new javax.swing.GroupLayout(kGradientPanel17);
        kGradientPanel17.setLayout(kGradientPanel17Layout);
        kGradientPanel17Layout.setHorizontalGroup(
            kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel17Layout.createSequentialGroup()
                .addGap(464, 464, 464)
                .addGroup(kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel17Layout.createSequentialGroup()
                        .addComponent(jButton32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton31))
                    .addGroup(kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel53)
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel17Layout.createSequentialGroup()
                .addContainerGap(490, Short.MAX_VALUE)
                .addGroup(kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addGap(485, 485, 485))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addGap(24, 24, 24))))
        );
        kGradientPanel17Layout.setVerticalGroup(
            kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel17Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel55)
                .addGap(217, 217, 217)
                .addComponent(jLabel54)
                .addGap(18, 18, 18)
                .addComponent(jLabel53)
                .addGap(2, 2, 2)
                .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kGradientPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton31)
                    .addComponent(jButton32))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MainTabbedPane.addTab("Khóa Phần Mềm", jPanel26);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(MainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // Tim kiem thong tin thue phong
        thongTinPhong();
        thongTinKH();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable11.getModel(); // get the table
            model.setRowCount(0);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String IDthue = "'" + jTextField1.getText() + "'"; // Lay ID thue phong
            String query = "SELECT DV.IDdichVu, tenDichVu, giaDV FROM dichvu DV " +
                           "JOIN sudungdichvu SD ON DV.IDdichVu = SD.IDdichVu " +
                           "JOIN thuephong TP ON TP.IDthue = SD.IDthue " +
                           "WHERE SD.IDthue = ";
            String full = query.concat(IDthue);
            System.out.println(full);
            ResultSet rs = st.executeQuery(full);
            
            while (rs.next()) {
                String IDDV = rs.getString(1);
                String tenDV = rs.getString(2);
                String gia = rs.getString(3);

                String[] row = {IDDV, tenDV, gia};
                model.addRow(row);
            }

            
            st.close();
            con.close();
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Nhân viên không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // Clear khach hang
        jTextField18.setText("");
        jTextField22.setText("");
        jTextField23.setText("");
        jTextField24.setText("");
        jTextField25.setText("");
        buttonGroup1.clearSelection();
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jTextField28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField28ActionPerformed

    private void jTextField35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField35ActionPerformed
  
    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // Xac nhan su dung dich vu
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String test = "INSERT INTO sudungdichvu(IDthue, IDdichVu, ngaySuDung, soLuong)" + "VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);

            int soLuong = Integer.parseInt(jSpinner2.getValue().toString());
            String IDDV = jTextField34.getText();
            int tong = TongTienDV(IDDV, soLuong);
            
            ps.setString(1, jTextField33.getText());
            ps.setString(2, jTextField34.getText());
            ps.setString(3, jTextField35.getText());
            ps.setString(4, jSpinner2.getValue().toString());
            ps.execute();
            
            jTextField36.setText(Integer.toString(tong));
            
            st.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Xác nhận thành công!");
        } catch (Exception e) {
            //System.out.println(e);   
//            if (e.getMessage().contains("Duplicate entry")) {
//                JOptionPane.showMessageDialog(null, "Mã khuyến mãi đã tồn tại");
//            } 
        }
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // Auto khach hang
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String query = "SELECT IDKH FROM khachhang ORDER BY IDKH";
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            String lastID = rs.getString(1);
            // tim ID cuoi cung trong csdl
            
            String newIDnum, KH = "KH00", KH2 = "KH0", KH3 = "KH", newID="";
            
            int last = Integer.parseInt(lastID.substring(2, 5));
            last++;
            newIDnum = Integer.toString(last);
            System.out.println(newIDnum);
            
            switch (newIDnum.length()) {
                case 1:
                    newID = KH.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 2:
                    newID = KH2.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 3:
                    newID = KH3.concat(newIDnum);
                    System.out.println(newID);
                    break;
            }
            
            jTextField18.setText(newID);
                  
            st.close();
            con.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // Enter ID thue phong va tu fill cai text field con lai
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String IDDV = "'" + jTextField37.getText() + "'"; // lay ID 
            String query = "SELECT * FROM thuePhong JOIN khachhang ON khachhang.IDKH = thuephong.IDKH WHERE IDthue = ";
            String full = query.concat(IDDV); // Ket noi ID vao sql
            System.out.println(full);
            ResultSet rs = st.executeQuery(full);
            
            rs.next();
            // ko lay column 1 vi no la ID thue phong
            String IDKH = rs.getString(2);
            String IDPhong = rs.getString(3);
            String ngayThue = rs.getString(4);
            String ngayTra = rs.getString(5);
            // ko lay column 6 vi no la IDKH trung voi column 2
            String tenKH = rs.getString(7);
            // ko lay column 6 vi no la CMND
            String emailKH = rs.getString(9);
            String sdt = rs.getString(10);
            // ko lay column 6 vi no la gioi tinh KH
            String soLuongKhach = rs.getString(12);
            
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Date Thue = sd.parse(ngayThue); // chuyen tu String sang Date
            Date Tra = sd.parse(ngayTra);
            
            long dayDiff = TimeUnit.MILLISECONDS.toDays(Tra.getTime() - Thue.getTime()); // tinh so ngay thue phong
            System.out.println(dayDiff);
            
            int gia = giaPhong(IDPhong); // tim gia phong bang id
            tongThuePhong = gia * dayDiff;
            double[] dsTL = dsTL_decimal(); // tra ve danh sach cai ty le km o dang 1 - 0.xx
            
            for (int i=0; i<dsTL.length; i++) {
                tongThuePhong *=  dsTL[i]; // tinh tong sau khuyen mai
            }
            
            double tongTatCa = (double) (tongThuePhong + tongTienDV) + (tongThuePhong + tongTienDV) * 0.1;
            System.out.println("Tong tat ca: " + tongTatCa);
            
            jTextField38.setText(IDKH);
            jTextField39.setText(tenKH);
            jTextField40.setText(sdt);
            jTextField41.setText(emailKH);
            jTextField42.setText(IDPhong);
            jTextField43.setText(soLuongKhach);
            jTextField44.setText(ngayThue);
            jTextField45.setText(Long.toString(dayDiff));
            jTextField46.setText(DVsudung());
            jTextField47.setText(Double.toString(tongTatCa));
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Dịch vụ không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
        
    }//GEN-LAST:event_jTextField37ActionPerformed
  
    private void jTextField40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField40ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        // Auto ma gia dich
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            String query = "SELECT IDGiaoDich FROM thanhtoan ORDER BY IDGiaoDich";
            ResultSet rs = st.executeQuery(query);
            
            rs.last();
            String lastID = rs.getString(1);
            // tim ID cuoi cung trong csdl
            
            String newIDnum, TT = "TT00", TT2 = "TT0", TT3 = "TT", newID="";
            
            int last = Integer.parseInt(lastID.substring(2, 5));
            last++;
            newIDnum = Integer.toString(last);
            System.out.println(newIDnum);
            
            switch (newIDnum.length()) {
                case 1:
                    newID = TT.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 2:
                    newID = TT2.concat(newIDnum);
                    System.out.println(newID);
                    break;
                case 3:
                    newID = TT3.concat(newIDnum);
                    System.out.println(newID);
                    break;
            }
            
            jTextField48.setText(newID);
                  
            st.close();
            con.close();
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    
    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        // Thanh toan
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String test = "INSERT INTO hoadon(IDHoaDon, ngayLap, IDthue, IDNV, IDPhong, tongTien)" + "VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);
            
            Date currentDate = new Date();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sd.format(currentDate);
            double tongTatCa = (double) (tongThuePhong + tongTienDV) + (tongThuePhong + tongTienDV) * 0.1;

            themTT(autoHD());
            themVisa(jTextField48.getText());
            
            ps.setString(1, autoHD());
            ps.setString(2, dateString);
            ps.setString(3, jTextField37.getText()); 
            ps.setString(4, IDNhanVien); // 
            ps.setString(5, jTextField42.getText());
            ps.setDouble(6, tongTatCa); // 
            ps.execute();
            
            st.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Thanh toán thành công!");
        } catch (Exception e) {
            System.out.println(e);   
            
        }
    }//GEN-LAST:event_jButton44ActionPerformed

    
    
    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
   
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // Tim kiem khuyen mai
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable9.getModel(); // get the table
            model.setRowCount(0); // clean the whole table
            DateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

            String ID = "'" + jTextField6.getText() + "'"; // lay ID cua DV
            String query = "SELECT * FROM khuyenmai WHERE maKM = ";
            String motaSQL = query.concat(ID); // Ket noi ID vao sql
            
            System.out.println(motaSQL);
            ResultSet rs = st.executeQuery(motaSQL);
                
            rs.next();
            String maKM = rs.getString(1);
            String tenKM = rs.getString(2);
            Date BD = rs.getDate(3);
            String ngayBD = sd.format(BD);
            Date KT = rs.getDate(4);
            String ngayKT = sd.format(KT);
            String TL = rs.getString(5);
            String mota = rs.getString(6);
            
            System.out.println(ngayBD);
            System.out.println(KT);

            String[] row = {maKM, tenKM, ngayBD, ngayKT, TL};
            model.addRow(row);

            if (mota.equals(""))
                jTextArea4.setText("Không có mô tả");
            else jTextArea4.setText(mota);
            
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Khuyến mãi không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // Tim kiem theo loai phong
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel(); // get the table
            model.setRowCount(0);

            
            String IDLoaiP = "'" + jComboBox1.getSelectedItem().toString().subSequence(0, 5) + "'"; // lay ID
            String query = "SELECT * FROM Phong " +
                           "JOIN loaiphong ON loaiphong.IDLoai = phong.IDLoai " +
                           "WHERE phong.IDLoai = ";
            String fullSQL = query.concat(IDLoaiP); // Ket noi ID phong vao sql
            System.out.println(fullSQL);
            ResultSet rs = st.executeQuery(fullSQL);
            
            while (rs.next()) {
                String ID = rs.getString(1);
                String tenPhong = rs.getString(2);
                String trangThaiPhong = rs.getString(3);
                String giaPhong = rs.getString(4);
                String IDTang = rs.getString(5);
                String IDKV = rs.getString(6);
                String IDLoai = rs.getString(7);

                String[] row = {ID, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKV, IDLoai};
                model.addRow(row);
            }
            
            
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // Tim kiem phong
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable7.getModel(); // get the table
            model.setRowCount(0);

            String IDPhong = "'" + jTextField3.getText() + "'"; // lay ID cua Phong
            String query = "SELECT * FROM Phong WHERE IDPhong = ";
            String motaSQL = query.concat(IDPhong); // Ket noi ID phong vao sql
            System.out.println(motaSQL);
            ResultSet rs = st.executeQuery(motaSQL);
            
            rs.next();
            String ID = rs.getString(1);
            String tenPhong = rs.getString(2);
            String trangThaiPhong = rs.getString(3);
            String giaPhong = rs.getString(4);
            String IDTang = rs.getString(5);
            String IDKV = rs.getString(6);
            String IDLoai = rs.getString(7);
            String mota = rs.getString(8);

            String[] row = {ID, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKV, IDLoai};
            model.addRow(row);
            
            
            if (mota.equals(null))
                jTextArea2.setText("Không có mô tả");
            else jTextArea2.setText(mota);
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Phòng không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Refresh Nhan Vien
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            //String query = "select * from NhanVien";
            String query = "SELECT IDNV, tenNV, emailNV, luongNV, ngaySinhNV, gioiTinhNV, chucvu.tenCV FROM nhanvien JOIN chucvu ON chucvu.maCV = nhanvien.maCV";
            // query khong xuong hang duoc
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel(); // get the table

            String IDNV, tenNV, emailNV, luongNV, gioiTinhNV, ngaySinhNV, tenCV;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                IDNV = rs.getString(1);
                tenNV = rs.getString(2);
                emailNV = rs.getString(3);
                luongNV = rs.getString(4);
                Date birthday = rs.getDate(5);
                ngaySinhNV = dateFormat.format(birthday); // chuyen Date thanh String
                gioiTinhNV = rs.getString(6);            
                tenCV = rs.getString(7);
                
                if (gioiTinhNV.equals("1")) {
                    gioiTinhNV = "Nam";
                } else if (gioiTinhNV.equals("0")) {
                    gioiTinhNV = "Nữ";
                }

                String[] row = {IDNV, tenNV, emailNV, luongNV, ngaySinhNV, gioiTinhNV, tenCV};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // Tim kiem mo ta IDNV
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel(); // get the table
            model.setRowCount(0);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String maNV = "'" + jTextField2.getText() + "';";
            String query = "SELECT * FROM nhanvien WHERE IDNV = ";
            String full = query.concat(maNV);
            System.out.println(full);
            ResultSet rs = st.executeQuery(full);
            DefaultTableModel table2 = (DefaultTableModel) jTable2.getModel();
            table2.setRowCount(0);
            
            rs.next();
            String IDNV = rs.getString(1);
            String tenNV = rs.getString(2);
            String emailNV = rs.getString(3);
            String luongNV = rs.getString(4);
            Date birthday = rs.getDate(5);
            String ngaySinhNV = dateFormat.format(birthday); // chuyen Date thanh String
            String gioiTinhNV = rs.getString(6);
            String tenCV = rs.getString(7);
            String mota = rs.getString(8);

            String[] row = {IDNV, tenNV, emailNV, luongNV, ngaySinhNV, gioiTinhNV, tenCV};
            model.addRow(row);
            
            
            if (mota.equals(""))
                jTextArea1.setText("Không có mô tả");
            else jTextArea1.setText(mota);
            
            st.close();
            con.close();
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Nhân viên không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Them Nhan Vien
        ThemNV them = new ThemNV();
        them.setTitle("Thêm nhân viên");
        them.setDefaultCloseOperation(them.DISPOSE_ON_CLOSE);
        them.setLocationRelativeTo(null);
        them.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Clear
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel(); // get the table
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel(); // get the table
        model2.setRowCount(0);
        jTextArea1.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // Lam moi Phong
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT IDPhong, tenPhong, tenTrangThai, giaPhong, IDTang, IDKhuVuc, IDLoai FROM phong " +
                           "JOIN trangthai ON trangthai.MaTrangThai = phong.MaTrangThai";
            // query khong xuong hang duoc
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel(); // get the table

            String IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKV, IDLoai;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                IDPhong = rs.getString(1);
                tenPhong = rs.getString(2);
                trangThaiPhong = rs.getString(3);
                giaPhong = rs.getString(4);
                IDTang = rs.getString(5);
                IDKV = rs.getString(6);
                IDLoai = rs.getString(7);

                String[] row = {IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKV, IDLoai};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Them Phong
        ThemPhong newPhong = new ThemPhong();
        newPhong.setDefaultCloseOperation(newPhong.DISPOSE_ON_CLOSE);
        newPhong.setLocationRelativeTo(null);
        newPhong.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Them Dich Vu
        ThemDichVu dv = new ThemDichVu();
        dv.setDefaultCloseOperation(dv.DISPOSE_ON_CLOSE);
        dv.setLocationRelativeTo(null);
        dv.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Them Khu Vuc
        ThemKhuVuc kv = new ThemKhuVuc();
        kv.setDefaultCloseOperation(kv.DISPOSE_ON_CLOSE);
        kv.setLocationRelativeTo(null);
        kv.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Them Khuyen Mai
        ThemKM km = new ThemKM();
        km.setDefaultCloseOperation(km.DISPOSE_ON_CLOSE);
        km.setLocationRelativeTo(null);
        km.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Them Loai Phong
        ThemLoaiPhong LP = new ThemLoaiPhong();
        LP.setDefaultCloseOperation(LP.DISPOSE_ON_CLOSE);
        LP.setLocationRelativeTo(null);
        LP.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Them Tang
        ThemTang t = new ThemTang();
        t.setDefaultCloseOperation(t.DISPOSE_ON_CLOSE);
        t.setLocationRelativeTo(null);
        t.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        // TODO add your handling code here:
        if (jTextField2.getText().equals("Mã Nhân Viên")) {
            jTextField2.setText("");
            jTextField2.setForeground(Color.black);
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        // TODO add your handling code here: Mã Nhân Viên
        if (jTextField2.getText().equals("")) {
            jTextField2.setText("Mã Nhân Viên");
            jTextField2.setForeground(Color.black);
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Clear
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel(); // get the table
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable7.getModel(); // get the table
        model2.setRowCount(0);
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        // TODO add your handling code here: Mã Phòng
        if (jTextField3.getText().equals("Mã Phòng")) {
            jTextField3.setText("");
            jTextField3.setForeground(Color.black);
        }
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        if (jTextField3.getText().equals("")) {
            jTextField3.setText("Mã Phòng");
            jTextField3.setForeground(Color.black);
        }
    }//GEN-LAST:event_jTextField3FocusLost

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // Clear
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel(); // get the table
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable8.getModel(); // get the table
        model2.setRowCount(0);
        jTextArea3.setText("");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // Refresh
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT IDdichVu, tenDichVu, giaDV FROM dichvu";
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable5.getModel(); // get the table

            String IDDV, tenDV, giaDV;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                IDDV = rs.getString(1);
                tenDV = rs.getString(2);
                giaDV = rs.getString(3);

                String[] row = {IDDV, tenDV, giaDV};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // Tim kiem dich vu
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            DefaultTableModel model = (DefaultTableModel) jTable8.getModel(); // get the table
            model.setRowCount(0);

            String IDDV = "'" + jTextField5.getText() + "'"; // lay ID cua DV
            String query = "SELECT * FROM dichvu WHERE IDdichVu = ";
            String full = query.concat(IDDV); // Ket noi ID vao sql
            System.out.println(full);
            ResultSet rs = st.executeQuery(full);
            
            rs.next();
            String id = rs.getString(1);
            String tenDV = rs.getString(2);
            String gia = rs.getString(3);
            String mota = rs.getString(4);
            
            String[] row = {id, tenDV, gia};
            model.addRow(row);
            
            if (mota.equals(""))
                jTextArea3.setText("Không có mô tả");
            else jTextArea3.setText(mota);
            
            st.close();
            con.close();
            
        } catch (Exception e) {
            if ("Illegal operation on empty result set.".equals(e.getMessage())) {
                // Kiem tra su ton tai cua nhan vien
                JOptionPane.showMessageDialog(null, "Dịch vụ không tồn tại!");
            }
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // Refresh Khuyen mai
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT maKM, tenKM, ngayBD, ngayKT, tyLe FROM khuyenmai";
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable6.getModel(); // get the table

            String maKM, tenKM, ngayBD, ngayKT, tyLe;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                maKM = rs.getString(1);
                tenKM = rs.getString(2);
                ngayBD = rs.getString(3);
                ngayKT = rs.getString(4);
                tyLe = rs.getString(5);

                String[] row = {maKM, tenKM, ngayBD, ngayKT, tyLe};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // Clear KM
        DefaultTableModel model = (DefaultTableModel) jTable6.getModel(); // get the table
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable9.getModel(); // get the table
        model2.setRowCount(0);
        jTextArea4.setText("");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sd.format(new Date()); // Lay string cua ngay hien tai
            Date d1 = sd.parse(currentDate);     // Chuyen sang Date de co the so sanh 
            System.out.println("test: " + currentDate);

            String query = "SELECT maKM, tenKM, ngayBD, ngayKT, tyLe FROM khuyenmai";
//            String dateKT = "SELECT ngayKT FROM khuyenmai";
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable6.getModel(); // get the table

            String maKM, tenKM, ngayBD, ngayKT, tyLe;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            while(rs.next()){
                maKM = rs.getString(1);
                tenKM = rs.getString(2);
                ngayBD = rs.getString(3);
                Date KT = rs.getDate(4);
                ngayKT = sd.format(KT);  
                tyLe = rs.getString(5);
 
                if (KT.after(d1)) {
                    String[] row = {maKM, tenKM, ngayBD, ngayKT, tyLe};
                    model.addRow(row);
                }

            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    public String checkGT() {
        String gtinh = "";
        if (jRadioButton1.isSelected())
            gtinh = "1";
        else if (jRadioButton2.isSelected())
            gtinh = "0";
        return gtinh;
    }
    
    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // Them khach hang
        if (!jTextField18.getText().substring(0, 2).equals(("KH"))) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đúng mã khách hàng");
            return;
        } else if (jTextField22.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Hãy nhập tên khách hàng");
            return;
        } else if (jTextField23.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đúng CMND");
            return;
        }else if (!jTextField24.getText().contains("@gmail.com")) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đúng email");
            return;
        } else if (jTextField25.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đúng số điện thoại");
            return;
        } else if (checkGT().equals("")) {
            JOptionPane.showMessageDialog(null, "Hãy chọn giới tính");
            return;
        }
        
        String gt = checkGT();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String test = "INSERT INTO khachhang(IDKH, tenKH, CMND, emailKH, SDT, gioiTinhKH, soLuongKhach)" + "VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);

            ps.setString(1, jTextField18.getText());
            ps.setString(2, jTextField22.getText());
            ps.setString(3, jTextField23.getText());
            ps.setString(4, jTextField24.getText());
            ps.setString(5, jTextField25.getText());
            ps.setString(6, gt);
            ps.setString(7, jSpinner1.getValue().toString());

            ps.execute();
            
            st.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công!");
        } catch (Exception e) {
            System.out.println(e);   
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "ID khách hàng đã tồn tại");
            } 
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jTextField33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField33ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // Clear su dung dich vu
        jTextField33.setText("");
        jTextField34.setText("");
        jTextField35.setText("");
        jTextField36.setText("");
        jSpinner2.setValue(1);
    }//GEN-LAST:event_jButton36ActionPerformed

    
    
    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // Thue Phong
        if (jTextField26.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Hãy nhập ID phòng!");
            return;
        } else if (!isDate(jTextField27) || !isDate(jTextField28)) {
            JOptionPane.showMessageDialog(null, "Hãy nhập đúng ngày!");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String test = "INSERT INTO thuephong(IDthue, IDKH, IDPhong, ngayThue,ngayTra)" + "VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(test);
            
            ps.setString(1, autoThuePhong());
            ps.setString(2, khachdat.getText()); // id khach hang
            ps.setString(3, jTextField26.getText()); // id phong
            ps.setString(4, jTextField27.getText()); // ngay thue
            ps.setString(5, jTextField28.getText()); // ngay tra
            ps.execute();
            
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Date ngayThue = sd.parse(jTextField27.getText()); // chuyen tu String sang Date
            Date ngayTra = sd.parse(jTextField28.getText());
            
            long dayDiff = TimeUnit.MILLISECONDS.toDays(ngayTra.getTime() - ngayThue.getTime()); // tinh so ngay thue phong
            System.out.println(dayDiff);
            
            int gia = giaPhong(jTextField26.getText()); // tim gia phong bang id
            tongThuePhong = gia * dayDiff;
            double[] dsTL = dsTL_decimal(); // tra ve danh sach cai ty le km o dang 1 - 0.xx
            
            for (int i=0; i<dsTL.length; i++) {
                tongThuePhong *=  dsTL[i]; // tinh tong sau khuyen mai
            }
            
            System.out.println("Tong: " + tongThuePhong);
            jTextField29.setText(Float.toString(tongThuePhong)); // tong tien thue phong qua giam gia
            
            st.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Xác nhận thành công!");
        } catch (Exception e) {
            System.out.println(e);   
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Phòng đang được thuê, hãy nhập lại thông tin!");
            } 
        }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26ActionPerformed

    private void jTextField29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField29ActionPerformed

    private void jTextField27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        // Clear thue phong
        jTextField26.setText("");
        jTextField27.setText("");
        jTextField28.setText("");
        jTextField29.setText("");
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // Danh sach khu vuc
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT IDKhuVuc, tenKV FROM khuvuc";
            // query khong xuong hang duoc
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel(); // get the table
            
            String IDKhuVuc, tenKV;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
             String[] colName = {"Mã Khu Vực", "Tên Khu Vực"};

            model.setColumnIdentifiers(colName);
//            System.err.println(col);
            while(rs.next()){
                //lay thong tin ve id khu vuc va ten khu vuc
                IDKhuVuc = rs.getString(1);
                tenKV = rs.getString(2);           

                String[] row = {IDKhuVuc, tenKV};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // Danh sach khu vuc
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();

            String query = "SELECT IDTang, tenTang, IDKhuVuc FROM tang";
            // query khong xuong hang duoc
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel(); // get the table
            
            String IDTang, tenTang, IDKV;
            
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
             String[] colName = {"Mã Tầng", "Tên Tầng", "Mã Khu Vực"};

            model.setColumnIdentifiers(colName);
//            System.err.println(col);
            while(rs.next()){
                IDTang = rs.getString(1);
                tenTang = rs.getString(2);
                IDKV = rs.getString(3);

                String[] row = {IDTang, tenTang, IDKV};
                model.addRow(row);
            }

            st.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton11ActionPerformed
    
    
    private void MainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_MainTabbedPaneStateChanged
        // TODO add your handling code here:
        // ham nay de bien tab ngon ngu giong button
        if (MainTabbedPane.getSelectedIndex() == 4) {
            //ngonNgu();
            MainTabbedPane.setSelectedIndex(languageTab);
        }
        
        languageTab = MainTabbedPane.getSelectedIndex();
    }//GEN-LAST:event_MainTabbedPaneStateChanged

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HoltelMagReal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoltelMagReal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoltelMagReal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoltelMagReal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HoltelMagReal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Main;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JTabbedPane MainTabblePane;
    private javax.swing.JPanel StaffManagement;
    private javax.swing.JPanel ThongTInKhach_Info;
    private javax.swing.JPanel ThongTinKhach_TK;
    private javax.swing.JPanel ThongTinPhongTK_Info;
    private javax.swing.JPanel ThongTinPhongTK_Info1;
    private javax.swing.JPanel ThongTinPhong_TK;
    private javax.swing.JPanel ThongTinPhong_TK1;
    private javax.swing.JPanel TrangChu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel10;
    private keeptoo.KGradientPanel kGradientPanel11;
    private keeptoo.KGradientPanel kGradientPanel12;
    private keeptoo.KGradientPanel kGradientPanel13;
    private keeptoo.KGradientPanel kGradientPanel14;
    private keeptoo.KGradientPanel kGradientPanel15;
    private keeptoo.KGradientPanel kGradientPanel16;
    private keeptoo.KGradientPanel kGradientPanel17;
    private keeptoo.KGradientPanel kGradientPanel18;
    private keeptoo.KGradientPanel kGradientPanel19;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel20;
    private keeptoo.KGradientPanel kGradientPanel21;
    private keeptoo.KGradientPanel kGradientPanel22;
    private keeptoo.KGradientPanel kGradientPanel23;
    private keeptoo.KGradientPanel kGradientPanel24;
    private keeptoo.KGradientPanel kGradientPanel25;
    private keeptoo.KGradientPanel kGradientPanel26;
    private keeptoo.KGradientPanel kGradientPanel27;
    private keeptoo.KGradientPanel kGradientPanel28;
    private keeptoo.KGradientPanel kGradientPanel29;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel30;
    private keeptoo.KGradientPanel kGradientPanel31;
    private keeptoo.KGradientPanel kGradientPanel34;
    private keeptoo.KGradientPanel kGradientPanel4;
    private keeptoo.KGradientPanel kGradientPanel5;
    private keeptoo.KGradientPanel kGradientPanel6;
    private keeptoo.KGradientPanel kGradientPanel7;
    private keeptoo.KGradientPanel kGradientPanel8;
    private keeptoo.KGradientPanel kGradientPanel9;
    private javax.swing.JTextField khachdat;
    // End of variables declaration//GEN-END:variables
}
