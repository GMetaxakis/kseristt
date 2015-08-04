package tei.epp.kseritts;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

class MyHttpClient extends AsyncTask<String, Void, String> {
	
	private static String ip="192.168.1.64";
	private static String port = "8000";
	private static boolean ipSetted = false;
	private static boolean portSetted = false;
	private static Handler mHandler;
	
	private String urlString = "http://"+ip+":"+port;
	
	private Exception exception;
	
	
	public static void setIp(String _ip){
		ip = _ip;
		ipSetted = true;
	}
	public static void setPort(String _port){
		port = _port;
		portSetted = true;
	}
	public static void setHandler(Handler handler){
		mHandler = handler;
	}
	
	protected String doInBackground(String... params) {
		
		if(!ipSetted || !portSetted){
			this.exception = new Exception("ip and port not setted");
			return "ip and port not setted";
		}
		
		String result = "";
		try {
			URI url = new URI(urlString);
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Header","value");
			httppost.addHeader("Content-Type", "text/html");
			httppost.addHeader("message",getMessage(params[0]));

			HttpResponse response;
			
			try {
				response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					InputStream instream = entity.getContent();
					result = Utils.convertStreamToString(instream);
					convertResultToCardsAndPoints(result);
					instream.close();
				}

			} catch (Exception e) {
				this.exception = e;
				//Log.e(Utils.LOGTAG, e.getMessage());
				return "error";
			}
		} catch (Exception e) {
			this.exception = e;
			return "error";
		}
		return result;
	}
	
	private void convertResultToCardsAndPoints(String result) {
		/*
		 * hands:{something} bottomCard:{something} Points:{something}
		 */
		try{
			String tempResult = result;
			Log.i(Utils.LOGTAG,tempResult);
			int first;
			int second;
			String[] cards = new String[6];
			String bottomCard;
			String[] points = new String[2];
			first = tempResult.indexOf('{');
			second = tempResult.indexOf('}');
			cards = tempResult.substring(first+1,second).trim().split(",",6);
			
			tempResult = tempResult.substring(second+1);
			first = tempResult.indexOf('{');
			second = tempResult.indexOf('}');
			bottomCard = tempResult.substring(first+1,second);
			
			tempResult = tempResult.substring(second+1);
			first = tempResult.indexOf('{');
			second = tempResult.indexOf('}');
			points = tempResult.substring(first+1,second).split(",",2);
			
			Log.i(Utils.LOGTAG,"cards on hand count : "+cards.length);
			for(String str : cards)
				Log.i(Utils.LOGTAG,"card :" + str);
			Log.i(Utils.LOGTAG,"card at bottom : "+bottomCard);
			Log.i(Utils.LOGTAG,"points player : "+points[0]);
			Log.i(Utils.LOGTAG,"points computer : "+points[1]);
			
			if(cards.length==1 && bottomCard.equals("-"))
				Utils.setGameIsFinished();
			Utils.sentData(cards,bottomCard,points);
			
		}catch(Exception ex){
			Log.d(Utils.LOGTAG,ex.getMessage());
		}	
	}
	
	private String getMessage(String string) {
		if(string.equalsIgnoreCase("loadData"))
			return string;
		
		if(string.equalsIgnoreCase("checkIpAndPort"))
			return string;
		
		
		String rv = "error";
		Log.i(Utils.LOGTAG,"getMessage string  = " + string);
		
		if(string.startsWith("ρίξε το ")){
			string = string.substring(8);
			rv = selectNumber(string);
		}
		else if(string.startsWith("ρίξε ")){
			string = string.substring(5);
			rv = selectNumber(string);
		}
		else if(string.startsWith("το ")){
				string = string.substring(3);
				rv = selectNumber(string);
		}
		else {
			rv = selectNumber(string);
		}
		Log.i(Utils.LOGTAG, "rv = "+string+"="+rv);
		return rv;
	}
	
	private String selectNumber(String string) {
		String rv ="error";
		if(stringIs1(string))
			rv = "ena";
		else if(stringIs2(string))
			rv = "dio";
		else if(stringIs3(string))
			rv = "tria";
		else if(stringIs4(string))
			rv = "tessera";
		else if(stringIs5(string))
			rv = "pente";
		else if(stringIs6(string))
			rv = "eksi";
		return rv;
	}
	
	private boolean stringIs1(String string) {
		return (string.equalsIgnoreCase("ένα") ||
				string.equalsIgnoreCase("πρώτο") ||
				string.equalsIgnoreCase("1"));
	}
	private boolean stringIs2(String string) {
		return (string.equalsIgnoreCase("δύο") ||
				string.equalsIgnoreCase("δεύτερο")||
				string.equalsIgnoreCase("2"));
	}
	private boolean stringIs3(String string) {
		return (string.equalsIgnoreCase("τρία") ||
				string.equalsIgnoreCase("τρίτο") ||
				string.equalsIgnoreCase("3") );
	}
	private boolean stringIs4(String string) {
		return (string.equalsIgnoreCase("τέσσερα") ||
				string.equalsIgnoreCase("τέταρτο") ||
				string.equalsIgnoreCase("4") );
	}
	private boolean stringIs5(String string) {
		return (string.equalsIgnoreCase("πέντε") ||
				string.equalsIgnoreCase("πέμπτο") ||
				string.equalsIgnoreCase("5") );
	}
	private boolean stringIs6(String string) {
		return (string.equalsIgnoreCase("έξι") ||
				string.equalsIgnoreCase("έκτο") ||
				string.equalsIgnoreCase("6"));
	}
	
	protected void onPostExecute(String result) {
		Message msg = Message.obtain();
		if(exception!=null){
			Log.e(Utils.LOGTAG,exception.getMessage());
			msg.obj = "onPostExecute exception : " + exception.getMessage();
		}
		else{
			msg.obj = "onPostExecute ok";
		}
		msg.setTarget(mHandler);
		msg.sendToTarget(); 
		
		
	}
}