package at.spengergasse.schluesselweb.domain;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
public class MotorController {

    public static void bewegeMotor(String motornr,String richtung,String anzahlderSchritte)
    {
        File file = new File("/tmp/pipe");
        FileWriter writer;
        try
        {
            writer = new FileWriter(file,true);
            PrintWriter printer = new PrintWriter(writer);
            printer.append(motornr+":"+richtung+":"+anzahlderSchritte+"\n");
            printer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void controlMotoren(boolean abholung, Fach fach) throws InterruptedException
    {
        if(fach.getPos_y()!=null)
        {
            bewegeMotor("2","F",fach.getPos_y());
        }
        if(fach.getPos_x()!=null)
        {
            bewegeMotor("1","F",fach.getPos_x());
        }
        if(abholung)
        {
            bewegeMotor("1","F","400");
            TimeUnit.SECONDS.sleep(5);
            bewegeMotor("1","B","400");
        }
        bewegeMotor("1","B",fach.getPos_x());
        bewegeMotor("2","B",fach.getPos_y());
    }
}

