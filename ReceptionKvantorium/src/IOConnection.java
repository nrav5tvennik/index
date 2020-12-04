import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class IOConnection {
    private Socket socket;
    private InputStreamReader reader;
    private OutputStreamWriter writer;

    public IOConnection(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(socket.getInputStream());
        writer = new OutputStreamWriter(socket.getOutputStream());
    }

    public String read() throws IOException {
        int data;
        StringBuilder strData = new StringBuilder();

        while ((data = reader.read()) != -1) {
            strData.append((char) data);

            if (!reader.ready())
                break;
        }
        if (strData.toString().equals("_"))
            return "";

        return strData.toString();
    }

    public void write(String data) throws IOException {
        if (data == null || data.equals(""))
            data = "_";
        for (char i : data.toCharArray())
            writer.write(i);
        writer.flush();
    }

    public void close() {
        try {
            socket.close();
        }
        catch (IOException exp) {}
    }
}