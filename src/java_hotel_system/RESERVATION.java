package java_hotel_system;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 1BestCsharp
 */
public class RESERVATION {
    
    // in the reservation table we need to add two FOREIGN keys:
    // 1 for the client
    // -> alter TABLE reservations ADD CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES clients(id) on DELETE CASCADE
    // 2 for the room
    // -> alter TABLE reservations ADD CONSTRAINT fk_room_number FOREIGN KEY (room_number) REFERENCES rooms(r_number) on DELETE CASCADE
    // and add another foreign key between table types and rooms
    // -> alter TABLE rooms ADD CONSTRAINT fk_type_id FOREIGN KEY (type) REFERENCES type(id) on DELETE CASCADE
    
    
    // some fixes we need to do
    // 1 - when we add a new reservation the room associated with it should be set to reserved = YES
    //     and when deleting the reservation it should be set to reserved = NO
    // 2 - when we add a new reservation we need to check if the room is already reserved
    // 3 - check if the date_in > the current date
    // 4 - check if the date_out > the date in
    
    
    MY_CONNECTION my_connection = new MY_CONNECTION();
    ROOMS room = new ROOMS();
     // create a function to add a new room
    public boolean addReservation(int client_id, int room_number, String dateIn, String dateOut)
    {
        PreparedStatement st;
        String addQuery = "INSERT INTO `reservations`(`client_id`, `room_number`, `date_in`, `date_out`) VALUES (?,?,?,?)";
        
        try {
            
            st = my_connection.createConnection().prepareStatement(addQuery);
            
            st.setInt(1, client_id);
            st.setInt(2, room_number);
            st.setString(3, dateIn);
            st.setString(4, dateOut);
            
            if(room.isRoomReserved(room_number).equals("No"))
            {
              if (st.executeUpdate() > 0)
                {
                    room.setRoomToReserved(room_number, "Yes");
                    return true;
                }
              else{
                    return false;
                  }    
            }else{
                JOptionPane.showMessageDialog(null, "This Room Is Already Reserved", "Room Reserved", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
   
    
    // create a function to edit the selected room
    public boolean editReservation(int reservation_id,int client_id, int room_number, String dateIn, String dateOut)
    {
        PreparedStatement st;
        String editQuery = "UPDATE `reservations` SET `client_id`=?,`room_number`=?,`date_in`=?,`date_out`=? WHERE `id`=?";
        
        try {
            
            st = my_connection.createConnection().prepareStatement(editQuery);
            
            st.setInt(1, client_id);
            st.setInt(2, room_number);
            st.setString(3, dateIn);
            st.setString(4, dateOut);
            st.setInt(5, reservation_id);
            
            return (st.executeUpdate() > 0);
             
           } 
           catch (SQLException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    
    
     // create a function to remove the selected reservation
    public boolean removeReservation(int reservation_id)
    {
        PreparedStatement st;
        String deleteQuery = "DELETE FROM `reservations` WHERE `id`=?";
        
        try {
            
            st = my_connection.createConnection().prepareStatement(deleteQuery);

            st.setInt(1, reservation_id);
            
            // we need to get the room number before delting the reservation
            int room_number = getRoomNumberFromReservation(reservation_id);
            
            if (st.executeUpdate() > 0)
            {
                room.setRoomToReserved(room_number, "No");
                return true;
                
            }else{
                
                return false;
            }
             
           } 
           catch (SQLException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    
     // create a function to display all rooms in jtable
    public void fillReservationsJTable(JTable table)
    {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT * FROM `reservations`";
        
        try {
            
            ps = my_connection.createConnection().prepareStatement(selectQuery);
            
            rs = ps.executeQuery();
            
            DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
            
            Object[] row;
            
            while(rs.next())
            {
               row = new Object[5];
               row[0] = rs.getInt(1);
               row[1] = rs.getInt(2);
               row[2] = rs.getInt(3);
               row[3] = rs.getString(4);
               row[4] = rs.getString(5);
               
               tableModel.addRow(row);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
   
    
    // create a function to get the room number from a reservation
    public int getRoomNumberFromReservation(int reservationID)
    {
        PreparedStatement ps;
        ResultSet rs;
        String selectQuery = "SELECT `room_number` FROM `reservations` WHERE `id` = ?";
        
        try {
            
            ps = my_connection.createConnection().prepareStatement(selectQuery);
            
            ps.setInt(1, reservationID);
            
            rs = ps.executeQuery();
            
            if(rs.next()) 
            {
                return rs.getInt(1);
            }
            else{
                return 0;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CLIENT.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } 
    }
    
}


