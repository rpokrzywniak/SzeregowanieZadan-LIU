package Projekcik;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class Startup 
{
    static ZadanieMenadzer menadzer = ZadanieMenadzer.getInstance();

    public static void main( String[] args )
    {
        try {
            odczytajZPliku("dane.txt");            
        } catch(IOException e){
            System.out.println("Problem z odczytem pliku");
        }finally{
            menadzer.LIU();
        }
    }

    public static void odczytajZPliku(String nazwaPliku) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nazwaPliku));
        String kolejnaLinia;
        int numerZadania = 1;
        while((kolejnaLinia = br.readLine()) != null){
            String[] dane = kolejnaLinia.split(",");
            int[] wezel = zamienNaInt(dane[0]);

            if(wezel.length != 3) throw new IllegalArgumentException("Ilosc wartosci nie jest rowna 3");
            menadzer.dodajZadanie(numerZadania, wezel[0], wezel[1], wezel[2]);

            if(dane.length == 2){
                int[] prevTasks = zamienNaInt(dane[1]);
                for(int task : prevTasks){
                    menadzer.polacz(task, numerZadania);
                }
            }
            numerZadania++;
        }
        br.close();
    }

    private static int[] zamienNaInt(String data){
        return Arrays.stream(data.split(" "))
                .mapToInt(Integer::parseInt).toArray();
    }
}
