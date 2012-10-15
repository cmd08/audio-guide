package guide;


import location.Location;
import media.Track;

import java.util.NoSuchElementException;

public interface MediaLibrary {
    // Add methods to this interface as you discover the AudioGuide needs them

    Track getTrackByLocation(Location newLocation) throws NoSuchElementException;

    Boolean locationHasTrack(Location location) throws NoSuchElementException;

}
