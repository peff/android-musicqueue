package net.peff.musicqueue;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import java.util.Random;

public class MusicQueue extends Activity
{
    protected MediaPlayer mp;
    protected Cursor tracks;
    private int count;
    private Random rand;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	mp = new MediaPlayer();

	String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
	String[] projection = {
		MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.DATA
	};

	tracks = managedQuery(
			MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
			projection,
			selection,
			null,
			null);
	count = tracks.getCount();

	rand = new Random();
    }

    public int pickSomething()
    {
	// in theory you would iterate through the cursor, looking at the
	// artists and titles and making some clever decision about what to
	// play next. Here we just pick something at random.
	return rand.nextInt(count);
    }

    public void playSomething(View view)
    {
	tracks.moveToPosition(pickSomething());

	try {
	    if (mp.isPlaying()) {
		mp.reset();
	    }
	    mp.setDataSource(tracks.getString(3));
	    mp.prepare();
	    mp.start();
	} catch (Exception e) {
	}
    }
}
