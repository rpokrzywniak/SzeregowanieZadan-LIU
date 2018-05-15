package Projekcik;

import java.util.ArrayList;

public class Zadanie {
    // p - czasTrwania, r - czasStartu, d - czasZakonczenia
    private final int numer, r;
    private int p , d, czasZakonczenia;
    private boolean zakonczony;
    private int opoznienie;
    private ArrayList<Zadanie> poprzednieZadania;
    private ArrayList<Zadanie> nastepneZadania;

    public Zadanie(int numer, int czasTrwania, int czasStartu, int czasZakonczenia){
        this.numer = numer;
        this.p = czasTrwania;
        this.r = czasStartu;
        this.d = czasZakonczenia;
        this.zakonczony = false;
        poprzednieZadania = new ArrayList<>();
        nastepneZadania = new ArrayList<>();
    }

    void uruchom(int czas){
        if(p == 0) throw new IllegalArgumentException("Zadanie juz ukonczone");
        p--;
        if(p == 0){
            zakonczony = true;
            czasZakonczenia = czas + 1;
            obliczOpoznienie();
        }
    }

    int getNumer() { return numer;}
    int getCzasTrwania() {return d;}
    void setCzasZakonczenia(int deadline) {this.d = deadline;}
    int getCzasStartu() { return r;}
    private void obliczOpoznienie() {
        this.opoznienie = czasZakonczenia - d;
    }
    int getOpoznienie() { return opoznienie;}
    boolean jestZakonczony(){ return zakonczony;}

    public void dodajPoprzenieZadania(Zadanie prevTask){
        poprzednieZadania.add(prevTask);
    }
    public void dodajKolejneZadania(Zadanie nextTask){
        nastepneZadania.add(nextTask);
    }
    public ArrayList<Zadanie> getPoprzednieZadania() {
        return poprzednieZadania;
    }
    public ArrayList<Zadanie> getKolejneZadania() {
        return nastepneZadania;
    }


}
