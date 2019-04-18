package com.gmail.luxdevpl.fbot.event;

import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

public interface IEventHandler<T> {

    void handleEvent(T eventClass, IBotWrapper wrapper);

    EventTypes getEventType();
}
