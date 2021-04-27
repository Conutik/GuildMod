package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{


    public static final String MODID = "guild";
    public static final String VERSION = "1.0";

    @EventHandler
        public void init(FMLInitializationEvent event)
        {
            MinecraftForge.EVENT_BUS.register(this);
            ClientCommandHandler.instance.registerCommand(new commands());
        }

        @SubscribeEvent
        public void xd(ClientChatReceivedEvent event) throws IOException {
            String message = event.message.getFormattedText();
            String nou = event.message.getFormattedText();
            //message is the message which the client receives.
            if(message.contains("Guild") && message.contains(":")) {
                event.setCanceled(true);
                String[] sMessage = message.split(" ");
                //Checks if the coin message you got isn't the tip message
                String[] fixing = sMessage[2].split("");
                String color = fixing[0]+fixing[1];
                message = message.replace("Guild", "G");
                message = message.replace(sMessage[2], "");
                message = message.replace("> ", ">");
                String namecolor = color+sMessage[3];
                message = message.replace(sMessage[3], namecolor);
                String totalCoins = message;
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(totalCoins));

            } else if(message.contains("Guild >")) {
                event.setCanceled(true);
                String[] sMessage = message.split(" ");
                message = message.replace("Guild", "G");
                String totalCoins = message;
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(totalCoins));
            } else if(message.contains("group") && message.contains("Dungeon") && message.contains("Finder")) {
                String[] msg = message.split(" ");
                //Dungeon Finder > user joined the dungeon group!

                if(msg[0].contains("Dungeon") && msg[1].contains("Finder") && msg[2].contains(">") && msg[4].contains("joined") && msg[5].contains("the") && msg[6].contains("dungeon") && msg[7].contains("group!")) {



                    Gson gson = new Gson();

                    String output = null;
                    String uuid = null;
                    String username = msg[3];
                    String[] usernamesplit = username.split("");
                    String code = usernamesplit[0] + usernamesplit[1] + usernamesplit[2] + usernamesplit[3];
                    username = username.replace(code, "");
                    String id = null;
                    String api = null;

                    try {

                        uuid = getUrlContents("https://api.mojang.com/users/profiles/minecraft/" + username);


                    } catch (Exception ignored) {
                        return;
                    }

                    JsonObject info = gson.fromJson(uuid, JsonObject.class);

                    id = info.get("id").getAsString();


                    try {
                        api = "https://api.hypixel.net/player?key=b3c9ef26-3744-40a0-9807-ba6595ceb3be&uuid=" + id;
                        output = getUrlContents(api);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }


                    JsonObject json = gson.fromJson(output, JsonObject.class);
                    int secrets = json.getAsJsonObject("player")
                            .getAsJsonObject("achievements")
                            .get("skyblock_treasure_hunter").getAsInt();

                    Integer ss = null;
                    Double sc = null;

                    try {

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

                    if (secrets < ss) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/p remove " + username);
                    }

                }


            }
        }

    private static String getUrlContents(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
