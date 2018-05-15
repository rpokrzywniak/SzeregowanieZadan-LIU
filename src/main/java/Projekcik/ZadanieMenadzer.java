package Projekcik;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class ZadanieMenadzer {
    private ArrayList<Zadanie> zadania;
    private ArrayList<Integer> rozklad;
    private int czas;
    private int lMax;


    private static ZadanieMenadzer menadzer = new ZadanieMenadzer();

    public static ZadanieMenadzer getInstance() {
        return menadzer;
    }

    private ZadanieMenadzer() {
        zadania = new ArrayList<>();
        rozklad = new ArrayList<>();
        czas = 0;
    }

    void dodajZadanie(int number, int duration, int startTime, int deadline){
        zadania.add(new Zadanie(number, duration, startTime, deadline));
    }

    private Zadanie getZadanieNumer(int numer){
        return zadania.stream()
                .filter(p -> p.getNumer() == numer)
                .findFirst().orElse(null);
    }

    public void polacz(int poprzedniNumer, int kolejnyNumer){
        if(poprzedniNumer >= kolejnyNumer)
        	throw new IllegalArgumentException("Nastapil blad");
        Zadanie poprzenie = getZadanieNumer(poprzedniNumer);
        Zadanie kolejne = getZadanieNumer(kolejnyNumer);

        if(poprzenie == null || kolejne == null)
            throw new IllegalArgumentException("Nastapil blad");

        poprzenie.dodajKolejneZadania(kolejne);
        kolejne.dodajPoprzenieZadania(poprzenie);
    }

    private void przejdz(Zadanie zadanie, ArrayList<Integer> czasyZakonczen) {
        if(!czasyZakonczen.contains(zadanie.getCzasTrwania()))
            czasyZakonczen.add(zadanie.getCzasTrwania());
        if(!zadanie.getKolejneZadania().isEmpty()){
            for(Zadanie kolejneZadanie : zadanie.getKolejneZadania()){
                przejdz(kolejneZadanie, czasyZakonczen);
            }
        }
    }

    private void noweCzasyZakonczen(){
        ArrayList<Integer> czasyZakonczen = new ArrayList<>();
        ArrayList<Integer> ostateczneCzasy = new ArrayList<>();
        for(Zadanie zadanie : zadania){
            przejdz(zadanie, czasyZakonczen);
            ostateczneCzasy.add(Collections.min(czasyZakonczen));
            czasyZakonczen.clear();
        }

        for(Zadanie zadanie : zadania){
            zadanie.setCzasZakonczenia(ostateczneCzasy.get(zadanie.getNumer() - 1));
        }
    }

    private void dodajDoRozkladu(Zadanie dodane){
        dodane.uruchom(czas);
        rozklad.add(dodane.getNumer());
    }

    private boolean jestZakonczone(){
        for(Zadanie zadanie : zadania){
            if(!zadanie.jestZakonczony())
                return false;
        }
        return true;
    }

    void LIU(){
        noweCzasyZakonczen();
        while(!jestZakonczone()){
            try{
                Zadanie wczesne = zadania.stream()
                        .filter(p -> p.getCzasStartu() <= czas && !p.jestZakonczony())
                        .min(Comparator.comparingInt(Zadanie::getCzasTrwania))
                        .get();
                if(sprawdzCzyPoprzednieZakonczone(wczesne))
                    dodajDoRozkladu(wczesne);
                else
                	rozklad.add(0);
            } catch(NoSuchElementException e){
                System.out.println("Bedzie dodane pozniej");
                rozklad.add(0);
            } finally {
                czas++;
            }

        }
        setLMAX();
        wyswietl();
    }

    boolean sprawdzCzyPoprzednieZakonczone(Zadanie zadanie){
        for(Zadanie poprzednieZadanie : zadanie.getPoprzednieZadania()){
            if(!poprzednieZadanie.jestZakonczony())
                return false;
        }
        return true;
    }

    private void setLMAX(){
        lMax = zadania.stream()
                .max(Comparator.comparingInt(Zadanie::getOpoznienie))
                .get().getOpoznienie();
    }

    public void wyswietl(){
        System.out.print("[ ");
        int i=0;
        PrintWriter writer = null;
		try {
			writer = new PrintWriter("strona.html", "UTF-8");
	        writer.println("<!doctype html>");
	        writer.println("<html lang='en'>");
	        writer.println("<head>");
	        writer.println("<meta charset='utf-8'>");
	        writer.println("<title>Graficzne Przedstawienie</title>");
	        writer.println("</head>");
	        writer.println("<body>");
	        
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			writer.println("<div style='float:left;border: 1px solid #000000'>");
			writer.println("<p style='text-align: center'>Uporządkowane zadania</p>");
	        for(int zadanie : rozklad){
	        	if(i == rozklad.size()-1) System.out.print(zadanie + " ]\n\n");
	        	else System.out.print(zadanie + " ][ ");
	        	writer.println("<h5 style='color:blue;font-size:200%;float:left;border: 1px solid #000000'>"+zadanie+"</h5>");
	        	i++;
	        }
	        writer.println("</div>");
			writer.println("<div style='color:red;float:left;border: 1px solid #00f000'>");
	        for(Zadanie zadanie : zadania){
	            System.out.println("L" + zadanie.getNumer() + " = " + zadanie.getOpoznienie());
	            writer.println("<h5 style='color:black;font-size:100%'>L"+zadanie.getNumer()+" = "+ zadanie.getOpoznienie()+"  </h5>");
	        }
	        writer.println("</div>");
	        System.out.println();
	
	        System.out.println("Lmax = " + lMax);
	        writer.println("<h5 style='color:black;font-size:100%'>L<sub>max</sub> = "+lMax+"</h5>");
	        System.out.println("Czas = " + czas);
	        writer.println("</body>");
	        writer.println("</html>");
	        writer.close();
		}
    }

    void resetuj(){
        zadania = new ArrayList<>();
        rozklad = new ArrayList<>();
        czas++;
    }
}
