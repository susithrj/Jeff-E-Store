package sample;

import emailService.Email;
import emailService.GroupEmail;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.show();
    }

    public static void main(String[] args)  {
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(new Runnable() {//using a delayed timed function to send newsletter
            @Override
            public void run() {
                Date today = new Date();
                LocalDate localDate = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int day   = localDate.getDayOfMonth();//getting the day
                if(day == 1){
                    try {
                        Email email = new GroupEmail(GroupEmail.getMessage());//creating email object
                        email.sendEmail();//sending email
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, 0, 24, TimeUnit.HOURS);
        launch(args);
    }
}
