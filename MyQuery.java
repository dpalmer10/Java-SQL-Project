/*****************************
 Query the Books Database
 *****************************/
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.util.*;
import java.lang.String;

public class MyQuery {

    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public MyQuery(Connection c)throws SQLException
    {
        conn = c;
        // Statements allow to issue SQL queries to the database
        statement = conn.createStatement();
    }

    public void findAuthorJuanAdams() throws SQLException
    {
        String query  = "select title from BOOKS natural join BOOKAUTHOR natural join AUTHOR"
                + " where fname = \'JUAN\' and lname = \'ADAMS\';";

        resultSet = statement.executeQuery(query);
    }

    public void printAuthorJuanAdams() throws IOException, SQLException
    {
        System.out.println("******** Query 0 ********");
        System.out.println("Book_Title");
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number which starts at 1
            String name = resultSet.getString(1);
            System.out.println(name);
        }
        System.out.println();
    }

    public void findCustomerOrder() throws SQLException
    {
        String query1 = "select concat(FirstName, ' ', LastName), count(distinct Order_num), sum(Quantity) "
                + "from CUSTOMERS join ORDERS using (Customer_num) natural join ORDERITEMS "
                + "group by Customer_num order by sum(Quantity) desc;";

        resultSet = statement.executeQuery(query1);
    }

    public void printCustomerOrder() throws IOException, SQLException
    {
        System.out.println("******** Query 1 ********");
        System.out.format("%-15s %-5s %s", "Full_Name", "Num_Orders", "Num_of_Books\n");
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int count = resultSet.getInt(2);
            int numBooks = resultSet.getInt(3);
            System.out.format("%-15s %d %10d", name, count, numBooks);
            System.out.println();
        }
        System.out.println();
    }

    public void findBusyAuthor() throws SQLException
    {
        String query2 = "select concat(Fname, ' ', Lname), count(AuthorID) from AUTHOR natural join BOOKAUTHOR "
                + "group by AuthorID having count(AuthorID) >= all(select count(AuthorID) from BOOKAUTHOR "
                + "group by AuthorID);";

        resultSet = statement.executeQuery(query2);
    }

    public void printBusyAuthor() throws IOException, SQLException
    {
        System.out.println("******** Query 2 ********");
        System.out.format("%-15s %s" , "Full_Name", "Num_of_Books\n");
        while(resultSet.next()){
            String name = resultSet.getString(1);
            int numBooks = resultSet.getInt(2);
            System.out.format("%-15s %d" , name, numBooks);
            System.out.println();
        }
        System.out.println();
    }

    public void findBookProfit() throws SQLException
    {
        String query3 = "select distinct ISBN, Title, Category, sum((PaidEach - Cost) * Quantity) "
                + "from BOOKS join ORDERITEMS using (ISBN) group by ISBN order by sum((PaidEach - Cost) * Quantity) asc;";

        resultSet = statement.executeQuery(query3);
    }

    public void printBookProfit() throws IOException, SQLException
    {
        System.out.println("******** Query 3 ********");
        System.out.format("%-15s %-33s %-18s %s", "ISBN", "Title", "Category","Profit\n");
        while(resultSet.next()){
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String category = resultSet.getString(3);
            double profit = resultSet.getDouble(4);
            System.out.format("%-15s %-33s %-18s %.2f", isbn, title, category,profit);
            System.out.println();
        }
        System.out.println();
    }

    public void findHighestProfitPerCategory() throws SQLException
    {
        String query4 = "select ISBN, Title, Category, sum((PaidEach - Cost) * Quantity) "
                + "from BOOKS b1 join ORDERITEMS using (ISBN) group by ISBN "
                + "having sum((PaidEach - Cost) * Quantity) >= all (select sum((PaidEach - Cost) * Quantity) "
                + "from BOOKS join ORDERITEMS using (ISBN) where Category = b1.Category group by ISBN) "
                + "order by sum((PaidEach - Cost) * Quantity) asc;";

        resultSet = statement.executeQuery(query4);
    }

    public void printHighestProfitPerCategory() throws IOException, SQLException
    {
        System.out.println("******** Query 4 ********");
        System.out.format("%-15s %-33s %-18s %s", "ISBN", "Title", "Category","Profit\n");
        while(resultSet.next()){
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String category = resultSet.getString(3);
            double profit = resultSet.getInt(4);
            System.out.format("%-15s %-33s %-18s %.2f", isbn, title, category,profit);
            System.out.println();
        }
        System.out.println();
    }

    public void findMinMaxOrderDate() throws SQLException
    {
        String query5 = "SELECT ISBN, Title, Name, ifnull(min(OrderDate), \"N/A\" ), ifnull(max(OrderDate), \"N/A\"), ifnull(sum(quantity), 0)" +
                " FROM BOOKS join PUBLISHER using (PubID) left outer join ORDERITEMS using(ISBN) left outer join ORDERS using (Order_num)" +
                " group by ISBN" +
                " order by sum(Quantity) desc;";

        resultSet = statement.executeQuery(query5);
    }

    public void printMinMaxOrderDate() throws IOException, SQLException
    {
        System.out.println("******** Query 5 ********");
        System.out.format("%-15s %-33s %-30s %-23s %-23s %s", "ISBN", "Title", "Name", "Earliest_Order_Date", "Latest_Order_Date", "Total_quantity\n");
        while(resultSet.next()){
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String name = resultSet.getString(3);
            String earliest = resultSet.getString(4);
            String latest = resultSet.getString(5);
            int total = resultSet.getInt(6);
            System.out.format("%-15s %-33s %-30s %-23s %-23s %d", isbn, title, name, earliest, latest, total);
            System.out.println();
        }
        System.out.println();
    }

    public void updateDiscount() throws SQLException
    {
        String query6 = "create temporary table BookCopy (select * from BOOKS);";
        statement.executeUpdate(query6);
        query6 = "update BookCopy set discount = retail * .2 where PubID = 4; ";
        statement.executeUpdate(query6);
        query6 = "select * from BookCopy; ";
        resultSet = statement.executeQuery(query6);
    }

    public void printUpdatedDiscount() throws IOException, SQLException
    {
        System.out.println("******** Query 6 ********");
        System.out.format("%-15s %-33s %-15s %s \t%s\t%s\t%s\t%s", "ISBN", "Title", "PubDate", "PubID", "Cost", "Retail", "Discount", "Category\n");
        while(resultSet.next()){
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String pubDate = resultSet.getString(3);
            int pubID = resultSet.getInt(4);
            double cost = resultSet.getDouble(5);
            double retail = resultSet.getDouble(6);
            double discount = resultSet.getDouble(7);
            String category = resultSet.getString(8);
            System.out.format("%-15s %-33s %-15s %d \t\t%.2f \t%.2f \t%.2f \t\t%-20s", isbn, title, pubDate, pubID, cost, retail, discount, category);
            System.out.println();
        }
        System.out.println();
    }

    public void findHighestProfit() throws SQLException
    {
        System.out.println("******** Query 7 ********");
        System.out.println("Please enter the Category name:");
        Scanner kb = new Scanner(System.in);
        String inputCategory = kb.nextLine().toUpperCase();
        CallableStatement stmt = conn.prepareCall("{call highestProfit(?)}");
        stmt.setString(1,inputCategory);
        resultSet = stmt.executeQuery();
        while(resultSet.next()){
            String isbn = resultSet.getString(1);
            String title = resultSet.getString(2);
            String category = resultSet.getString(3);
            double profit = resultSet.getDouble(4);
            System.out.printf("Book \"%s\" (ISBN: %s) has the highest profit $%.2f in %s category", title, isbn, profit, category);
        }
        System.out.println();
    }

}
