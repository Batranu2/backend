package flore.cristi.project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class SerialRead implements SerialPortEventListener {

    SerialPort serialPort;
    private static final String[] PORT_NAMES = {"COM3"};
    private BufferedReader input;
    private OutputStream output;
    Set<String> colectie = new LinkedHashSet<>();
    String inputLine;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;

    @PostConstruct
    public void initialize() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public String timestamp() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        date.setTime(ts.getTime());
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        return formattedDate;
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                inputLine = input.readLine();
                System.out.println(inputLine);
                colectie.add(inputLine);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        System.out.println(colectie);

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("UID").child(inputLine);
            DatabaseReference dbTable = database.getReference("Timestamp");
            DatabaseReference ref1 = dbTable.child(inputLine).child(timestamp());
            ref.setValueAsync(inputLine);
            ref1.setValueAsync(inputLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}