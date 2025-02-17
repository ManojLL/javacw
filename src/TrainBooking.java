import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/***
 *Student name : Manoj laksjan
 * UOW ID : w1761261
 * IIT ID : 2019274
 * DAte : 7/03/2020
 */
public class TrainBooking extends Application {
    final static int SEAT_CAPACITY = 42;

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        //
        //this array contain up trip user details
        //user seat number , user name, address, contact number, email , id number
        //
        String[][] upUserDetails = new String[SEAT_CAPACITY][6];
        //this array contains down (badulla to colombo) trip dtails
        String[][] downUserDetails = new String[SEAT_CAPACITY][6];

        //
        //in this 2d array
        //1st row contain seat numebers of trip from colombo to badulla
        //2st row contain seat numebers of trip from badulla to clombo
        //
        int[][] seats = new int[2][SEAT_CAPACITY];

        //time formatter
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        //get current date
        Calendar calendar = Calendar.getInstance();

        //booking date (day after tomorrow)
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date dayAfTomo = calendar.getTime();

        //formatting date yyyy/mm/dd
        String bookingtDate = dt1.format(dayAfTomo);

        Scanner sc = new Scanner(System.in);
        menu:
        while (true) {
            System.out.println("***************************************************************************");
            System.out.println("============= DENUWARA MANIKE A/C COMPARTMENT SEAT BOOKING ================");
            System.out.println("***************************************************************************\n");
            System.out.println("\"A\" Add customer seat ");
            System.out.println("\"V\" View all seats ");
            System.out.println("\"E\" View empty seat ");
            System.out.println("\"D\" Delete customer from seat ");
            System.out.println("\"F\" Find the seat for given customer name ");
            System.out.println("\"S\" Store data ");
            System.out.println("\"L\" Load data ");
            System.out.println("\"O\" View seats ordered alphabetically by name ");
            System.out.println("\"Q\" exit \n");

            System.out.print("Enter your option : ");
            //option contain what the user chose and make the input to lower case
            String option = sc.next().toLowerCase();

            //use the switch case to call other method according to user choice
            switch (option) {
                case "a":
                    addSeat(upUserDetails, downUserDetails, seats, bookingtDate);
                    break;
                case "v":
                    viewAll(seats, bookingtDate);
                    break;
                case "e":
                    showEmpty(seats, bookingtDate);
                    break;
                case "d":
                    deleteUser(sc, seats, upUserDetails, downUserDetails);
                    break;
                case "f":
                    findSeat(sc, upUserDetails, downUserDetails);
                    break;
                case "s":
                    saveToFile(upUserDetails, downUserDetails, bookingtDate);
                    break;
                case "l":
                    loadArray(upUserDetails, downUserDetails, bookingtDate, seats);
                    break;
                case "o":
                    orderByAlphabeticOrde(sc, upUserDetails, downUserDetails);
                    break;
                case "q":
                    break menu;
                default:
                    System.out.println("Please enter correct input!!");
            }
        }

    }

    //add a set to customer
    private void addSeat(String[][] uDetails, String[][] dudetails, int[][] seats, String tomorrow) {
        Stage addStage = new Stage();
        AnchorPane mainPane = new AnchorPane();
        Label lName = new Label("DUNUWARA MANIKE \nA/C SEAT ");
        mainPane.getChildren().addAll(lName);
        lName.setStyle("-fx-text-fill: #4c4fd4;-fx-font-size: 30px;-fx-font-family:'Abyssinica SIL';");
        lName.setPadding(new Insets(20));
        lName.setLayoutX(205);

        Label date = new Label("Book seat for " + tomorrow);
        date.setLayoutX(250);
        date.setLayoutY(150);
        date.setStyle("-fx-font-size: 20px");

        Button badullaBtn = new Button("Book seat To Badulla");
        Button colomboBtn = new Button("Book seat to Colombo");
        Button closeStage = new Button("Go to menu");

        mainPane.getChildren().addAll(badullaBtn, colomboBtn, closeStage, date);

        badullaBtn.setLayoutX(320);
        colomboBtn.setLayoutX(320);
        badullaBtn.setLayoutY(300);
        colomboBtn.setLayoutY(350);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(450);

        Scene scene = new Scene(mainPane, 750, 650);
        addStage.setScene(scene);
        //up trip and down trip selection goes here
        badullaBtn.setOnAction(e -> toBadulla(addStage, uDetails, dudetails, seats, scene));
        colomboBtn.setOnAction(e -> toColombo(addStage, uDetails, dudetails, seats, scene));

        closeStage.setOnAction(e -> addStage.close());

        addStage.showAndWait();
    }

    //book seat colombo to badulla
    private void toBadulla(Stage addStage, String[][] uDetails, String[][] dUser, int[][] seats, Scene scn) {
        int x = 1;//this is use for grt to know what user chosed
        //
        //when wokingg with x =1
        //use seat[0][<~>]
        //and use upUserDetails array to save user data
        //
        BorderPane root = new BorderPane();

        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();

        root.setCenter(pane);
        root.setBottom(flowpane);

        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);

        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));
