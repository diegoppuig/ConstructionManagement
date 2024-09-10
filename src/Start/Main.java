package Start;

import domain.*;
import systemGuis.*;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class Main {
    
    public static void main(String[] args) throws Exception {
        ConstructionsManagementSystem system1 = new ConstructionsManagementSystem();

        runSplash();
        promptChooseOption(system1);
        
    }
    
    public static void runSplash() throws Exception{
        SplashScreen splash = new SplashScreen();
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);
        Thread.sleep(5000);
        splash.dispose();
    }
    
    public static void promptChooseOption(ConstructionsManagementSystem system1){
        OptionChooser choiceMenu = new OptionChooser(system1);
        choiceMenu.setLocationRelativeTo(null);
        choiceMenu.setVisible(true);
    }
    
}
