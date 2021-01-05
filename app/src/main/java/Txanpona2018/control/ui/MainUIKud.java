package Txanpona2018.control.ui;

import Txanpona2018.Txanpon;
import Txanpona2018.control.db.DBKudeatzaile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainUIKud implements Initializable {

    @FXML
    private TableView<Txanpon> taula;


    @FXML
    private TableColumn<?, ?> zutID;

    @FXML
    private TableColumn<?, ?> zutTxanpon;

    @FXML
    private TableColumn<?, ?> zutNoiz;

    @FXML
    private TableColumn<?, ?> zutZenbat;

    @FXML
    private TableColumn<?, ?> zutBolumena;

    @FXML
    private TableColumn<?, ?> zutPortaera;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultSet resultSet=DBKudeatzaile.getInstantzia().execSQL("SELECT * FROM txanponak");
        try {
            datuakSartu(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        zutID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        zutTxanpon.setCellValueFactory(new PropertyValueFactory<>("Txanpon"));
        zutNoiz.setCellValueFactory(new PropertyValueFactory<>("Noiz"));
        zutZenbat.setCellValueFactory(new PropertyValueFactory<>("Zenbat"));
        zutBolumena.setCellValueFactory(new PropertyValueFactory<>("Bolumena"));
        zutPortaera.setCellValueFactory(new PropertyValueFactory<>("Portaera"));

    }

    private void datuakSartu(ResultSet resultSet) throws SQLException {
        ObservableList<Txanpon> emaitza= FXCollections.observableArrayList();

        while(resultSet.next()){
            Txanpon txanpon=new Txanpon();
            txanpon.setId(resultSet.getInt("id"));
            txanpon.setData(resultSet.getString("data"));
            txanpon.setBalioa(resultSet.getDouble("balioa"));
            txanpon.setMota(resultSet.getString("mota"));
            txanpon.setBolumena(resultSet.getFloat("bolumena"));
            emaitza.add(txanpon);
        }
        taula.setItems(emaitza);
    }
}
