package ShutterServer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.Optional;


public class Controller {



    @FXML
    private Label lbl_Serverip;
    @FXML
    private Label lbl_Servername;
    @FXML
    private Label lbl_Serverport;
    @FXML
    private Label lbl_Serverstatus;
    @FXML
    private Label lbl_srvmsg;
    @FXML
    private Button btn_starteServer;
    @FXML
    private Button btn_stoppeServer;
    @FXML
    private TextArea ta_srvlog;
    @FXML
    private Label lbl_shutterpos;
    @FXML
    private Label lbl_desiredshutterpos;

    public static PrintStream ps;

    public Shutter shutter1 = null;



    public void BTNServerStarten(ActionEvent event) throws IOException {
       if(shutter1 == null){
            shutter1 = new Shutter();
        }


        ps = new PrintStream(new OutputStream() {

            @Override
            public void write(int i) throws IOException {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ta_srvlog.appendText(String.valueOf((char) i));
                    }
                });
            }
        });
        System.setOut(ps);

        /*Server wird gestartet*/

        lbl_srvmsg.setText(shutter1.startServer());
        lbl_Serverip.setText(shutter1.getServerIP());
        lbl_Servername.setText(shutter1.shuttername);
        lbl_Serverstatus.setText(shutter1.serverstatus);


        lbl_shutterpos.textProperty().bind(shutter1.ShutterPosition);
        lbl_desiredshutterpos.textProperty().bind(shutter1.desiredShutterPosition);

        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(shutter1.serverport);
        String srvport = sb.toString();

        lbl_Serverport.setText(srvport);

        if(lbl_Serverstatus.getText() == "Gestartet"){
            btn_starteServer.setDisable(true);
            btn_stoppeServer.setDisable(false);
        }
    }

    public void BTNServerStoppen(ActionEvent event){
        lbl_srvmsg.setText(shutter1.stopServer());
        lbl_Serverstatus.setText(shutter1.serverstatus);

        if (lbl_Serverstatus.getText() == "Gestoppt"){
            btn_stoppeServer.setDisable(true);
            btn_starteServer.setDisable(false);
        }
    }
}
