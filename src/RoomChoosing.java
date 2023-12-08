
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author HP
 */
public class RoomChoosing extends javax.swing.JFrame {
    private static boolean initDone = false;
    public interface RoomChoosingInterface {
        void onRoomSelected(String selectedIDNhanVien);
    }
    private RoomChoosingInterface callback;
    /**
     * Creates new form RoomChoosing
     */
    private static String SelectedRoom = new String("");
    
    public RoomChoosing() {
        initComponents();
        initshits();
        inittable();
        initDone = true; //This is here because in the initshits function, the combobox's state change! So, it make a mess
        //This is here to prevent the state change event to make an absolute mess 
    }
    
    private String getFloarNameFromDatabase(String IDTang){
        String ten = new String();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT tenTang FROM tang WHERE IDTang = " + "'" + IDTang + "'"; //select the all from taikhoan where username = username        
//             System.err.println(query);
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
//        System.out.println(ten);
        return ten; 
    }
    
    private String getAreaNameFromDatabase(String IDKhuVuc){
        String ten = new String();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT tenKV FROM khuvuc WHERE IDKhuVuc = " + "'" + IDKhuVuc + "'"; //select the all from taikhoan where username = username        
//            System.err.println(query);
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
//        System.out.println(ten);
        return ten; 
    }
    
    
    
    
    private String getMaTrangThai(String TenTrangThai){
        String ten = new String();
        if (TenTrangThai.equals("All")) return "";
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT MaTrangThai FROM trangthai WHERE TenTrangThai = " + "'" + TenTrangThai + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
//        System.out.println(ten);
        return ten; 
    }
    
    private String getMaLoaiPhong(String TenLoaiPhong){
        String ten = new String();
        if (TenLoaiPhong.equals("All")) return "";
        
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT IDLoai FROM loaiphong WHERE tenLoai = " + "'" + TenLoaiPhong + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
//        System.out.println(ten);
        return ten; 
    }
    
    private String getMaTang(String TenTang){
        if (TenTang.equals("All")) return "";
        String ten = new String();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT IDTang FROM tang WHERE tenTang = " + "'" + TenTang + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
        return ten; 
    }
    
    private String getMaKhuVuc(String TenKhuVuc){
        String ten = new String();
        if (TenKhuVuc.equals("All")) return "";
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT IDKhuVuc FROM khuvuc WHERE TenKV = " + "'" + TenKhuVuc + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
        return ten; 
    }

    
    
    
    
    private String getRoomTypeNameFromDatabase(String IDLoai){
        String ten = new String();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT tenLoai FROM loaiphong WHERE IDLoai = " + "'" + IDLoai + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
        return ten; 
    }
    private String getStateNameFromDatabase(String MaTrangThai){
        String ten = new String();
         try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT TenTrangThai FROM trangthai WHERE MaTrangThai = " + "'" + MaTrangThai + "'"; //select the all from taikhoan where username = username        
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            rs.next();
            ten = rs.getString(1);
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
        return ten; 
    }
    
    
    
