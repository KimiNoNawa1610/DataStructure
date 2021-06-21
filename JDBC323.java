import java.sql.*;
import java.util.Scanner;

class JDBC323 {

    public static void main(String[] args) throws SQLException {
        int err=0;
        String nameIn="";
        String bookTitle;
        String stmt;
        String publisher="";
        PreparedStatement pstmt=null;
        ResultSet result;
        Scanner input = new Scanner(System. in );
        Connection con= DriverManager.getConnection("jdbc:derby://localhost:1527/writingBooks");// Connect to the JDBC database
        System.out.println(" Database Connected");
        boolean stillChoosing = true;

        do {/*Output basic menu for the user to choose and prompt the user for a specific operation depend on the number (from 1 to 10) that the user chooses*/
            System.out.println("-----Please select one of the following choices:-----\n" +
                    "\n1) List all writing groups" +
                    "\n2) List all the data for a specified writing group."+
                    "\n3) List all publishers." +
                    "\n4) List all the data for a publisher specified by...?" +
                    "\n5) List all book titles." +
                    "\n6) List all the data for a book specified by...." +
                    "\n7) Insert a new book." +
                    "\n8) Insert a new publisher (this will update all books published by this person)" +
                    "\n9) Remove a book." +
                    "\n10) Quit");

            String choice = input.nextLine();

            switch (choice) {
                /*Display all the information from for all the Writing group*/
                case "1":
                    stmt="SELECT GroupName, HeadWriter, YearFormed, Subject FROM WritingGroup";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    System.out.println("All writing groups:");
                    stmt="SELECT GROUPNAME, HEADWRITEr, YEARFORMED,SUBJECT FROM WritingGroup";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String GroupName=result.getString(1);
                        String HeadWriter=result.getString(2);
                        Integer year=result.getInt(3);
                        String subject=result.getString(4);
                        System.out.format("Group Name: %s | HeadWriter: %s | YearFormed: %s | Subject: %s\n",GroupName,HeadWriter,year,subject+"\n");
                    }
                    break;

                /*Display all the information, include publishers and book that a writing group has*/
                case "2":
                    err=0;
                    System.out.println("The Names of all Writing Group:");
                    stmt="SELECT GROUPNAME FROM WritingGroup";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String userData="*"+result.getString(1);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("Please enter the writing group name you want to have data:");
                    nameIn=input.nextLine();
                    stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1,nameIn);
                    result=pstmt.executeQuery();
                    while(result.next()==false){
                        String error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        nameIn=input.nextLine();
                        if(nameIn.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="SELECT writinggroup.groupname from writinggroup where groupname=?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,nameIn);
                        result=pstmt.executeQuery();
                    }

                    if(err!=-1){
                        stmt="SELECT writinggroup.GroupName, writinggroup.HeadWriter, writinggroup.YearFormed, writinggroup.Subject, "
                            + "Book.Booktitle, Book.yearpublished, Book.numberpages, Book.Publishername, publisher.publisheraddress, "
                            + "publisher.publisherphone, publisher.publisheremail FROM WritingGroup "
                            + "left outer Join Book on  Book.Groupname= Writinggroup.Groupname"
                            + " left outer Join Publisher on Book.publishername=publisher.publishername WHERE writinggroup.GroupName= ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, nameIn);
                        result=pstmt.executeQuery();
                        System.out.println("The data of the "+nameIn+" that you want:");
                        while(result.next()){
                            String GroupName=result.getString(1);
                            String HeadWriter=result.getString(2);
                            Integer year=result.getInt(3);
                            String subject=result.getString(4);
                            String booktitle=result.getString(5);
                            Integer yearPublished=result.getInt(6);
                            Integer pages=result.getInt(7);
                            String Publisher=result.getString(8);
                            String Publisheradd=result.getString(9);
                            String publisherphone=result.getString(10);
                            String publisheremail=result.getString(11);
                            System.out.format("Writing Group Name: %s | Head Writer: %s | Year Formed: %s | Subject: %s | Book Title: %s"
                                    + " | Year Published: %s | Page Number: %s | Publisher Name: %s | Publisher Address: %s "
                                    + "| Publisher Phone: %s | Publisher Email: %s\n",GroupName,HeadWriter,year,subject,booktitle,
                                    yearPublished,pages,Publisher,Publisheradd,publisherphone,publisheremail+"\n");
                        }
                    }
                    break;
                
                /*List all publishers with their information*/
                case "3":
                    stmt="Select PUBLISHERNAME, PUBLISHERADDRESS,PUBLISHERPHONE,PUBLISHEREMAIL From Publisher";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String Publisher=result.getString(1);
                        String Publisheradd=result.getString(2);
                        String publisherphone=result.getString(3);
                        String publisheremail=result.getString(4);
                        System.out.format("Publisher Name: %s | publisher Address: %s | Publisher Phone: %s | "
                                + "Publisher Email: %s\n",Publisher,Publisheradd,publisherphone,publisheremail+"\n");
                    }
                    break;

                /*List all the data for a publisher specified by the user*/
                case "4":
                    err=0;
                    System.out.println("The Names of all Publishers:");
                    stmt="SELECT publishername FROM publisher";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String userData="*"+result.getString(1);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("Please select a publilsher you want to know about");
                    publisher = input.nextLine();
                    stmt ="select publishername FROM publisher WHERE publisherName= ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1, publisher);
                    result=pstmt.executeQuery();
                    while(result.next()==false){
                        String error="\nError: Your publisher name does not exist in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        publisher=input.nextLine();
                        if(publisher.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="select publishername FROM publisher WHERE publisherName= ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,publisher);
                        result=pstmt.executeQuery();
                    }
                    if(err!=-1){
                        stmt="SELECT Publisher.Publishername, publisher.publisheraddress, publisher.publisherphone, publisher.publisheremail,"
                            + "writinggroup.GroupName, writinggroup.HeadWriter, writinggroup.YearFormed, writinggroup.Subject, "
                            + "Book.Booktitle, Book.yearpublished, Book.numberpages FROM Publisher "
                            + "left outer Join Book on  Book.publishername= Publisher.Publishername"
                            + " left outer Join WritingGroup on writinggroup.GroupName=Book.groupName WHERE Publisher.Publishername= ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, publisher);
                        result=pstmt.executeQuery();
                        System.out.println("The data of the publisher "+publisher+" that you want:");
                        while(result.next()){
                            String Publisher=result.getString(1);
                            String Publisheradd=result.getString(2);
                            String publisherphone=result.getString(3);
                            String publisheremail=result.getString(4);
                            String GroupName=result.getString(5);
                            String HeadWriter=result.getString(6);
                            Integer year=result.getInt(7);
                            String subject=result.getString(8);
                            String booktitle=result.getString(9);
                            Integer yearPublished=result.getInt(10);
                            Integer pages=result.getInt(11);
                            System.out.format("Publisher Name: %s | Publisher Address: %s | Publisher Phone: %s | Publisher Email: %s | Writing Group Name: %s"
                                    + " | Head Writer: %s | Year Formed: %s | Subject: %s | Book Title: %s "
                                    + "| Year Published: %s | Page Number: %s\n",Publisher,Publisheradd,publisherphone,publisheremail,GroupName,
                                    HeadWriter,year,subject,booktitle,yearPublished,pages+"\n");
                        }
                    }

                    break;

                /*List all book titles with some information*/
                case "5":
                    stmt="Select Book.BookTitle, Book.PublisherName, Book.GroupName From Book";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    System.out.println("All the book titles:");
                    while(result.next())
                    {
                        bookTitle=result.getString(1);
                        publisher=result.getString(2);
                        nameIn=result.getString(3);
                        System.out.format("Book Title: %s | Publisher Name: %s | Writing Group Name: %s\n",bookTitle,publisher,nameIn+"\n");
                    }
                    break;

                /*List all the data for a book specified by the user
                    prompt the user the entire key of the Book
                    */
                case "6":
                    err=0;
                    stmt="Select BookTitle, GROUPNAME From Book";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    System.out.println("All the book titles and writing group:");
                    while(result.next())
                    {
                        String userData="* Title:"+result.getString(1)+"| Writing Group:"+result.getString(2);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("Which book would you like all the data for?");
                    bookTitle = input.nextLine();
                    stmt ="select booktitle FROM book WHERE booktitle= ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1, bookTitle);
                    result=pstmt.executeQuery();
                    while(result.next()==false){
                        String error="\nError: Your book title does not exist in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        bookTitle=input.nextLine();
                        if(bookTitle.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="select booktitle FROM book WHERE booktitle= ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        result=pstmt.executeQuery();
                    }
                    if(err!=-1){
                        System.out.println("What is the group name for that book?");
                        nameIn = input.nextLine();
                        stmt="SELECT writinggroup.groupname from writinggroup where groupname=?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,nameIn);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            nameIn=input.nextLine();
                            if(nameIn.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,nameIn);
                            result=pstmt.executeQuery();
                        }
                    }
                    if(err!=-1){
                        stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        pstmt.setString(2,nameIn);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your book "+bookTitle+" written by "+nameIn+" does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            System.out.println("What is the name of the book you would like to insert ?");
                            bookTitle = input.nextLine();
                            if(bookTitle.equals("-1")){
                                err=-1;
                                break;
                            }
                            System.out.println("What is the group name for that book ?");
                            nameIn = input.nextLine();
                            if(nameIn.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,nameIn);
                            result=pstmt.executeQuery();
                            while(result.next()==false){
                                error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                                System.out.println(error);
                                nameIn=input.nextLine();
                                if(nameIn.equals("-1")){
                                    err=-1;
                                    break;
                                }
                                stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                                pstmt=con.prepareStatement(stmt);
                                pstmt.setString(1,nameIn);
                                result=pstmt.executeQuery();
                            }
                            if(err!=-1){
                                stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                                pstmt=con.prepareStatement(stmt);
                                pstmt.setString(1,bookTitle);
                                pstmt.setString(2,nameIn);
                                result=pstmt.executeQuery();
                            }
                            else{
                                break;
                            }
                        }
                    }
                    if(err!=-1){
                        stmt="SELECT Book.Booktitle, Book.yearpublished, Book.numberpages, Publisher.Publishername,"
                            + "publisher.publisheraddress, publisher.publisherphone, publisher.publisheremail, writinggroup.GroupName, "
                            + "writinggroup.HeadWriter, writinggroup.YearFormed, writinggroup.Subject FROM Book "
                            + "Inner Join Publisher on  Book.publishername= Publisher.Publishername"
                            + " Inner Join WritingGroup on writinggroup.GroupName=Book.groupName WHERE Book.BookTitle = ? And Book.GroupName = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, bookTitle);
                        pstmt.setString(2,nameIn);
                        result=pstmt.executeQuery();
                        System.out.println("The data of the Book "+bookTitle+" that you want:");
                        while(result.next()){
                            String booktitle=result.getString(1);
                            Integer yearPublished=result.getInt(2);
                            Integer pages=result.getInt(3);
                            String Publisher=result.getString(4);
                            String Publisheradd=result.getString(5);
                            String publisherphone=result.getString(6);
                            String publisheremail=result.getString(7);
                            String GroupName=result.getString(8);
                            String HeadWriter=result.getString(9);
                            Integer year=result.getInt(10);
                            String subject=result.getString(11);
                            System.out.format("Book Title: %s | Year Published: %s | Page Number: %s | Publisher Name: %s | Publisher Address: %s"
                                    + " | Publisher Phone: %s | Publisher Email: %s | Writing Group Name: %s | Head Writer: %s "
                                    + "| Year Formed: %s | Subject: %s\n",booktitle,yearPublished,pages,Publisher,Publisheradd,
                                    publisherphone,publisheremail,GroupName,HeadWriter,year,subject+"\n");
                        }
                    }
                    break;

                /*Insert a new book*/
                case "7":
                    err=0;
                    System.out.println("What is the name of the book you would like to insert?");
                    bookTitle = input.nextLine();
                    System.out.println("The Names of all Writing Group:");
                    stmt="SELECT GROUPNAME FROM WritingGroup";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String userData="*"+result.getString(1);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("What is the group name for that book?");
                    nameIn = input.nextLine();
                    stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1,nameIn);
                    result=pstmt.executeQuery();
                    while(result.next()==false){
                        String error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        nameIn=input.nextLine();
                        if(nameIn.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,nameIn);
                        result=pstmt.executeQuery();
                    }
                    
                    if(err!=-1){
                        stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        pstmt.setString(2,nameIn);
                        result=pstmt.executeQuery();
                        while(result.next()!=false){
                            String error="\nError: Your book already exists in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            System.out.println("What is the name of the book you would like to insert ?");
                            bookTitle = input.nextLine();
                            if(bookTitle.equals("-1")){
                                err=-1;
                                break;
                            }
                            System.out.println("What is the group name for that book ?");
                            nameIn = input.nextLine();
                            if(nameIn.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,nameIn);
                            result=pstmt.executeQuery();
                            while(result.next()==false){
                                error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                                System.out.println(error);
                                nameIn=input.nextLine();
                                if(nameIn.equals("-1")){
                                    err=-1;
                                    break;
                                }
                                stmt="SELECT writinggroup.groupname from writinggroup where groupname = ?";
                                pstmt=con.prepareStatement(stmt);
                                pstmt.setString(1,nameIn);
                                result=pstmt.executeQuery();
                            }
                            if(err!=-1){
                                stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                                pstmt=con.prepareStatement(stmt);
                                pstmt.setString(1,bookTitle);
                                pstmt.setString(2,nameIn);
                                result=pstmt.executeQuery();
                            }
                            else{
                                break;
                            }
                        }
                    }
                    
                    if(err!=-1){
                        System.out.println("The Names of all Publishers:");
                        stmt="SELECT publishername FROM publisher";
                        pstmt=con.prepareStatement(stmt);
                        result=pstmt.executeQuery();
                        while(result.next())
                        {
                            String userData="*"+result.getString(1);
                            System.out.println(userData+"\n");
                        }
                        System.out.println("What is the publisher for that book?");
                        publisher = input.nextLine();
                        stmt ="select publishername FROM publisher WHERE publisherName = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, publisher);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your publisher name does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            publisher=input.nextLine();
                            if(publisher.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="select publishername FROM publisher WHERE publisherName= ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,publisher);
                            result=pstmt.executeQuery();
                        }
                    }
                        
                    if(err!=-1){
                        stmt="SELECT BookTitle,Publishername FROM Book WHERE BookTitle = ? AND Publishername = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        pstmt.setString(2,publisher);
                        result=pstmt.executeQuery();
                        while(result.next()!=false){
                            String error="\nError: Your book "+bookTitle+", published by "+publisher+" exists in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            System.out.println("What is the name of the book you would like to insert or enter -1 to exit?");
                            bookTitle = input.nextLine();
                            if(bookTitle.equals("-1")){
                                err=-1;
                                break;
                            }
                            System.out.println("What is the publisher name for that book or enter -1 to exit?");
                            publisher = input.nextLine();
                            if(publisher.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT publisherName from Publisher where publisherName = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,publisher);
                            result=pstmt.executeQuery();
                            while(result.next()==false){
                                error="\nError: Your publishername name does not exist in the database. Please enter again or type -1 to exit\n";
                                System.out.println(error);
                                publisher=input.nextLine();
                                if(publisher.equals("-1")){
                                    err=-1;
                                    break;
                                }
                                stmt="SELECT publisherName from Publisher where publisherName = ?";
                                pstmt=con.prepareStatement(stmt);
                                pstmt.setString(1,publisher);
                                result=pstmt.executeQuery();
                            }
                    
                        }
                    
                    }
                        
                    if(err!=-1){
                        System.out.println("What is the year that this book was published ?");
                        String yearPub = input.nextLine();
                        while(!yearPub.matches("\\d+")){
                            System.out.println("Your input is not an approriate integer, Please try again:");
                            yearPub = input.nextLine();
                        }
                        System.out.println("How many number of pages does this book have ?");
                        String numPages = input.nextLine();
                        while(!numPages.matches("\\d+")){
                            System.out.println("Your input is not an approriate integer, Please try again:");
                            numPages = input.nextLine();
                        }
                        stmt="INSERT INTO Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES (?,?,?,?,?)";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, nameIn);
                        pstmt.setString(2, bookTitle);
                        pstmt.setString(3, publisher);
                        pstmt.setInt(4, Integer.parseInt(yearPub));
                        pstmt.setInt(5,Integer.parseInt(numPages));
                        pstmt.executeUpdate();
                        System.out.println("This book has been successfully added into the database!\n");
                        System.out.println("The current books in the database:\n");
                        stmt="Select GROUPNAME, BOOKTITLE, PUBLISHERNAME, YEARPUBLISHED,NUMBERPAGES FROM Book";
                        pstmt=con.prepareStatement(stmt);
                        result=pstmt.executeQuery();
                        while(result.next()){
                            String groupname=result.getString(1);
                            String booktitle=result.getString(2);
                            String publisherName=result.getString(3);
                            Integer year=result.getInt(4);
                            Integer pages=result.getInt(5);
                            System.out.format("Writing Group Name: %s | Book Title: %s | Publisher Name: %s "
                                + "| Year Published: %s | Page Number: %s\n",groupname,booktitle,publisherName,year,pages+"\n");
                        }
                    
                    }
                    
