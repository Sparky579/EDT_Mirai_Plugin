package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class SmallNumberGame extends GameEngine{
    GroupMessageEvent event;
    FriendMessageEvent friendMessageEvent;
    final int playersNeed = 2;
    static Properties properties = new Properties();
    static int round, ready;
    Group group;
    User member;
    storage st;
    void start() {
        properties = new Properties();
        memberList = new ArrayList<>();
    }
    void broadcast(float average, float mini) {
        if (average > -0.2) {
            for (Group group1: groups) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FirstModule.send(group1, "本局的平均值为：" + (average * 3.00 / 2) + "\n2/3 为 " +average + '\n' +  "最小的差距为：" + mini);
            }
        }
        if (average > 0) {
            StringBuffer stringBuffer = new StringBuffer("当前游戏积分：\n");
            for (User member1 : memberList) {
                stringBuffer.append(member1.getNick() + "：" +
                        String.format("%.2f", Float.parseFloat(properties.getProperty("C" + member1.getId()))));
                stringBuffer.append("，发送了" + Integer.parseInt(properties.getProperty("B" + member1.getId())));
                stringBuffer.append('\n');
            }
            for (Group group1 : groups) {
                FirstModule.send(group1, stringBuffer.toString());
                //    FirstModule.send(group1, group1.toString() + round + memberList.toString());//debug
            }
        }
        if (average < -0.2) {
            StringBuffer stringBuffer1 = new StringBuffer("奖励金币\n");
            for (User member1: memberList) {
                float val = Float.parseFloat(properties.getProperty("C" + member1.getId()));
                long val1 = (long)(val * val * 75) +  (long)(val * val) * st.getLevel(member1.getId()) * st.getLevel(member1.getId()) / 2;
                stringBuffer1.append(member1.getNick() + "：" + (long)(val1));

                st.addCoin(member1.getId(), (long)(val1));
                stringBuffer1.append("，当前金币为" + st.getCoin(member1.getId()));
                stringBuffer1.append('\n');
            }
            for (Group group1: groups) {
                FirstModule.send(group1, stringBuffer1.toString());
            }
        }
    }
    void end() {
        for (Group group1: groups) {
            FirstModule.send(group1, "数字漩涡游戏已结束！");
        }
        broadcast(-1, -1);
        properties = new Properties();
        round = 0;
        ready = 0;
        super.end();
    }
    SmallNumberGame(GroupMessageEvent event, storage st) {
        super(event, st);
        this.event = event;
        this.st = st;
        group = event.getGroup();
        member = event.getSender();
    }//need super.end
    SmallNumberGame(FriendMessageEvent event, storage st) {
        super(event, st);
        this.friendMessageEvent = event;
        this.st = st;
        member = event.getSender();
    }
    @Override
    void detect(String s) {
        if (s.equals("开始游戏") &&GameEngine.playing == 1) {
            if (playersNeed > GameEngine.nowPlayer) {
                FirstModule.send(group, "人数不足，无法开始");
            }
            else {
                int x = 0;
                for (Group group1: groups) {
                //    FirstModule.send(group1, groups.toString() + "  " + group1.getName() + " " + x);
                    x++;
                    FirstModule.send(group1, "已开始数字漩涡！");
                    FirstModule.send(group1, "游戏规则：每个人给 bot 私聊发送一个数字(0到100)，每局最接近" +
                            "所有人平均数的2/3的玩家胜利，获得等同于人数的积分，若多人相同则减少。另外" +
                            "玩家发送的数字较大，可以获得奖励分。奖励为数字*120。\nSupported By Kevin327");
                }

                GameEngine.playing = 2;
                round = 0;
                oneRound();
            }
        }
        if (s.equals("结束回合")) {
            boolean flag = false;
            for (Group group1: groups) {
                FirstModule.send(group1, "有人进行结束，当前准备就绪的玩家为：" + ready);
                try {
                    Random random = new Random();
                    Thread.sleep(200 + random.nextInt(300));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ready >= nowPlayer) {
                    flag = true;
                }
                else FirstModule.send(group1, "还有玩家没有发送数字！");
            }
            if (flag) allOK();
        }
    }
    void detectFriend(String s) {
        if (properties.contains("B" + member.getId())) return;
        if (!FirstModule.isInteger(s) || s.length() > 3) {

//            FirstModule.send(member, "请发送一个0到100之间的整数！");
            return;
        }
        int val = Integer.parseInt(s);
        if (val < 0 || val > 100) {
//            FirstModule.send(member, "请发送一个0到100之间的整数！");
            return;
        }
        properties.setProperty("B" + member.getId(), "" + val);
        ready++;
        boolean CAN;
        try {
            Random random = new Random();
            Thread.sleep(100 + random.nextInt(300));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   //     FirstModule.send(member, "成功！当前发送完毕的人数为：" + ready);
        for (Group group1: groups) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FirstModule.send(group1, member.getNick() + "发送完毕了！");
        }
        if (ready == nowPlayer) {
            for (Group group1: groups) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                FirstModule.send(group1, "所有人发送完毕了！");
            }
        }
        if (ready == nowPlayer) {
            allOK();
        //    CANOPEN = true;
        }
    }
    void allOK() {
        ready = 0;
        float average = 0.0f, mini = 1000f;
        for (Member x: GameEngine.memberList) {
            average += Integer.parseInt(properties.getProperty("B" + x.getId()));
        }
        average /= nowPlayer;
        average = average * 2 / 3;
        int havePlayer = 0;
        for (Member x: GameEngine.memberList) {
            mini = Math.min(Math.abs(average - Integer.parseInt(properties.getProperty("B" + x.getId()))),
                    mini);
        }
        for (Member x: GameEngine.memberList) {
            if (Math.abs(average - Integer.parseInt(properties.getProperty("B" + x.getId()))) < mini + 0.001f)
                havePlayer ++;
        }
        float bonus = (float) ((0.0 + nowPlayer) / (Math.pow(havePlayer, 1.5)));
        for (Member x: GameEngine.memberList) {
        //    FirstModule.send(x, "" + Integer.parseInt(properties.getProperty("B" + x.getId())) + " " + Integer.parseInt(properties.getProperty("A" + x.getId())));//debug
            if (Math.abs(average - Integer.parseInt(properties.getProperty("B" + x.getId()))) < mini + 0.001f)
                addValue(properties, "C"+x.getId(), bonus);
//            if (Integer.parseInt(properties.getProperty("B" + x.getId())) ==
//                    75)
//                    //Integer.parseInt(properties.getProperty("A" + x.getId())))
//                addValue(properties, "C"+x.getId(), 1.0f);
            addValue(properties, "C"+x.getId(), 0.012f*Integer.parseInt(properties.getProperty("B" + x.getId())));
        }
        broadcast(average, mini);
        oneRound();
    }
    void oneRound() {
        Random random = new Random();
        round++;
        ready = 0;
        if (round < 4)
        for (Group group1: groups) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FirstModule.send(group1, "====第"+round+"局====");
        }
        for (Member x: GameEngine.memberList) {
            int num = 50 + random.nextInt(50);
            properties.setProperty("A"+x.getId(), String.valueOf(num));
            if (round == 1) properties.setProperty("C" + x.getId(), "0");
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (round != 4) {
                try {
                    Random random1 = new Random();
                    Thread.sleep(200 + random1.nextInt(300));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //    FirstModule.send(x, "第" + round + "局，" + "你这一局的幸运数字为：" + num);
            }
        }
        if (round >= 4) end();
    }
}