//add pane to form
        setPane(addStage, pane, scn, x, seats, dUser, uDetails);

        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[0][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");

            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-min-width: 80px");
                button.setVisible(false);

            }
        }
        Scene scene = new Scene(root, 750, 650);
        addStage.setScene(scene);

    }

    //book seat badulla to colombo
    private void toColombo(Stage addStage, String[][] uDetail, String[][] dUser, int[][] seats, Scene scn) {
        int x = 2;//this is use for grt to know what user chosed
        //
        //when wokingg with x =1
        //use seat[1][<~>]
        //and use downUserDetails array to save user data
        //
        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();
        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);
        root.setCenter(pane);
        root.setBottom(flowpane);
        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));

        //add pane to from
        setPane(addStage, pane, scn, x, seats, dUser, uDetail);

        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[1][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");
            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-min-width: 80px");
                button.setVisible(false);

            }
        }
        Scene scene = new Scene(root, 750, 650);
        addStage.setScene(scene);
    }


    private void setPane(Stage addStage, Pane pane, Scene scn, int x, int[][] seats, String[][] dUser, String[][] uDetails) {
//add labels here
        Label name = new Label("Name*: ");
        Label address = new Label("Address* :");
        Label contact = new Label("Contact(TP)* :");
        Label email = new Label("Email* :");
        Label idNum = new Label("NIC* :");
        Label slSeat = new Label("Select A seat* :");
        Label available = new Label("Available seats");


        //add text filelds to get data from user
        TextField nameText = new TextField("");
        TextField addressText = new TextField("");
        TextField contactText = new TextField("");
        TextField emailText = new TextField("");
        TextField idText = new TextField("");
        Button backB = new Button("back");
        Button closeStage = new Button("Go to menu");
        Button booking = new Button("booking");


        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        choiceBox.setStyle("-fx-min-width: 50px;");
        if (x == 1) {
            for (int i = 1; i <= SEAT_CAPACITY; i++) {
                if (seats[0][i - 1] == 0) {
                    choiceBox.getItems().add(String.valueOf(i));
                }
            }
        } else {
            for (int i = 1; i <= SEAT_CAPACITY; i++) {
                if (seats[1][i - 1] == 0) {
                    choiceBox.getItems().add(String.valueOf(i));
                }
            }
        }
        pane.getChildren().addAll(idText, idNum, available, slSeat, name, address, contact, nameText, addressText, contactText, email, emailText, backB, closeStage, booking, choiceBox);
        available.setLayoutX(40);
        available.setLayoutY(375);
        name.setLayoutX(10);
        name.setLayoutY(30);

        idNum.setLayoutX(10);
        idNum.setLayoutY(60);

        address.setLayoutX(10);
        address.setLayoutY(90);

        email.setLayoutX(10);
        email.setLayoutY(150);

        contact.setLayoutX(10);
        contact.setLayoutY(120);

        nameText.setLayoutX(130);
        nameText.setLayoutY(30);

        idText.setLayoutX(130);
        idText.setLayoutY(60);

        addressText.setLayoutX(130);
        addressText.setLayoutY(90);

        contactText.setLayoutX(130);
        contactText.setLayoutY(120);

        emailText.setLayoutX(130);
        emailText.setLayoutY(150);

        choiceBox.setLayoutX(170);
        choiceBox.setLayoutY(200);
        choiceBox.setStyle("-fx-pref-width: 180");

        backB.setLayoutX(40);
        backB.setLayoutY(300);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(300);

        slSeat.setLayoutX(50);
        slSeat.setLayoutY(200);

        booking.setLayoutX(350);
        booking.setLayoutY(300);

        backB.setOnAction(e -> addStage.setScene(scn));

        closeStage.setOnAction(e -> addStage.close());
        booking.setOnAction(e -> {
            String uName = nameText.getText();
            String uCn = contactText.getText();
            String uAddress = addressText.getText();
            String uEmail = emailText.getText();
            String sNum = choiceBox.getValue();
            String id = idText.getText();

            if (x == 1) {
                sendToArrays(scn, seats, dUser, uName, uAddress, uCn, uEmail, sNum, uDetails, x, nameText, addressText, contactText, emailText, choiceBox, addStage, idText, id);

            } else {
                sendToArrays(scn, seats, dUser, uName, uAddress, uCn, uEmail, sNum, uDetails, x, nameText, addressText, contactText, emailText, choiceBox, addStage, idText, id);
            }
        });

    }

    //set data array
    private void sendToArrays(Scene scn, int[][] seats, String[][] dUser, String uName, String uAddress, String uCn, String uEmail, String sNum, String[][] uDetails, int x, TextField n, TextField ad, TextField c, TextField e, ChoiceBox<String> choiceBox, Stage stage, TextField idT, String idNum) {
        if (uName.equals("") || uAddress.equals("") || uCn.equals("") || uEmail.equals("") || sNum == null || idNum.equals("")) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("fill all ");
            a.show();
        }
        if (!uCn.matches("[0-9]+" ) ) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Fill contact number correctly only using numbers ");
            a.show();

        } else {
            if (x == 1) {
                showAlert(uName, sNum);
                seats[0][Integer.parseInt(sNum) - 1] = Integer.parseInt(sNum); //add seat
                uDetails[Integer.parseInt(sNum) - 1][0] = sNum; //add seat to upuserdetiails
                uDetails[Integer.parseInt(sNum) - 1][1] = uName; //add seat to upuserdetiails
                uDetails[Integer.parseInt(sNum) - 1][2] = uAddress;//add seat to upuserdetiails
                uDetails[Integer.parseInt(sNum) - 1][3] = uCn;//add seat to upuserdetiails
                uDetails[Integer.parseInt(sNum) - 1][4] = uEmail;//add seat to upuserdetiails
                uDetails[Integer.parseInt(sNum) - 1][5] = idNum;//add seat to upuserdetiails
                resetText(n, ad, c, e, choiceBox, idT);
                stage.setScene(scn);
            } else {
                showAlert(uName, sNum);
                seats[1][Integer.parseInt(sNum) - 1] = Integer.parseInt(sNum);
                dUser[Integer.parseInt(sNum) - 1][0] = sNum;//add seat to upuserdetiails
                dUser[Integer.parseInt(sNum) - 1][1] = uName;//add seat to upuserdetiails
                dUser[Integer.parseInt(sNum) - 1][2] = uAddress;//add seat to upuserdetiails
                dUser[Integer.parseInt(sNum) - 1][3] = uCn;//add seat to upuserdetiails
                dUser[Integer.parseInt(sNum) - 1][4] = uEmail;//add seat to upuserdetiails
                dUser[Integer.parseInt(sNum) - 1][5] = idNum;//add seat to upuserdetiails
                resetText(n, ad, c, e, choiceBox, idT);
                stage.setScene(scn);
            }
        }
    }

    private void showAlert(String name, String sn) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setContentText(sn + " seat booked to " + name);
        a.showAndWait();
    }

    private void resetText(TextField n, TextField ad, TextField c, TextField e, ChoiceBox<String> choiceBox, TextField id) {
        n.setText("");
        ad.setText("");
        c.setText("");
        e.setText("");
        id.setText("");
        choiceBox.setValue(null);
    }

    //
    //view all seats
    //@param seats
    //
    private void viewAll(int[][] seats, String tomorrow) {
        Stage addStage = new Stage();
        AnchorPane mainPane = new AnchorPane();
        Label lName = new Label("DUNUWARA MANIKE \n");
        mainPane.getChildren().addAll(lName);
        lName.setStyle("-fx-text-fill: #4c4fd4;-fx-font-size: 30px;-fx-font-family:'Abyssinica SIL';");
        lName.setPadding(new Insets(20));
        lName.setLayoutX(205);
        Label date = new Label(" seats on " + tomorrow);
        date.setLayoutX(250);
        date.setLayoutY(150);
        date.setStyle("-fx-font-size: 20px");

        Button badullaBtn = new Button("View all seats To Badulla");
        Button colomboBtn = new Button("View all seats to Colombo");
        Button closeStage = new Button("Go to menu");

        mainPane.getChildren().addAll(badullaBtn, colomboBtn, closeStage, date);

        badullaBtn.setLayoutX(320);
        colomboBtn.setLayoutX(320);
        badullaBtn.setLayoutY(300);
        colomboBtn.setLayoutY(350);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(450);

        Scene scene = new Scene(mainPane, 750, 650);
        addStage.setScene(scene);
        //up trip and down trip selection goes here
        badullaBtn.setOnAction(e -> showAllToBadulla(addStage, seats, scene));
        colomboBtn.setOnAction(e -> showAllToColombo(addStage, seats, scene));
        closeStage.setOnAction(e -> addStage.close());

        addStage.showAndWait();
    }

    private void showAllToBadulla(Stage addStage, int[][] seats, Scene scn) {
        //
        // when wokingg with x =1
        // use seat[0][<~>]
        // and use downUserDetails array to save user data
        //
        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();
        pane.setMaxHeight(400);
        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);
        root.setCenter(pane);
        root.setBottom(flowpane);
        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));

        Label topic = new Label("All seats  in dumbara manike train from Colombo to Badulla");
        pane.getChildren().add(topic);
        topic.setLayoutX(30);
        topic.setLayoutY(20);
        //add pane to from
        //setPane(addStage, pane, scn, x, seats, dUser, uDetails);
        setPaneView(addStage, pane, scn);


        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[0][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");
            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-background-color: #ff622e;-fx-min-width: 80px");
            }
        }
        Scene scene = new Scene(root, 750, 650);
        addStage.setScene(scene);
    }

    private void showAllToColombo(Stage addStage, int[][] seats, Scene scn) {
        //
        //when wokingg with x =1
        //use seat[1][<~>]
        //and use downUserDetails array to save user data
        //
        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();
        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);
        root.setTop(pane);
        root.setBottom(flowpane);
        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));

        Label topic = new Label("All seats  in dumbara manike train from badulla to colommbo ");
        pane.getChildren().add(topic);
        topic.setLayoutX(30);
        topic.setLayoutY(20);

        //add pane to from
        //setPane(addStage, pane, scn, x, seats, dUser, uDetails);
        setPaneView(addStage, pane, scn);

        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[1][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");
            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-background-color: #ff622e;-fx-min-width: 80px");

            }
        }
        Scene scene = new Scene(root, 750, 650);
        addStage.setScene(scene);
    }

    private void setPaneView(Stage addStage, Pane pane, Scene scn) {
        Label avBtn = new Label("Available seats");
        Label bBtn = new Label("Booked seats");

        //buttons
        Button backB = new Button("back");
        Button closeStage = new Button("Go to menu");
        Button bookedS = new Button();
        Button avS = new Button();


        pane.getChildren().addAll(avBtn, bBtn, backB, closeStage, bookedS, avS);

        avBtn.setLayoutX(30);
        avBtn.setLayoutY(60);

        bookedS.setLayoutX(150);
        bookedS.setLayoutY(60);
        bookedS.setStyle("-fx-background-color: #98caff; ");

        bBtn.setLayoutX(30);
        bBtn.setLayoutY(100);
        avS.setStyle("-fx-background-color: #ff622e; ");

        avS.setLayoutX(150);
        avS.setLayoutY(100);

        backB.setLayoutX(40);
        backB.setLayoutY(300);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(300);

        backB.setOnAction(e -> addStage.setScene(scn));

        closeStage.setOnAction(e -> addStage.close());
    }

    private void showEmpty(int[][] seats, String tomorrow) {
        Stage addStage = new Stage();
        AnchorPane mainPane = new AnchorPane();
        Label lName = new Label("DENUWARA MANIKE \n");
        mainPane.getChildren().addAll(lName);
        lName.setStyle("-fx-text-fill: #4c4fd4;-fx-font-size: 30px;-fx-font-family:'Abyssinica SIL';");
        lName.setPadding(new Insets(20));
        lName.setLayoutX(205);

        Label date = new Label("Empty seats on " + tomorrow);
        date.setLayoutX(250);
        date.setLayoutY(150);
        date.setStyle("-fx-font-size: 20px");

        Button badullaBtn = new Button("View empty seats To Badulla");
        Button colomboBtn = new Button("View empty seats to Colombo");
        Button closeStage = new Button("Go to menu");

        mainPane.getChildren().addAll(badullaBtn, colomboBtn, closeStage, date);

        badullaBtn.setLayoutX(320);
        colomboBtn.setLayoutX(320);
        badullaBtn.setLayoutY(300);
        colomboBtn.setLayoutY(350);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(450);

        Scene scene = new Scene(mainPane, 750, 650);
        addStage.setScene(scene);
        //up trip and down trip selection goes here
        badullaBtn.setOnAction(e -> {
            showEmptyToBadulla(addStage, seats, scene);
        });

        colomboBtn.setOnAction(e -> {
            showEmptyToColombo(addStage, seats, scene);
        });
        closeStage.setOnAction(e -> {
            addStage.close();
        });

        addStage.showAndWait();
    }

    private void showEmptyToColombo(Stage addStage, int[][] seats, Scene scn) {
        /***
         * when wokingg with x =1
         * use seat[1][<~>]
         * and use downUserDetails array to save user data
         */
        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();
        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);
        root.setTop(pane);
        root.setBottom(flowpane);
        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));

        Label topic = new Label("Empty seats  in dumbara manike train from badulla to colommbo ");
        pane.getChildren().add(topic);
        topic.setLayoutX(30);
        topic.setLayoutY(20);

        Button backB = new Button("back");
        Button closeStage = new Button("Go to menu");
        pane.getChildren().addAll(backB, closeStage);

        backB.setLayoutX(40);
        backB.setLayoutY(100);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(100);

        backB.setOnAction(e -> {
            addStage.setScene(scn);
        });

        closeStage.setOnAction(e -> {
            addStage.close();
        });

        //add pane to from
        //setPane(addStage, pane, scn, x, seats, dUser, uDetails);
        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[1][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");
            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-min-width: 80px");
                button.setVisible(false);

            }
        }
        Scene scene = new Scene(root, 750, 400);
        addStage.setScene(scene);
    }

    private void showEmptyToBadulla(Stage addStage, int[][] seats, Scene scn) {
        /***
         * when wokingg with x =1
         * use seat[1][<~>]
         * and use downUserDetails array to save user data
         */
        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        FlowPane flowpane = new FlowPane();
        flowpane.setMaxWidth(650);
        flowpane.setMaxHeight(500);
        root.setTop(pane);
        root.setBottom(flowpane);
        flowpane.setVgap(8);
        flowpane.setHgap(8);
        flowpane.setPadding(new Insets(10, 0, 15, 100));

        Label topic = new Label("Empty seats  in dumbara manike train from badulla to colommbo ");
        pane.getChildren().add(topic);
        topic.setLayoutX(30);
        topic.setLayoutY(20);

        Button backB = new Button("back");
        Button closeStage = new Button("Go to menu");
        pane.getChildren().addAll(backB, closeStage);

        backB.setLayoutX(40);
        backB.setLayoutY(100);

        closeStage.setLayoutX(100);
        closeStage.setLayoutY(100);

        backB.setOnAction(e -> {
            addStage.setScene(scn);
        });

        closeStage.setOnAction(e -> {
            addStage.close();
        });

        //add pane to from
        //setPane(addStage, pane, scn, x, seats, dUser, uDetails);
        for (int i = 1; i <= SEAT_CAPACITY; i++) {
            if (seats[0][i - 1] == 0) {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                //set Style for buttons
                button.setStyle("-fx-background-color: #98caff;-fx-min-width: 80px");
            } else {
                Button button = new Button(String.valueOf(i));
                flowpane.getChildren().add(button);
                button.setStyle("-fx-min-width: 80px");
                button.setVisible(false);
            }
        }
        Scene scene = new Scene(root, 750, 400);
        addStage.setScene(scene);
    }

    //delete customer
    private void deleteUser(Scanner sc, int[][] seats, String[][] up, String[][] down) {
        System.out.println("\n============================================================================================");
        System.out.println("------------------------------------- DELETE USER --------------------------------------");
        System.out.println("============================================================================================\n");
        delete:
        while (true) {
            //
            //if use 1 use up int 2d array
            //if use 2 use down int 2d array
            //
            System.out.println("\" 1\" delete customer to Badulla");
            System.out.println("\" 2\" delete customer to Colombo");
            System.out.println("\" Q\" Go to main menu");
            System.out.print("Enter your option : ");
            String option = sc.next().toLowerCase();
            switch (option) {
                case "1":
                    System.out.println("****************\nDelete customer colombo to Badull\n***********");
                    System.out.print("Enter seat number to delete : ");
                    try {
                        int sNumber = sc.nextInt();
                        if (sNumber <= SEAT_CAPACITY && sNumber >= 1) {
                            if (seats[0][sNumber - 1] != 0) {
                                seats[0][sNumber - 1] = 0;
                                up[sNumber - 1][0] = null;
                                up[sNumber - 1][1] = null;
                                up[sNumber - 1][2] = null;
                                up[sNumber - 1][3] = null;
                                up[sNumber - 1][4] = null;
                                System.out.println("delete " + sNumber + " seat successfully!");
                            } else {
                                System.out.println(sNumber + " seat hasn't booked!!!");
                            }
                        } else {
                            System.out.println("enter seat number between 1 - " + SEAT_CAPACITY);
                        }
                    } catch (Exception error) {
                        System.out.println("please use seat number");
                    }
                    break;

                case "2":
                    System.out.println("****************\nDelete customer Badulla to Colombo\n***********");
                    System.out.print("Enter seat number to delete : ");
                    try {
                        int sNumber = sc.nextInt();
                        if (sNumber <= SEAT_CAPACITY && sNumber >= 1) {
                            if (seats[1][sNumber - 1] != 0) {
                                seats[1][sNumber - 1] = 0;
                                down[sNumber - 1][0] = null;
                                down[sNumber - 1][1] = null;
                                down[sNumber - 1][2] = null;
                                down[sNumber - 1][3] = null;
                                down[sNumber - 1][4] = null;
                                System.out.println("delete " + sNumber + " seat successfully!");
                            } else {
                                System.out.println(sNumber + " seat hasn't booked!!!");
                            }
                        } else {
                            System.out.println("enter seat number between 1 - " + SEAT_CAPACITY);
                        }
                    } catch (Exception error) {
                        System.out.println("please use seat number");
                    }
                    break;
                case "q":
                    break delete;
                default:
                    System.out.println(" check and enter correct inputs");

            }
        }
    }

    /***
     * find seat according to user name
     */
    private void findSeat(Scanner sc, String[][] up, String[][] down) {
        System.out.println("\n===================================================================================");
        System.out.println("-------------------------- FIND A SEAT -----------------------------");
        System.out.println("===================================================================================\n");
        find:
        while (true) {
            System.out.println("\" 1\" find customer in colombo to Badulla ");
            System.out.println("\" 2\" find customer in badulla to Colombo");
            System.out.println("\" Q\" Go to main menu");
            System.out.print("Enter your option : ");
            String option = sc.next().toLowerCase();
            switch (option) {
                case "1":
                    System.out.println("****************\nfind customer colombo to Badull\n***********");
                    System.out.print("Enter customer name to find : ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    boolean find = false;
                    for (int x = 0; x < SEAT_CAPACITY; x++) {
                        if (name.equalsIgnoreCase(up[x][1])) {
                            System.out.println("\n" + up[x][1] + " - seat number : " + up[x][0] + "\n");
                            find = true;
                        }

                    }
                    if (!find) {
                        System.out.println(name + " hasn't book a seat");
                    }
                    break;
                case "2":
                    System.out.println("****************\nfind customer Badull to Colombo\n***********");
                    System.out.print("Enter customer name to find : ");
                    sc.nextLine();
                    String name1 = sc.nextLine();
                    boolean findDown = false;
                    for (int x = 0; x < SEAT_CAPACITY; x++) {
                        if (name1.equalsIgnoreCase(down[x][1])) {
                            System.out.println();
                            System.out.println("\n" + down[x][1] + " - seat number : " + down[x][0] + "\n");
                            System.out.println();
                            findDown = true;
                        }

                    }
                    if (!findDown) {
                        System.out.println(name1 + " hasn't book a seat");
                    }

                    break;
                case "q":
                    break find;
                default:
                    System.out.println("check and enter correct input!!");
            }
        }
    }

    //order name alpbticly order
    private void orderByAlphabeticOrde(Scanner sc, String[][] up, String[][] down) {
        System.out.println("\n===================================================================================");
        System.out.println("-------------------------- FIND A SEAT -----------------------------");
        System.out.println("===================================================================================\n");


        order:
        while (true) {
            System.out.println("\" 1\" order customer in colombo to Badulla ");
            System.out.println("\" 2\" order customer in badulla to Colombo");
            System.out.println("\" Q\" Go to main menu");
            System.out.print("Enter your option : ");
            String option = sc.next().toLowerCase();
            switch (option) {
                case "1":
                    ArrayList<String> name = new ArrayList<>();
                    ArrayList<String> seats = new ArrayList<>();
                    putToArrays(up, name, seats);

                    int lenthName = name.size();
                    sorting(name, seats, lenthName);
                    showOrder(name, seats, lenthName);
                    break;
                case "2":
                    ArrayList<String> name1 = new ArrayList<>();
                    ArrayList<String> seats2 = new ArrayList<>();
                    putToArrays(down, name1, seats2);
                    int lenthName1 = name1.size();
                    sorting(name1, seats2, lenthName1);
                    showOrder(name1, seats2, lenthName1);
                    break;
                case "q":
                    break order;
                default:
                    System.out.println("check and enter correct input!!");
            }
        }
    }

    private void putToArrays(String[][] userDetails, ArrayList<String> name, ArrayList<String> seat) {
        for (int i = 0; i < SEAT_CAPACITY; i++) {
            if (userDetails[i][1] != null) {
                name.add(userDetails[i][1]);
                seat.add(userDetails[i][0]);
            }
        }
    }

    private void sorting(ArrayList<String> name, ArrayList<String> seat, int n) {
        String temp;
        String tempSnum;
        for (int j = 0; j < n - 1; j++) {
            for (int i = j + 1; i < n; i++) {
                if (name.get(j).compareToIgnoreCase(name.get(i)) > 0) {
                    temp = name.get(j);
                    name.set(j, name.get(i));
                    name.set(i, temp);

                    tempSnum = seat.get(j);
                    seat.set(j, seat.get(i));
                    seat.set(i, tempSnum);

                }
            }
        }
    }

    private void showOrder(ArrayList<String> name, ArrayList<String> seats, int n) {
        System.out.println("\nOder by Alphabetically order\n");
        for (int i = 0; i < n; i++) {
            System.out.println(name.get(i) + " - " + seats.get(i));
        }
    }

    //save to txt file
    private void saveToFile(String[][] upDetails, String[][] downDetails, String bDate) throws IOException {
        File file1 = new File("/home/manoj/IdeaProjects/pp2as/src/tripToBadulla.txt");
        File file2 = new File("/home/manoj/IdeaProjects/pp2as/src/tripToColombo.txt");
        saveToText(file1, upDetails, bDate);
        saveToText(file2, downDetails, bDate);
    }

    private void saveToText(File file, String[][] userDetails, String bDate) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        for (int i = 0; i < SEAT_CAPACITY; i++) {
            if (userDetails[i][0] != null) {
                bw.write(userDetails[i][0] + "-" + userDetails[i][1] + "-" + userDetails[i][2] + "-" + userDetails[i][3] + "-" + userDetails[i][4] + "-" + userDetails[i][5] + "-" + bDate + "\n");
            }
        }
        bw.close();
    }

    //load data fro files
    private void loadArray(String[][] upDetails, String[][] downDetails, String bDate, int[][] seat) throws IOException {
        try {
            File file1 = new File("/home/manoj/IdeaProjects/pp2as/src/tripToBadulla.txt");
            File file2 = new File("/home/manoj/IdeaProjects/pp2as/src/tripToColombo.txt");
            readFiles(file1, upDetails, downDetails, bDate, seat, 1);
            readFiles(file2, upDetails, downDetails, bDate, seat, 2);
        } catch (IOException e) {
            System.out.println("cant find file");
        }
    }

    //reading files
    private void readFiles(File file, String[][] up, String[][] down, String bDate, int[][] seat, int x) throws IOException {
        try {
            BufferedReader bw = new BufferedReader(new FileReader(file));
            String st;
            while ((st = bw.readLine()) != null) {
                slipRead(st, up, down, bDate, seat, x);
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("cant find file!!");
        }
    }

    /***
     * this method use to split read line and ssend that data to arrays
     * @param line
     * @param up
     * @param down
     * @param bDate
     * @param seat
     * @param x
     */
    private void slipRead(String line, String[][] up, String[][] down, String bDate, int[][] seat, int x) {
        try {
            if (x == 1) {
                String[] details = line.split("-");
                if (details[6].equals(bDate)) {
                    up[Integer.parseInt(details[0]) - 1][0] = details[0];
                    up[Integer.parseInt(details[0]) - 1][1] = details[1];
                    up[Integer.parseInt(details[0]) - 1][2] = details[2];
                    up[Integer.parseInt(details[0]) - 1][3] = details[3];
                    up[Integer.parseInt(details[0]) - 1][4] = details[4];
                    up[Integer.parseInt(details[0]) - 1][5] = details[5];

                    seat[0][Integer.parseInt(details[0]) - 1] = Integer.parseInt(details[0]);
                }
            } else {
                String[] details = line.split("-");
                if (details[6].equals(bDate)) {
                    down[Integer.parseInt(details[0]) - 1][0] = details[0];
                    down[Integer.parseInt(details[0]) - 1][1] = details[1];
                    down[Integer.parseInt(details[0]) - 1][2] = details[2];
                    down[Integer.parseInt(details[0]) - 1][3] = details[3];
                    down[Integer.parseInt(details[0]) - 1][4] = details[4];
                    down[Integer.parseInt(details[0]) - 1][5] = details[5];

                    seat[1][Integer.parseInt(details[0]) - 1] = Integer.parseInt(details[0]);
                }
            }
        } catch (Exception e) {
            System.out.println("SOme thing went wrong!!!");
        }
    }
}