
package db;
//import javax.validation.constraints.Null;

import java.sql.*;
import java.util.ArrayList;
/**
 * the SQL connectin and search
 * @author Changdi Chen
 * @version 1.0
 */
public class MySQL{
    public static ArrayList<String> displaylist = new ArrayList<String>();
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:8081/hw5";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/hw5?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String USER = "root";
    static final String PASS = "example";
    public static void main(String[] args) {

        Connection conn = null;
        Statement stmt = null;
//        try{
//
//            Class.forName(JDBC_DRIVER);
//
//
//            System.out.println("linking...");
//            conn = DriverManager.getConnection(DB_URL,USER,PASS);
//
//
//            System.out.println(" creating statement...");
//            stmt = conn.createStatement();
//            String sql;
//            sql = "SELECT * FROM farmers";
//            ResultSet rs = stmt.executeQuery(sql);
//
//
//            while(rs.next()){
//
//                int id  = rs.getInt("FMID");
//                String name = rs.getString("MarketName");
//                String url = rs.getString("Website");
//
//
//                System.out.print("ID: " + id);
//                System.out.print(", name: " + name);
//                System.out.print(", URL: " + url);
//                System.out.print("\n");
//            }
//
//            rs.close();
//            stmt.close();
//            conn.close();
//        }catch(SQLException se){
//
//            se.printStackTrace();
//        }catch(Exception e){
//
//            e.printStackTrace();
//        }finally{
//
//            try{
//                if(stmt!=null) stmt.close();
//            }catch(SQLException se2){
//            }
//            try{
//                if(conn!=null) conn.close();
//            }catch(SQLException se){
//                se.printStackTrace();
//            }
//        }
//        System.out.println("Goodbye!");
        //search("Troy","","","",30,0,"");
    }

