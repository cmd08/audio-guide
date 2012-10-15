package location;
import gps.Position;
import gps.PositionListener;


public class LocationTracker implements PositionListener {
	private final ReverseGeocoder reverseGeocoder;
	private final LocationAware listener;
	
	private Location lastLocation = null;

	public LocationTracker(ReverseGeocoder reverseGeocoder, LocationAware listener) {
		this.reverseGeocoder = reverseGeocoder;
		this.listener = listener;
	}

	@Override
	public void positionChanged(Position newPosition) {
		Location location = reverseGeocoder.locationOf(newPosition);
		if (!location.equals(lastLocation)) {
			listener.locationChanged(location);
			lastLocation = location;
		}
	}
}
