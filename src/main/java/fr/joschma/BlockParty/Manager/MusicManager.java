// 
// Decompiled by Procyon v0.5.36
// 

package fr.joschma.BlockParty.Manager;

import com.craftmend.openaudiomc.api.ClientApi;
import com.craftmend.openaudiomc.api.MediaApi;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.joschma.BlockParty.Arena.Arena;
import fr.joschma.BlockParty.Arena.State.SongProvider;
import fr.joschma.BlockParty.Arena.State.SongSetting;
import net.mcjukebox.plugin.bukkit.api.JukeboxAPI;
import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

public class MusicManager {

    public void startMusic(final Arena a) {
        if (a.getSongProvider() == SongProvider.NoteBlock) {
            if (a.isPlayMusic()) {
                a.getRadioSongPlayer().setPlaying(true);
                a.getRadioSongPlayer().setVolume((byte) a.getVolumeNoteBlock());
            }
        } else if (a.getSongProvider() == SongProvider.MCJukebox) {
            if (a.isPlayMusic()) {
                for (Player p : a.getPlayers()) {
                    JukeboxAPI.play(p, a.getMcJukeboxSong());
                }
            }
        } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
            Random rand = new Random();
            int i = rand.nextInt(a.getOpenAudioMusic().keySet().size());
            String music = (String) a.getOpenAudioMusic().keySet().toArray()[i];
            com.craftmend.openaudiomc.api.media.Media sound = new com.craftmend.openaudiomc.api.media.
                    Media(a.getOpenAudioMusic().get(music));
            sound.setLoop(true);
            sound.setVolume(50);
            sound.setMediaId("BlockParty");

            for (Player p : a.getPlayers()) {
                ClientApi.getInstance().getClient(p.getUniqueId()).playMedia(sound);
            }
        }
    }

    public void startStopMusic(final Arena a) {
        if (a.getSongProvider() == SongProvider.NoteBlock) {
            if (a.isPlayStopMusic())
                loadStopRadioSongPlayer(a);
        } else if (a.getSongProvider() == SongProvider.MCJukebox) {
            if (a.isPlayStopMusic()) {
                for (Player p : a.getPlayers()) {
                    JukeboxAPI.play(p, a.getMcJukeboxStopSong());
                }
            }
        } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
            com.craftmend.openaudiomc.api.media.Media sound = new com.craftmend.openaudiomc.api.media.
                    Media(a.getOpenAudioStopMusic());
            sound.setLoop(true);
            sound.setVolume(50);
            sound.setMediaId("BlockPartyStop");

            for (Player p : a.getPlayers()) {
                ClientApi.getInstance().getClient(p.getUniqueId()).playMedia(sound);
            }
        }
    }

    public void stopMusic(final Arena a) {
        if (a.isPlayMusic()) {
            if (a.getSongProvider() == SongProvider.NoteBlock) {
                a.getRadioSongPlayer().setPlaying(false);
            } else if (a.getSongProvider() == SongProvider.MCJukebox) {
                for (Player p : a.getPlayers()) {
                    JukeboxAPI.stopMusic(p);
                }
            } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
                for (Player p : a.getPlayers()) {
                    MediaApi.getInstance().stopFor("BlockParty", ClientApi.getInstance().getClient(p.getUniqueId()));
                }
            }
        }
    }

    public void stopStopMusic(final Arena a) {
        if (a.isPlayStopMusic()) {
            if (a.getSongProvider() == SongProvider.NoteBlock) {
                if (a.getRadioStopSongPlayer() != null) {
                    a.getRadioStopSongPlayer().setPlaying(false);
                    a.getRadioStopSongPlayer().destroy();
                }
            } else if (a.getSongProvider() == SongProvider.MCJukebox) {
                for (Player p : a.getPlayers())
                    JukeboxAPI.stopMusic(p);
            } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
                for (Player p : a.getPlayers()) {
                    MediaApi.getInstance().stopFor("BlockPartyStop", ClientApi.getInstance().getClient(p.getUniqueId()));
                }
            }
        }
    }

    public void stopMcJukeboxMusic(Player p) {
        JukeboxAPI.stopMusic(p);
    }

    public void setUpMusics(final Arena a) {
        if (a.getSongProvider() == SongProvider.NoteBlock) {
            if (a.getPl().isNoteIsEnable()) {
                this.createSongs(a);
                this.createStopSong(a);

                if (a.getSongs() != null && a.getSongs().length > 0) {
                    if (a.getSongSetting() == SongSetting.RANDOM) {
                        loadRandomSongPlayer(a);
                        a.setPlayMusic(true);
                    } else {
                        a.setPlayMusic(true);

                        if (a.getSongManager().getNumberOfVoteMap().values().size() > 0) {
                            Map.Entry<String, Integer> entry = sortByValue(a.getSongManager().getNumberOfVoteMap()).entrySet().iterator().next();
                            String key = entry.getKey();

                            a.setChosenSong(createChosenSong(a, key));
                        } else {
                            Random rand = new Random();
                            a.setChosenSong(a.getSongs()[rand.nextInt(a.getSongs().length)]);
                        }

                        if (a.getChosenSong() != null) {
                            loadChoseSongPlayer(a);
                        } else {
                            for (Player p : a.getPlayers()) {
                                a.getPl().getDebug().error(p, "No sound found !");
                                a.setPlayMusic(false);
                            }
                        }
                    }
                } else {
                    for (Player p : a.getPlayers()) {
                        a.getPl().getDebug().error(p, "No sound found. Try to check the music path");
                        a.setPlayMusic(false);
                    }
                }

                if (a.getStopSong() != null) {
                    a.setPlayStopMusic(true);
                }
            } else {
                a.getPl().getDebug().broadcastError(" *** NoteBlockAPI is not installed or not enabled. ***");
                a.getPl().getDebug().broadcastError("https://www.spigotmc.org/resources/noteblockapi.19287/");
            }
        } else if (a.getSongProvider() == SongProvider.MCJukebox) {
            if (a.getPl().isMCJukeboxIsEnable()) {
                a.setPlayMusic(true);
                loadMcJuckboxStopSong(a);

                String url = "";
                if (a.getSongSetting() == SongSetting.RANDOM || a.getSongManager().getNumberOfVoteMap().isEmpty()) {
                    Random rand = new Random();
                    int randNumb = rand.nextInt(a.getLinkToMusic().keySet().size());
                    url = a.getLinkToMusic().get(a.getLinkToMusic().keySet().toArray()[randNumb]);
                } else {
                    Map.Entry<String, Integer> entry = sortByValue(a.getSongManager().getNumberOfVoteMap()).entrySet().iterator().next();
                    String key = entry.getKey();

                    if (a.getLinkToMusic().containsKey(key)) {
                        url = a.getLinkToMusic().get(key);
                    } else {
                        for (Player p : a.getPlayers()) {
                            a.getPl().getDebug().error(p, key + " not found ! Stopping game");
                        }

                        a.urgentLeaveGame();
                        a.setPlayMusic(false);
                        return;
                    }
                }

                loadMcJuckboxSong(a, url);
            } else {
                a.setPlayMusic(false);
                a.getPl().getDebug().broadcastError(" *** MCJukebox is not installed or not enabled. ***");
                a.getPl().getDebug().broadcastError("https://www.spigotmc.org/resources/mcjukebox.16024/");
            }
        } else if (a.getSongProvider() == SongProvider.OpenAudioMC) {
            a.setPlayMusic(true);
            startMusic(a);
        }
    }

    public void loadMcJuckboxSong(Arena a, String url) {
        Media media = new Media(ResourceType.MUSIC, url);
        media.setLooping(true);
        media.setFadeDuration(0);

        a.setMcJukeboxSong(media);
        a.setPlayMusic(true);

        startMusic(a);
    }

    public void loadMcJuckboxStopSong(Arena a) {
        Media media = new Media(ResourceType.MUSIC, a.getLinkToStopMusic());
        media.setLooping(false);
        media.setFadeDuration(0);

        a.setMcJukeboxStopSong(media);
        a.setPlayStopMusic(true);
    }

    // TODO this reset music ?
    public void loadStopRadioSongPlayer(final Arena a) {
        if (a.isPlayStopMusic()) {
            RadioSongPlayer rsp = null;
            try {
                new RadioSongPlayer(a.getStopSong());
                rsp = new RadioSongPlayer(a.getStopSong());

                for (final Player p : a.getPlayers()) {
                    rsp.addPlayer(p);
                }

                rsp.setVolume((byte) a.getVolumeNoteBlock());
                rsp.setPlaying(true);
            } catch (Exception e) {
                a.getPl().getDebug().sysout("Stop song is corrupted !");
            }

            a.setRadioStopSongPlayer(rsp);
        }
    }

    private void loadRandomSongPlayer(final Arena a) {
        final Playlist playlist = new Playlist(a.getSongs());
        final RadioSongPlayer rsp = new RadioSongPlayer(playlist);
        rsp.setRepeatMode(RepeatMode.ALL);
        rsp.setRandom(true);
        for (final Player p : a.getPlayers()) {
            rsp.addPlayer(p);
        }
        rsp.setPlaying(true);
        a.setRadioSongPlayer(rsp);
        this.startMusic(a);
    }

    private void loadChoseSongPlayer(final Arena a) {
        final RadioSongPlayer rsp = new RadioSongPlayer(a.getChosenSong());
        rsp.setRepeatMode(RepeatMode.ALL);
        for (final Player p : a.getPlayers()) {
            rsp.addPlayer(p);
        }
        rsp.setPlaying(true);
        a.setRadioSongPlayer(rsp);
        this.startMusic(a);
    }

    private Song createChosenSong(Arena a, String musicName) {
        String pathToMusic = "";
        for (String path : a.getPathToMusic()) {
            if (path.contains(musicName + ".nbs")) {
                pathToMusic = path;
            }
        }

        return NBSDecoder.parse(new File(a.getPl().getDataFolder(), pathToMusic));
    }

    private void createStopSong(final Arena a) {
        String pathToMusic = "";

        if (a.getPathToStopMusic().contains("/")) {
            pathToMusic = a.getPathToStopMusic().replace("/", File.separator);
        } else if (a.getPathToStopMusic().contains("\\")) {
            pathToMusic = a.getPathToStopMusic().replace("\\", File.separator);
        } else {
            pathToMusic = a.getPathToStopMusic();
        }
        if (new File(a.getPl().getDataFolder(), pathToMusic) != null && new File(a.getPl().getDataFolder(), pathToMusic).exists() && new File(a.getPl().getDataFolder(), pathToMusic).isFile()) {
            final Song stopSong = NBSDecoder.parse(new File(a.getPl().getDataFolder(), pathToMusic));
            a.setStopSong(stopSong);
        }
    }

    private void createSongs(final Arena a) {
        final List<Song> songs = new ArrayList<Song>();
        for (int i = 0; i < a.getPathToMusic().size(); ++i) {
            String pathToMusic = "";
            if (a.getPathToMusic().get(i).contains("/")) {
                pathToMusic = a.getPathToMusic().get(i).replace("/", File.separator);
            } else if (a.getPathToMusic().get(i).contains("\\")) {
                pathToMusic = a.getPathToMusic().get(i).replace("\\", File.separator);
            } else {
                pathToMusic = a.getPathToMusic().get(i);
            }

            if (new File(a.getPl().getDataFolder(), pathToMusic) != null && new File(a.getPl().getDataFolder(), pathToMusic).exists() && new File(a.getPl().getDataFolder(), pathToMusic).isFile()) {
                final Song song = NBSDecoder.parse(new File(a.getPl().getDataFolder(), pathToMusic));
                if (song != null) {
                    songs.add(song);
                }
            }
        }

        final Song[] array = new Song[songs.size()];
        songs.toArray(array);
        a.setSongs(array);
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Entry<K, V>>() {
                    @Override
                    public int compare(Entry<K, V> e1, Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : sortedEntries) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
