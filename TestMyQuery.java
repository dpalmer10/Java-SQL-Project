/*******************************************************
 Tester for the project
 By: Dan Li
 *******************************************************/
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMyQuery {
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            MyQuery mquery = new MyQuery(conn);

            // Query 0
            mquery.findAuthorJuanAdams();
            mquery.printAuthorJuanAdams();

            // Query 1
            mquery.findCustomerOrder();
            mquery.printCustomerOrder();

            // Query 2
            mquery.findBusyAuthor();
            mquery.printBusyAuthor();

            // Query 3
            mquery.findBookProfit();
            mquery.printBookProfit();

            // Query 4
            mquery.findHighestProfitPerCategory();
            mquery.printHighestProfitPerCategory();

            // Query 5
            mquery.findMinMaxOrderDate();
            mquery.printMinMaxOrderDate();

            // Query 6
            mquery.updateDiscount();
            mquery.printUpdatedDiscount();

            // Query 7
            mquery.findHighestProfit();

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        //Create a connection to the database
        String serverName = ""; //change needed
        String mydatabase = ""; //change needed
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase; // a JDBC url
        String username = ""; //change needed
        String password = ""; //change needed

        connection = DriverManager.getConnection(url, username, password);
        return connection;

    }
}
