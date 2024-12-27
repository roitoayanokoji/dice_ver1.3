package com.github.roitoayanokoji.dice_ver13;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DiceCommandExecutor implements CommandExecutor {

    private final Dice_ver13 plugin;
    private final HashMap<String, List<String>> diceHistory = new HashMap<>();

    public DiceCommandExecutor(Dice_ver13 plugin){
        this.plugin = plugin;
    }

    String pl = "[Dice]";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dice")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("このコマンドはプレイヤーのみ実行可能です。");
                return true;
            }
            Player player = (Player) sender;

            if (args.length == 0) {
                // "/dice"
                return handleDiceRoll(player);
            } else if (args[0].equalsIgnoreCase("log")) {
                // "/dice log"
                return handleDiceLog(player);
            } else if (args[0].equalsIgnoreCase("help")) {
                // "/dice help"
                return handleDiceHelp(player);
            } else {
                try {
                    int sides = Integer.parseInt(args[0]);
                    return handleCustomDiceRoll(player,sides);
                } catch (NumberFormatException e){
                    player.sendMessage(pl + "無効な値です。整数値を入力してください!");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean handleDiceRoll(Player player){
        return handleCustomDiceRoll(player,6);
    }

    private boolean handleCustomDiceRoll(Player player,int sides) {
        if (sides <= 0){
            player.sendMessage(pl + "面の数は1以上を指定してください。");
        }
        int random = new Random().nextInt(sides) + 1;
        //save dice result
        String SaveDiceLog = (sides + "面ダイスを振って" + random + "が出た。");
        diceHistory.computeIfAbsent(player.getName(), k -> new ArrayList<>()).add(SaveDiceLog);

        plugin.getServer().broadcastMessage(pl + player.getName() + "は" + sides + "面ダイスを振って" + random + "が出た。");
        return true;
    }

    private  boolean handleDiceLog(Player player) {
        List<String> history = diceHistory.get(player.getName());

        if (history == null || history.isEmpty()) {
            player.sendMessage(pl + "ダイスの使用履歴がない為表示できません。");
            return true;
        }
        player.sendMessage("======= ダイス履歴 =======");
        int displaylimit = Math.min(history.size(),5);
        for (int i = history.size()-displaylimit; i<history.size(); i++){
            player.sendMessage(history.get(i));
        }
        player.sendMessage("=======================");
        return true;
    }

    private  boolean handleDiceHelp(Player player) {
        String set1 = "===== Dice Plugin Help =====";
        String set2 = "========================";

        player.sendMessage(set1);
        player.sendMessage("/dice - 指定がない場合6面ダイスを振ります。");
        player.sendMessage("/dice [] - 指定したダイスを振ります。");
        player.sendMessage("/dice log - ログイン時にダイスを振ったときのログを表示");
        player.sendMessage("/dice help - コマンドのヘルプを表示");
        player.sendMessage(set2);
        return true;
    }
}
