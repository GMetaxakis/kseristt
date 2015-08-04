package tei.epp.kseritts;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class MainActivity extends ActionBarActivity {
	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;
			if (message.equals("onPostExecute ok")) {
				showDialogToContinue();
			} else if (message.startsWith("onPostExecute exception")) {
				showDialogError(message.substring(26));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	protected void showDialogError(String message) {
		new AlertDialog.Builder(this).setTitle("IP και PORT Error")
				.setMessage(message).setNeutralButton("ΟΚ", null)
				.setIcon(android.R.drawable.ic_dialog_alert).create().show();
	}

	protected void showDialogToContinue() {
		new AlertDialog.Builder(this)
				.setTitle("IP και PORT OK")
				.setMessage("Θες να συνεχίσεις στο παιχνίδι;")
				.setNeutralButton("Ναι", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent myIntent = new Intent(MainActivity.this,
								GameActivity.class);
						MainActivity.this.startActivity(myIntent);
						finish();
					}
				}).setNegativeButton("Όχι", null)
				.setIcon(android.R.drawable.ic_dialog_info).create().show();
	}

	public void checkIpAndPort(View v) {
		String ip = ((EditText) findViewById(R.id.ipEditText)).getText()
				.toString();
		String port = ((EditText) findViewById(R.id.portEditText)).getText()
				.toString();

		boolean ipOk = Utils.checkIp(ip);
		boolean portOk = Utils.checkPort(port);

		if (ipOk && portOk) {
			MyHttpClient.setIp(ip);
			MyHttpClient.setPort(port);
			MyHttpClient.setHandler(mHandler);
			new MyHttpClient().execute("checkIpAndPort");
		} else {
			if (!ipOk && !portOk)
				showDialogError("Μη αποδεκτή τιμή για ip και port");
			if (!ipOk)
				showDialogError("Μη αποδεκτή τιμή για ip");
			if (!portOk)
				showDialogError("Μη αποδεκτή τιμή για port");

		}
	}
}