package client.messages.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import client.messages.CommandProcessorUtil;
import constants.GameConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.handler.InterServerHandler;
import handling.login.handler.AutoRegister;
import handling.world.World;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import scripting.PortalScriptManager;
import scripting.ReactorScriptManager;
import server.CashItemFactory;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.Setting;
import server.games.BattleGroundGameHandler;
import server.games.BloomingRace;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.MobSkill;
import server.life.OverrideMonsterStats;
import server.life.PlayerNPC;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleReactor;
import server.maps.MapleReactorFactory;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.PacketHelper;

public class SuperGMCommand {
  public static ServerConstants.PlayerGMRank getPlayerLevelRequired() {
    return ServerConstants.PlayerGMRank.SUPERGM;
  }
  
  public static class ????????? extends InternCommand.??? {}
  
  public static class ??????????????? extends ????????? {}
  
  public static class ????????? extends CommandExecute {
    protected boolean hellban = false;
    
    private String getCommand() {
      if (this.hellban)
        return "???????????????"; 
      return "?????????";
    }
    
    public int execute(MapleClient c, String[] splitted) {
      byte ret;
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(6, "[Syntax] !" + getCommand() + " <IGN>");
        return 0;
      } 
      if (this.hellban) {
        ret = MapleClient.unHellban(splitted[1]);
      } else {
        ret = MapleClient.unban(splitted[1]);
      } 
      if (ret == -2) {
        c.getPlayer().dropMessage(6, "[" + getCommand() + "] SQL error.");
        return 0;
      } 
      if (ret == -1) {
        c.getPlayer().dropMessage(6, "[" + getCommand() + "] The character does not exist.");
        return 0;
      } 
      c.getPlayer().dropMessage(6, "[" + getCommand() + "] Successfully unbanned!");
      byte ret_ = MapleClient.unbanIPMacs(splitted[1]);
      if (ret_ == -2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
      } else if (ret_ == -1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
      } else if (ret_ == 0) {
        c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
      } else if (ret_ == 1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
      } else if (ret_ == 2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
      } 
      return (ret_ > 0) ? 1 : 0;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(6, "[Syntax] !unbanip <IGN>");
        return 0;
      } 
      byte ret = MapleClient.unbanIPMacs(splitted[1]);
      if (ret == -2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] SQL error.");
      } else if (ret == -1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] The character does not exist.");
      } else if (ret == 0) {
        c.getPlayer().dropMessage(6, "[UnbanIP] No IP or Mac with that character exists!");
      } else if (ret == 1) {
        c.getPlayer().dropMessage(6, "[UnbanIP] IP/Mac -- one of them was found and unbanned.");
      } else if (ret == 2) {
        c.getPlayer().dropMessage(6, "[UnbanIP] Both IP and Macs were unbanned.");
      } 
      if (ret > 0)
        return 1; 
      return 0;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dispelDebuffs();
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        MapleCharacter player = null;
        player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
        if (player != null) {
          byte number = Byte.parseByte(splitted[2]);
          player.setGMLevel(number);
          player.dropMessage(5, "[??????] " + splitted[1] + " ??????????????? GM?????? " + splitted[2] + " (???)??? ?????????????????????.");
        } 
        c.getPlayer().dropMessage(5, "[??????] " + splitted[1] + " ??????????????? GM?????? " + splitted[2] + " (???)??? ?????????????????????.");
      } 
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        MapleCharacter player = null;
        player = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
        if (player != null)
          player.getClient().getSession().writeAndFlush(CField.onUserTeleport(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]))); 
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 5) {
        c.getPlayer().dropMessage(6, "!??????????????? <?????????> <??????> <???> <??????>");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "?????? ????????? ??????????");
      } else {
        int t = Integer.parseInt(splitted[2]);
        String key = splitted[3];
        String value = splitted[4];
        chrs.setKeyValue(t, key, value);
      } 
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 5) {
        c.getPlayer().dropMessage(6, "!????????????????????? <?????????> <???> <??????>");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "?????? ????????? ??????????");
      } else {
        String key = splitted[2];
        String value = splitted[3];
        chrs.getClient().setKeyValue(key, value);
      } 
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "?????? ????????? ????????? ???????????????. ?????? ?????? ????????? ????????? ???????????????."));
      int saved = 0;
      if (splitted[1] == "-1") {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
          for (MapleCharacter chr : cs.getPlayerStorage().getAllCharacters().values()) {
            chr.saveToDB(false, false);
            chr.dropMessage(5, "?????????????????????.");
            saved++;
          } 
        } 
      } else {
        ChannelServer ch = ChannelServer.getInstance(Integer.parseInt(splitted[1]));
        if (ch != null) {
          for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters().values()) {
            chr.saveToDB(false, false);
            chr.dropMessage(5, "?????????????????????.");
            saved++;
          } 
        } else {
          c.getPlayer().dropMessageGM(6, "???????????? ?????? ???????????????.");
        } 
      } 
      World.Broadcast.broadcastMessage(CWvsContext.serverNotice(6, "", "???  " + saved + "?????? ???????????? ?????????????????????."));
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        MapleData data = MapleDataProviderFactory.getDataProvider(MapleDataProviderFactory.fileInWZPath("Skill.wz")).getData(StringUtil.getLeftPaddedStr(splitted[1], '0', 3) + ".img");
        byte maxLevel = 0;
        for (MapleData skill : data) {
          if (skill != null)
            for (MapleData skillId : skill.getChildren()) {
              if (!skillId.getName().equals("icon")) {
                maxLevel = (byte)MapleDataTool.getIntConvert("maxLevel", skillId.getChildByPath("common"), 0);
                if (MapleDataTool.getIntConvert("invisible", skillId, 0) == 0)
                  c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(Integer.parseInt(skillId.getName())), maxLevel, SkillFactory.getSkill(Integer.parseInt(skillId.getName())).isFourthJob() ? maxLevel : 0); 
              } 
            }  
        } 
        c.getPlayer().dropMessage(-1, "?????? ????????? ????????? ?????? ??????????????? ???????????????.");
      } catch (Exception e) {
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File("wz/String.wz")).getData("Skill.img").getChildren()) {
        try {
          Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
          if (skill.getId() < 1009 || skill.getId() > 1011);
          c.getPlayer().changeSkillLevel(skill, (byte)skill.getMaxLevel(), (byte)skill.getMaxLevel());
        } catch (NumberFormatException nfe) {
          break;
        } catch (NullPointerException npe) {}
      } 
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[2]));
      byte level = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);
      byte masterlevel = (byte)CommandProcessorUtil.getOptionalIntArg(splitted, 4, 1);
      if (level > skill.getMaxLevel())
        level = (byte)skill.getMaxLevel(); 
      if (masterlevel > skill.getMaxLevel())
        masterlevel = (byte)skill.getMaxLevel(); 
      victim.changeSingleSkillLevel(skill, level, masterlevel);
      return 1;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Map<Item, MapleInventoryType> eqs = new HashMap<>();
      boolean add = false;
      if (splitted.length < 2 || splitted[1].equals("??????")) {
        for (MapleInventoryType type : MapleInventoryType.values()) {
          for (Item item : c.getPlayer().getInventory(type)) {
            if (ItemFlag.LOCK.check(item.getFlag())) {
              item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
              add = true;
            } 
            if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
              item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
              add = true;
            } 
            if (add)
              eqs.put(item, type); 
            add = false;
          } 
        } 
      } else if (splitted[1].equals("????????????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).newList()) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.EQUIP); 
          add = false;
        } 
      } else if (splitted[1].equals("??????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.EQUIP)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.EQUIP); 
          add = false;
        } 
      } else if (splitted[1].equals("??????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.USE)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.USE); 
          add = false;
        } 
      } else if (splitted[1].equals("??????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.SETUP)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.SETUP); 
          add = false;
        } 
      } else if (splitted[1].equals("??????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.ETC); 
          add = false;
        } 
      } else if (splitted[1].equals("??????")) {
        for (Item item : c.getPlayer().getInventory(MapleInventoryType.CASH)) {
          if (ItemFlag.LOCK.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.LOCK.getValue());
            add = true;
          } 
          if (ItemFlag.UNTRADEABLE.check(item.getFlag())) {
            item.setFlag(item.getFlag() - ItemFlag.UNTRADEABLE.getValue());
            add = true;
          } 
          if (add)
            eqs.put(item, MapleInventoryType.CASH); 
          add = false;
        } 
      } else {
        c.getPlayer().dropMessage(6, "[??????/????????????/??????/??????/??????/??????/??????]");
      } 
      for (Map.Entry<Item, MapleInventoryType> eq : eqs.entrySet())
        c.getPlayer().forceReAddItem_NoUpdate(((Item)eq.getKey()).copy(), eq.getValue()); 
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int itemId = Integer.parseInt(splitted[1]);
      short quantity = (short)CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
      MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
      if (GameConstants.isPet(itemId)) {
        c.getPlayer().dropMessage(5, "Please purchase a pet from the cash shop instead.");
      } else if (!ii.itemExists(itemId)) {
        c.getPlayer().dropMessage(5, itemId + " does not exist");
      } else {
        Item toDrop;
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
          toDrop = ii.getEquipById(itemId);
        } else {
          toDrop = new Item(itemId, (short)0, quantity, 0);
        } 
        if (!c.getPlayer().isAdmin()) {
          toDrop.setGMLog(c.getPlayer().getName() + " used !drop");
          toDrop.setOwner(c.getPlayer().getName());
        } 
        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
      } 
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need <name> <itemid>");
        return 0;
      } 
      int itemId = Integer.parseInt(splitted[2]);
      if (!GameConstants.isEffectRing(itemId)) {
        c.getPlayer().dropMessage(6, "Invalid itemID.");
      } else {
        MapleCharacter fff = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
        if (fff == null) {
          c.getPlayer().dropMessage(6, "Player must be online");
        } else {
          long[] ringID = { MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance() };
          try {
            MapleCharacter[] chrz = { fff, c.getPlayer() };
            for (int i = 0; i < chrz.length; i++) {
              Equip eq = (Equip)MapleItemInformationProvider.getInstance().getEquipById(itemId, ringID[i]);
              if (eq == null) {
                c.getPlayer().dropMessage(6, "Invalid itemID.");
                return 0;
              } 
              MapleInventoryManipulator.addbyItem(chrz[i].getClient(), eq.copy());
              chrz[i].dropMessage(6, "Successfully married with " + chrz[(i == 0) ? 1 : 0].getName());
            } 
            MapleRing.addToDB(itemId, c.getPlayer(), fff.getName(), fff.getId(), ringID);
          } catch (SQLException e) {
            e.printStackTrace();
          } 
        } 
      } 
      return 1;
    }
  }
  
  public static class ???????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.setPoints(chrs.getPoints() + Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getPoints() + " points, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.gainDonationPoint(Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getDonationPoint() + " Dpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
//  public static class ????????????????????? extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length < 3) {
//        c.getPlayer().dropMessage(6, "Need playername and amount.");
//        return 0;
//      } 
//      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
//      if (chrs == null) {
//        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
//      } else {
//        chrs.AddStarDustCoin(Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]));
//        c.getPlayer().dropMessage(6, "ok");
//      } 
//      return 1;
//    }
//  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.gainHPoint(Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getHPoint() + " Hpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class V???????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 3) {
        c.getPlayer().dropMessage(6, "Need playername and amount.");
        return 0;
      } 
      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (chrs == null) {
        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
      } else {
        chrs.setVPoints(chrs.getVPoints() + Integer.parseInt(splitted[2]));
        c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getVPoints() + " vpoints, after giving " + splitted[2] + ".");
      } 
      return 1;
    }
  }
  
  public static class ResetOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forfeit(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]));
      return 1;
    }
  }
  
  public static class FStartOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceStart(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]), (splitted.length > 4) ? splitted[4] : null);
      return 1;
    }
  }
  
  public static class FCompleteOther extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[2])).forceComplete(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[3]));
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleLifeFactory.getMonsterStats().clear();
      Setting.setting();
      Setting.setting2();
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleReactor reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])), Integer.parseInt(splitted[1]));
      reactor.setDelay(-1);
      c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor, new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y - 20));
      return 1;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      c.getPlayer().dropMessage(6, "?????? ?????? ?????? ????????? ??????????????????.");
      for (MapleMapObject mo : map.getAllNPCs())
        c.getPlayer().dropMessage(6, ((MapleNPC)mo).getId() + " : " + ((MapleNPC)mo).getName()); 
      return 1;
    }
  }
  
  public static class ??????????????????OID extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      int targetId = Integer.parseInt(splitted[1]);
      int damage = Integer.parseInt(splitted[2]);
      MapleMonster monster = map.getMonsterByOid(targetId);
      if (monster != null) {
        map.broadcastMessage(MobPacket.damageMonster(targetId, damage, false));
        monster.damage(c.getPlayer(), damage, false);
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "Map does not exist");
        return 0;
      } 
      int damage = Integer.parseInt(splitted[1]);
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
        mob.damage(c.getPlayer(), damage, false);
      } 
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      int damage = Integer.parseInt(splitted[1]);
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        if (mob.getId() == Integer.parseInt(splitted[2])) {
          map.broadcastMessage(MobPacket.damageMonster(mob.getObjectId(), damage, false));
          mob.damage(c.getPlayer(), damage, false);
        } 
      } 
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      c.getPlayer().dropMessage(6, "?????? ??? : " + map.getId() + " - " + map.getStreetName() + " : " + map.getMapName());
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "?????? ???????????? ????????????.");
        return 1;
      } 
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        c.getPlayer().dropMessage(6, "????????? " + mob.toString());
        if (mob.getStats().isBoss())
          for (MobSkill msi : mob.getSkills())
            c.getPlayer().dropMessageGM(5, msi.getSkillId() + "???????????? : " + msi.getSkillLevel() + "  ?????? : " + msi.getHP() + "  ?????????" + msi.isOnlyOtherSkill() + " ???????????? : " + msi.isOnlyFsm());  
      } 
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      double range = Double.POSITIVE_INFINITY;
      if (splitted.length > 1) {
        int irange = Integer.parseInt(splitted[1]);
        if (splitted.length <= 2) {
          range = (irange * irange);
        } else {
          map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
        } 
      } 
      if (map == null) {
        c.getPlayer().dropMessage(6, "?????? ???????????? ????????????.");
        return 0;
      } 
      for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.MONSTER }))) {
        MapleMonster mob = (MapleMonster)monstermo;
        map.killMonster(mob, c.getPlayer(), true, false, (byte)1);
      } 
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleNPC npc = MapleLifeFactory.getNPC(npcId);
      if (npc != null && !npc.getName().equals("MISSINGNO")) {
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy((c.getPlayer().getPosition()).y);
        npc.setRx0((c.getPlayer().getPosition()).x);
        npc.setRx1((c.getPlayer().getPosition()).x);
        npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        npc.setCustom(true);
        c.getPlayer().getMap().addMapObject(npc);
        c.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
      } else {
        c.getPlayer().dropMessage(6, "WZ???????????? ?????? ?????????????????? ?????????????????????.");
        return 0;
      } 
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleCharacter chr = null;
      if (splitted.length <= 1) {
        chr = c.getPlayer();
      } else {
        chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      } 
      for (Skill sk : chr.getSkills().keySet()) {
        if (!PacketHelper.jobskill(c.getPlayer(), sk.getId()))
          System.out.println(sk.getName() + "(" + sk.getId() + ") : " + chr.getSkillLevel(sk.getId())); 
      } 
      if (chr != null) {
        String info = "#fs11##b" + chr.getName() + "#k?????? ?????? ?????????.\r\n\r\n";
        info = info + "MAXHP : " + chr.getStat().getCurrentMaxHp() + "\r\n";
        info = info + "MAXMP : " + chr.getStat().getCurrentMaxMp(chr) + "\r\n";
        info = info + "STR : " + chr.getStat().getTotalStr() + "\r\n";
        info = info + "DEX : " + chr.getStat().getTotalDex() + "\r\n";
        info = info + "INT : " + chr.getStat().getTotalInt() + "\r\n";
        info = info + "LUK : " + chr.getStat().getTotalLuk() + "\r\n";
        info = info + "????????? : " + chr.getStat().getMastery() + "  ???????????? : " + GameConstants.getJobAttackSpeed(c.getPlayer()) + " ??????\r\n";
        info = info + "????????? : " + chr.getStat().getTotalWatk() + "   ?????? : " + chr.getStat().getTotalMagic() + "\r\n\r\n";
        info = info + "?????? ????????? : #e" + chr.getStat().BeforeStatWatk(chr) + " ~ " + chr.getStat().AfterStatWatk(chr) + "#n\r\n";
        info = info + "????????? : #d" + chr.getStat().getDamagePercent() + "%#k  ?????? ????????? : #d" + chr.getStat().getBossDamage() + "%#k\r\n";
        info = info + "?????? ????????? : #d" + chr.getStat().getFinalDamage() + "%#k  ????????? ?????? : #d" + chr.getStat().getIgnoreMobpdpR() + "%#k\r\n";
        info = info + "???????????? ?????? : #d" + chr.getStat().getCritical_rate() + "%#k\r\n";
        info = info + "???????????? ????????? : #d" + chr.getStat().getCritical_damage() + "%#k\r\n";
        info = info + "?????????????????? : #d" + chr.getStat().getASR() + "#k  ????????? : #d" + chr.getStat().getStance() + "#k\r\n";
        info = info + "???????????? : #d" + chr.getStat().getStarforce() + "#k  ??????????????? : #d" + chr.getStat().getArc() + "#k\r\n ";
        c.getSession().writeAndFlush(CField.NPCPacket.getNPCTalkText(9062240, info));
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleNPC npc = MapleLifeFactory.getNPC(npcId);
      if (npc != null && !npc.getName().equals("MISSINGNO")) {
        npc.setPosition(c.getPlayer().getPosition());
        npc.setCy((c.getPlayer().getPosition()).y);
        npc.setRx0((c.getPlayer().getPosition()).x);
        npc.setRx1((c.getPlayer().getPosition()).x);
        npc.setFh(c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        c.getPlayer().getMap().addMapObject(npc);
        c.getPlayer().getMap().broadcastMessage(CField.NPCPacket.spawnNPC(npc, true));
      } else {
        c.getPlayer().dropMessage(6, "WZ??? ???????????? ?????? NPC??? ??????????????????.");
      } 
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?)";
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, npcId);
        ps.setInt(2, (c.getPlayer().getPosition()).x - 50);
        ps.setInt(3, (c.getPlayer().getPosition()).x + 50);
        ps.setInt(4, (c.getPlayer().getPosition()).y);
        ps.setInt(5, c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        ps.setString(6, "n");
        ps.setInt(7, (c.getPlayer().getFacingDirection() == 1) ? 0 : 1);
        ps.setInt(8, c.getPlayer().getMapId());
        ps.setInt(9, 0);
        ps.executeUpdate();
        ps.close();
        con.close();
      } catch (Exception e) {
        System.err.println("[??????] ???????????? ?????? ??????????????? ??????????????????.");
        e.printStackTrace();
      } finally {
        try {
          if (con != null)
            con.close(); 
          if (ps != null)
            ps.close(); 
          if (rs != null)
            rs.close(); 
        } catch (SQLException se) {
          se.printStackTrace();
        } 
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int npcId = Integer.parseInt(splitted[1]);
      MapleReactor reactor = null;
      try {
        reactor = new MapleReactor(MapleReactorFactory.getReactor(Integer.parseInt(splitted[1])), Integer.parseInt(splitted[1]));
      } catch (Exception e) {
        c.getPlayer().dropMessage(5, "???????????? ?????? ????????? ?????????.");
        return 1;
      } 
      if (splitted[2] == null) {
        c.getPlayer().dropMessage(5, "????????? ????????? ?????? ?????????????????? ????????? : -1");
        return 1;
      } 
      reactor.setDelay(-1);
      c.getPlayer().getMap().spawnReactorOnGroundBelow(reactor, new Point((c.getPlayer().getTruePosition()).x, (c.getPlayer().getTruePosition()).y));
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        String sql = "INSERT INTO `spawn`(`lifeid`, `rx0`, `rx1`, `cy`, `fh`, `type`, `dir`, `mapid`, `mobTime`) VALUES (? ,? ,? ,? ,? ,? ,? ,? ,?)";
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, npcId);
        ps.setInt(2, (c.getPlayer().getPosition()).x);
        ps.setInt(3, (c.getPlayer().getPosition()).x);
        ps.setInt(4, (c.getPlayer().getPosition()).y);
        ps.setInt(5, c.getPlayer().getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
        ps.setString(6, "r");
        ps.setInt(7, (c.getPlayer().getFacingDirection() == 1) ? 0 : 1);
        ps.setInt(8, c.getPlayer().getMapId());
        ps.setInt(9, Integer.parseInt(splitted[2]));
        ps.executeUpdate();
        ps.close();
        con.close();
      } catch (Exception e) {
        System.err.println("[??????] ???????????? ?????? ??????????????? ??????????????????.");
        e.printStackTrace();
      } finally {
        try {
          if (con != null)
            con.close(); 
          if (ps != null)
            ps.close(); 
          if (rs != null)
            rs.close(); 
        } catch (SQLException se) {
          se.printStackTrace();
        } 
      } 
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "???????????????????????? ??????????????????.");
        MapleCharacter chhr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
        if (chhr == null) {
          c.getPlayer().dropMessage(6, splitted[1] + "?????? ???????????? ????????????, ???????????? ?????? ??????????????????. ????????? ???????????? ?????? ??????????????????.");
          return 0;
        } 
        PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(), c.getPlayer());
        npc.addToServer();
        c.getPlayer().dropMessage(6, "??????!");
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "???????????????????????? ??????????????? ?????????????????????. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class ??????????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "?????? ???????????????????????? ??????????????????.");
        MapleClient cs = new MapleClient(c.getSession(), null, null);
        MapleCharacter chhr = MapleCharacter.loadCharFromDB(MapleCharacterUtil.getIdByName(splitted[1]), cs, false);
        if (chhr == null) {
          c.getPlayer().dropMessage(6, splitted[1] + "?????? ???????????? ????????????.");
          return 0;
        } 
        PlayerNPC npc = new PlayerNPC(chhr, Integer.parseInt(splitted[2]), c.getPlayer().getMap(), c.getPlayer());
        npc.addToServer();
        c.getPlayer().dropMessage(6, "??????!");
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "?????? ???????????????????????? ??????????????? ?????????????????????. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class ??????????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      try {
        c.getPlayer().dropMessage(6, "???????????????????????? ????????? ?????????.");
        MapleNPC npc = c.getPlayer().getMap().getNPCByOid(Integer.parseInt(splitted[1]));
        if (npc instanceof PlayerNPC) {
          ((PlayerNPC)npc).destroy(true);
          c.getPlayer().dropMessage(6, "??????!");
        } else {
          c.getPlayer().dropMessage(6, "????????? : !??????????????????????????? [???????????????]");
        } 
      } catch (Exception e) {
        c.getPlayer().dropMessage(6, "???????????? ??????????????? ?????????????????????. : " + e.getMessage());
        e.printStackTrace();
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      String outputMessage = StringUtil.joinStringFrom(splitted, 1);
      for (ChannelServer cserv : ChannelServer.getAllInstances())
        cserv.setServerMessage(outputMessage); 
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMonster onemob;
      int mid = Integer.parseInt(splitted[1]);
      int num = Math.min(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), 500);
      Integer level = CommandProcessorUtil.getNamedIntArg(splitted, 1, "lvl");
      Long hp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "hp");
      Long exp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "exp");
      Double php = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "php");
      Double pexp = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "pexp");
      try {
        onemob = MapleLifeFactory.getMonster(mid);
      } catch (RuntimeException e) {
        c.getPlayer().dropMessage(5, "Error: " + e.getMessage());
        return 0;
      } 
      if (onemob == null) {
        c.getPlayer().dropMessage(5, "Mob does not exist");
        return 0;
      } 
      long newhp = 0L;
      long newexp = 0L;
      if (hp != null) {
        newhp = hp.longValue();
      } else if (php != null) {
        newhp = (long)(onemob.getMobMaxHp() * php.doubleValue() / 100.0D);
      } else {
        newhp = onemob.getMobMaxHp();
      } 
      if (exp != null) {
        newexp = exp.longValue();
      } else if (pexp != null) {
        newexp = (long)(onemob.getMobExp() * pexp.doubleValue() / 100.0D);
      } else {
        newexp = onemob.getMobExp();
      } 
      if (newhp < 1L)
        newhp = 1L; 
      OverrideMonsterStats overrideStats = new OverrideMonsterStats(newhp, onemob.getMobMaxMp(), newexp, false);
      for (int i = 0; i < num; i++) {
        MapleMonster mob = MapleLifeFactory.getMonster(mid);
        mob.setHp(newhp);
        if (level != null) {
          mob.changeLevel(level.intValue(), false);
        } else {
          mob.setOverrideStats(overrideStats);
        } 
        c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
        c.getPlayer().dropMessage(6, "oid : " + mob.getObjectId());
      } 
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      InterServerHandler.EnterCS(c, c.getPlayer(), false);
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      Setting.CashShopSetting();
      CashItemFactory.getInstance().initialize();
      c.getPlayer().dropMessage(-8, "[GM ??????] ????????? ?????? ????????? ?????? ???????????????.");
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      System.out.println(c.getCreated());
      System.out.println(c.getCreated().getMonth() + 1);
      System.out.println(c.getCreated().getDate());
      return 1;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      BloomingRace.RaceIinviTation();
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().setBattleGrondJobName("" + splitted[1]);
      c.getPlayer().dropMessage(5, "?????? : " + splitted[1] + "??? ?????? ??????.");
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      BattleGroundGameHandler.BattleGroundIinviTation();
      return 1;
    }
  }
  
  public static class ??? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap target = null;
      ChannelServer cserv = c.getChannelServer();
      target = cserv.getMapFactory().getMap(Integer.parseInt(splitted[1]));
      MaplePortal targetPortal = null;
      if (splitted.length > 2)
        try {
          targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
        } catch (IndexOutOfBoundsException e) {
          c.getPlayer().dropMessage(5, "Invalid portal selected.");
        } catch (NumberFormatException numberFormatException) {} 
      if (targetPortal == null)
        targetPortal = target.getPortal(0); 
      c.getPlayer().changeMap(target, targetPortal);
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      int mapId = Integer.parseInt(splitted[1]);
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        if (cserv.getMapFactory().isMapLoaded(mapId) && cserv.getMapFactory().getMap(mapId).getCharactersSize() > 0) {
          c.getPlayer().dropMessage(5, "There exists characters on channel " + cserv.getChannel());
          return 0;
        } 
      } 
      for (ChannelServer cserv : ChannelServer.getAllInstances()) {
        if (cserv.getMapFactory().isMapLoaded(mapId))
          cserv.getMapFactory().removeMap(mapId); 
      } 
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().respawn(true);
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().gainMeso(9999999999L - c.getPlayer().getMeso(), true);
      return 1;
    }
  }
  
  public static class ?????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().gainMeso(Long.parseLong(splitted[1]), true);
      return 1;
    }
  }
  
  public static class ????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().modifyCSPoints(1, Integer.parseInt(splitted[1]), true);
      return 1;
    }
  }
  public static class DebugLucidLaser extends CommandExecute { // WUT
    public int execute (MapleClient c, String[] splitted) {
      List<Integer> laserIntervals;
      Point pos = new Point(665,-490);
      MapleMonster monster = MapleLifeFactory.getMonster(8880151);
      laserIntervals = new ArrayList<Integer>() {
        {
          for (int i = 0; i < 15; ++i) {
            this.add(500);
          }
        }
      };
      monster.setCustomInfo(23807, 0, 10000);
      c.getPlayer().warp(450004250);
      c.getPlayer().getMap().spawnMonsterOnGroundBelow(monster,pos);
      c.getPlayer().getMap().broadcastMessage(MobPacket.BossLucid.doLaserRainSkill(4500, laserIntervals));
      c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("???????????? ????????? ????????? ???????????? ?????????!", 222, 2000));
      c.getPlayer().dropMessageGM(6, "[DEBUG] ????????? ????????? ??????");
      return -1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (victim == null) {
        c.getPlayer().dropMessage(1, splitted[1] + " ???????????? ???????????? ????????????.");
        return 0;
      } 
      c.getPlayer().dropMessage(1, splitted[1] + "????????? " + splitted[2] + " Happy !  ???????????? ?????? ???????????????.");
      victim.modifyCSPoints(1, Integer.parseInt(splitted[2]), true);
      victim.sendNote(splitted[1], "?????????GM", "" + splitted[1] + "?????????! ???????????? ????????? ?????? ??????????????????, ???????????? ???????????? ??????????????? ?????? ????????? ???????????????. ??????????????? ?????? ????????? " + splitted[2] + "???????????? ?????? ??????????????????. ????????? ????????? ???????????? ????????????.", 0, 6, 0);
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
      if (victim == null) {
        c.getPlayer().dropMessage(1, splitted[1] + "???????????? ???????????? ????????????.");
        return 0;
      } 
      c.getPlayer().dropMessage(1, splitted[1] + "????????? " + splitted[2] + " ?????? ???????????? ?????? ???????????????.");
      victim.modifyCSPoints(2, Integer.parseInt(splitted[2]), true);
      victim.sendNote(splitted[1], "?????????GM", "" + splitted[1] + "?????????! ???????????? ????????? ?????? ??????????????????, ???????????? ???????????? ??????????????? ?????? ????????? ???????????????. ??????????????? ?????? ????????? " + splitted[2] + "???????????? ?????? ??????????????????. ????????? ????????? ???????????? ????????????.", 0, 6, 0);
      return 1;
    }
  }
  
      public static class ???????????????????????? extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, "Need playername and amount.");
                return 0;
            }
            MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (chrs == null) {
                c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
            } else {
                chrs.gainDonationPoint(Integer.parseInt(splitted[2]));
                //chrs.gainnDonationPoint(Integer.parseInt(splitted[2]));
                c.getPlayer().dropMessage(6, splitted[1] + " has " + chrs.getDonationPoint() + " Dpoints, after giving " + splitted[2] + ".");
            }
            return 1;
        }
    }
  
  public static class ????????????????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().modifyCSPoints(2, Integer.parseInt(splitted[1]), true);
      return 1;
    }
  }
  
  public static class ??????????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().setPoints(c.getPlayer().getPoints() + Integer.parseInt(splitted[1]));
      return 1;
    }
  }
  
  public static class V?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length < 2) {
        c.getPlayer().dropMessage(5, "Need amount.");
        return 0;
      } 
      c.getPlayer().setVPoints(c.getPlayer().getVPoints() + Integer.parseInt(splitted[1]));
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      SendPacketOpcode.reloadValues();
      RecvPacketOpcode.reloadValues();
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMonsterInformationProvider.getInstance().clearDrops();
      ReactorScriptManager.getInstance().clearDrops();
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      PortalScriptManager.getInstance().clearScripts();
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      NPCScriptManager.getInstance().scriptClear();
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleShopFactory.getInstance().clear();
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(6, "????????? ?????? ??????");
      c.getChannelServer().reloadEvents();
      c.getPlayer().dropMessage(6, "????????? ?????? ??????");
      return 1;
    }
  }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().resetFully();
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forfeit(c.getPlayer());
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).start(c.getPlayer(), Integer.parseInt(splitted[2]));
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).complete(c.getPlayer(), Integer.parseInt(splitted[2]), Integer.valueOf(Integer.parseInt(splitted[3])), false);
      return 1;
    }
  }
  
  public static class F??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceStart(c.getPlayer(), Integer.parseInt(splitted[2]), (splitted.length >= 4) ? splitted[3] : null);
      return 1;
    }
  }
  
  public static class F??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleQuest.getInstance(Integer.parseInt(splitted[1])).forceComplete(c.getPlayer(), Integer.parseInt(splitted[2]));
      return 1;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().getReactorByOid(Integer.parseInt(splitted[1])).hitReactor(c);
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {//??????
    public int execute(MapleClient c, String[] splitted) {
      String id = splitted[1];
      String pw = splitted[2];
      if (AutoRegister.getAccountExists(id) == true) {
        c.getPlayer().dropMessage(6, "?????? ???????????? ???????????????.");
      } else if (AutoRegister.createAccount(id, pw, "/" + id + ":")) {
        c.getPlayer().dropMessage(6, "??????????????? ?????????????????????.");
      } else {
        c.getPlayer().dropMessage(6, "??????????????? ?????????????????????.");
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleMap map = c.getPlayer().getMap();
      List<MapleMapObject> reactors = map.getMapObjectsInRange(c.getPlayer().getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(new MapleMapObjectType[] { MapleMapObjectType.REACTOR }));
      if (splitted[1].equals("all")) {
        for (MapleMapObject reactorL : reactors) {
          MapleReactor reactor2l = (MapleReactor)reactorL;
          c.getPlayer().getMap().destroyReactor(reactor2l.getObjectId());
        } 
      } else {
        c.getPlayer().getMap().destroyReactor(Integer.parseInt(splitted[1]));
      } 
      return 1;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().getMap().resetReactors(c);
      return 1;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted.length >= 1) {
        String text = StringUtil.joinStringFrom(splitted, 1);
        for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters().values())
          mch.sendNote("[Happy ! ]", text, 6, 0); 
      } else {
        c.getPlayer().dropMessage(6, "Use it like this, !sendallnote <text>");
        return 0;
      } 
      return 1;
    }
  }
  
  public static class ???????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      SkillFactory.getSkill(Integer.parseInt(splitted[1])).getEffect(Integer.parseInt(splitted[2])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class ??????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleItemInformationProvider.getInstance().getItemEffect(Integer.parseInt(splitted[1])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class ???????????????EX extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      MapleItemInformationProvider.getInstance().getItemEffectEX(Integer.parseInt(splitted[1])).applyTo(c.getPlayer(), true);
      return 0;
    }
  }
  
  public static class ?????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getPlayer().dropMessage(6, "Number of items: " + MapleItemInformationProvider.getInstance().getAllItems().size());
      return 0;
    }
  }
  
