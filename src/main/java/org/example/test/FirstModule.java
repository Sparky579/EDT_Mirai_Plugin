package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

public class FirstModule extends SimpleListenerHost {
    Map<Long, Integer> map;
    Map<Long, Long> dateMap = new HashMap<>();
    Map<Long, Long> responseMap = new HashMap<>();
    static storage st;
    static long[] master = new long[]{1779894826, 480485539};
    Properties properties;
    public static boolean isInteger(String str) {
        if(str.equals("")) return  false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    static void send(Group group, String string) {
        try {
            Random random = new Random();
            Thread.currentThread().sleep(1000 + random.nextInt(800));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        group.sendMessage(string);
    }
    static void send(User member, String string) {
        try {
            Random random = new Random();
            Thread.currentThread().sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        member.sendMessage(string);
    }
    @EventHandler
    private ListeningStatus onEvent(GroupMessageEvent event) {
        String s = event.getMessage().contentToString();
        if (s.length() < 2) return ListeningStatus.LISTENING;
        String name = event.getSender().getNick();
        Group group = event.getGroup();
        long ID = event.getSender().getId();
        if (!responseMap.containsKey(ID));
        else if (new Date().getTime() - responseMap.get(ID) < 2000) return ListeningStatus.LISTENING;
        responseMap.put(ID, new Date().getTime());
        if (event.getSender().getNameCard().length() > 1) name = event.getSender().getNameCard();
        if (st == null || map == null || !map.containsKey(event.getSender().getId())) {
            if (st == null) st = new storage();
            if (map == null) map = new HashMap<>();
            properties = st.properties;
            if (!map.containsKey(event.getSender().getId()))
                map.put(event.getSender().getId(), 0);
            try {
                if (st.properties.getProperty(ID + ".coin") == null)
                st.properties.setProperty(event.getSender().getId() + ".coin", "500");
            //    System.out.println(properties);
                st.write();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        GameEngine gameEngine = new NullGame(event, st);
        gameEngine.playing(s);
        GameEngine gameEngine1 = gameEngine.gameType(GameEngine.gameType);
        Twentiefour twentiefour = new Twentiefour(event, st);
        twentiefour.detect(s);
        GuessNumber guessNumber = new GuessNumber(event, st);
        guessNumber.detect(s);
        PlainGuess plainGuess = new PlainGuess(event, st);
        plainGuess.detect(s);
        Shotshot shotshot = new Shotshot(event, st);
        shotshot.detect(s);
        Stock stock = new Stock(event, st);
        stock.detect(s);
        Achievements achievements = new Achievements(event, st);
        achievements.detect();
        if (gameEngine1 != null) gameEngine1.detect(s);

        if (s.equals("帮助")) {
            send(group, "帮助：输入金币查看自己的金币，赌博X进行赌博（如赌博100赌100金币），" +
            "转账（qq号） （金额）进行转账，输入“抽卡”进行普通抽卡，100一次，“十连”花费1000，“梦幻抽卡”花费510" +
                    "梦幻十连花费5100，至尊抽卡花费4180，至尊十连花费41800。神奇单抽1266，神奇十连12660。" );
            send(group, "扔漂流瓶 XXX，捡漂流瓶 捡起海中的一个瓶子（可以跨群），跳进海里让海里出现自己");
            send(group, "给bot送XXX可以增加好感度，不同好感度输入 好感度 会有不同反应哦\n如果你没钱了，试试指令“V我50”让bot" +
                    "给你赞助！但前提是bot喜欢你……");
            send(group, "输入打劫(QQ号)可以打劫其他人！成功率和战斗力有关哦，输入“雇佣卫兵”可以花5000金币在12小时内增加防御力。输入“雇佣刺客”可以花2000金币在5次打劫内增加攻击力。");
        }
        int level = st.getLevel(ID);
        if (s.equals("签到")) {
            long val = new Date().getTime() / 1000;
            if (!dateMap.containsKey(ID) || val - dateMap.get(ID) > 3600 * 3) {
                long val1 = ((long)(Math.pow(level, 2.5)) * 3 + 333);
                send(group, name + "签到成功！获得" + val1 + "金币！获得" + (val1 / 3) + "经验！");
                st.addCoin(ID, val1);
                st.addEXP(event.getSender(), val1 / 3, group);
                dateMap.put(ID, val);
            }
            else {
                send(group, name+"还有" + (3600 * 3 - (val - dateMap.get(ID))) + "秒才能下一次签到！");
            }
        }
        if (s.equals("雇佣卫兵") || s.equals("雇佣守卫")) {
            if (st.getCoin(ID) < 5000) send(group, "你的钱不足以雇佣卫兵！雇佣12小时需要5000金币。");
            else {
                st.addCoin(ID, -5000);
                st.setGuard(ID);
                send(group, "雇佣成功！");
            }
        }
        if (s.equals("雇佣刺客")) {
            if (st.getCoin(ID) < 2000) send(group, "你的钱不足以雇佣刺客！雇佣5次需要2000金币。");
            else {
                st.addCoin(ID, -2000);
                st.setASS(ID);
                send(group, "雇佣成功！剩余次数为：5次！");
            }
        }
        if (s.equals("jf") || s.equals("金币") || s.equals("积分")) {
//            send(group, st.properties.toString());
//            send(group, ID + ".coin");
//            send(group, String.valueOf(Long.parseLong(st.properties.getProperty(ID + ".coin"))));
            group.sendMessage("你有" + st.getCoin(event.getSender().getId()) + "金币");
        }
        if (s.charAt(0)=='转' || s.charAt(1) == '帐') {
            String[] split1 = s.split(" ");
            if (split1.length == 2) {
                if (!isInteger(split1[0].substring(2))) return ListeningStatus.LISTENING;
                long ID1 = Long.parseUnsignedLong(split1[0].substring(2));
                long val = Long.parseUnsignedLong(split1[1]);
                if (st.getCoin(ID) < val || val > 100000000 || val < 0) {
                    send(group, "没钱转账什么");
                }
                else {
                    st.addCoin2(ID, -val);
                    st.addCoin(ID1, val * 19 / 20);
                    send(group, "转账成功");
                    send(group, "你有" + st.getCoin(event.getSender().getId()) + "金币");
                }
            }
        }
        if (s.charAt(0)=='增' && s.charAt(1) == '加') {
            if (ID != 1779894826) send(group, "你不是bot的主人捏");
            else {
                String[] split1 = s.split(" ");
                if (split1.length == 2) {
                    long ID1 = Long.parseLong(split1[0].substring(2));
                    long val = Long.parseLong(split1[1]);
                        st.addCoin(ID1, val);
                        send(group, "增加成功");
                        send(group, "对方有" + st.getCoin(ID1) + "金币");
                }
            }
        }
        if (s.startsWith("设置等级")) {
            if (ID != 1779894826) send(group, "你不是bot的主人捏");
            else {
                String[] split1 = s.split(" ");
                if (split1.length == 2) {
                    long ID1 = Long.parseLong(split1[0].substring(4));
                    int val = Integer.parseInt(split1[1]);
                    st.addEXP(ID1, storage.requireEXP(val-1) + 1 - st.getEXP(ID1));
                    send(group, "设置成功");
                }
            }
        }
        if (s.startsWith("设置伪造币")) {
            if (ID != 1779894826) send(group, "你不是bot的主人捏");
            else {
                String[] split1 = s.split(" ");
                if (split1.length == 2) {
                    long ID1 = Long.parseLong(split1[0].substring(5));
                    long val = Long.parseLong(split1[1]);
                    st.addStat(ID1, "wzcoin", - st.getStat(ID1, "wzcoin") + val);
                    send(group, "设置成功");
                }
            }
        }
        if (s.startsWith("伪造")) {
            //格式为伪造1234567890 XXX YYY
            s = s.substring(2);
            s = s.trim();
            long resCoin = st.getStat(ID, "wzcoin");
            if (resCoin < 1) {
                send(group, "你的伪造次数不足！请联系bot的主人！");
                return ListeningStatus.LISTENING;
            }

            String s0, s1 = "空白消息";
            long ID1 = 0;
            String nick = null;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    s0 = s.substring(0, i);
                    s1 = s.substring(i + 1);
                    if (isInteger(s0)) {
                        ID1 = Long.parseLong(s0);
                    }
                    for (int j = i + 1; j < s.length(); j++) {
                        if (s.charAt(j) == ' ') {
                            s1 = s.substring(j + 1);
                            nick = s.substring(i + 1, j);
                            break;
                        }
                    }
                    break;
                }
            }
            if (ID1 == 0) {
                send(group, "伪造失败！请检查格式！");
            }
            else if (ID1 == 1779894826) {
                send(group, "禁止伪造bot的主人!");
            }
            else {
                if (nick == null) nick = String.valueOf(ID1);
                ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(group);
                forwardMessageBuilder.add(ID1, nick, new PlainText(s1));
                ForwardMessage forwardMessage = forwardMessageBuilder.build();
                group.sendMessage(forwardMessage);
            }
        }
        if (s.charAt(0) == '查' && s.charAt(1) == '询') {
            String split1 = s.substring(2);
            long ID1;
            if (s.length() == 2) split1 = null;
            if (split1 == null || isInteger(split1)) {
                if (split1 != null) ID1 = Long.parseLong(split1);
                else ID1 = ID;
                int lv = st.getLevel(ID1);
                StringBuffer sb = new StringBuffer("查询成功！\n查询对象数据为" + st.getCoin(ID1) + "金币，等级为"+ lv + "级【" + st.getStat(ID1, "zhuan") + "】转，战斗力为" + st.getPower(ID1) + "。头衔为" + storage.getStat(lv) + "。");
                if (split1 == null) {
                    sb.append("\n经验为：" + st.getEXP(ID1) + "/" + (storage.requireEXP(lv + 1)) + "\n距离升级还差" + (storage.requireEXP(lv + 1) - st.getEXP(ID1)) + "经验。");
                }
//                if (st.withGuard(ID1) > 0)
                    sb.append("\n守卫还能保护TA的秒数为：" + st.withGuard(ID1));
                send(group, sb.toString());
            }
        }
        if (s.equals("转生")) {
            StringBuffer stringBuffer = new StringBuffer("转生系统(15级解锁)：\n");
            stringBuffer.append("转生可以提升你的战斗力，但需要耗费你的等级，而且有概率失败\n");
            stringBuffer.append("当前转生等级为" + st.getStat(ID, "zhuan") + "转，成功概率为：\n");
            int lv = st.getLevel(ID);
            int rateX = (int) (Math.pow(lv, 3) / 1000);
            int rateY = 100 * (int) Math.pow(1.18, st.getStat(ID, "zhuan"));
            int rate = rateX * 100 / rateY;
            if (rate > 90) rate = 90;
            stringBuffer.append(rate + "%\n");
            stringBuffer.append("确定请输入：确认转生");
            send(group, stringBuffer.toString());
        }
        if (s.equals("确认转生")) {
            //降低3级等级（exp），提升一级转生等级（zhuan）
            int lv = st.getLevel(ID);
            if (lv < 15) send(group, "你的等级不足以转生");
            else {
                st.addEXP(ID, -storage.requireEXP(lv) + storage.requireEXP(lv - 1));
                Random random = new Random();
                int v = random.nextInt(100);
                int rateX = (int) (Math.pow(lv, 3) / 1000);
                int rateY = 100 * (int) Math.pow(1.18, st.getStat(ID, "zhuan"));
                long zhuan = st.getStat(ID, "zhuan");
                rateX = rateX * rateX;
                rateY = rateY * rateY;
                int rate = rateX * 100 / rateY;
                if (rate > 90) rate = 90;
                int derate = 100 - rate;
                int DDrate = (int)(derate * (zhuan + 1) / (zhuan + 4));
                if (v < rate / 3 && v < 5) {
                    st.addStat(ID, "zhuan", 1);
                    //add back double of exp
                    st.addEXP(ID, storage.requireEXP(lv + 1) - storage.requireEXP(lv - 1));
                    //输出提示
                    send(group, "转生大成功！你的转生等级提升了1级！你的经验恢复了2倍！！");
                }
                else if (v < rate) {
                    st.addStat(ID, "zhuan", 1);
                    send(group, "转生成功！你的转生等级提升了1级！你的等级降低了1级！你的战斗力大幅提升了");
                }
                else if (v < 100 - DDrate && v < 98) send(group, "转生失败！你的等级降低了1级！");
                else if (v < 98){
                    //转生等级也降低一级
                    st.addStat(ID, "zhuan", -1);
                    send(group, "太倒霉了，转生失败！你的等级降低了2级！你的转生等级降低了1级！");
                    st.addEXP(ID, -storage.requireEXP(lv) + storage.requireEXP(lv - 1));
                }
                else {
                    //转生等级也降低2级
                    st.addStat(ID, "zhuan", -2);
                    send(group, "太倒霉了，转生大失败！你的等级降低了2级！你的转生等级降低了2级！");
                    st.addEXP(ID, -storage.requireEXP(lv) + storage.requireEXP(lv - 1));
                }
                if (st.getStat(ID, "zhuan") < 0) st.addStat(ID, "zhuan", -st.getStat(ID, "zhuan"));
            }
        }
        if (s.length() > 2&&s.substring(0, 2).equals("赌博")) {
            if (!isInteger(s.substring(2))) return ListeningStatus.LISTENING;
            long val = Long.parseUnsignedLong(s.substring(2));
            if (st.getCoin(ID) < val || val > 100000000 || val < 0) {
                send(group, "没钱赌什么博");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            if (v < 32) {
                send(group, "运气不错，你赢回了" + val * 3 + "金币，获得经验" + val / 2);
                st.addCoin(ID, val * 2);
                st.addEXP(event.getSender(), val / 2, group);
            }
            else {
                send(group, "运气不佳，你的"+val+"金币输掉了");
                st.addCoin(ID, - val);
            }
        }
        if (s.equals("排行榜")) {
            send(group, "排行榜功能尚在制作中！");
        }
        if (s.startsWith("打劫")) {
            if (!isInteger(s.substring(2))) return ListeningStatus.LISTENING;
            long ID1 = Long.parseUnsignedLong(s.substring(2));
            if (ID == ID1) return ListeningStatus.LISTENING;
            long pw1 = st.getPower(ID);
            long pw2 = st.getPower(ID1);
            Random random = new Random();
            int A = 700;
            int val = random.nextInt(1000);
            if (st.getCoin(ID) > st.getCoin(ID1) * 20 || st.getCoin(ID1) < 300) {
                send(group, "对方的钱太少啦！");
                return ListeningStatus.LISTENING;
            }
            if (st.withGuard(ID1) > 0) {
                pw2 += 75;
                A = 500;
            }
            if (st.getASS(ID) > 0) {
                pw1 += 108;
                st.consumeASS(ID);
                if (st.getASS(ID) == 0) send(group, "你的刺客离开啦！");
            }
            for (int i = 1; i < master.length; i++) {
                if (master[i] == ID) pw1 *= 3;
                if (master[i] == ID1) pw2 *= 3;
            }
            if (master[0] == ID) pw1 *= 20;
            if (master[0] == ID1) pw2 *= 20;
            if (val < 400 * pw1 / pw2 && val < 750) {
                long rate = 10 + random.nextInt(10);
                long val1 = (1L + st.getCoin(ID1) * rate / 233);
                send(group, name+"打劫成功，恭喜你打劫到了金币：" + val1 + "\n对方获得了经验值：" + val1 / 3 );
                if (val1 < 0) val1 = 0;
                st.addCoin(ID, val1 * 11 / 12);
                st.addCoin(ID1, -val1);
                st.addEXP(ID1, val1 / 3);
            }
            else if (val < 588 * pw1 / pw2 && val < 950) {
                send(group, "打劫失败，对方跑掉了！" );
            }
            else {
                long rate = 10 + random.nextInt(10);
                long val1 = (1L + st.getCoin(ID) * rate / 233);
                if (val1 < 0) val1 = 0;
                send(group, name+"非但没有打劫成功，还被对方抢走了金币：" + val1
                + ((st.withGuard(ID1) > 0)?"。试试换个对方卫兵不在的时候再来吧！":"。"));
                st.addCoin(ID, -val1);
                st.addCoin(ID1, val1);
            }

        }
        if (s.equals("抽卡") || s.equals("十连")) {
            int typ = 1;
            if (s.equals("十连")) typ = 10;
            if (st.getCoin(ID) < 100 * typ) {
                send(group, "抽一发100，没钱抽什么卡");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            StringBuffer sb = new StringBuffer("恭喜你抽中了：");
            long tot = 0;
            for (int i = 1; i <= typ; i++) {
                v = random.nextInt(100);
                if (v < 1) {
                    sb.append("SSR+ ");
                    tot += 2500;
                }
                else if (v < 2) {
                    sb.append("SSR ");
                    tot += 700;
                }
                else if (v < 7) {
                    sb.append("SR+ ");
                    tot += 350;
                }
                else if (v < 17) {
                    sb.append("SR ");
                    tot += 200;
                }
                else if (v < 49) {
                    sb.append("R ");
                    tot += 100;
                }
                else {
                    sb.append("空气 ");
                }
            }
            send(group, sb.toString());
            send(group, name + "的欧气为" + tot +"，获得等额的金币！你的消费为" + 100 * typ + "金币！获得" + 14 * typ + "经验！");
            st.addEXP(event.getSender(), 14 * typ, group);
            st.addCoin(ID, tot - 100 * typ);
        }
        if (s.equals("梦幻抽卡") || s.equals("梦幻十连")) {
            int typ = 1;
            if (s.equals("梦幻十连")) typ = 10;
            if (st.getCoin(ID) < 510 * typ) {
                send(group, "抽一发510，没钱抽什么卡");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            StringBuffer sb = new StringBuffer("恭喜你抽中了：");
            long tot = 0;
            for (int i = 1; i <= typ; i++) {
                v = random.nextInt(100);
                if (v < 1) {
                    sb.append("UR ");
                    tot += 8000;
                }
                else if (v < 6) {
                    sb.append("SSR+ ");
                    tot += 2500;
                }
                else if (v < 28) {
                    sb.append("SSR ");
                    tot += 700;
                }
                else if (v < 52) {
                    sb.append("SR+ ");
                    tot += 350;
                }
                else if (v < 76) {
                    sb.append("SR ");
                    tot += 200;
                }
                else {
                    sb.append("R ");
                    tot += 100;
                }
            }
            send(group, sb.toString());
            send(group, name + "的欧气为" + tot +"，获得等额的金币！你的消费为" + 510 * typ + "金币！获得" + 67 * typ + "经验！");
            st.addEXP(event.getSender(), 67 * typ, group);
            st.addCoin(ID, tot - 510 * typ);
        }
        if (s.equals("至尊抽卡") || s.equals("至尊十连")) {
            int typ = 1;
            if (s.equals("至尊十连")) typ = 10;
            if (st.getCoin(ID) < 4180 * typ) {
                send(group, "抽一发4180，没钱抽什么卡");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            StringBuffer sb = new StringBuffer("恭喜你抽中了：");
            long tot = 0;
            for (int i = 1; i <= typ; i++) {
                v = random.nextInt(100);
                if (v < 1) {
                    sb.append("MR ");
                    tot += 60000;
                }
                else if (v < 9) {
                    sb.append("UR+ ");
                    tot += 15000;
                }
                else if (v < 21) {
                    sb.append("UR ");
                    tot += 8000;
                }
                else if (v < 70) {
                    sb.append("SSR+ ");
                    tot += 2500;
                }
                else if (v < 90) {
                    sb.append("SSR ");
                    tot += 700;
                }
                else {
                    sb.append("SR+ ");
                    tot += 350;
                }
            }
            send(group, sb.toString());
            send(group, name + "的欧气为" + tot +"，获得等额的金币！你的消费为" + 4180 * typ + "金币！获得" + 788 * typ + "经验！");
            st.addEXP(event.getSender(), 788 * typ, group);
            st.addCoin(ID, tot - 4180 * typ);
        }
        if (s.equals("神奇抽卡") || s.equals("神奇十连")) {
            int typ = 1;
            if (s.equals("神奇十连")) typ = 10;
            if (st.getCoin(ID) < 1266 * typ) {
                send(group, "抽一发1266，没钱抽什么卡");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            StringBuffer sb = new StringBuffer("恭喜你抽中了：");
            long tot = 0;
            for (int i = 1; i <= typ; i++) {
                v = random.nextInt(1000);
                if (v < 7) {
                    sb.append("MR ");
                    tot += 60000;
                }
                else if (v < 100) {
                    sb.append("UR ");
                    tot += 8000;
                }
                else {
                    sb.append("空气 ");
                }
            }
            send(group, sb.toString());
            send(group, name + "的欧气为" + tot +"，获得等额的金币！你的消费为" + 1266 * typ + "金币！获得" + 377 * typ + "经验！");
            st.addEXP(event.getSender(), 377 * typ, group);
            st.addCoin(ID, tot - 1266 * typ);
        }
        if (s.equals("无敌抽卡") || s.equals("无敌十连")) {
            int typ = 1;
            if (s.equals("无敌十连")) typ = 10;
            if (st.getCoin(ID) < 20000 * typ) {
                send(group, "抽一发20000，没钱抽什么卡");
                return ListeningStatus.LISTENING;
            }
            Random random = new Random();
            int v = random.nextInt(100);
            StringBuffer sb = new StringBuffer("恭喜你抽中了：");
            long tot = 0;
            for (int i = 1; i <= typ; i++) {
                v = random.nextInt(100);
                if (v < 1) {
                    sb.append("Eternal ");
                    tot += 99999;
                }
                else if (v < 16) {
                    sb.append("MR ");
                    tot += 60000;
                }
                else if (v < 36) {
                    sb.append("UR+ ");
                    tot += 15000;
                }
                else {
                    sb.append("UR ");
                    tot += 8000;
                }
            }
            send(group, sb.toString());
            send(group, name + "的欧气为" + tot +"，获得等额的金币！你的消费为" + 20000 * typ + "金币！获得" + 2588 * typ + "经验！");
            st.addEXP(event.getSender(), 2588 * typ, group);
            st.addCoin(ID, tot - 20000 * typ);
        }
        if (s.substring(0, 2).equals("氪金")) {
            if (!isInteger(s.substring(2))) return ListeningStatus.LISTENING;
            long val = Long.parseUnsignedLong(s.substring(2));
            if (val > 100000000 || val < 0) return ListeningStatus.LISTENING;
            if (st.getCoin(ID) < val) {
                send(group, name + "金币不够了！");
            }
            else {
                send(group, "氪金成功！获得经验值：" + val * 2 / 3);
                st.addEXP(event.getSender(), val * 2 / 3, group);
                st.addCoin(ID, -val);
            }
        }
        if (s.equals("V我50") || s.equals("v我50")) {
            int value = map.get(event.getSender().getId());
            if (value < -10) {
                send(group, "bot 不喜欢你！才不给你呢！");
            }
            else if (value < 100) {
                if (st.getCoin(ID) < 300) {
                    send(group, "好吧，botV你50 (好感度Down)");
                    st.addCoin(event.getSender().getId(), 50);
                    map.put(event.getSender().getId(), value - 15);
                } else {
                    send(group, "你太有钱啦！V bot 50 (好感度Up)");
                    st.addCoin(event.getSender().getId(), -50);
                    map.put(event.getSender().getId(), value + 10);
                }
            }
            else if (value < 300) {
                if (st.getCoin(ID) < 800) {
                    send(group, "不用客气，bot V你200！");
                    st.addCoin(event.getSender().getId(), 200);
                    map.put(event.getSender().getId(), value - 30);
                } else {
                    send(group, "你太有钱啦！V bot 50 (好感度Up)");
                    st.addCoin(event.getSender().getId(), -50);
                    map.put(event.getSender().getId(), value + 10);
                }
            }
            else if (value < 800) {
                if (st.getCoin(ID) < 2000) {
                    send(group, "不用客气，bot V你500！");
                    st.addCoin(event.getSender().getId(), 500);
                    map.put(event.getSender().getId(), value - 50);
                } else {
                    send(group, "你太有钱啦！V bot 50 (好感度Up)");
                    st.addCoin(event.getSender().getId(), -50);
                    map.put(event.getSender().getId(), value + 10);
                }
            }
            else {
                if (st.getCoin(ID) < 5000) {
                    send(group, "不用客气，bot V你 2000！");
                    st.addCoin(event.getSender().getId(), 2000);
                    map.put(event.getSender().getId(), value - 135);
                } else {
                    send(group, "你太有钱啦！V bot 50 (好感度Up)");
                    st.addCoin(event.getSender().getId(), -50);
                    map.put(event.getSender().getId(), value + 20);
                }
            }
            group.sendMessage("你有" + st.getCoin(event.getSender().getId()) + "金币");
        }
        else if (s.contains("vme") || s.contains("v我") || s.contains("V我")){
            event.getGroup().sendMessage("名卡V你50");
        }
        else if (s.equals("明日运势")) {
            Random random = new Random();
            event.getGroup().sendMessage(name + "的明日运势为：" + random.nextInt(100));
        }
        else if (s.contains("涩涩") && ! s.contains("不")) {
            event.getGroup().sendMessage("不可以涩涩哦！");
            event.getGroup().sendMessage("(扭头)");
        }
        else if (s.equals("好感度")) {
            int value = map.get(event.getSender().getId());
            if (value < -10) send(group, "你过来干什么呀！bot不喜欢你！");
            else if (value < 60) event.getGroup().sendMessage(name + " 诶？" + name + "你好呀~（挥手）");
            else if (value < 300) event.getGroup().sendMessage(name + "你也在啊，要跟 bot 一起出去吃一顿吗？");
            else if (value < 800) event.getGroup().sendMessage(name + "，你来啦！要跟 bot 一起看电影吗？");
            else {
                event.getGroup().sendMessage("bot 最喜欢"+name+"啦！");
                event.getGroup().sendMessage("凑上前去，蹭蹭" + name);
            }
        }
        else if (s.startsWith("给bot送")) {
            st.addStat(ID, "gift", 1);
            Random random = new Random();
            int add = 7 + random.nextInt(10);
            int val = map.get(event.getSender().getId());
            if (s.contains("名卡")) {
                send(group, "bot 不喜欢名卡！");
                int v = val - add;
                if (v > 0) v = v * 3 / 4;
                map.put(event.getSender().getId(), v);
            }
            else if (s.contains("送终") || s.contains("TNT") || s.contains("葬")) {
                send(group, "开什么玩笑呢！");
                int v = val - 2 * add;if (v > 0) v /= 2;
                map.put(event.getSender().getId(), v);
            }
            else if (s.contains("项圈") && (group.getId() == 877235796 || group.getId() == 860780884)) {
                send(group, "啊啊？你真的要给我送这个吗？");
                send(group, "才不是喜欢这样的东西，只是好奇戴一下呢（低头）");
                int v = val + 6 * add;
                map.put(event.getSender().getId(), v);
            }
            else if (s.contains("尿") || s.contains("屎") || s.contains("屁") || s.contains("憨") || s.contains("烂") || s.contains("坨") || s.contains("答辩")) {
                int v = val - 2 * add;
                event.getGroup().sendMessage("这是什么，好恶心！");
                if (v > 0) v /= 2;
                map.put(event.getSender().getId(), v);
            }
            else if (s.contains("巧克力") || s.contains("果") || s.contains("糖") || s.contains("肉") || s.contains("奶")) {
                event.getGroup().sendMessage("(一口吞下去) 好耶，还有吗？");
                map.put(event.getSender().getId(), val + 2 * add);
            }
            else if (s.contains("pe") || s.contains("mcp") || s.contains("MCP") || s.contains("星芒") || s.contains("MCp")) {
                send(group, "收下并调教一番捏");
                map.put(event.getSender().getId(), val + 2 * add);
            }
            else if (s.contains("凯文") || s.contains("kevin") || s.contains("Kevin") || s.contains("送bot") || s.contains("空气")) {
                event.getGroup().sendMessage("你这家伙，在消遣我吗？");
                int v = val;
                if (v > 100) v -= add;
                map.put(event.getSender().getId(), v);
            }
            else if (s.contains("kEvIn579")) {
                map.put(event.getSender().getId(), val + 99);
            }
            else if (s.contains("清零")) {
                event.getGroup().sendMessage("恭喜你触发了彩蛋！");
                map.put(event.getSender().getId(), 0);
            }
            else {
                event.getGroup().sendMessage("这是什么？虽然认不出来，但是谢谢你！");
                map.put(event.getSender().getId(), val + add);
            }
            val = map.get(event.getSender().getId());
            send(group, "bot 对 " + name + " 的好感度为 " + val + "。你获得了经验：" + Math.min(400, val / 5));
            st.addEXP(event.getSender(), Math.min(400, val / 5), group);
        }
        else if (s.contains("名卡") || s.equals("打卡")) {
            Random random = new Random();
            int v = random.nextInt(2);
            if (v == 1) {
                event.getGroup().sendMessage("bot 的评价：名卡就是个憨批。恭喜你获得经验：" + level * 18L);
                st.addEXP(event.getSender(), level * 18L, group);
            }
        }

        if (s.startsWith("扔漂流瓶 ")) {
            st.addStat(ID, "throw", 1);
            Random random = new Random();
            if (random.nextInt(5) == 0) {
                send(group, "恭喜你扔漂流瓶砸到了一条大鱼！获得经验：" + level * 80);
                st.addEXP(event.getSender(), level * 80L, group);
            }
        }
        if (s.equals("捡漂流瓶")) {
            st.addStat(ID, "pick", 1);
            Random random = new Random();
            int ss = random.nextInt(8);
            if (ss == 0) {
                send(group, "恭喜你捡到一大包 金币！获得经验："+ level * 99 + "，获得金币：" + (level * level * 4 + 100));
                st.addEXP(event.getSender(), level * 99L, group);
                st.addCoin(ID, level * level +100);
            }
            else if (ss == 1) {
                send(group, "恭喜你捡到一大包……炸弹！扣除经验：" + level * 132L);
                st.addEXP(event.getSender(), -level * 222L, group);
            }
        }
        if (s.equals("跳进海里") || s.equals("跳海")) {
            st.addStat(ID, "jump", 1);
            Random random = new Random();
            int ss = random.nextInt(8);
            if (ss == 0) {
                send(group, "你感觉你的潜水技能提升了！获得经验：" + level * 133);
                st.addEXP(event.getSender(), level * 133L, group);
            }
            else if (ss == 1) {
                send(group, "恭喜你撞到了炸弹！扣除金币:" + level * 133);
                st.addCoin(ID, -level * 166);
            }
        }
        if (s.contains("or")) {
            Random random = new Random();
            String[] strings = s.split("or");
            int num = strings.length;
            String v = strings[random.nextInt(num)];
            send(group, "bot 想好了！" + v);
        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler
    private ListeningStatus onEvent(FriendMessageEvent event) {

        if (st == null || map == null || !map.containsKey(event.getSender().getId())) {
            if (st == null) st = new storage();
            if (map == null) map = new HashMap<>();
            properties = st.properties;
            if (!map.containsKey(event.getSender().getId()))
                map.put(event.getSender().getId(), 0);
            try {
                if (st.properties.getProperty(event.getSender().getId() + ".coin") == null)
                    st.properties.setProperty(event.getSender().getId() + ".coin", "500");
                //    System.out.println(properties);
                st.write();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        GameEngine gameEngine = new NullGame(event, st);
        GameEngine gameEngine1 = gameEngine.gameType(GameEngine.gameType);
        gameEngine1.detectFriend(event.getMessage().contentToString());

        return ListeningStatus.LISTENING;
    }
}
