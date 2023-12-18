package humanitarni_klijent;

import java.io.*;
import java.net.*;

public class Klijent implements Runnable {

	static Socket soket;

	static int port = 7259;
	static String adresa = "localhost";

	static BufferedReader konzola = null;
	static BufferedReader od_servera = null;
	static PrintStream ka_serveru = null;

	static boolean kraj = false;

	public static void main(String[] args) {

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
			adresa = args[1];
		}

		try {
			// otvaranje soketa
			soket = new Socket(adresa, port);
			// inicijalizacija tokova
			konzola = new BufferedReader(new InputStreamReader(System.in));
			od_servera = new BufferedReader(new InputStreamReader(soket.getInputStream()));
			ka_serveru = new PrintStream(soket.getOutputStream());
			// startovanje threada za primanje poruka
			new Thread(new Klijent()).start();
			// slanje poruka
			while (!kraj) {
				ka_serveru.println(konzola.readLine());
			}
			soket.close();
		} catch (SocketException e) {
			System.err.println("Host ugasen");
		} catch (IOException e) {
			System.err.println("IOException pri konektovanju sa hostom: " + e);
		} 

	}

	@Override
	public void run() {
		String string;
		try {
			while ((string = od_servera.readLine()) != null) {
				System.out.println(string);
				if (string.startsWith("> Prijatno;")) {
					kraj = true;
					return;
				}
			}
		} catch (SocketException e) {
			System.err.println("Host ugasen");
		} catch (IOException e) {
			System.err.println("IOException pri konektovanju sa hostom: " + e);
		}

	}

}