//  public static class ???????????????????????? extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length == 2)
//        if (splitted[0].equals("?????????")) {
//          c.getPlayer().AddStarDustPoint(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), new Point(-1, -1));
//        } else if (splitted[0].equals("??????")) {
//          c.getPlayer().AddStarDustCoin(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]));
//        }  
//      return 0;
//    }
//  }
  
//  public static class ???????????? extends CommandExecute {
//    public int execute(MapleClient c, String[] splitted) {
//      if (splitted.length < 3) {
//        c.getPlayer().dropMessage(6, "Need playername and amount.");
//        return 0;
//      } 
//      MapleCharacter chrs = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]);
//      if (chrs == null) {
//        c.getPlayer().dropMessage(6, "Make sure they are in the correct channel");
//      } else if (Integer.parseInt(splitted[1]) > 3 || Integer.parseInt(splitted[1]) < 1) {
//        c.getPlayer().dropMessage(6, "1 : ?????? ?????? ");
//        c.getPlayer().dropMessage(6, "2 : ?????? ??? ");
//        c.getPlayer().dropMessage(6, "3 : ?????? ?????? ");
//      } else {
//        chrs.AddStarDustCoin(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[3]));
//        String coin = (Integer.parseInt(splitted[1]) == 1) ? "?????? ??????" : ((Integer.parseInt(splitted[1]) == 2) ? "?????? ???" : "?????? ??????");
//        c.getPlayer().dropMessage(6, splitted[2] + "??????  " + coin + " " + Integer.parseInt(splitted[3]) + "??? ????????????");
//      } 
//      return 0;
//    }
//  }
  
  
  public static class ???????????????????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      c.getSession().writeAndFlush(CWvsContext.InfoPacket.updateClientInfoQuest(217, "reward=" + GameConstants.getCurrentDate() + ";count=0;uDate=" + FileoutputUtil.CurrentReadable_Date() + ";qState=2;logis=0CL=9;T=20190114093031;use=47;total=" + splitted[1] + ";exp=" + splitted[2]));
      c.getSession().writeAndFlush(CField.UIPacket.openUI(1207));
      return 0;
    }
  }
  
      public static class ???????????? extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
            ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
            return 0;
        }
    }

    public static class ??????????????? extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
            return 0;
        }
    }

    public static class ???????????? extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
            return 0;
        }
    }
  
  public static class ????????? extends CommandExecute {
    public int execute(MapleClient c, String[] splitted) {
      if (splitted[1].equals("s") || splitted[1].equals("???")) {
        ServerConstants.DEBUG_SEND = !ServerConstants.DEBUG_SEND;
      } else if (splitted[1].equals("r") || splitted[1].equals("???")) {
        ServerConstants.DEBUG_RECEIVE = !ServerConstants.DEBUG_RECEIVE;
      } 
      c.getPlayer().dropMessage(6, "SEND : " + ServerConstants.DEBUG_SEND + "");
      c.getPlayer().dropMessage(6, "RECV : " + ServerConstants.DEBUG_RECEIVE + "");
      return 0;
    }
  }
}
