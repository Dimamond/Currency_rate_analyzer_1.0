import dao.ExchangeRatesDao;

import model.ExchangeRates;
import pages.Page;
import pages.RSHBPage;
import pages.SberbankPage;
import pages.VtbPage;
import service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
    }

    private void init(){
        List<Page> pages = new ArrayList<>();
        pages.add(new RSHBPage());
        pages.add(new SberbankPage());
        pages.add(new VtbPage());

        Service service = new Service(pages, new ExchangeRatesDao());

        Timer timer = new Timer();

        timer.schedule(service, 0, 120000);


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("Для выхода введите 'exit'");
            try {
                String string = reader.readLine();
                if(string.equals("exit")){
                    timer.cancel();
                    break;
                }
            } catch (IOException e) {

            }
        }
    }
}
