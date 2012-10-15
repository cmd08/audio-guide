package location;

import gps.Position;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class LocationTrackerTest {
    Mockery context = new JUnit4Mockery();

	ReverseGeocoder reverseGeocoder = context.mock(ReverseGeocoder.class);
	LocationAware listener = context.mock(LocationAware.class);
	
	LocationTracker tracker = new LocationTracker(reverseGeocoder, listener);

    @Test
	public void reportsWhenFirstLocationDiscovered() {
		final Position position = new Position(1,2);
		final Location location = new Location("South Kensington");
		
		context.checking(new Expectations() {{
			allowing (reverseGeocoder).locationOf(position); will(returnValue(location));
			oneOf (listener).locationChanged(location);
		}});
		
		tracker.positionChanged(position);
	}

    @Test
	public void reportsWhenLocationChanges() {
		final Position position1 = new Position(1,2);
		final Position position2 = new Position(3,4);
		final Location location1 = new Location("South Kensington");
		final Location location2 = new Location("Gloucester Road");
		
		context.checking(new Expectations() {{
			allowing (reverseGeocoder).locationOf(position1); will(returnValue(location1));
			allowing (reverseGeocoder).locationOf(position2); will(returnValue(location2));
			
			oneOf (listener) .locationChanged(location1);
		}});
		
		tracker.positionChanged(position1);
		
		context.checking(new Expectations() {{
			oneOf (listener).locationChanged(location2);
		}});		
		
		tracker.positionChanged(position2);
	}

    @Test
	public void doesNotReportWhenLocationStaysTheSame() {
		final Position position1 = new Position(1,2);
		final Position position2 = new Position(3,4);
		final Location location = new Location("South Kensington");
		
		context.checking(new Expectations() {{
			allowing (reverseGeocoder).locationOf(position1); will(returnValue(location));
			allowing (reverseGeocoder).locationOf(position2); will(returnValue(location));
			oneOf (listener).locationChanged(location);
		}});
		
		tracker.positionChanged(position1);
		tracker.positionChanged(position2);
	}
}