                    break;

                /*Insert a new publisher and update all books published by one publisher to be published by the new publisher*/
                case "8":
                    err=0;
                    System.out.println("The Names of all existing Publishers that has published a book:");
                    stmt="SELECT publishername FROM Publisher";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    while(result.next())
                    {
                        String userData="*"+result.getString(1);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("Please Enter a new publisher name:");
                    publisher =input.nextLine();
                    stmt ="select publishername FROM publisher WHERE publisherName = ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1, publisher);
                    result=pstmt.executeQuery();
                    while(result.next()!=false){
                        String error="\nError: Your publisher name already exists in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        publisher=input.nextLine();
                        if(publisher.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="select publishername FROM publisher WHERE publisherName = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,publisher);
                        result=pstmt.executeQuery();
                    }
                    if(err!=-1){
                        System.out.println("Enter publisher adress");
                        String publihser_add=input.nextLine();
                        System.out.println("publihser phone");
                        String publish_phone=input.nextLine();
                        System.out.println("publisher email");
                        String publish_em=input.nextLine();
                        stmt="INSERT INTO PUBLISHER (PUBLISHERNAME, PUBLISHERADDRESS, PUBLISHERPHONE, PUBLISHEREMAIL) VALUES (?,?,?,?)";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, publisher);
                        pstmt.setString(2, publihser_add);
                        pstmt.setString(3, publish_phone);
                        pstmt.setString(4, publish_em);
                        pstmt.executeUpdate();
                        System.out.println("publisher to replace");
                        String old_pub=input.nextLine();
                        stmt ="select publishername FROM publisher WHERE publisherName = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, old_pub);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your publisher name does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            old_pub=input.nextLine();
                            if(old_pub.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="select publishername FROM publisher WHERE publisherName = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,old_pub);
                            result=pstmt.executeQuery();
                        }
                        if(err!=-1){
                            stmt="UPDATE BOOK " +
                                "SET publishername = ? where PUBLISHERNAME = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1, publisher);
                            pstmt.setString(2, old_pub);
                            pstmt.executeUpdate();
                            System.out.println("Publisher updated:");
                            stmt="Select GROUPNAME, BOOKTITLE, PUBLISHERNAME, YEARPUBLISHED,NUMBERPAGES FROM Book WHERE publisherName = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1, publisher);
                            result=pstmt.executeQuery();
                            while(result.next()){
                                String groupname=result.getString(1);
                                String booktitle=result.getString(2);
                                String publisherName=result.getString(3);
                                Integer year=result.getInt(4);
                                Integer pages=result.getInt(5);
                                System.out.format("Writing Group Name: %s | Book Title: %s | Publisher Name: %s "
                                    + "| Year Published: %s | Page Number: %s\n",groupname,booktitle,publisherName,year,pages+"\n");
                            }
                        }
                    }
                    break;

                /*Remove a book specified by the user*/
                case "9":
                    err=0;
                    stmt="Select BookTitle, GROUPNAME From Book";
                    pstmt=con.prepareStatement(stmt);
                    result=pstmt.executeQuery();
                    System.out.println("All the book titles and writing group:");
                    while(result.next())
                    {
                        String userData="* Title:"+result.getString(1)+"| Writing Group:"+result.getString(2);
                        System.out.println(userData+"\n");
                    }
                    System.out.println("What is the name of the book you would like to Remove?");
                    bookTitle = input.nextLine();
                    stmt="SELECT bookTitle from book where bookTitle = ?";
                    pstmt=con.prepareStatement(stmt);
                    pstmt.setString(1,bookTitle);
                    result=pstmt.executeQuery();
                    while(result.next()==false){
                        String error="\nError: Your book name does not exist in the database. Please enter again or type -1 to exit\n";
                        System.out.println(error);
                        bookTitle=input.nextLine();
                        if(bookTitle.equals("-1")){
                            err=-1;
                            break;
                        }
                        stmt="SELECT bookTitle from book where bookTitle = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        result=pstmt.executeQuery();
                    }
                    if(err!=-1){
                        System.out.println("The Names of all Writing Group:");
                        stmt="SELECT GROUPNAME FROM WritingGroup";
                        pstmt=con.prepareStatement(stmt);
                        result=pstmt.executeQuery();
                        while(result.next())
                        {
                            String userData="*"+result.getString(1);
                            System.out.println(userData+"\n");
                        }
                        System.out.println("What is the group name for that book?");
                        nameIn = input.nextLine();
                        stmt="SELECT writinggroup.groupname from writinggroup where groupname=?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,nameIn);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your group name does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            nameIn=input.nextLine();
                            if(nameIn.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT writinggroup.groupname from writinggroup where groupname=?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,nameIn);
                            result=pstmt.executeQuery();
                        }
                    }
                    if(err!=-1){
                        stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1,bookTitle);
                        pstmt.setString(2,nameIn);
                        result=pstmt.executeQuery();
                        while(result.next()==false){
                            String error="\nError: Your book "+bookTitle+" written by "+nameIn+" does not exist in the database. Please enter again or type -1 to exit\n";
                            System.out.println(error);
                            System.out.println("What is the name of the book you would like to insert?");
                            bookTitle = input.nextLine();
                            if(bookTitle.equals("-1")){
                                err=-1;
                                break;
                            }
                            System.out.println("What is the group name for that book?");
                            nameIn = input.nextLine();
                            if(nameIn.equals("-1")){
                                err=-1;
                                break;
                            }
                            stmt="SELECT BookTitle,GROUPNAME FROM Book WHERE BookTitle = ? AND GROUPNAME = ?";
                            pstmt=con.prepareStatement(stmt);
                            pstmt.setString(1,bookTitle);
                            pstmt.setString(2,nameIn);
                            result=pstmt.executeQuery();
                        }
                    }
                    if(err!=-1){
                        stmt="Delete From Book Where groupname = ? AND booktitle = ?";
                        pstmt=con.prepareStatement(stmt);
                        pstmt.setString(1, nameIn);
                        pstmt.setString(2, bookTitle);
                        pstmt.executeUpdate();
                        System.out.println("Book "+bookTitle+", written by "+nameIn+" was deleted!!!\n");
                        System.out.println("The remaining books:\n");
                        stmt="Select GROUPNAME, BOOKTITLE, PUBLISHERNAME, YEARPUBLISHED,NUMBERPAGES FROM Book";
                        pstmt=con.prepareStatement(stmt);
                        result=pstmt.executeQuery();
                        while(result.next()){
                            String groupname=result.getString(1);
                            String booktitle=result.getString(2);
                            String publisherName=result.getString(3);
                            Integer year=result.getInt(4);
                            Integer pages=result.getInt(5);
                            System.out.format("Writing Group Name: %s | Book Title: %s | Publisher Name: %s "
                                + "| Year Published: %s | Page Number: %s\n",groupname,booktitle,publisherName,year,pages+"\n");
                        }
                    }
                    break;

                case "10":
                    stillChoosing = false;
                    break;

                default:
                    System.out.println("\nError: Invalid choice. Please try again\n");
                    break;
            }

        } while ( stillChoosing == true );
        if(pstmt!=null){
            pstmt.close();
        }
        con.close();
        input.close();
    }
}

