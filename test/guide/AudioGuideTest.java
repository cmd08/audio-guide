package guide;

import static org.junit.Assert.fail;

import location.Location;
import media.MediaPlayerControl;
import media.MediaPlayerListener;
import media.Track;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;


@RunWith(JMock.class)
public class AudioGuideTest {
    Mockery context = new JUnit4Mockery();

	MediaPlayerControl mediaPlayer = context.mock(MediaPlayerControl.class);
	MediaLibrary mediaLibrary = context.mock(MediaLibrary.class);
	
	AudioGuide guide = new AudioGuide(mediaLibrary, mediaPlayer);

    @Test
	public void playsMediaTrackForInitialLocation() throws Exception {
        final Location initialLocation = new Location("initialLocation");
        final Track initialTrack = context.mock(Track.class, "initialTrack");

        context.checking(new Expectations() {{
            allowing (mediaLibrary).getTrackByLocation(initialLocation); will(returnValue(initialTrack));
            oneOf (mediaPlayer).play(initialTrack);
        }});

        guide.locationChanged(initialLocation);
	}

    @Test
    public void playsMediaTrackForCurrentLocationOnPreviousLocationTrackEnd() throws Exception {
        final Location initialLocation = new Location("initialLocation");
        final Location finalLocation = new Location("finalLocation");
        final Track initialTrack = context.mock(Track.class, "initialTrack");
        final Track finalTrack = context.mock(Track.class, "finalTrack");

        context.checking(new Expectations() {{
            allowing(mediaLibrary).getTrackByLocation(initialLocation); will(returnValue(initialTrack));
            allowing(mediaLibrary).getTrackByLocation(finalLocation); will(returnValue(finalTrack));
            oneOf(mediaPlayer).play(initialTrack);
        }});

        guide.locationChanged(initialLocation);

        guide.locationChanged(finalLocation);

        context.checking(new Expectations() {{
            oneOf(mediaPlayer).play(finalTrack);
        }});

        guide.trackFinished();
    }

    @Test
    public void playsMediaTrackForCurrentLocation(){
        final Location initialLocation = new Location("initialLocation");
        final Location finalLocation = new Location("finalLocation");
        final Track initialTrack = context.mock(Track.class, "initialTrack");
        final Track finalTrack = context.mock(Track.class, "finalTrack");

        context.checking(new Expectations() {{
            allowing(mediaLibrary).getTrackByLocation(initialLocation); will(returnValue(initialTrack));
            allowing(mediaLibrary).getTrackByLocation(finalLocation); will(returnValue(finalTrack));
            oneOf(mediaPlayer).play(initialTrack);
        }});

        guide.locationChanged(initialLocation);  // initial track playing

        guide.trackFinished();                   // initial track finishes

        context.checking(new Expectations() {{
            oneOf(mediaPlayer).play(finalTrack);
        }});

        guide.locationChanged(finalLocation);    // new location, expect new track playing

    }

    @Test
    public void nothingPlayingIfNoTrackAssociatedWithLocation(){
        final Location location = new Location("location");
        final Track track = context.mock(Track.class, "track");

        context.checking(new Expectations() {{
            allowing(mediaLibrary).getTrackByLocation(location); will(throwException(new NoSuchElementException()));
        }});

        try {
            guide.locationChanged(location);
        } catch (NoSuchElementException e){
            System.out.print(e);
        }
    }

}
