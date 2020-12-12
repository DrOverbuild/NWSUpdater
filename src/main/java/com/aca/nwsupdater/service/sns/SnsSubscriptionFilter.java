package com.aca.nwsupdater.service.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aca.nwsupdater.model.webapp.Alert;
import com.amazonaws.services.sns.model.SetSubscriptionAttributesRequest;

public class SnsSubscriptionFilter {
	private final Map<String, Attribute> filterPolicy = new HashMap<>();
	
    public void addAttribute(final String attributeName, final String attributeValue) {
        filterPolicy.put(attributeName, new Attribute<>(AttributeType.String, attributeValue));
    }

    public void addAttributes(final String attributeName, final List<String> attributeValues) {
        final ArrayList<Attribute> attributes = new ArrayList<>();
        for (final String s : attributeValues) {
            attributes.add(new Attribute<>(AttributeType.String, s));
        }
        filterPolicy.put(attributeName, new Attribute<>(AttributeType.List, attributes));
    }
	
    public void apply(final String subscriptionArn) {
        final SetSubscriptionAttributesRequest request =
                new SetSubscriptionAttributesRequest(subscriptionArn,
                        "FilterPolicy", formatFilterPolicy());
        SnsClient.getAwsClient().setSubscriptionAttributes(request);
    }

    private String formatFilterPolicy() {
        return filterPolicy.entrySet()
                .stream()
                .map(entry -> "\"" + entry.getKey() + "\": [" + entry.getValue() + "]")
                .collect(Collectors.joining(", ", "{", "}"));
    }
    
    private enum AttributeType {
        String, Numeric, Prefix, List, AnythingBut
    }
	
	private class Attribute<T> {
	    final T value;
	    final AttributeType type;

	    Attribute(final AttributeType type, final T value) {
	        this.value = value;
	        this.type = type;
	    }

	    public String toString() {
	        switch (type) {
	            case Prefix:
	                return String.format("{\"prefix\":\"%s\"}", value.toString());
	            case Numeric:
	                return String.format("{\"numeric\":%s}", value.toString());
	            case List:
	                final List list = (List)value;
	                final ArrayList<T> values = new ArrayList<T>(list);
	                return values
	                        .stream()
	                        .map(Object::toString)
	                        .collect(Collectors.joining(","));
	            case AnythingBut:
	                return String.format("{\"anything-but\":\"%s\"}", value);
	            default:
	                return String.format("\"%s\"", value);
	        }
	    }
	}
}


