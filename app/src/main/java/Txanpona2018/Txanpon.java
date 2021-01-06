package Txanpona2018;

import javafx.scene.image.Image;

public class Txanpon {
    private int id;
    private String time;
    private double price;
    private float volume;

    private String mota;
    private Image portaera;


    public Image getPortaera() {
        return portaera;
    }

    public void setPortaera(Image portaera) {
        this.portaera = portaera;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float bolumena) {
        this.volume = bolumena;
    }
}
