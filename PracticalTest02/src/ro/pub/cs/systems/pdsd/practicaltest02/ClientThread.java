package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.Utilities;
import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {
	private String   address;
	private int      port;
	private TextView timeTextView;

	private Socket   socket;

	public ClientThread(
			String address,
			int port,
			TextView timeTextView) {
		this.address = address;
		this.port = port;
		this.timeTextView = timeTextView;
	}

	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
				return;
			}
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				String timeInformation;
				while ((timeInformation = bufferedReader.readLine()) != null) {
					final String finalizedTimeInformation = timeInformation;
					timeTextView.post(new Runnable() {
						@Override
						public void run() {
							timeTextView.append(finalizedTimeInformation + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}
}