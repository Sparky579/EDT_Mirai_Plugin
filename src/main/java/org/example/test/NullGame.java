package org.example.test;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class NullGame extends GameEngine{
    NullGame(GroupMessageEvent event, storage st) {
        super(event, st);
    }
    NullGame(FriendMessageEvent friendMessageEvent, storage st) {
        super(friendMessageEvent, st);
    }

    @Override
    void start() {

    }

    @Override
    void detect(String s) {

    }

    @Override
    void detectFriend(String s) {

    }
}
