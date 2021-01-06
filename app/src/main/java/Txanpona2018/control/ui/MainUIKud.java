package Txanpona2018.control.ui;

import Txanpona2018.Txanpon;
import Txanpona2018.control.db.DBKudeatzaile;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
    private TableColumn<Txanpon, Image> zutPortaera;

    @FXML
    private ComboBox<TxanponIzenak> cmbox;

    private float btc=0;
    private float ltc=0;
    private float eth=0;

    ObservableList<Txanpon> emaitza= FXCollections.observableArrayList();

    private class TxanponIzenak{
        private String izena;
        private String laburdura;

        public String getLaburdura() {
            return laburdura;
        }

        public void setLaburdura(String laburdura) {
            this.laburdura = laburdura;
        }

        public TxanponIzenak(String izena, String laburdura) {
            this.izena = izena;
            this.laburdura = laburdura;
        }

        @Override
        public String toString() {
            return izena;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultSet resultSet=DBKudeatzaile.getInstantzia().execSQL("SELECT * FROM txanponak");
        try {
            datuakSartu(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        zutID.setCellValueFactory(new PropertyValueFactory<>("id"));
        zutTxanpon.setCellValueFactory(new PropertyValueFactory<>("mota"));
        zutNoiz.setCellValueFactory(new PropertyValueFactory<>("time"));
        zutZenbat.setCellValueFactory(new PropertyValueFactory<>("price"));
        zutBolumena.setCellValueFactory(new PropertyValueFactory<>("volume"));
        zutPortaera.setCellValueFactory(new PropertyValueFactory<>("portaera"));


        zutPortaera.setCellFactory(p -> new TableCell<>() {
            public void updateItem(Image image, boolean empty) {
                if (image != null && !empty){
                    final ImageView imageview = new ImageView();
                    imageview.setFitHeight(30);
                    imageview.setFitWidth(50);
                    imageview.setImage(image);
                    setGraphic(imageview);
                    setAlignment(Pos.CENTER);
                }else{
                    setGraphic(null);
                    setText(null);
                }
            };
        });

        cmbox.getItems().addAll(
                new TxanponIzenak("Ethereum","ETH"),
                new TxanponIzenak("Litecoin","LTC"),
                new TxanponIzenak("Bitcoin","BTC")
        );
        cmbox.getSelectionModel().selectFirst();
    }

    private void datuakSartu(ResultSet resultSet) throws SQLException {

        while(resultSet.next()){
            Txanpon txanpon=new Txanpon();
            txanpon.setId(resultSet.getInt("id"));
            txanpon.setTime(resultSet.getString("data"));
            txanpon.setPrice(resultSet.getDouble("balioa"));
            txanpon.setMota(resultSet.getString("mota"));
            txanpon.setVolume(resultSet.getFloat("bolumena"));
            emaitza.add(txanpon);

            Image image=lortuPortaera(txanpon);
            txanpon.setPortaera(image);
        }
        taula.setItems(emaitza);
    }

    private Image lortuPortaera(Txanpon txanpon) {
        Image irudia = null;
        if(txanpon.getMota().equals("BTC")){
            irudia=kalkulatuIrudia(btc,txanpon.getVolume());
            btc=txanpon.getVolume();
        }

        else if(txanpon.getMota().equals("LTC")){
            irudia=kalkulatuIrudia(ltc,txanpon.getVolume());
            ltc=txanpon.getVolume();
        }
        else {
            irudia=kalkulatuIrudia(eth,txanpon.getVolume());
            eth=txanpon.getVolume();
        }

        return irudia;
    }

    private Image kalkulatuIrudia(float zbk,float txanponBalioa){
        Image irudia = null;

        var unekoa=txanponBalioa-zbk;
        if (unekoa < 0) {
            irudia=new Image("down.png");
        }
        else if (unekoa<10 && unekoa==0){
            irudia=new Image("equal.png");
        }
        else irudia=new Image("up.png");
        return irudia;
    }

    @FXML
    void onClickGorde(ActionEvent event) {
        //ezabatu elementu guztiak
        DBKudeatzaile.getInstantzia().execSQL("delete from txanponak");

        //sartu elementu guztiak berriz
        emaitza.forEach(elem->{
            String query="insert into txanponak (data,balioa,mota,bolumena)"+
                    " values('"+
                    elem.getTime()+"','"+
                    elem.getPrice()+"','"+
                    elem.getMota()+"','"+
                    elem.getVolume()+"')";
            DBKudeatzaile.getInstantzia().execSQL(query);
        });
    }

    @FXML
    void onClickSartu(ActionEvent event) {
        var txanpontxoa=readFromUrl(cmbox.getValue().getLaburdura());

        String data=txanpontxoa.getTime();
        data=data.replace("T"," ");
        data=data.replaceAll("\\.\\d+\\w",""); //regex erabili dut ez naiz timestamp erabiltzeko kapaza izan:(
        txanpontxoa.setTime(data);

        txanpontxoa.setMota(cmbox.getValue().getLaburdura());
        Image image=lortuPortaera(txanpontxoa);
        txanpontxoa.setPortaera(image);

        emaitza.add(txanpontxoa);
    }

    public static Txanpon readFromUrl(String txanpona) {
        txanpona = txanpona.toLowerCase();
        String inputLine = "";

        try {
            URL coinMarket = new URL("https://api.gdax.com/products/" + txanpona + "-eur/ticker");
            URLConnection yc = coinMarket.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            new InputStreamReader(coinMarket.openStream());

            inputLine = in.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(inputLine, Txanpon.class);
    }
}
