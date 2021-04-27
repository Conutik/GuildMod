package com.example.examplemod;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import scala.Int;

public class commands extends CommandBase {

    @Override
    public String getCommandName() {
        return "secrets";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "secrets <num>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        Integer secrets = null;
        Double sc = null;

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("user.json"));

            // convert JSON file to map
            Map<?, ?> map = gson.fromJson(reader, Map.class);

            sc = (Double) map.get("secrets");
            secrets = sc.intValue();


            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Current req: " + secrets + " secrets"));
            return;
        }

        if (args.length == 1) {
            String enchant_name = args[0];
            if(!enchant_name.matches("^[0-9]*$")) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Please specify a number"));
            }

            try {
                // create user object
                User user = new User(new Integer(args[0]));

                // create Gson instance with pretty-print
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                // create a writer
                Writer writer = Files.newBufferedWriter(Paths.get("user.json"));

                // convert user object to JSON file
                gson.toJson(user, writer);

                // close writer
                writer.close();

                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Set req to: " + secrets + " secrets"));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }



}
