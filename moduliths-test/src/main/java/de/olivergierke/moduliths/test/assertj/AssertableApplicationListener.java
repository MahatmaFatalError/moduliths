/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.olivergierke.moduliths.test.assertj;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.assertj.core.api.AssertProvider;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;

/**
 * @author Oliver Gierke
 */
public class AssertableApplicationListener
		implements AssertProvider<EventAsserts>, EventAsserts, ApplicationListener<ApplicationEvent> {

	private List<Object> events = new ArrayList<>();

	/* 
	 * (non-Javadoc)
	 * @see org.assertj.core.api.AssertProvider#assertThat()
	 */
	@Override
	public EventAsserts assertThat() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent event) {
		this.events.add(event);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.olivergierke.moduliths.test.assertj.EventAsserts#hasEventsFired(java.lang.Class, java.util.function.Consumer)
	 */
	@Override
	public <T> EventAsserts hasEventsFired(Class<T> eventType, Consumer<T> event) {

		List<T> publishedEvents = events.stream() //
				.map(it -> uwrapPayloadEvent(it)) //
				.filter(eventType::isInstance) //
				.map(eventType::cast) //
				.collect(Collectors.toList());

		if (publishedEvents.isEmpty()) {
			throw new IllegalStateException(String.format("No event of type %s published!", eventType));
		}

		publishedEvents.forEach(event::accept);

		return this;
	}

	private static Object uwrapPayloadEvent(Object source) {

		return PayloadApplicationEvent.class.isInstance(source) //
				? ((PayloadApplicationEvent<?>) source).getPayload() //
				: source;
	}
}
