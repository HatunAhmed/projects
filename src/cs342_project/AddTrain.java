
package cs342_project;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * @author Htoun
 */
public class AddTrain extends JFrame implements ActionListener{
    private JLabel id, arrivalST, departureST, passengersMax;
    private JButton addButton, cancelButton;
    private JComboBox arrivalList, departureList;
    private JTextField idField, psngField;
    private String cities[] = {"Buraydah", "Unayzah", "Bukairiyah", "Al Badaya", "Oyun Al Jiwaa", "Al Khabraa", "Ar Rass"};
    BufferedWriter trainsFile;
     Connection con =null;
     PreparedStatement pst=null;
     ResultSet rs= null;
 
    
    public AddTrain(){
          setLocation(800,500);
        JPanel panel1 = (JPanel)getContentPane();
        panel1.setLayout(null);
        this.setTitle("Add Train");
       this.setSize(400, 250);
        
        id = new JLabel("Train ID:");
        id.setBounds(0, 0, 80, 25);
        panel1.add(id);
        
        idField = new JTextField("");
        idField.setBounds(150, 0,  100, 25);
        panel1.add(idField);
        
        departureST = new JLabel("Departure Station:");
        departureST.setBounds(0, 30, 110, 25);
        panel1.add(departureST);
        
        departureList = new JComboBox(cities);
        departureList.setBounds(150, 30, 100, 25);
        panel1.add(departureList);
        
        
        arrivalST = new JLabel("Arrival Station:");
        arrivalST.setBounds(0, 60, 100, 25);
        panel1.add(arrivalST);
        
        arrivalList = new JComboBox(cities);
        arrivalList.setBounds(150, 60, 100, 25);
        panel1.add(arrivalList);
        
        passengersMax = new JLabel("Max Passengers:");
        passengersMax.setBounds(0, 90, 100, 25);
        panel1.add(passengersMax);
        
        psngField = new JTextField("");
        psngField.setBounds(150, 90,  100, 25);
        panel1.add(psngField);
        
        addButton = new JButton("Add");
        addButton.setBounds(60, 120, 75, 25);
        addButton.addActionListener(this);
        panel1.add(addButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBounds(140, 120, 80, 25);
        
        panel1.add(cancelButton);
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
       
        
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String str0 =idField.getText();
        String str1 = String.valueOf(departureList.getSelectedItem());
        String str2 = String.valueOf(arrivalList.getSelectedItem());
        String str3 = psngField.getText(),line;
        int intgr2,flag=0;
        String array [];
        
        
         if(e.getSource() == cancelButton){
           System.exit(0);
        }
      else 
        if(e.getSource() == addButton){
           
             if(str0.equals("") || str3.equals("")){
                    JOptionPane.showMessageDialog(null, "Check inputs!", "Input Error", JOptionPane.ERROR_MESSAGE);
                     System.out.println("Error: check inputs");}
             else  
               if(str1.equals(str2) ){ // dept==arraival
                    System.out.println("Error: check inputs");
                    JOptionPane.showMessageDialog(null, "Check inputs!", "Input Error", JOptionPane.ERROR_MESSAGE);}
               else{
                 //  intgr2 = Integer.parseInt(str3);
                   BufferedReader bf;
               try {
                   bf = new BufferedReader(new FileReader("TrainFile.txt"));
                
                
                       while((line=bf.readLine())!=null){
                           array=line.split(";");
                           String id=array[0];
                           //System.out.println(id);
                           if(id.equals(str0)){
                               flag=1;
                               break;}
                       }
                       if(flag==1)
                           JOptionPane.showMessageDialog(null, "Train exists"," ",JOptionPane.ERROR_MESSAGE);
                      else if(flag==0){
                      WriteToFile(str0, Integer.parseInt(str3), str2, str1);
                      }
                           bf.close();
                            
      }//end try
       catch (FileNotFoundException ex) {
           System.out.println("FileNotFound Exception");                                        } 
                      
        catch (IOException ex) {
            System.out.println("IOException is found");
                               }
       }
   
   }
        
        
           
   
    } // actionperformed 
    
 
public void WriteToFile(String id, int mxP, String dest, String dep){
     FileWriter w; 
        try { 
             String sql="INSERT INTO Train VALUES (?,?,?,?) ";
             con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","System","1234");
              pst.setString(1,id);
              pst.setInt(2, mxP);
              pst.setString(3,dest);
              pst.setString(4,dep);  
              pst.executeUpdate();
              
                  
                  
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TrainData.txt",true));
            
            w = new FileWriter("TrainFile.txt",true);
            PrintWriter file=new PrintWriter(w);
            
            Train n= new Train(id,  mxP,  dest,  dep);
            oos.writeObject(n);
            
            System.out.println(n);
            file.println(id+";"+ mxP+";"+dest+ ";"+dep);
              idField.setText("");
              psngField.setText("");
              file.close();
              oos.close();
             JOptionPane.showMessageDialog(null, "the addition is done successfully","Operation is done ",JOptionPane.INFORMATION_MESSAGE);
        } 
        catch (IOException ex) {
            System.out.println("IO Exceptionis found ");
        } catch (SQLException ex) {
            System.out.println("SQL Exceptionis found ");
        }
     
}

public static void main(String[] args) {
        AddTrain n= new AddTrain();
    
}

}