    /**
     * searching through the database with specific limitations
     * @param City targeet city
     * @param state target state
     * @param ID target ID
     * @param Zip target Zip
     * @param lim target limitation
     * @param page page number
     * @param central_state state used for searching
     * @param central_city city used for searching
     * @param central_zip zip used for searching
     * @return whether there is a next page
     */
    public static boolean search(String City,String state, String ID,String Zip, double lim,int page,String central_state,String central_city,String central_zip){
        Connection conn = null;
        Statement stmt = null;
        displaylist.clear();
        int counc=0;
        try{

            Class.forName(JDBC_DRIVER);
            String zipcity="";
            double city_la=0;
            double city_lo=0;
            if (central_zip!="" || (central_city!=""&& central_state!="")){
                System.out.println(central_zip);
                System.out.println(central_city);
                System.out.println(central_state);
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                String sql = "SELECT * FROM zip_codes_states WHERE 1 ";
                if(central_zip!=""){
                    sql = sql+" AND `zip_code`=?";
                }
                if(central_city!=""){
                    sql = sql+" AND `city`=?";
                }
                if(central_state!=""){
                    sql = sql+" AND `state`=?";
                }
                //sql = sql+" LIMIT ? OFFSET ?";
                PreparedStatement p = conn.prepareStatement(sql);

                int tt = 1;
                if(central_zip!="") {
                    p.setString(1, central_zip);
                    tt=2;
                }
                if(central_city!="" && central_state!="") {
                    p.setString(tt, central_city);
                    p.setString(tt+1, central_state);
//                    p.setInt(4,14);
//                    p.setInt(5,((page-1)*13));
                }else if(central_state!="" && central_city == ""){
                    p.setString(tt, central_state);
//                    p.setInt(3,14);
//                    p.setInt(4,(page-1)*13);
                }else if (central_city!="" && central_state == "") {
                    p.setString(tt, central_city);
//                    p.setInt(3, 14);
//                    p.setInt(4, (page - 1) * 13);
                }
                System.out.println(p);
//                }else{
//                    p.setInt(1,14);
//                    p.setInt(2,(page-1)*13);
//
//                }
                ResultSet rs = p.executeQuery();
                zipcity="-1";
                while(rs.next()){

//                    int id  = rs.getInt("FMID");
//                    String name = rs.getString("MarketName");
//                    String url = rs.getString("Website");
//
//                    String a ="ID: "+id+", name: "+name+", URL "+url;
//                    displaylist.add(a);
//                    System.out.print("ID: " + id);
//                    System.out.print(", name: " + name);
//                    System.out.print(", URL: " + url);
//                    System.out.print("\n");
                    zipcity = rs.getString("city");
                    try {
                        city_la = Double.valueOf(rs.getString("latitude"));
                        city_lo = Double.valueOf(rs.getString("longitude"));
                        System.out.println("latitude: "+city_la);
                        System.out.println("longitude: "+city_lo);
                    }catch (Exception e){}
                }
                rs.close();
                p.close();
                conn.close();
            }

            if (!zipcity.equals("") && !zipcity.equals("-1") && lim!=0){
                String between="";
                if (City!=""){
                    between = between + " AND `city` = ?";
                }
                if (state!=""){
                    between = between + " AND `state` = ?";
                }
                if (ID!=""){
                    between = between + " AND `FMID` = ?";
                }
                if (Zip!=""){
                    between = between + " AND `zip` = ?";
                }
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                PreparedStatement p = conn.prepareStatement("SELECT *, \n" +
                        "( 3959 * \n" +
                        "    ACOS( \n" +
                        "        COS( RADIANS( y ) ) * \n" +
                        "        COS( RADIANS( " + city_la +" ) ) * \n" +
                        "        COS( RADIANS( "+ city_lo + " ) - \n" +
                        "        RADIANS( x ) ) + \n" +
                        "        SIN( RADIANS( y ) ) * \n" +
                        "        SIN( RADIANS( "+ city_la +" ) ) \n" +
                        "    ) \n" +
                        ") \n" +
                        "AS distance FROM farmers WHERE 1"+between +" HAVING distance <= ? ORDER BY distance ASC"+" LIMIT 14 OFFSET ?");
//                p.setDouble(1, city_la);
//                p.setDouble(2, city_lo);
//                p.setDouble(3, city_la);
                int count = 1;
                if (City!=""){
                    p.setString(count,City);
                    count = count+1;
                }
                if (state!=""){
                    p.setString(count,state);
                    count = count+1;
                }
                if (ID!=""){
                    p.setString(count,ID);
                    count = count+1;
                }
                if (Zip!=""){
                    p.setString(count,Zip);
                    count = count+1;
                }
                p.setDouble(count, lim);
                p.setInt(count+1,(page-1)*13);
                System.out.println(p);
                ResultSet rs = p.executeQuery();
                counc=0;
                while(rs.next()){

                    int id  = rs.getInt("FMID");
                    String name = rs.getString("MarketName");
                    String url = rs.getString("Website");

                    String a ="ID: "+id+", name: "+name+", URL "+url;
                    displaylist.add(a);
                    System.out.print("ID: " + id);
                    System.out.print(", name: " + name);
                    System.out.print(", URL: " + url);
                    System.out.print("\n");
                    counc = counc+1;
                }

                rs.close();
                p.close();
                conn.close();
                if(counc == 14){
                    return true;
                }else{
                    return false;
                }
            }
            System.out.println("linking...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);


            System.out.println(" creating statement...");
            String sql = "SELECT * FROM farmers";
            int counter=0;
            if (City!="" && lim==0){
                sql = sql+" WHERE `city` = ?";
                counter = counter+1;
            }
            if(ID!=""){
                if (counter == 0){
                    sql = sql+" WHERE `FMID` = ?";
                }else{
                    sql = sql+" AND `FMID` = ?";
                }
                counter = counter+1;
            }
//            if (zipcity!="" && lim==0){
//                if (counter == 0){
//                    sql = sql+" WHERE `city` = ?";
//                }else{
//                    sql = sql+" AND `city` = ?";
//                }
//                counter = counter+1;
//            }
            if (state!=""){
                if (counter == 0){
                    sql = sql+" WHERE `state` = ?";
                }else{
                    sql = sql+" AND `state` = ?";
                }
                counter = counter+1;
            }
            if(lim!=0 && zipcity!=""){
                if (counter == 0){
                    sql = sql+ " WHERE `X`< "+String.valueOf(city_la+lim);
                }else {
                    sql = sql + " AND `X`< " + String.valueOf(city_la + lim);
                }
                sql = sql+ " AND `X`> "+String.valueOf(city_la-lim);
                sql = sql+ " AND `Y`< "+String.valueOf(city_lo+lim);
                sql = sql+ " AND `Y`> "+String.valueOf(city_lo-lim);
                //counter = counter+1;
            }
            sql = sql+" LIMIT ? OFFSET ?";
            System.out.println(sql);
            PreparedStatement p = conn.prepareStatement(sql);
           // p.setString(1, Title);
            counter=1;
            if(City!="" && lim==0){
                p.setString(counter, City);
                counter = counter+1;
            }
            if(ID !=""){
                p.setString(counter, ID);
                counter = counter+1;
            }
//            if(zipcity!="" && lim==0){
//                p.setString(counter, zipcity);
//                counter = counter+1;
//            }
            if(state!=""){
                p.setString(counter, state);
                counter = counter+1;
            }
            p.setInt(counter,14);
            p.setInt(counter+1,(page-1)*13);
            System.out.println(p);

            ResultSet rs = p.executeQuery();

            counc = 0;
            while(rs.next()){

                int id  = rs.getInt("FMID");
                String name = rs.getString("MarketName");
                String url = rs.getString("Website");

                String a ="ID: "+id+", name: "+name+", URL "+url;
                displaylist.add(a);
                System.out.print("ID: " + id);
                System.out.print(", name: " + name);
                System.out.print(", URL: " + url);
                System.out.print("\n");
                counc = counc+1;
            }

            rs.close();
            p.close();
            conn.close();
        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
            if(counc == 14){
                return true;
            }else{
                return false;
            }
        }
    }
}

