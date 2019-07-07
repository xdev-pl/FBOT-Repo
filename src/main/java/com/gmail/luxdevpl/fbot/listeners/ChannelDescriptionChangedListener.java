package com.gmail.luxdevpl.fbot.listeners;

import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

public class ChannelDescriptionChangedListener implements IEventHandler<ChannelDescriptionEditedEvent> {


    @Override
    public void handleEvent(ChannelDescriptionEditedEvent e, IBotWrapper bot){
        /*if(!e.getInvokerUniqueId().isEmpty()) {
            if(bot.getFbotApi().getClientByUid(e.getInvokerUniqueId()).isRegularClient()) {
                ChannelInfo channelInfo = bot.getFbotApi().getChannelInfoById(e.getChannelId());

                StrBuilder description = new StrBuilder().append(channelInfo.getDescription());

                bot.getExtendedConfiguration().channelEditedListener.channelDescriptionFilter.forEach(entry -> description.replaceAll(entry, "{@FBOT: LINK/SŁOWO USUNIĘTE}"));

                bot.getFbotApi().editChannelDescription(e.getChannelId(), description.toStringBuilder());
            }
        }*/
    }

    @Override
    public EventTypes getEventType(){
        return EventTypes.CHANNEL_DESCRIPTION_EDITED_EVENT;
    }

}
