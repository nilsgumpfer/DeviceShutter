package ShutterServer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
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
    private Label lbl_heizungtemp;
    @FXML
    private Button btn_setTemp;

    public static PrintStream ps;

    public Shutter shutter1 = null;



    public void BTNServerStarten(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Name des Shutter definieren");
        dialog.setHeaderText("Shutter anlegen");
        dialog.setContentText("Bitte diesem Shutter einen Namen geben:");

        if(!(lbl_Servername.getText().equals("-"))){

        }
        else{
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() == true && !result.get().equals("")){
            lbl_Servername.setText(result.get());}
            else{
                return;
            }
        }

        if(shutter1 == null){
            shutter1 = new Shutter();
        }
        else{
            shutter1 = new Shutter();
        }

        /*ps = new PrintStream(new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                ta_srvlog.appendText(String.valueOf((char) i));
            }
        });
        System.setOut(ps);*/

        /*Server wird gestartet*/

        lbl_srvmsg.setText(shutter1.startServer(lbl_Servername.getText()));
        lbl_Serverip.setText(shutter1.getServerIP());
        lbl_Servername.setText(shutter1.shuttername);
        lbl_Serverstatus.setText(shutter1.serverstatus);

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

    public void BTNMoveup(ActionEvent event){

        if(!shutter1.isUpSrv()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Neue Position des Shutter");
            shutter1.moveUpSrv();
            String neuPos = String.valueOf(shutter1.getPosSrv());

            alert.setContentText(neuPos);

            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Neue Position des Shutter");
            String neuPos = "Shutter ist bereits ganz oben!";

            alert.setContentText(neuPos);

            alert.showAndWait();
        }

    }

    public void BTNMovedown(ActionEvent event){

        if(!shutter1.isDownSrv()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Neue Position des Shutter");
            shutter1.moveDownSrv();
            String neuPos = String.valueOf(shutter1.getPosSrv());

            alert.setContentText(neuPos);

            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Neue Position des Shutter");
            String neuPos = "Shutter ist bereits ganz unten!";

            alert.setContentText(neuPos);

            alert.showAndWait();
        }

    }

    public void BTNGetPos(ActionEvent event)throws RemoteException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Aktuelle Position des Shutter");
        String aktuelleTemp = String.valueOf(shutter1.getPosSrv());

        alert.setContentText(aktuelleTemp);

        alert.showAndWait();
    }
}
