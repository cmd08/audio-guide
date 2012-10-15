package guide;

import location.Location;
import location.LocationAware;
import media.MediaPlayerControl;
import media.Track;

import java.util.NoSuchElementException;


public class AudioGuide implements LocationAware {
	private final MediaLibrary mediaLibrary;
	private final MediaPlayerControl mediaPlayer;
    private Boolean isPlaying = false;
    private Location nextLocation = null;
	
	public AudioGuide(MediaLibrary mediaLibrary, MediaPlayerControl mediaPlayer) {
		this.mediaLibrary = mediaLibrary;
		this.mediaPlayer = mediaPlayer;
	}

	@Override
	public void locationChanged(Location newLocation) {
            if(!isPlaying) {
                try {
                    mediaPlayer.play(mediaLibrary.getTrackByLocation(newLocation));
                    this.isPlaying = true;
                } catch (NoSuchElementException e) {
                    // no track don't play
                }
            } else {
                nextLocation = newLocation;
            }
	}

    public void trackFinished() {
        if (nextLocation != null) {
            try {
                mediaPlayer.play(mediaLibrary.getTrackByLocation(nextLocation));
                this.isPlaying = true;
            } catch (NoSuchElementException e) {
                // no track don't play
            }
        } else {
            this.isPlaying = false;
            this.nextLocation = null;
        }
    }
}