    private void initshits(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT IDTang FROM phong"; //select the all from taikhoan where username = username
            String queryArea = "SELECT IDKhuVuc FROM khuvuc";
            String queryRoomType = "SELECT IDLoai FROM loaiphong";
            String queryState = "SELECT MaTrangThai FROM trangthai";
            
            DefaultComboBoxModel comboBoxFloor = new DefaultComboBoxModel();
            DefaultComboBoxModel comboBoxArea = new DefaultComboBoxModel();
            DefaultComboBoxModel comboBoxRoomType = new DefaultComboBoxModel();
            DefaultComboBoxModel comboBoxState = new DefaultComboBoxModel();
            
            jComboBox3.setModel(comboBoxFloor);
            jComboBox1.setModel(comboBoxState);
            jComboBox2.setModel(comboBoxRoomType);
            jComboBox4.setModel(comboBoxArea);
            //This part print out the table row by row
            
            comboBoxFloor.addElement("All");
            comboBoxArea.addElement("All");
            comboBoxState.addElement("All");
            comboBoxRoomType.addElement("All");
            
            String tenTang = new String();
            String IDTang = new String();
            String tenKV = new String();
            String IDKV = new String();
            String tenLoaiPhong = new String();
            String IDLoaiPhong = new String();
            String tenTrangThai = new String();
            String IDTrangThai = new String();
            
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
            while(rs.next()){
                IDTang = rs.getString(1);
                tenTang = getFloarNameFromDatabase(IDTang);
                comboBoxFloor.addElement(tenTang);
            }
            
            rs = st.executeQuery(queryArea);
            while(rs.next()){
                IDKV = rs.getString(1);
                tenKV = getAreaNameFromDatabase(IDKV);
                comboBoxArea.addElement(tenKV);
            }
            
            rs = st.executeQuery(queryRoomType);
            while(rs.next()){
                IDLoaiPhong = rs.getString(1);
                tenLoaiPhong = getRoomTypeNameFromDatabase(IDLoaiPhong);
                comboBoxRoomType.addElement(tenLoaiPhong);
            }
            
            rs = st.executeQuery(queryState);
            while(rs.next()){
                IDTrangThai = rs.getString(1);
                tenTrangThai = getStateNameFromDatabase(IDTrangThai);
                comboBoxState.addElement(tenTrangThai);
            }
            
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
        
    }
    private void inittable(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            
            String query = "SELECT * FROM phong";
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
//            
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel(); // get the table
            String IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKhuVuc, IDLoai;
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] colName = {"Mã Phòng", "Tên Phòng", "Trạng Thái Phòng", "Giá Phòng", "Mã Tầng", "Mã Khu Vực", "Mã Loại"};
            model.setColumnIdentifiers(colName);
////            System.err.println(col);
//
//            This part print out the table row by row
            String tentrangThai = new String();
            while(rs.next()){
                IDPhong = rs.getString(1);
                tenPhong = rs.getString(2);
                
                //This right here make the while loop not clean :( i am sad :(
                //Any way, sooo, State in the data base is actually the ID of the state
                //So, this change the stateID to State's name!
                tentrangThai = rs.getString(3);
//                System.out.println("!!!!!!!!!!^!!!!!!!!" + tentrangThai);
                trangThaiPhong = getStateNameFromDatabase(tentrangThai);
                
                
                giaPhong = rs.getString(4);
                IDTang = rs.getString(5);
                IDKhuVuc = rs.getString(6);
                IDLoai = rs.getString(7);

                String[] row = {IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKhuVuc, IDLoai};
                model.addRow(row);
            }
//            
//        // delete the password from the text field
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(844, 490));

        kGradientPanel1.setkEndColor(new java.awt.Color(33, 115, 70));
        kGradientPanel1.setkGradientFocus(0);
        kGradientPanel1.setkStartColor(new java.awt.Color(35, 104, 66));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("CHỌN PHÒNG");

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel4)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        kGradientPanel2.setkEndColor(new java.awt.Color(204, 248, 216));
        kGradientPanel2.setkGradientFocus(0);
        kGradientPanel2.setkStartColor(new java.awt.Color(61, 192, 96));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Tìm kiếm bằng Mã Phòng:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel2.setText("Tìm Kiếm Bằng Tên Phòng:");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel3.setText("Tìm Kiếm Bằng Trạng Thái:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ComboBoxStateNameChange(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel5.setText("Tìm Kiếm Bằng Loại Phòng");

        jLabel6.setText("Tìm Kiếm Bằng Vị Trí");

        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(2, 2, 2)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Tìm Kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xóa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton1)
                .addGap(26, 26, 26)
                .addComponent(jButton2)
                .addGap(24, 24, 24)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1ActionPerformed
    private void IDSearch(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/hotelmanagement", "root", "");
            Statement st = (Statement) con.createStatement();
            String MaPhong = new String();
            String TenPhong = new String();
            String MaTrangThai = new String();
            String MaLoaiPhong = new String();
            String MaTang = new String();
            String MaKhuVuc = new String();
            
            MaPhong = jTextField1.getText();
            TenPhong = jTextField2.getText();
            MaTrangThai = getMaTrangThai((String) jComboBox1.getSelectedItem());
            MaLoaiPhong = getMaLoaiPhong((String) jComboBox2.getSelectedItem());
            MaTang = getMaTang((String) jComboBox3.getSelectedItem());
            MaKhuVuc = getMaKhuVuc((String) jComboBox4.getSelectedItem());

            
            
            String query = "SELECT * FROM phong WHERE IDPhong LIKE '" + MaPhong + "%'" + " AND tenPhong LIKE '%" + TenPhong + "%'" + " AND trangThaiPhong LIKE " + "'" + MaTrangThai + "%'" + " AND IDLoai LIKE " + "'" + MaLoaiPhong + "%'" + " AND IDTang LIKE " + "'" + MaTang + "%'" + " AND IDKhuVuc LIKE " + "'" + MaKhuVuc + "%'";
            ResultSet rs = st.executeQuery(query); // Xu ly chuoi full vao database
//            
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel(); // get the table
            String IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKhuVuc, IDLoai;
            model.setRowCount(0); // clean the whole table
            //This part is to get the whole data table
            ResultSetMetaData rsmd = rs.getMetaData();
            String[] colName = {"Mã Phòng", "Tên Phòng", "Trạng Thái Phòng", "Giá Phòng", "Mã Tầng", "Mã Khu Vực", "Mã Loại"};
            model.setColumnIdentifiers(colName);
////            System.err.println(col);
//
//            This part print out the table row by row
            String tentrangThai = new String();
            while(rs.next()){
                IDPhong = rs.getString(1);
                tenPhong = rs.getString(2);
                
                //This right here make the while loop not clean :( i am sad :(
                //Any way, sooo, State in the data base is actually the ID of the state
                //So, this change the stateID to State's name!
                tentrangThai = rs.getString(3);
//                System.out.println("!!!!!!!!!!^!!!!!!!!" + tentrangThai);
                trangThaiPhong = getStateNameFromDatabase(tentrangThai);
                
                
                giaPhong = rs.getString(4);
                IDTang = rs.getString(5);
                IDKhuVuc = rs.getString(6);
                IDLoai = rs.getString(7);

                String[] row = {IDPhong, tenPhong, trangThaiPhong, giaPhong, IDTang, IDKhuVuc, IDLoai};
                model.addRow(row);
            }
//            
//        // delete the password from the text field
            st.close();
            con.close();
        }catch( Exception e) {
                e.printStackTrace();
        }
    }
    public void setRoomChoosingListener(RoomChoosing.RoomChoosingInterface listener) {
        this.callback = listener;
    }
                      
    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        IDSearch(); //The key to realtime searching :3
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
        IDSearch();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
         // TODO add your handling code here:
        //This code run every time the user "clicked" on the table
        int selectedRow = jTable1.getSelectedRow(); //get the row of the clicked row, if its not a row, then, selected row = -1
        if (selectedRow != -1) { // if the user click a row
            String selectedIDNV = (String) jTable1.getValueAt(selectedRow, 0);
            SelectedRoom = selectedIDNV;
            
            if (callback != null) {
                callback.onRoomSelected(SelectedRoom);
            }
        }
        //Okay, why is initDone = false here? i have no idea, but if it's not here, the second time 
        //the user open this form, there will be some small sql error ( i just want it to be perfect :( )
        //my guess is because this form is implemented inside the holtelmagreal class
        initDone = false; 
        this.dispose();
    }//GEN-LAST:event_jTable1MouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        IDSearch();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jTextField2.setText("");
//        jTextField3.setText("");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel(); // get the table
        model.setRowCount(0);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void ComboBoxStateNameChange(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ComboBoxStateNameChange
        // TODO add your handling code here:
        if (initDone) IDSearch();
    }//GEN-LAST:event_ComboBoxStateNameChange

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (initDone) IDSearch();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        if (initDone) IDSearch();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
        if (initDone) IDSearch();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

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
            java.util.logging.Logger.getLogger(RoomChoosing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RoomChoosing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RoomChoosing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RoomChoosing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RoomChoosing().setVisible(true);
            }
            
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    // End of variables declaration//GEN-END:variables
}
