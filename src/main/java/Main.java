import dao.ExchangeRatesDao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.*;
import service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class Main {
    private static final Long PERIOD = 120000L;
    private final static Logger LOG = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {

        Main main = new Main();
        main.init();
    }

    private void init(){
        List<Page> pages = new ArrayList<>();
        pages.add(new SberbankPage());
        pages.add(new VtbPage());
        pages.add(new RSHBPage());
        pages.add(new NKCPage());
        pages.add(new AlfabankPage());
        pages.add(new MKBPage());
        pages.add(new OpenbankPage());
        pages.add(new UnicreditbankPage());
        pages.add(new PsbankPage());
        pages.add(new RaiffeisenBank());

        Service service = new Service(pages, new ExchangeRatesDao());
        Timer timer = new Timer();
        timer.schedule(service, 0, PERIOD);


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.println("Для выхода введите 'exit':");
            try {
                String string = reader.readLine();
                if(string.equals("exit")){
                    timer.cancel();
                    reader.close();
                    break;
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
