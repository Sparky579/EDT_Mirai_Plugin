package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class GameEngine {
    static int playing = 0;//0: end 1: waiting for start 2: playing
    static int gameType = 0;
    static int nowPlayer = 0;
    static boolean CANOPEN = false;
    static List<Group> groups = new ArrayList<>();
    static Properties properties = new Properties();
    static List<Member> memberList = new ArrayList<>();
    protected GroupMessageEvent event;
    protected FriendMessageEvent friendMessageEvent;
    static storage st;
    GameEngine(GroupMessageEvent event, storage st) {
        this.event = event;
        GameEngine.st = st;
    }
    GameEngine(FriendMessageEvent event, storage st) {
        this.friendMessageEvent = event;
        GameEngine.st = st;
    }
    abstract void start();
    static void addValue(Properties properties, String s, int val) {
        properties.setProperty(s,
                Integer.parseInt(properties.getProperty(s)) + val + "");
    }
    static void addValue(Properties properties, String s, float val) {
        properties.setProperty(s,
                Float.parseFloat(properties.getProperty(s)) + val + "");
    }
    void createGame(int x) {
        if (playing != 0) return;
        gameType = x;
        playing = 1;
        properties = new Properties();
        nowPlayer = 0;
        groups.clear();
        memberList.clear();
        FirstModule.send(event.getGroup(), "创建成功！");
    }
    GameEngine gameType(int x) {
        if (event == null) {
            if (x == 1) return new SmallNumberGame(friendMessageEvent, st);
        }
        else {
            if (x == 1) return new SmallNumberGame(event, st);
        }
        return null;
    }
    void help(String s) {
        if (s.equals("数字漩涡帮助")) {
            FirstModule.send(event.getGroup(), "每一轮");
        }
    }
    void playing(String s) {
        if (s.equals("结束当前游戏")) {
            end();
            FirstModule.send(event.getGroup(), "结束成功！");
        }
        if (s.equals("加入")) {
            if (!groups.contains(event.getGroup()))
                groups.add(event.getGroup());
            if (gameType == 0) return;
            if (properties.contains(event.getSender().getId()+"A"))
                FirstModule.send(event.getGroup(), event.getSender().getNick() + "加入失败！你已经加入了！");
            else {

                properties.setProperty(event.getSender().getId()+"A", "1");

                if (!memberList.contains(event.getSender())){
                memberList.add(event.getSender());
                nowPlayer++;
                FirstModule.send(event.getGroup(), event.getSender().getNick() + "加入成功！当前人数：" + nowPlayer);
                }
            }
        }
        if (s.equals("创建数字漩涡")) createGame(1);
    }
    void end() {
        GameEngine.playing = 0;
        properties = new Properties();
        nowPlayer = 0;
        GameEngine.gameType = 0;
        memberList = new ArrayList<>();
        groups = new ArrayList<>();
    }
    abstract void detect(String s);
    abstract void detectFriend(String s);
}
