package tei.epp.kseritts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.conn.util.InetAddressUtils;

public class Utils {
	public static final String LOGTAG = "KSERITTS";
	
	public static boolean checkPort(String port) {
		try {
			Integer.parseInt(port);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean checkIp(String ip) {
		return InetAddressUtils.isIPv4Address(ip);
	}
	
	static int getIdOfCard() {
		return getIdBasedOnString(_bottomCard);
	}
	static int getIdOfCard(int i) {
		if(i>=_cards.length)
			return getIdBasedOnString("null");
		
		return getIdBasedOnString(_cards[i]);
	}
	/**
	 * 1-10 or J,Q,K black/red club/spade/heart/diamond
	 * 
	 * @param card
	 * @return the id of image
	 */
	private static int getIdBasedOnString(String card) {
		int id = R.drawable.b1fv;
		if (card.contains("1 black club"))
			id = R.drawable.image1;
		else if (card.contains("1 black spade"))
			id = R.drawable.image2;
		else if (card.contains("1 red heart"))
			id = R.drawable.image3;
		else if (card.contains("1 red diamond"))
			id = R.drawable.image4;

		else if (card.contains("K black club"))
			id = R.drawable.image5;
		else if (card.contains("K black spade"))
			id = R.drawable.image6;
		else if (card.contains("K red heart"))
			id = R.drawable.image7;
		else if (card.contains("K red diamond"))
			id = R.drawable.image8;

		else if (card.contains("Q black club"))
			id = R.drawable.image9;
		else if (card.contains("Q black spade"))
			id = R.drawable.image10;
		else if (card.contains("Q red heart"))
			id = R.drawable.image11;
		else if (card.contains("Q red diamond"))
			id = R.drawable.image12;

		else if (card.contains("J black club"))
			id = R.drawable.image13;
		else if (card.contains("J black spade"))
			id = R.drawable.image14;
		else if (card.contains("J red heart"))
			id = R.drawable.image15;
		else if (card.contains("J red diamond"))
			id = R.drawable.image16;

		else if (card.contains("10 black club"))
			id = R.drawable.image17;
		else if (card.contains("10 black spade"))
			id = R.drawable.image18;
		else if (card.contains("10 red heart"))
			id = R.drawable.image19;
		else if (card.contains("10 red diamond"))
			id = R.drawable.image20;

		else if (card.contains("9 black club"))
			id = R.drawable.image21;
		else if (card.contains("9 black spade"))
			id = R.drawable.image22;
		else if (card.contains("9 red heart"))
			id = R.drawable.image23;
		else if (card.contains("9 red diamond"))
			id = R.drawable.image24;

		else if (card.contains("8 black club"))
			id = R.drawable.image25;
		else if (card.contains("8 black spade"))
			id = R.drawable.image26;
		else if (card.contains("8 red heart"))
			id = R.drawable.image27;
		else if (card.contains("8 red diamond"))
			id = R.drawable.image28;

		else if (card.contains("7 black club"))
			id = R.drawable.image29;
		else if (card.contains("7 black spade"))
			id = R.drawable.image30;
		else if (card.contains("7 red heart"))
			id = R.drawable.image31;
		else if (card.contains("7 red diamond"))
			id = R.drawable.image32;

		else if (card.contains("6 black club"))
			id = R.drawable.image33;
		else if (card.contains("6 black spade"))
			id = R.drawable.image34;
		else if (card.contains("6 red heart"))
			id = R.drawable.image35;
		else if (card.contains("6 red diamond"))
			id = R.drawable.image36;

		else if (card.contains("5 black club"))
			id = R.drawable.image37;
		else if (card.contains("5 black spade"))
			id = R.drawable.image38;
		else if (card.contains("5 red heart"))
			id = R.drawable.image39;
		else if (card.contains("5 red diamond"))
			id = R.drawable.image40;

		else if (card.contains("4 black club"))
			id = R.drawable.image41;
		else if (card.contains("4 black spade"))
			id = R.drawable.image42;
		else if (card.contains("4 red heart"))
			id = R.drawable.image43;
		else if (card.contains("4 red diamond"))
			id = R.drawable.image44;

		else if (card.contains("3 black club"))
			id = R.drawable.image45;
		else if (card.contains("3 black spade"))
			id = R.drawable.image46;
		else if (card.contains("3 red heart"))
			id = R.drawable.image47;
		else if (card.contains("3 red diamond"))
			id = R.drawable.image48;

		else if (card.contains("2 black club"))
			id = R.drawable.image49;
		else if (card.contains("2 black spade"))
			id = R.drawable.image50;
		else if (card.contains("2 red heart"))
			id = R.drawable.image51;
		else if (card.contains("2 red diamond"))
			id = R.drawable.image52;

		else
			id = R.drawable.b1fv;
		return id;
	}
	
	static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	static String[] _cards, _points;
	static String _bottomCard;

	public static void sentData(String[] cards, String bottomCard,
			String[] points) {
		_cards = cards;
		_points = points;
		_bottomCard = bottomCard;
	}
	
	private static boolean gameIsFinished = false;
	public static void setGameIsFinished() {
		gameIsFinished = true;
	}
	public static boolean getGameIsFinished() {
		return gameIsFinished ;
	}	
}
