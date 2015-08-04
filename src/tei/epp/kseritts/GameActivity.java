package tei.epp.kseritts;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GameActivity extends ActionBarActivity {

	private static TextView pointsTV;
	private static ImageView card1, card2, card3, card4, card5, card6,
			bottomCard;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private boolean clicked = false;

	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String message = (String) msg.obj;

			if (message.equals("onPostExecute ok")) {
				updateCardsAndPoints();
				clicked = false;
			} else if (message.startsWith("onPostExecute exception")) {
				// TODO message?
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		card1 = (ImageView) findViewById(R.id.ImageViewCard1);
		card2 = (ImageView) findViewById(R.id.ImageViewCard2);
		card3 = (ImageView) findViewById(R.id.imageViewCard3);
		card4 = (ImageView) findViewById(R.id.ImageViewCard4);
		card5 = (ImageView) findViewById(R.id.ImageViewCard5);
		card6 = (ImageView) findViewById(R.id.imageViewCard6);
		bottomCard = (ImageView) findViewById(R.id.imageView1);

		pointsTV = (TextView) findViewById(R.id.pointsTextview);

		MyHttpClient.setHandler(mHandler);
		new MyHttpClient().execute("loadData");

		btnSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!Utils.getGameIsFinished() && !clicked) {
					clicked = true;
					// new MyHttpClient().execute("1");
					promptSpeechInput();
				}
			}
		});

	}

	private Boolean exit = false;

	@Override
	public void onBackPressed() {
		if (exit)
			this.finish();
		else {
			Toast.makeText(this, "Πάτα πίσω για κλείσιμο.", Toast.LENGTH_SHORT)
					.show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}

	}

	private void updateCardsAndPoints() {
		if (Utils.getGameIsFinished()) {
			gameFinished();
			btnSpeak.setEnabled(false);
		}

		card1.setImageResource(Utils.getIdOfCard(0));
		card2.setImageResource(Utils.getIdOfCard(1));
		card3.setImageResource(Utils.getIdOfCard(2));
		card4.setImageResource(Utils.getIdOfCard(3));
		card5.setImageResource(Utils.getIdOfCard(4));
		card6.setImageResource(Utils.getIdOfCard(5));
		bottomCard.setImageResource(Utils.getIdOfCard());

		pointsTV.setText(Utils._points[0] + " - " + Utils._points[1]);
	}

	private void gameFinished() {
		String message = "Ησασταν άτυχος το παιχνίδι έλειξε ισόπαλο";
		int playersPoints = Integer.parseInt(Utils._points[0]);
		int computersPoints = Integer.parseInt(Utils._points[1].trim());

		if (playersPoints > computersPoints)
			message = "Συγχαρητήρια κερδίσατε";
		else if (playersPoints < computersPoints)
			message = "Δυστυχώς χάσατε";

		new AlertDialog.Builder(this).setTitle("Τέλος παιχνιδιού")
				.setMessage(message).setNeutralButton("OK", null)
				.setIcon(android.R.drawable.ic_dialog_info).create().show();
	}

	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el");
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Διάλεξε κάρτα!");

		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					"Δεν υποστηρίζεται η φωνητική αναγνώριση",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				Toast.makeText(this, "message : " + result.get(0),
						Toast.LENGTH_LONG).show();
				new MyHttpClient().execute(result.get(0));
			}
			break;
		}
		}
	}
}
