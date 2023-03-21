package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.Random;

public class Shotshot {
    protected GroupMessageEvent event;
    storage st;
    Group group;
    Member member;
    Shotshot(GroupMessageEvent event, storage st) {
        this.event = event;
        this.st = st;
        group = event.getGroup();
        member = event.getSender();
    }
    static int have = 100;
    final static int rate = 16;
    void detect(String s) {
        Random random = new Random();
        if (s.equals("轮盘赌")) {
            int need = have * have / 200 + 300;
            if (st.getCoin(member.getId()) < need) {
                FirstModule.send(group, member.getNick() + "的筹码不够。当前需要筹码为" + need);
                return;
            }
            int rand = random.nextInt(100);
            if (rand >= rate) {
                FirstModule.send(group, member.getNick() + "没有中弹，赢得金币" + have + "，获得经验" + (have + 100)/2);
                st.addCoin(member.getId(), have);
                have += 100;
            }
            else {
                have = 100;
                FirstModule.send(group, member.getNick() + "中弹了！！！失去金币" + need +"，获得经验" + have / 2);
                st.addCoin(member.getId(), - need);
            }
            st.addEXP(member, have / 2, group);
        }
        if (s.equals("轮盘赌帮助")) {
            FirstModule.send(group, "发送轮盘赌以进行游戏。每次发送会获得当前的筹码，并且增加筹码。中弹即损失大量金币！");
        }
        if (s.equals("轮盘赌查看")) {
            FirstModule.send(group, "当前的筹码为" + have);
        }
    }
}
